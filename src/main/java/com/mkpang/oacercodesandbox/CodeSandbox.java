package com.mkpang.oacercodesandbox;

import com.mkpang.oacercodesandbox.model.ExecuteCodeRequest;
import com.mkpang.oacercodesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
