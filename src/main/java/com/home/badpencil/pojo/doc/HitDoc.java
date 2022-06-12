package com.home.badpencil.pojo.doc;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.highlight.Highlighter;
import java.io.File;
import java.io.StringReader;
/**
 *  返回的文档
 */
@Data
public class HitDoc {
    private String fullText;
    private String title;
    private String summary;
    private float score;
    private String categoryPath;
    private String path;
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
    /**
     *  获取类目结构
     */
    public static String categoryPath(String path) {
        int begin = path.indexOf("bad-pencil/") + "bad-pencil/".length();
        int end = path.lastIndexOf("/");
        if (begin >= end) {
            return "";
        } else {
            return path.substring(begin, end);
        }
    }
    public static String fileNameWithoutSuffix(File file) {
        if(file == null ) return "";
        if(file.isDirectory())  return  file.getName();
        return file.getName().substring(0,file.getName().lastIndexOf("."));
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
        public HitDocBuilder title(File file) {
            hitDoc.setTitle(fileNameWithoutSuffix(file));
            return this;
        }
        public HitDocBuilder title(String title) {
            hitDoc.setTitle(title);
            return this;
        }
        public HitDocBuilder path(String path) {
            hitDoc.setPath(path);
            return this;
        }
        public HitDocBuilder score(float score) {
            hitDoc.setScore(score);
            return this;
        }
        public HitDocBuilder categoryPath(String path) {
            hitDoc.setCategoryPath(HitDoc.categoryPath(path));
            return this;
        }

        public HitDoc build() {
            return hitDoc;
        }
    }
}
