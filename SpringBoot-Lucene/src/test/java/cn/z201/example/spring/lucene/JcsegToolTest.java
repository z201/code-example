package cn.z201.example.spring.lucene.lucene;

import org.junit.jupiter.api.Test;
//import org.lionsoul.jcseg.ISegment;
//import org.lionsoul.jcseg.IWord;
//import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
//import org.lionsoul.jcseg.dic.ADictionary;
//import org.lionsoul.jcseg.dic.DictionaryFactory;
//import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import java.io.IOException;

public class JcsegToolTest {

    @Test
    public void test() throws IOException {
        // SegmenterConfig config = new SegmenterConfig(true);
        // ADictionary dic = DictionaryFactory.createSingletonDictionary(config);
        // config.setAppendCJKSyn(true);
        // config.setAppendCJKPinyin(true);
        // Analyzer analyzer = new JcsegAnalyzer(ISegment.COMPLEX, config, dic);
        // ISegment seg = ISegment.COMPLEX.factory.create(config, dic);
        // //备注：以下代码可以反复调用，seg为非线程安全
        // //设置要被分词的文本
        // String str = "研究生命起源。";
        // seg.reset(new StringReader(str));
        // //获取分词结果
        // IWord word = null;
        // while ((word = seg.next()) != null) {
        // System.out.println(word.getValue());
        // }
    }

}