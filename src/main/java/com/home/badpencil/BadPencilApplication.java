package com.home.badpencil;

import com.home.badpencil.constans.Constants;
import com.home.badpencil.lucence.index.IndexDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BadPencilApplication {

    public static void main(String[] args) throws Exception{
        IndexDao indexDao = new IndexDao();
        indexDao.createIndex(Constants.DOC_PATH);
        SpringApplication.run(BadPencilApplication.class, args);
    }
}
