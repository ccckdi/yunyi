package com.cy.yunyi.admin.service;


import com.cy.yunyi.admin.dto.OssCallbackResult;
import com.cy.yunyi.admin.dto.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: chx
 * @Description: oss上传管理Service
 * @DateTime: 2021/11/30 11:00
 **/
public interface OssService {
        /**
     * oss上传策略生成
     */
    OssPolicyResult policy();
    /**
     * oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);
}
