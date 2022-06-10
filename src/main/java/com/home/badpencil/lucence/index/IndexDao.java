package com.home.badpencil.lucence.index;

import com.hankcs.lucene.HanLPIndexAnalyzer;
import com.home.badpencil.constans.Constants;
import com.home.badpencil.pojo.doc.FieldName;
import com.home.badpencil.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 执行索引操作
 */
public class IndexDao {
    private IndexWriterConfig indexWriterConfig;
    private Directory indexDirectory;
    public IndexDao() {
        try {
            // 该路径持久化索引
            indexDirectory = MMapDirectory.open(Paths.get(Constants.LUCENE_INDEX_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析每篇文章
     */
    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        //index file contents   这里文件的重复由文件系统路径决定,不同路径下重复文件es不会去重处理
        Field contentField = new TextField(FieldName.contents.name(), new FileReader(file));
        //index file name
        Field fileNameField = new TextField(FieldName.filename.name(), file.getName(), Field.Store.YES);
        //添加文件路径
        document.add(new TextField(FieldName.filepath.name(), file.getCanonicalPath(), Field.Store.YES));
        //添加文本内容
        document.add(new TextField(FieldName.text.name(), new String(Files.readAllBytes(file.toPath())), Field.Store.YES));
        document.add(contentField);
        document.add(fileNameField);
        return document;
    }
    /**
     *  注意addDocument方法不是幂等的 indexWriter.addDocument(document);
     *  同样的一个文档两次重复添加会创建两个索引
     */
    private void indexFile(IndexWriter indexWriter, File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        indexWriter.addDocument(document);
    }



    public int createIndex(String dataDirPath) throws IOException {
        //get all files in the data directory
        if (StringUtils.isBlank(dataDirPath)) dataDirPath = Constants.DOC_PATH;
        IndexWriter indexWriter = new IndexWriter(indexDirectory, getIndexWriterConf());
        File rootPathFile = new File(dataDirPath);
        // bfs提供的路径
        Queue<File> queue = new LinkedList<>();
        queue.add(rootPathFile);
        int totalIndexCnt = 0;
        while (!queue.isEmpty()) {
            File file = queue.poll();
            if (file == null) continue;
            if (file.isDirectory() && file.listFiles() != null) {
                Collections.addAll(queue, Objects.requireNonNull(file.listFiles()));
            } else {
                if (FileUtil.isMarkDown(file)) {
                    indexFile(indexWriter, file);
                    totalIndexCnt++;
                }
            }
        }
        indexWriter.close();
        indexWriter = null; // help gc
        return totalIndexCnt;
    }

    private IndexWriterConfig getIndexWriterConf() throws IOException {
        // 使用hanLp分词器
        indexWriterConfig = new IndexWriterConfig(new HanLPIndexAnalyzer());
        // CREATE 每次都新建索引快照,不容易有冲突; 但是IndexReader需要重新打开才能读到新索引
        // APPEND, CREATE_OR_APPEND; 追加或新建，存在则追加,不存在则新建
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return  indexWriterConfig;
    }
}
