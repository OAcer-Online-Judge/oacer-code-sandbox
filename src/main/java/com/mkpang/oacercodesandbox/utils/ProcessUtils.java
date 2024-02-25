package com.mkpang.oacercodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.mkpang.oacercodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 进程工具类
 */
public class ProcessUtils {

    /**
     * 执行进程并获取信息
     *
     * @param runProcess 运行的进程
     * @param opName     操作名称
     * @return 执行信息
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        if (runProcess == null) {
            throw new IllegalArgumentException("Process must not be null");
        }
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 等待程序执行，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            // 根据退出值判断是否正常退出
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 获取进程的输出信息
                List<String> outputStrList = readProcessOutput(runProcess.getInputStream());
                executeMessage.setMessage(String.join("\n", outputStrList));
            } else {
                System.out.println(opName + "失败，错误码： " + exitValue);
                // 获取进程的输出信息
                List<String> outputStrList = readProcessOutput(runProcess.getInputStream());
                executeMessage.setMessage(String.join("\n", outputStrList));
                // 获取进程的错误信息
                List<String> errorOutputStrList = readProcessError(runProcess.getErrorStream());
                executeMessage.setErrorMessage(String.join("\n", errorOutputStrList));
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }

    private static List<String> readProcessOutput(InputStream inputStream) throws IOException {
        List<String> outputStrList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String compileOutputLine;
        while ((compileOutputLine = bufferedReader.readLine()) != null) {
            outputStrList.add(compileOutputLine);
        }
        return outputStrList;
    }

    private static List<String> readProcessError(InputStream inputStream) throws IOException {
        List<String> errorOutputStrList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String errorCompileOutputLine;
        while ((errorCompileOutputLine = bufferedReader.readLine()) != null) {
            errorOutputStrList.add(errorCompileOutputLine);
        }
        return errorOutputStrList;
    }


    /**
     * 执行交互式进程并获取信息
     *
     * @param runProcess 运行的进程
     * @param args       传递给进程的参数
     * @return 执行结果
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
        if (runProcess == null) {
            throw new IllegalArgumentException("Process must not be null");
        }
        if (args == null || args.isEmpty()) {
            throw new IllegalArgumentException("Arguments must not be null or empty");
        }

        ExecuteMessage executeMessage = new ExecuteMessage();

        try (OutputStream outputStream = runProcess.getOutputStream();
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
            String[] s = args.split(" ");
            String join = StrUtil.join("\n", s) + "\n";
            outputStreamWriter.write(join);
            outputStreamWriter.flush();

            try (InputStream inputStream = runProcess.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutputLine);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
            }
        } catch (IOException e) {
            // 可以在这里记录日志或者通知用户
            e.printStackTrace();
        } finally {
            runProcess.destroy();
        }
        return executeMessage;
    }
}
