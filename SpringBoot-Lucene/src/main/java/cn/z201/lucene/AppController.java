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
            //创建索引目录文件
            File indexFile = new File(path);
            File[] files = indexFile.listFiles();
            // 1. 创建分词器,分析文档，对文档进行分词
            analyzer = new IKAnalyzer();
            // 2. 创建Directory对象,声明索引库的位置
            directory = FSDirectory.open(Paths.get(path));
            // 3. 创建IndexWriteConfig对象，写入索引需要的配置
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            // 4.创建IndexWriter写入对象
            indexWriter = new IndexWriter(directory, writerConfig);
            // 5.写入到索引库，通过IndexWriter添加文档对象document
            Document doc = new Document();
            //查询是否有该索引，没有添加，有则更新
            TopDocs topDocs = null;
            //判断索引目录文件是否存在文件，如果没有文件，则为首次添加，有文件，则查询id是否已经存在
            if (files != null && files.length != 0) {
                //创建查询对象
                QueryParser queryParser = new QueryParser("id", analyzer);
                Query query = queryParser.parse(String.valueOf(entity.getId()));
                indexReader = DirectoryReader.open(directory);
                IndexSearcher indexSearcher = new IndexSearcher(indexReader);
                //查询获取命中条目
                topDocs = indexSearcher.search(query, 1);
            }
            //StringField 不分词 直接建索引 存储
            doc.add(new StringField("id", String.valueOf(entity.getId()), Field.Store.YES));
            //TextField 分词 建索引 存储
            doc.add(new TextField("title", entity.getTitle(), Field.Store.YES));
            //TextField 分词 建索引 存储
            doc.add(new TextField("content", entity.getContent(), Field.Store.YES));
            //如果没有查询结果，添加
            if (topDocs != null && topDocs.totalHits.value == 0) {
                indexWriter.addDocument(doc);
                //否则，更新
            } else {
                indexWriter.updateDocument(new Term("id", String.valueOf(entity.getId())), doc);
            }
        } catch (Exception e) {
            log.error("添加索引库出错 {}", e.getMessage());
            throw new RuntimeException("添加索引库出错：" + e.getMessage());
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
     * @param keyWord 查询条件
     * @param page    当前页
     * @param limit   页大小
     * @return
     */
    public List<ArticleVo> fullTextSearch(String keyWord, Integer page, Integer limit) {
        List<ArticleVo> searchList = new ArrayList<>(10);
        File indexFile = new File(path);
        File[] files = indexFile.listFiles();
        //沒有索引文件，不然沒有查詢結果
        if (files == null || files.length == 0) {
            return searchList;
        }
        IndexReader indexReader = null;
        Directory directory = null;
        try (Analyzer analyzer = new IKAnalyzer()) {
            directory = FSDirectory.open(Paths.get(path));
            //多项查询条件
            QueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
            //单项
            //QueryParser queryParser = new QueryParser("title", analyzer);
            Query query = queryParser.parse(!StrUtil.isEmpty(keyWord) ? keyWord : "*:*");
            indexReader = DirectoryReader.open(directory);
            //索引查询对象
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //  query 查询参数 ,  n - 指定返回排序以后的搜索结果的前n个
            TopDocs topDocs = indexSearcher.search(query, 1);
            // 计算记录起始数
            int start = (page - 1) * limit;
            //获取条数
            int total = (int) topDocs.totalHits.value;
            //获取结果集
            ScoreDoc lastSd = null;
            if (page > 1) {
                int num = limit * (page - 1);
                TopDocs tds = indexSearcher.search(query, start);
                lastSd = tds.scoreDocs[num - 1];
            }
            log.info("total {}", total);
            //通过最后一个元素去搜索下一页的元素 如果lastSd为null，查询第一页
            TopDocs tds = indexSearcher.searchAfter(lastSd, query, limit);
            QueryScorer queryScorer = new QueryScorer(query);
            Highlighter highlighter = null;
            if (StrUtil.isNotEmpty(keyWord)) {
                //最佳摘要
                SimpleSpanFragmenter simpleSpanFragmenter = new SimpleSpanFragmenter(queryScorer, 200);
                //高亮前后标签
                SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
                //高亮对象
                highlighter = new Highlighter(formatter, queryScorer);
                //设置高亮最佳摘要
                highlighter.setTextFragmenter(simpleSpanFragmenter);
            }
            //遍历查询结果 把标题和内容替换为带高亮的最佳摘要
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = indexSearcher.doc(sd.doc);
                float score = sd.score;
                ArticleVo articleEntity = new ArticleVo();
                String id = doc.get("id");
                articleEntity.setId(id);
                articleEntity.setScore(score);
                if (StrUtil.isNotEmpty(keyWord)) {
                    //获取标题的最佳摘要
                    String titleBestFragment = highlighter.getBestFragment(analyzer, "title", doc.get("title"));
                    //获取文章内容的最佳摘要
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
            throw new RuntimeException("全文檢索出错：" + e.getMessage());
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
        //删除全文检索
        IndexWriter indexWriter = null;
        Directory directory = null;
        try (Analyzer analyzer = new IKAnalyzer()) {
            directory = FSDirectory.open(Paths.get(path));
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, writerConfig);
            //根据id字段进行删除
            indexWriter.deleteDocuments(new Term("id", id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除索引库出错：" + e.getMessage());
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
