package com.lwt.tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.lwt.util.DirUtil;
import com.lwt.util.TextFile;

public class Fenci {

    public static void main(String[] args) throws IOException {
        long time1 = System.currentTimeMillis();
        String dir_path = "E:\\Download\\电子书\\网络\\";
        DirUtil du = new DirUtil(new File(dir_path));
        File[] files = du.getAllFiles();

        Map<String, Long> map = new HashMap<>();
        for (File file : files) {
            TextFile tf = new TextFile(file);
            List<String> lines = tf.readAllLines();

            for (String line : lines) {
                List<Term> termList = HanLP.segment(line);
                CoreStopWordDictionary.apply(termList);

                for (Term term : termList) {
                    String word = term.toString();
                    if (map.containsKey(word)) {
                        map.put(word, map.get(word) + 1);
                    } else {
                        map.put(word, 1L);
                    }
                }
            }
            
        }
        
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Long> kv : map.entrySet()) {
            String kvstr = kv.getKey() + "," + kv.getValue();

            sb.append(kvstr + "\n");
        }

        PrintWriter out = new PrintWriter("test.txt");
        out.println(sb.toString());
        out.close();
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time1);
    }

}
