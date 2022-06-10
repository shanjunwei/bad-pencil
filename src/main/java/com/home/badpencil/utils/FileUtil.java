package com.home.badpencil.utils;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    /*private static void createFileRecursion(String fileName,int depth) throws IOException {
       Path path = Paths.get(fileName);
       if (Files.exists(path)) return;
       if (!Files.exists(path.getParent())){
           // 递归创建文件夹
           createFile(path.getParent().toString(),++depth);
       }
       if(depth==0){
           Files.createFile(path);
       }else{
           Files.createDirectory(path);
       }
   }*/
    private static void createFile(String fileName) throws IOException {
        File file = new File(new File(fileName).getParent());
        file.mkdirs();
        Files.createFile(Paths.get(fileName));
    }
    public static void write(String path,String content) {
        try {
            createFile(path);
            RandomAccessFile raf2 = new RandomAccessFile(path, "rw");
            FileChannel channel2 = raf2.getChannel();
            channel2.write(ByteBuffer.wrap(content.getBytes("utf-8")));
            channel2.close();
            raf2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isReadFile(File file) {
        return !file.isHidden() && file.exists() && file.canRead();
    }

    public static boolean isMarkDown(File file) {
        return isReadFile(file) && file.getName().toLowerCase().endsWith(".md");
    }
}
