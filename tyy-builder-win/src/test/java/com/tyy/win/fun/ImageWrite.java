package com.tyy.win.fun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ImageWrite {

    public static Set<String> exPath() throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cons)));
        Set<String> a = new HashSet<>();
        String l;
        while ((l = br.readLine()) != null) {
            l = l.trim();
            if (l.startsWith("public static final String ")) {
                a.add(l);
            }
        }
        br.close();
        return a;
    }

    public static void write(Set<String> l) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cons)));
        bw.write("package com.tyy.win.comm;\r\n" + "\r\n" + "public class Constant {\r\n\n");

        ArrayList<String> a = new ArrayList<>(l);
        a.sort(String::compareTo);
        for (String string : a) {

            bw.write("    " + string + "\n\n");
        }
        bw.write("}\n");
        bw.close();
    }

    public static String toKey(String p) {
        if (p.startsWith("images")) {
            p = p.substring(7, p.length());
        }
        p = p.substring(0, p.indexOf("."));
        // char[] charArray = p.toCharArray();
        StringBuffer sb = new StringBuffer();
        boolean iu = false;
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c == '_') {
                iu = true;
                continue;
            }
            if (iu) {
                c = Character.toUpperCase(c);
                iu = false;
            }
            sb.append(c);
        }
        return sb.toString().replace("/", "_");
    }

    public static void f(File f, String path, Consumer<String> cf) {
        path = path == "" ? f.getName() : path + "/" + f.getName();

        if (f.isDirectory()) {
            File[] listFiles = f.listFiles();
            for (File file : listFiles) {
                f(file, path, cf);
            }
        } else {
            cf.accept(path);
        }
    }

    public static File p;

    public static File imgs;

    public static File cons;
    static {
        try {
            p = new File(".").getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgs = new File(p, "src\\main\\resources\\static\\images");
        cons = new File(p, "src\\main\\java\\com\\tyy\\win\\comm\\Constant.java");
    }

    public static void main(String[] args) throws Exception {
        Set<String> exPath = exPath();
        f(imgs, "", a -> {
            a = "public static final String " + toKey(a) + " = \"" + a + "\";";
            exPath.add(a);
        });
        write(exPath);
    }

}
