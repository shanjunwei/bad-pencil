package com.home.badpencil.pojo.doc;

import com.home.badpencil.utils.FileUtil;
import lombok.Data;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * 文档树
 */
@Data
public class DocTree {
    // 文件夹或者文件名
    private String title;
    private List<DocTree> children;
    private boolean isLeaf;
    private File path;

    public DocTree(File file) {
        this.title = fileNameWithoutSuffix(file);
        this.children = new ArrayList<>();
        this.isLeaf = !file.isDirectory();
        this.path = file;
    }
    /**
     *  设置一棵树
     */
    public void setATree(DocTree docTree) {
        this.children.add(docTree);
    }
    /**
     *  设置一系列子树
     */
    public void setKidTrees(File[] files) {
        for (File file :files){
            if((FileUtil.isReadFile(file))){
                setATree(new DocTree(file));
            }
        }
    }

    public static String fileNameWithoutSuffix(File file) {
        if(file == null ) return "";
        if(file.isDirectory())  return  file.getName();
        return file.getName().substring(0,file.getName().lastIndexOf("."));
    }
}
