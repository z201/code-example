package cn.z201.lucene;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
@Slf4j
public class AppController {

    @Value(value = "${analyzer.path}")
    private String path;

    @RequestMapping(value = "add")
    public Object add() {
        ArticleVo articleEntity = new ArticleVo();
        String test = RandomUtil.randomString(10);
        articleEntity.setId(test);
        articleEntity.setTitle(test);
        articleEntity.setContent(test);
        addOrUpIndex(articleEntity);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", articleEntity);
        return data;
    }

    @RequestMapping(value = "list/{keyWord}")
    public Object list(@PathVariable(required = false) String keyWord) {
        if (StrUtil.isEmpty(keyWord)) {
            keyWord = Strings.EMPTY;
        }
        List<ArticleVo> articleEntityList = fullTextSearch(keyWord, 1, 10);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", articleEntityList);
        return data;
    }

    @RequestMapping(value = "search")
    public Object search() {
        List<ArticleVo> articleEntityList = fullTextSearch(null, 1, 10);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", articleEntityList);
        return data;
    }

    @RequestMapping(value = "del/{id}")
    public Object del(@PathVariable(required = false) String id) {
        if (!StrUtil.isEmpty(id)) {
            deleteIndex(id);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", id);
        return data;
    }

    private void addOrUpIndex(ArticleVo entity) {
        IndexWriter indexWriter = null;
        IndexReader indexReader = null;
        Directory directory = null;
        Analyzer analyzer = null;
        try {
            //????????????????????????
            File indexFile = new File(path);
            File[] files = indexFile.listFiles();
            // 1. ???????????????,????????????????????????????????????
            analyzer = new IKAnalyzer();
            // 2. ??????Directory??????,????????????????????????
            directory = FSDirectory.open(Paths.get(path));
            // 3. ??????IndexWriteConfig????????????????????????????????????
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            // 4.??????IndexWriter????????????
            indexWriter = new IndexWriter(directory, writerConfig);
            // 5.???????????????????????????IndexWriter??????????????????document
            Document doc = new Document();
            //??????????????????????????????????????????????????????
            TopDocs topDocs = null;
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????id??????????????????
            if (files != null && files.length != 0) {
                //??????????????????
                QueryParser queryParser = new QueryParser("id", analyzer);
                Query query = queryParser.parse(String.valueOf(entity.getId()));
                indexReader = DirectoryReader.open(directory);
                IndexSearcher indexSearcher = new IndexSearcher(indexReader);
                //????????????????????????
                topDocs = indexSearcher.search(query, 1);
            }
            //StringField ????????? ??????????????? ??????
            doc.add(new StringField("id", String.valueOf(entity.getId()), Field.Store.YES));
            //TextField ?????? ????????? ??????
            doc.add(new TextField("title", entity.getTitle(), Field.Store.YES));
            //TextField ?????? ????????? ??????
            doc.add(new TextField("content", entity.getContent(), Field.Store.YES));
            //?????????????????????????????????
            if (topDocs != null && topDocs.totalHits.value == 0) {
                indexWriter.addDocument(doc);
                //???????????????
            } else {
                indexWriter.updateDocument(new Term("id", String.valueOf(entity.getId())), doc);
            }
        } catch (Exception e) {
            log.error("????????????????????? {}", e.getMessage());
            throw new RuntimeException("????????????????????????" + e.getMessage());
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    log.error("indexWriter.close  {}", e.getMessage());
                }
            }
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    log.error("indexReader.close  {}", e.getMessage());
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    log.error("directory.close  {}", e.getMessage());
                }
            }
            if (analyzer != null) {
                analyzer.close();
            }
        }
    }

    /**
     * @param keyWord ????????????
     * @param page    ?????????
     * @param limit   ?????????
     * @return
     */
    public List<ArticleVo> fullTextSearch(String keyWord, Integer page, Integer limit) {
        List<ArticleVo> searchList = new ArrayList<>(10);
        File indexFile = new File(path);
        File[] files = indexFile.listFiles();
        //?????????????????????????????????????????????
        if (files == null || files.length == 0) {
            return searchList;
        }
        IndexReader indexReader = null;
        Directory directory = null;
        try (Analyzer analyzer = new IKAnalyzer()) {
            directory = FSDirectory.open(Paths.get(path));
            //??????????????????
            QueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
            //??????
            //QueryParser queryParser = new QueryParser("title", analyzer);
            Query query = queryParser.parse(!StrUtil.isEmpty(keyWord) ? keyWord : "*:*");
            indexReader = DirectoryReader.open(directory);
            //??????????????????
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //  query ???????????? ,  n - ?????????????????????????????????????????????n???
            TopDocs topDocs = indexSearcher.search(query, 1);
            // ?????????????????????
            int start = (page - 1) * limit;
            //????????????
            int total = (int) topDocs.totalHits.value;
            //???????????????
            ScoreDoc lastSd = null;
            if (page > 1) {
                int num = limit * (page - 1);
                TopDocs tds = indexSearcher.search(query, start);
                lastSd = tds.scoreDocs[num - 1];
            }
            log.info("total {}", total);
            //??????????????????????????????????????????????????? ??????lastSd???null??????????????????
            TopDocs tds = indexSearcher.searchAfter(lastSd, query, limit);
            QueryScorer queryScorer = new QueryScorer(query);
            Highlighter highlighter = null;
            if (StrUtil.isNotEmpty(keyWord)) {
                //????????????
                SimpleSpanFragmenter simpleSpanFragmenter = new SimpleSpanFragmenter(queryScorer, 200);
                //??????????????????
                SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
                //????????????
                highlighter = new Highlighter(formatter, queryScorer);
                //????????????????????????
                highlighter.setTextFragmenter(simpleSpanFragmenter);
            }
            //?????????????????? ???????????????????????????????????????????????????
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = indexSearcher.doc(sd.doc);
                float score = sd.score;
                ArticleVo articleEntity = new ArticleVo();
                String id = doc.get("id");
                articleEntity.setId(id);
                articleEntity.setScore(score);
                if (StrUtil.isNotEmpty(keyWord)) {
                    //???????????????????????????
                    String titleBestFragment = highlighter.getBestFragment(analyzer, "title", doc.get("title"));
                    //?????????????????????????????????
                    String contentBestFragment = highlighter.getBestFragment(analyzer, "content", doc.get("content"));
                    articleEntity.setTitle(titleBestFragment);
                    articleEntity.setContent(contentBestFragment);
                } else {
                    articleEntity.setTitle(doc.get("title"));
                    articleEntity.setContent(doc.get("content"));
                }
                searchList.add(articleEntity);
            }
            return searchList;
        } catch (Exception e) {
            throw new RuntimeException("?????????????????????" + e.getMessage());
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    log.error("indexReader.close  {}", e.getMessage());
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    log.error("directory.close  {}", e.getMessage());
                }
            }
        }
    }

    private void deleteIndex(String id) {
        //??????????????????
        IndexWriter indexWriter = null;
        Directory directory = null;
        try (Analyzer analyzer = new IKAnalyzer()) {
            directory = FSDirectory.open(Paths.get(path));
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, writerConfig);
            //??????id??????????????????
            indexWriter.deleteDocuments(new Term("id", id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("????????????????????????" + e.getMessage());
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    log.error("indexWriter.close  {}", e.getMessage());
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    log.error("directory.close  {}", e.getMessage());
                }
            }
        }
    }
}
