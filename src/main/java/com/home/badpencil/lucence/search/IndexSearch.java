package com.home.badpencil.lucence.search;

import com.hankcs.lucene.HanLPIndexAnalyzer;
import com.home.badpencil.constans.Constants;
import com.home.badpencil.pojo.doc.FieldName;
import com.home.badpencil.pojo.doc.HitDoc;
import com.home.badpencil.pojo.doc.ResDocs;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.MMapDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Paths;

public class IndexSearch {
    private static final Logger logger = LoggerFactory.getLogger(IndexSearch.class);
    public static final int MAX_SEARCH = 10;
    IndexSearcher indexSearcher;
    Analyzer analyzer;
    SimpleHTMLFormatter simpleHTMLFormatter;
    // 保持单例,提高性能
    IndexReader reader = null;
    public IndexSearch() {
        try {
            //获取要查询的路径，也就是索引所在的位置
            reader = DirectoryReader.open(MMapDirectory.open(Paths.get(Constants.LUCENE_INDEX_PATH)));
            // 使用hanLp分词器
            analyzer = new HanLPIndexAnalyzer();
            //如果不指定参数的话，默认是加粗，即<b><b/>
            simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>", "</font></b>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当索引文件更新后,需要更新生成IndexSearcher; 否则新的索引文件不会被检索到,除非重启
     * 参考 https://blog.csdn.net/huangzhilin2015/article/details/86610074
     *   https://blog.csdn.net/lw_power/article/details/44007329
     */
    public IndexSearcher getSearcher() {
        try {
            if (reader == null) {
                reader = DirectoryReader.open(MMapDirectory.open(Paths.get(Constants.LUCENE_INDEX_PATH)));
            } else {
                IndexReader changeReader = DirectoryReader.openIfChanged((DirectoryReader) reader);
                if (changeReader != null) {
                    reader.close();
                    reader = changeReader;
                }
            }
        } catch (Exception ex) {
            logger.info("IndexSearch getSearcher() occur some ex.", ex);
        }
        return new IndexSearcher(reader);
    }

    public ResDocs search(String searchQuery) throws Exception {
        //查询解析器
        QueryParser parser = new QueryParser(FieldName.contents.name(), analyzer);
        //通过解析要查询的String，获取查询对象，q为传进来的待查的字符串
        Query query = parser.parse(searchQuery);
        //记录索引开始时间
        long startTime = System.currentTimeMillis();
        //开始查询，查询前10条数据，将记录保存在docs中
        IndexSearcher currentIndexSearcher = getSearcher();
        TopDocs docs = currentIndexSearcher.search(query, MAX_SEARCH);
        //记录索引结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("匹配" + searchQuery + "共耗时" + (endTime-startTime) + "毫秒");
        System.out.println("查询到" + docs.totalHits + "条记录");
        //根据查询对象计算得分，会初始化一个查询结果最高的得分
        QueryScorer scorer = new QueryScorer(query);
        //根据这个得分计算出一个片段
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        //将这个片段中的关键字用上面初始化好的高亮格式高亮
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        //设置一下要显示的片段
        highlighter.setTextFragmenter(fragmenter);
        ResDocs resDocs = new ResDocs(docs.totalHits,docs.getMaxScore());
        //取出每条查询结果
        for(ScoreDoc scoreDoc : docs.scoreDocs) {
            //scoreDoc.doc相当于docID,根据这个docID来获取文档
            Document doc = currentIndexSearcher.doc(scoreDoc.doc);
            String text = doc.get(FieldName.text.name());
            resDocs.addDoc(HitDoc.HitDocBuilder.aHitDoc().fullText(text)
                    .summary(HitDoc.generateSummary(text,analyzer,highlighter))
                    .score(scoreDoc.score).categoryPath(doc.get(FieldName.filepath.name()))
                    .build());
        }
        return resDocs;
    }
}
