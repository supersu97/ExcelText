package com.su.text.controller;

import com.alibaba.fastjson.JSONObject;
import com.gzhc365.common.utils.DateTool;
import com.su.text.service.FinancialAuditService;
import com.su.text.util.ExcelUtil;
import com.su.text.vo.FinancialAccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("text")
public class TextController {

    @Autowired
    FinancialAuditService financialAuditService;

    @RequestMapping("/build")
    public void buildExcel(HttpServletRequest request, HttpServletResponse response){
        try {
            String path = "D:\\IDEA\\work_space\\text\\target\\classes\\static\\img\\img2.jpg";
            String mainTitle = "武汉华中科技大学-附属协和医院_职工缴费导出报表";
            List<FinancialAccountVo> list = financialAuditService.getAllList();
            Map<String,String> param = new HashMap<>();
            param.put("startTime","2020-10-09");
            param.put("endTime","2020-10-09");
            param.put("total", String.valueOf(list.size()));
            BigDecimal allFee = new BigDecimal("0.00");
            List<String[]> contents = new ArrayList<>();
            String[] titles = new String[9];
            titles[0] = "序号";
            titles[1] = "姓名";
            titles[2] = "发薪号";
            titles[3] = "缴费时间";
            titles[4] = "缴费金额";
            titles[5] = "缴费状态";
            titles[6] = "交易类型";
            titles[7] = "审核状态";
            titles[8] = "院区";

            for (FinancialAccountVo accountVo : list) {
                String[] finance = new String[9];
                finance[0] = accountVo.getId();
                finance[1] = accountVo.getUserName();
                finance[2] = accountVo.getPayNo();
                finance[3] = accountVo.getPaymentTime() == null ? "" : DateTool.getFullDateTime().format(accountVo.getPaymentTime());
                BigDecimal fee = new BigDecimal(accountVo.getTotalFee());
                fee = fee.divide(new BigDecimal("100.00"), 1, BigDecimal.ROUND_HALF_UP);
                finance[4] = fee.toString();
                allFee = allFee.add(fee);
                String payType = accountVo.getPayType();
                //订单状态（U：初始预登记;P：付款完成，调用医院支付接口中;S：订单成功;H：调用医院支付接口异常;Z：调用医院支付接口异常，重发次数超限;C：已取消;）
                if ("U".equals(payType)){
                    finance[5] = "未支付";
                }else if ("P".equals(payType)){
                    finance[5] = "付款完成";
                }else if ("S".equals(payType)){
                    finance[5] = "订单完成";
                }else if ("H".equals(payType)){
                    finance[5] = "调用医院支付接口异常";
                }else if ("Z".equals(payType)){
                    finance[5] ="缴费异常";
                }else if ("C".equals(payType)){
                    finance[5] = "已取消";
                }else if ("R".equals(payType)){
                    finance[5] = "有退费";
                }

                String bizType = accountVo.getBizType();
                if ("water_rate".equals(bizType)){
                    finance[6] = "水费";
                }else if("electricity_rate".equals(bizType)){
                    finance[6] = "电费";
                }

                String auditStatus = accountVo.getAuditStatus();
                if ("0".equals(auditStatus)){
                    finance[7] = "未审核";
                }else {
                    finance[7] = "已审核";
                }

                String extFieldsViews = accountVo.getExtFieldsViews();
                JSONObject json = JSONObject.parseObject(extFieldsViews);
                String apartmentName = json.getString("apartmentName");
                if ("1".equals(apartmentName)){
                    apartmentName = "院内房屋";
                }else if ("2".equals(apartmentName)){
                    apartmentName = "永红公寓";
                }
                String buildingName = json.getString("buildingName");
                String roomNumber = json.getString("roomNumber");
                finance[8] = apartmentName+" "+buildingName+" "+roomNumber;
                contents.add(finance);
            }

            param.put("totalFee",allFee.toString());
            response.setContentType("application/msexcel");// 定义输出类型
            response.setHeader("Content-disposition", "attachment; filename=" + new String(java.net.URLEncoder.encode("账单报表.xls", "UTF-8").getBytes("UTF-8"),"ISO8859-1"));
            ExcelUtil.buildExcel(response.getOutputStream(),mainTitle,param, titles, contents,path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
