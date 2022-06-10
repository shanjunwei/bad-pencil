package com.home.badpencil.service;

import com.alibaba.fastjson.JSONObject;
import com.home.badpencil.constans.Constants;
import com.home.badpencil.pojo.doc.DocTree;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class DocService {
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
}
