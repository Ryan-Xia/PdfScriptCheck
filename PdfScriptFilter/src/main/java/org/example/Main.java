package org.example;

import org.example.pdfCheck.PdfScriptCheck;

import java.io.IOException;

/**
 * test
 */
public class Main {
    public static void main(String[] args) {
        String inputPath = "/Users/ryanxia/Desktop/errorPdf/31ad2c0b-eef5-4e6d-b958-eb42b61cb305.pdf";
        try {
            PdfScriptCheck.verifyPdf(inputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}