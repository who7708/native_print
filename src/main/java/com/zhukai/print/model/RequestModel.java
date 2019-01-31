package com.zhukai.print.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhukai
 * @date 2019/1/31
 */
@Getter
@Setter
@ToString
public class RequestModel implements Serializable {

    /**
     * ת����url
     */
    private String url;

    /**
     * ��ӡ������
     */
    private String printerType;

    /**
     * ��������
     */
    private String docType;
}
