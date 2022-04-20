package com.cy.yunyi.common.notify;

import lombok.Data;

/**
 * @Author caihx
 * @Description: 发生短信的返回结果
 * @Date 2022/2/7
 */
@Data
public class SmsResult {
    private boolean successful;
    private Object result;
}
