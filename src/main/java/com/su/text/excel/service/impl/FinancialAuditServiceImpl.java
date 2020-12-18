package com.su.text.excel.service.impl;


import com.su.text.excel.service.FinancialAuditService;
import com.su.text.vo.FinancialAccountVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FinancialAuditServiceImpl implements FinancialAuditService {
    @Override
    public List<FinancialAccountVo> getAllList() {
        List<FinancialAccountVo> list = new ArrayList<>();

        FinancialAccountVo financialAccountVo = new FinancialAccountVo();
        financialAccountVo.setId("1");
        financialAccountVo.setId("测试");
        financialAccountVo.setPayNo("111");
        financialAccountVo.setPaymentTime(new Date());
        financialAccountVo.setTotalFee("2000");
        financialAccountVo.setPayType("P");
        financialAccountVo.setBizType("water_rate");
        financialAccountVo.setAuditStatus("1");
        financialAccountVo.setExtFieldsViews("{\"apartmentName\":\"1\",\"buildingName\":\"20栋\",\"roomNumber\":\"20-A\"}");


        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);
        list.add(financialAccountVo);

        return list;
    }
}
