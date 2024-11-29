package org.example.pdfCheck;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;

import java.io.File;
import java.io.IOException;


/**
 * Check JS script files in PDF files
 * @Author Ryan.Xia
 * @Date 2024-11-29
 */
public class PdfScriptCheck {

    final static String jsStr = "'JS |JavaScript |alert";


    /**
     * check pdf file
     * @param inputPath
     * @throws IOException
     */
    public static void verifyPdf(String inputPath) throws IOException {

        try (PDDocument document = PDDocument.load(new File(inputPath))) {

            System.out.println("------------------PDF check start------------------");
            // 检查OpenAction中的JS脚本
            checkJavaScriptInOpenAction(document);

            // 检查OpenAction中的JS脚本
            checkJavaScriptInNames(document);

//            TODO 后续继续补充 TYPE和PAGES的检测

//            System.out.println("------------------check TYPE------------------");
//            COSBase cosBaseType = pdDocumentCatalog.getCOSObject().getDictionaryObject(COSName.TYPE);
//            System.out.println("typesDict："+typesDict);


//            System.out.println("------------------check PAGES------------------");
//            COSDictionary pagesDict = (COSDictionary) catalog.getCOSObject().getDictionaryObject(COSName.PAGES);
//            System.out.println("namesDict："+pagesDict);

            System.out.println("------------------PDF check end------------------");


        }
    }

    /**
     * Check Names
     * @param document
     * @throws IOException
     */
    private static void checkJavaScriptInNames(PDDocument document) throws IOException {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        System.out.println("------------------check NAMES------------------");
        COSDictionary namesDict = (COSDictionary) catalog.getCOSObject().getDictionaryObject(COSName.NAMES);
        COSDictionary javaScriptDict = (COSDictionary) namesDict.getDictionaryObject(COSName.JAVA_SCRIPT);
        COSArray namesArray = (COSArray) javaScriptDict.getDictionaryObject(COSName.NAMES);
//            System.out.println("namesArray里面共有："+namesArray.size());
        if (namesArray.size() > 0) {
            for (COSBase one:namesArray) {
                if (one instanceof COSObject) {
                    System.out.println("type is COSObject");
                    COSObject jsObject = (COSObject) one;
                    COSBase jsValue = jsObject.getObject();
                    COSDictionary jsActionDict = (COSDictionary) jsValue;
                    String jsObj = jsActionDict.getString(COSName.JS);
                    checkJsStr(jsObj);
                } else if (one instanceof COSString) {
                    System.out.println("type is COSString");
                    String jsString = ((COSString) one).getString();
                    checkJsStr(jsString);
                }
            }
        }
    }

    /**
     * Check OpenAction
     * @param document
     * @throws IOException
     */
    private static void checkJavaScriptInOpenAction(PDDocument document) throws IOException {

        // 移除PDF中OpenAction里面包含alert敏感词
        PDAction openAction = (PDAction) document.getDocumentCatalog().getOpenAction();
        if (openAction instanceof PDActionJavaScript) {
            COSDictionary actionDict = openAction.getCOSObject();
            String jsCode = actionDict.getString(COSName.JS);
            checkJsStr(jsCode);
        }
    }


    /**
     * Check JS
     * @param jsString
     */
    private static void checkJsStr(String jsString) throws IOException {

        String[] jsArr = jsStr.split("\\|");
        for (int i = 0; i < jsArr.length; i++) {
            if (jsString.indexOf(jsArr[i]) > -1) {
                throw new IOException("Please note that there may be malicious scripts in the PDF!");
            }
        }

    }






}
