package org.example;

import org.example.pdfCheck.PdfScriptCheck;

import java.io.IOException;

/**
 * test
 */
public class Main {
    public static void main(String[] args) {
        String inputPath = "/Users/ryanxia/Desktop/testalert1.pdf";
        try {
            PdfScriptCheck.verifyPdf(inputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}