package com.su.text.excel.service;

import com.su.text.vo.DoctorVo;
import com.su.text.vo.RegSource;

import java.util.List;
import java.util.Map;

public interface DoctorService {
    /**
     * 获取医生列表
     * @param hisId
     * @param deptCode
     * @return
     */
    List<DoctorVo> getDoctorList(Integer hisId,String deptCode);

    /**
     * 获取医生详细信息
     * @param doctorCode
     * @return
     */
    DoctorVo getDoctorDetail(String doctorCode);

    /**
     * 根据号源查询医生列表
     * @param regSources
     * @return
     */
    List<DoctorVo> getListByRegSource(List<RegSource> regSources);

    /**
     * 根据关键字查询医生信息
     * @return
     */
    DoctorVo getDoctorInfoByKey(Map<String,String> param);
}
