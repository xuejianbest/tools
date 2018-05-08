package com.lwt.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lwt.util.TextFile;

public class GoodRead {
    private static final String tag_ahtml = "<!DOCTYPE html><html><head> <meta charset='UTF-8'> <link href='style.css' rel='stylesheet' type='text/css'/> </head> <body >";
    private static final String tag_bhtml = "</body></html>";
    private static final String tag_code = "blockquote";
    private static final String tag_p = "p";
    // private static final String tag_hr = "hr";
    private static final String tag_b = "strong";
    private static final String tag_div = "div";
    private static final String tag_h2 = "h2";
     private static final String tag_say = "span";

    private static final String sep = File.separatorChar + "";

    private static final String[] illegal = {"", ""};
    private static final String[] BIGNUM = {"０", "１", "２", "３", "４", "５", "６", "７", "８", "９", "　"};
    private static final String[] LITTLENUM = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", " "};
    
    private static final String src_file = "e:/1.txt";
    private static final String desc_dir = "e:/zzGoodRead"; // 如果抛出异常会产生临时文件，手动删除

    public static void main(String[] args) throws IOException {
        String desc_file = "";
        if (desc_dir.endsWith(sep)) {
            desc_file = desc_dir + "out.html";
        } else {
            desc_file = desc_dir + sep + "out.html";
        }

        File desc = new File(desc_dir);
        if (!desc.exists()) {
            desc.mkdir();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(tag_ahtml);

        TextFile textFile = new TextFile(src_file, "gbk");
        String content = textFile.readAllString();
        
        content = bjh(content);
        content = ol(content);
        content = title(content);
        content = say(content);
        
        
        content = makeP(content);
        
        sb.append(content);
        
        sb.append(tag_bhtml);
        PrintWriter out = new PrintWriter(desc_file);
        out.println(sb.toString());
        out.close();

        copyFile("source/style.css", desc_dir);
    }

    //有序列表
    public static String ol(String content) {
        String regex = "( *1.*\n)( *2.*\n)( *\\d.*\n)*";
        Pattern patt = Pattern.compile(regex);
        Matcher mat = patt.matcher(content);
        
        while(mat.find()){
            String ol = mat.group(0);
            String ol_new = ol.replaceAll(" *\\d. *(.*)\n", tag("li", "$1"));
            content = content.replace(ol, tag("ol", ol_new));
        }
        return content;
    }
    
    //说明或信件
    public static String reference(String content) {
        String regex = "( *1.*\n)( *2.*\n)( *\\d.*\n)*";
        Pattern patt = Pattern.compile(regex);
        Matcher mat = patt.matcher(content);
        
        while(mat.find()){
            String ol = mat.group(0);
            String ol_new = ol.replaceAll(" *\\d. *(.*)\n", tag("li", "$1"));
            content = content.replace(ol, tag("ol", ol_new));
        }
        return content;
    }
    
    //半角化
    public static String bjh(String content) {
        for(int i=0; i<BIGNUM.length; i++) {
            content = content.replace(BIGNUM[i], LITTLENUM[i]);
        }
        for(int i=0; i<illegal.length; i++) {
            content = content.replace(illegal[i], "");
        }
        
        return content;
    }
    
    
    //章节标题
    public static String title(String content) {
        content = content.replaceAll("第.{1,7}章[^\n]+", tag(tag_h2, "$0"));
        return content;
    }
    
    //引号里内容背景色
    public static String say(String content) {
//        String regex = "“”";
        content = content.replaceAll("“[^”]*”", tag(tag_say, "$0"));
        return content;
    }
    
    //形成自然段
    public static String makeP(String content) {
        content = content.replaceAll(".*\n+", tag(tag_p, "$0"));
        return content;
    }
    
    public static String tag(String tag, String content) {
        String res = aTag(tag) + content + bTag(tag);
        return res;
    }

    public static String aTag(String tag) {
        final String HEAD = "<";
        final String END = ">";
        return HEAD + tag + END;
    }

    public static String bTag(String tag) {
        final String HEAD = "</";
        final String END = ">";
        return HEAD + tag + END;
    }

    public static void copyFile(String srcFile, String descDir) throws IOException {
        File src = new File(srcFile);
        FileInputStream fin = new FileInputStream(src);
        FileOutputStream fout = new FileOutputStream(descDir + sep + src.getName());

        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        ByteBuffer byteBuf = ByteBuffer.allocate(1024);

        while (true) {
            byteBuf.clear();
            if (fcin.read(byteBuf) == -1)
                break;
            byteBuf.flip();
            fcout.write(byteBuf);
        }
        fin.close();
        fout.close();
    }
}
