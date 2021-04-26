package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:32
 * @Description: TODO
 */
public class FileUtil {

    public static String getText(InputStream inputStream){
        InputStreamReader isr = null;
        BufferedReader bufferedReader = null;
        String jobConfigXml = "";
        try {
            isr = new InputStreamReader(inputStream,"utf-8");
            bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text = "";
            while ((text = bufferedReader.readLine())!=null){
                text = text + "\n";
                sb.append(text);
            }
            jobConfigXml = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(isr != null){
                try{
                    isr.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if (bufferedReader != null){
                try{
                    bufferedReader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return jobConfigXml;
    }

}
