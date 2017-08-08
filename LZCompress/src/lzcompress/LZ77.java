/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lzcompress;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrew
 */
public class LZ77 implements LZ{

    private String code;
    private String source;

    public LZ77(String s) {
        source = s;
        code = "";
    }

    public void Compress(LZCompress ob) {
        code = "";
        String tmp = "", ss = "";
        for (int i = 0; i < source.length(); ++i) {
            tmp = "";
            int lx = ss.length(),llx=ss.length();
            for (int j = i; j < source.length(); ++j) {
                ob.PRP(j * 1.0 / source.length() * 100);
                tmp += source.charAt(j);
                int x = ss.lastIndexOf(tmp);
                ss += source.charAt(j);

                if (x == -1 || j == source.length() - 1) {
                    code += (String.valueOf((llx - lx)) + " " + String.valueOf(tmp.length() - 1) + " " + String.valueOf((int) source.charAt(j)) + "\n");
                    i = j;
                    break;
                }
                lx = x;

            }
        }
        ob.PRP(100);
    }

    public String getCode() {
        return code;
    }

    public void Extract(File f, LZCompress ob) {
        Scanner s;
        code = "";
        try {
            s = new Scanner(f);
            int x, y, z;
            char c;
            while (s.hasNext()) {
                ob.PRP(1);
                x = s.nextInt();
                y = s.nextInt();
                z = s.nextInt();
                c = (char) z;
                if (x == 0 && y == 0) {
                    code += c;
                } 
                else{
                    int idx=code.length()-x;
                    for(int i=0;i<y;++i,idx++){
                        code+=code.charAt(idx);
                    }
                    code+=c;
                }

            }

            ob.PRP(100);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZ77.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
