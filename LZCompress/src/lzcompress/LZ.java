/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lzcompress;

import java.io.File;

/**
 *
 * @author Andrew
 */
public interface LZ {
    public void Compress(LZCompress ob);
    public void Extract(File f, LZCompress ob);
    public String getCode();
}
