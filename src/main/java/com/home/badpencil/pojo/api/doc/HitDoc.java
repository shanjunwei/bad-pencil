package com.home.badpencil.pojo.api.doc;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.io.StringReader;

/**
 *  返回的文档
 */
@Data
public class HitDoc {
    private String fullText;
    private String summary;
    private float score;
    private String category1;
    private String category2;
    private String category3;
    /**
     *  生成高亮文本
     */
    public static String generateSummary(String fullText, Analyzer analyzer, Highlighter highlighter) {
        if (StringUtils.isBlank(fullText)) return "";
        TokenStream tokenStream = analyzer.tokenStream("desc", new StringReader(fullText));
        try {
            return highlighter.getBestFragment(tokenStream, fullText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static final class HitDocBuilder {
        private HitDoc hitDoc;

        private HitDocBuilder() {
            hitDoc = new HitDoc();
        }

        public static HitDocBuilder aHitDoc() {
            return new HitDocBuilder();
        }

        public HitDocBuilder fullText(String fullText) {
            hitDoc.setFullText(fullText);
            return this;
        }

        public HitDocBuilder summary(String summary) {
            hitDoc.setSummary(summary);
            return this;
        }

        public HitDocBuilder score(float score) {
            hitDoc.setScore(score);
            return this;
        }

        public HitDocBuilder category1(String category1) {
            hitDoc.setCategory1(category1);
            return this;
        }

        public HitDocBuilder category2(String category2) {
            hitDoc.setCategory2(category2);
            return this;
        }

        public HitDocBuilder category3(String category3) {
            hitDoc.setCategory3(category3);
            return this;
        }

        public HitDoc build() {
            return hitDoc;
        }
    }
}
