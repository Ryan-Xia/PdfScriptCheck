package org.example;

import org.example.pdfCheck.PdfScriptCheck;

import java.io.IOException;

/**
 * test
 */
public class Main {
    public static void main(String[] args) {
        //2024年度内控评价咨询服务项目_采购项目需求表.pdf
//        String inputPath = "/Users/ryanxia/Desktop/2024年度内控评价咨询服务项目_采购项目需求表.pdf";
//        String inputPath = "/Users/ryanxia/Desktop/部分基层单位宣传阵地建设项目内部资格与能力审查报告(1).pdf";
        String inputPath = "/Users/ryanxia/Desktop/errorPdf/testalert1.pdf";
        try {
            PdfScriptCheck.verifyPdf(inputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}