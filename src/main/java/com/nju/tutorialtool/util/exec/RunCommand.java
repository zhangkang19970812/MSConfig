package com.nju.tutorialtool.util.exec;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunCommand {
    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        return (!osName.contains("Windows"));
    }

    public static void runCommand(String path, String windowsCommand, String linuxCommand) {
        try {
            String[] cmd = new String[3];
            if (!isLinux()) {
                cmd[0] = "cmd";
                cmd[1] = "/c";
                cmd[2] = path.substring(0, path.indexOf(":") + 1) + " && cd " + path + " && " + windowsCommand;
            }
            else {
                String cd = "cd " + path;
                System.out.println(path);
                executeNewFlow(new ArrayList<>(Arrays.asList(cd, linuxCommand)));
                return;
            }
            Runtime runtime=Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            // 打印程序输出
            readProcessOutput(process);

            // 等待程序执行结束并输出状态
            int exitCode = process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> executeNewFlow(List<String> commands) {
        List<String> rspList = new ArrayList<String>();
        Runtime run = Runtime.getRuntime();
        try {
            Process proc = run.exec("/bin/bash", null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands) {
                out.println(line);
            }
            out.println("exit");// 这个命令必须执行，否则in流不结束。
            String rspLine = "";
            while ((rspLine = in.readLine()) != null) {
                System.out.println(rspLine);
                rspList.add(rspLine);
            }
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rspList;
    }

    /**
     * 打印进程输出
     *
     * @param process 进程
     */
    private static void readProcessOutput(final Process process) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        read(process.getInputStream(), System.out);
        read(process.getErrorStream(), System.err);
    }

    // 读取输入流
    private static void read(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
