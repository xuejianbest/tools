package com.lwt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextFile {

    private BufferedReader reader = null;
    private boolean fileOver = true;

    public TextFile(File file, String charset) throws IOException {
        FileInputStream is = new FileInputStream(file);
        reader = new BufferedReader(new InputStreamReader(is, charset));
    }
    
    public TextFile(String file_path, String charset) throws IOException {
        this(new File(file_path), charset);
    }
    
    public TextFile(String file) throws IOException {
        this(file, "utf-8");
    }
    
    public TextFile(File file) throws IOException {
        this(file, "utf-8");
    }
    

    // 返回文件一个非空行。
    public String readLine() throws IOException {
        String line = null;
        line = reader.readLine();
        while (line == null || "".equals(line.trim())) {
            if (line == null) {
                fileOver = false;
                return "";
            } else {
                line = reader.readLine();
            }
        }

        return line;
    }

    // 返回文件所有非空行。
    public List<String> readAllLines() throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        while (fileOver) {
            lines.add(readLine());
        }
        lines.remove(lines.size() - 1);
        return lines;
    }

    // 返回整个文件内容字符串（去掉空行的）。
    public String readAllString() throws IOException {
        StringBuilder content = new StringBuilder();
        while (fileOver) {
            content.append(readLine());
            content.append("\n");
        }
        content.delete(content.length() - 2, content.length());
        return content.toString();
    }

    public static void main(String[] args) throws Exception {
        String file = "d:/t.txt";
        TextFile tf = new TextFile(file);
        System.out.print(tf.readAllString());
    }
}
