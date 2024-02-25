package com.mkpang.oacercodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    /**
     * Execution output
     */
    private List<String> outputList;

    /**
     * Interface message
     */
    private String message;

    /**
     * Execution status
     */
    private Integer status;

    /**
     * Judge info
     */
    private JudgeInfo judgeInfo;

}
