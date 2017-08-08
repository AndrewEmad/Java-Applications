/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lzcompress;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew
 */
public class SuffixTree {
    Map<Character ,SuffixTree>Children;
    int Code;
    
    SuffixTree(){
        Children = new HashMap<>();
        Code=-1;
    }
    
    
    public void insert(String word,int index, int code){
        if(index==word.length())
        {    
            Code=code;
            return;
        }
        if(!Children.containsKey(word.charAt(index)))
            Children.put(word.charAt(index), new SuffixTree());
        Children.get(word.charAt(index)).insert(word, index+1,code);
    }
    
    
    public int getCode(String word,int index){
        if(index==word.length())
            return Code;
        else if(!Children.containsKey(word.charAt(index)))
            return -1;
        return Children.get(word.charAt(index)).getCode(word, index+1);
    }
    
    
    public String getWord(String word,int code)
    {
        if(code==Code)
            return word;
        
        
        for(Map.Entry<Character,SuffixTree> element : Children.entrySet()){
            word+=element.getKey();
            String tmp_word=element.getValue().getWord(word, code);
            if(!tmp_word.equals(""))
                return tmp_word;
            word = new StringBuilder(word).deleteCharAt(word.length()-1).toString();
        }
        
        return "";
    }
    
}
