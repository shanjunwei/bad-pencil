package com.home.badpencil.service;

import com.alibaba.fastjson.JSONObject;
import com.home.badpencil.constans.Constants;
import com.home.badpencil.pojo.doc.DocTree;
import com.home.badpencil.pojo.doc.HitDoc;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class DocService {
    /**
     *   根据文件路径查询文件内容
     */
    public HitDoc getDocByPath(String path) throws Exception {
        String fullText = new String(Files.readAllBytes(Paths.get(path)));
        File file = new File(path);
        return HitDoc.HitDocBuilder.aHitDoc().title(file).categoryPath(path).fullText(fullText).build();
    }
    /**
     *  返回文档树
     */
    public DocTree docTrees() {
        File rootPathFile = new File(Constants.DOC_PATH);
        // bfs提供的路径  Tuple2<File,Integer> 文件/树深度
        Queue<DocTree> queue = new LinkedList<>();
        DocTree rootDocTree = new DocTree(rootPathFile);
        queue.add(rootDocTree);
        while (!queue.isEmpty()) {
            DocTree docTree = queue.poll();
            if (docTree.getPath() == null) continue;
            if (docTree.getPath().isDirectory() && docTree.getPath().listFiles() != null) {
                docTree.setKidTrees(docTree.getPath().listFiles());
                queue.addAll(docTree.getChildren());
            }
        }
        return rootDocTree;
    }

    public static void main(String[] args) {
        System.out.println(JSONObject.toJSONString(new DocService().docTrees()));
    }
}
