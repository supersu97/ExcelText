package com.su.text;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

@SpringBootTest
class TextApplicationTests {

    @Test
    void contextLoads() {
        String eleBill ="2060";
        String unit = "100.00";
        BigDecimal elePrice =new BigDecimal(eleBill);
        BigDecimal eleUnit = new BigDecimal(unit);


    }

    @Test
    void excel() throws FileNotFoundException {
        String filePath = ResourceUtils.getFile("classpath:static/img/img1.jpg").getPath();
        System.out.println(filePath);
    }

}
