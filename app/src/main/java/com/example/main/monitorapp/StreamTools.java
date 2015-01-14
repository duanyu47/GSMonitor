package com.example.main.monitorapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	
	public static byte[] isToData(InputStream is) throws IOException{  
        ByteArrayOutputStream bops = new ByteArrayOutputStream();  
        byte buffer[] = new byte[1024];  
        int len = 0;  
        while ((len = is.read(buffer)) != -1) {  
            bops.write(buffer, 0, len);  
        }  
        byte data[] = bops.toByteArray();  
        return data;  
    }  
}
