package com.su.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gzhc365.common.utils.DateTool;
import com.su.text.bio.client.Client;
import com.su.text.bio.server.ServerBetter;
import com.su.text.util.Calculator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

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

    @Test
    void spilt(){
        String idStr = "1,2,3,4,5,6";
        String[] split = idStr.split(",");
        System.out.println(split);
    }


    @Test
    void sort(){
        String time = "1";
        String time2 = "2";
        String time3 = "1";
        String time4 = "2";
        String time5 = "1";
        String time6 = "2";
        List<String> list = new ArrayList<>();
        list.add(time);
        list.add(time2);
        list.add(time3);
        list.add(time4);
        list.add(time5);
        list.add(time6);
        list.sort(String::compareTo);
        for (String s : list) {
            System.out.println(s);
        }

    }

    @Test
    public void getWeek() throws ParseException {
        System.out.println(String.valueOf(getWeekDay(DateTool.getFullDate().parse("2020-11-02"))));
    }

    /**
     * 获取星期数
     * @param date
     * @return
     */
    private Integer getWeekDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekDay=cal.get(Calendar.DAY_OF_WEEK)-1;
        if(weekDay==0) weekDay=7;
        return weekDay;
    }


    @Test
    public void dateFormat() throws ParseException {
        System.out.println(DateTool.getFullDate().parse(DateTool.getFullDate().format(new Date())));
    }

    @Test
    public void payMethod(){
        String hisOrdNum = "S0299086|S0299091";
        JSONObject extField = new JSONObject();
        extField.put("S0299086","46.17");
        extField.put("S0299091","2.52");
        System.out.println(getPayMethod(hisOrdNum,extField));
    }


    /**
     * 生成支付数据
     * @param hisOrdNum
     * @param extField
     * @return
     */
    private String getPayMethod(String hisOrdNum, JSONObject extField) {
        hisOrdNum = hisOrdNum.replace("|",",");
        String[] split = hisOrdNum.split(",");
        StringBuilder payMethod = new StringBuilder();
        payMethod.append(hisOrdNum).append("@@");
        for (int i = 0; i < split.length; i++) {
            String billNo = split[i];
            String payFee = extField.getString(billNo);
            payMethod.append("3").append("$$").append(payFee).append("$$").append(billNo);
            if (i != split.length - 1){
                payMethod.append("##");
            }
        }
        return payMethod.toString();
    }


    @Test
    public void testCalculator() throws ScriptException {
        System.out.println(Calculator.cal("4+10").toString());

    }

    @Test
    public void bioTest() throws InterruptedException {
        //运行服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerBetter.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //避免客户端先于服务器启动前执行代码
        Thread.sleep(100);
        //运行客户端
        char operators[] = {'+','-','*','/'};
        Random random = new Random(System.currentTimeMillis());
        new Thread(new Runnable() {
            @SuppressWarnings("static-access")
            @Override
            public void run() {
                while(true){
                    //随机产生算术表达式
                    String expression = random.nextInt(10)+""+operators[random.nextInt(4)]+(random.nextInt(10)+1);
                    Client.send(expression);
                    try {
                        Thread.currentThread().sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
