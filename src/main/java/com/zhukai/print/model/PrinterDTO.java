package com.zhukai.print.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhukai
 * @date 2019/1/30
 */
@Getter
@Setter
@ToString
public class PrinterDTO implements Serializable {

    /**
     * 序号
     */
    private Integer seqNum;

    /**
     * 打印机名称
     */
    private String printerName;
}
