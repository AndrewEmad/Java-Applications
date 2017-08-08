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
public class LZW implements LZ{
    private String code;
    private String source;

    public LZW(String s) 
    {
        source = s;
        code = "";
    }

    public void Compress(LZCompress ob)
    {
        SuffixTree Dictionary = new SuffixTree();
        int Current_Code=128;
        for(int i=0;i<source.length();++i)
        {
            int Last_Code=(int)(source.charAt(i));
            String Window=""+(source.charAt(i));
            if(i==source.length()-1)
            {
             code+=String.valueOf(Last_Code)+"\n";
            }
            for(int j=i+1;j<source.length();++j){
                ob.PRP(j * 1.0 / source.length() * 100);

                Window+=(source.charAt(j));
                int Tmp_Code=Dictionary.getCode(Window,0);
                if(Tmp_Code==-1)
                {
                   Dictionary.insert(Window, 0, Current_Code++);
                   code+=String.valueOf(Last_Code)+"\n";
                   i=j-1;
                   break;
                }
                
                Last_Code=Tmp_Code;
                
                if(j==source.length()-1)
                {   code+=String.valueOf(Last_Code)+"\n";
                    i=j;
                }
            }       
            
        }
        ob.PRP(100);

    }
    
    public String getCode() {
        return code;
    }
    
    public void Extract(File f, LZCompress ob) {
        
        try {
            SuffixTree Dictionary = new SuffixTree();
            code="";
            int Current_Code=128;
            Scanner input = new Scanner(f);
            String Last_Word="";
            while(input.hasNext())
            {
                 ob.PRP(1);
                 
                int tag=input.nextInt();
                if(tag<128)
                {  
                    code+=(char)tag;
                    Last_Word+=(char)tag;
                    if(Last_Word.length()>1)
                        Dictionary.insert(Last_Word, 0, Current_Code++);
                    Last_Word=""+(char)tag;
                }
                else
                {
                    String Current_Word=Dictionary.getWord("", tag);
                    if(Current_Word.equals(""))
                    {    
                        Current_Word=Last_Word+Last_Word.charAt(0);
                        Dictionary.insert(Current_Word, 0, Current_Code++);
                        code+=Current_Word;
                    }
                    else
                    {
                        code+=Current_Word;
                        Dictionary.insert(Last_Word+Current_Word.charAt(0), 0, Current_Code++);
                    }
                    Last_Word=Current_Word;

                }
              ob.PRP(100);

                
            }
            
            
            
            
            
            
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZW.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
