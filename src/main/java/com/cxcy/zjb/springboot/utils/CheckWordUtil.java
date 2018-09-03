package com.cxcy.zjb.springboot.utils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

/**
 * 用于读出文件的内容，并且检测是否含有敏感词
 */
public class CheckWordUtil {

    /**
     * 审核doc的文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public String readFileAsDoc(String fileName) throws IOException {
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            String doc1 = doc.getDocumentText();
            System.out.println(doc1);
            fis.close();
            String msg = checkWord(doc1);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "文件格式有误，请重新上传";
        }
    }

    /**
     * 审核docx文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public String readFileAsDocx(String fileName) throws IOException {
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String doc = extractor.getText();
            System.out.println(doc);
            fis.close();
            String msg = checkWord(doc.toString());
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "文件格式有误，请重新上传";
        }
    }

    /**
     * 审核文件
     * @param str
     * @return
     */
    public static String checkWord(String str){
        SensitiveWord sw = new SensitiveWord("CensorWords.txt");
        sw.InitializationWork();
        str = sw.filterInfo(str);
        int size = sw.sensitiveWordList.size();
        System.out.println("语句中包含敏感词的个数为：" + sw.sensitiveWordList.size() + "。包含：" + sw.sensitiveWordList);
        if(size==0){
           return "文件审核通过";
        }else{
            return "文件含有敏感词，请重新修改上传";
        }

    }

}
