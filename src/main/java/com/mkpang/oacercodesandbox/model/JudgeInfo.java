package com.mkpang.oacercodesandbox.model;

import lombok.Data;

/**
 * 判题信息
 *
 * @author <a href="https://github.com/k9evin">Mingkai Pang</a>
 */
@Data
public class JudgeInfo {

    /**
     * 程序运行结果
     */
    private String message;

    /**
     * 消耗内存（KB）
     */
    private Long memory;

    /**
     * 消耗时间（ms）
     */
    private Long time;
}
