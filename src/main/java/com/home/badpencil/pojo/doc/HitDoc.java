package com.home.badpencil.pojo.doc;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.highlight.Highlighter;

import java.io.StringReader;

/**
 *  返回的文档
 */
@Data
public class HitDoc {
    private String fullText;
    private String summary;
    private float score;
    private String categoryPath;
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
        public HitDocBuilder categoryPath(String path) {
            int begin = path.indexOf("bad-pencil/")+"bad-pencil/".length();
            int end = path.lastIndexOf("/");
            if (begin >= end) {
                hitDoc.setCategoryPath("");
            } else {
                hitDoc.setCategoryPath(path.substring(path.indexOf("bad-pencil/") + "bad-pencil/".length(), path.lastIndexOf("/")));
            }
            return this;
        }

        public HitDoc build() {
            return hitDoc;
        }
    }
}
