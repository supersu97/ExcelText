package com.su.text.excel.service;

import com.su.text.vo.DeptVo;

import java.util.List;

public interface DeptService {

    /**
     * 查询医生列表
     * @param hisId
     * @return
     */
    List<DeptVo> getDeptList(Integer hisId);


}
