package org.example.pdfCheck;

import cn.hutool.core.util.ObjectUtil;
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

        PDDocument document = PDDocument.load(new File(inputPath));
        System.out.println("------------------PDF CHECK START------------------");
        // 检查OpenAction中的JS脚本
        checkJavaScriptInOpenAction(document);

        // 检查Names中的JS脚本
        checkJavaScriptInNames(document);

        // 检查TYPE中的JS脚本
        checkJavaScriptInType(document);

        // 检查PAGES中的JS脚本
        checkJavaScriptInPages(document);
        System.out.println("------------------PDF CHECK END------------------");

    }


    /**
     * Check PAGES
     * @param document
     * @throws IOException
     */
    private static void checkJavaScriptInPages(PDDocument document) throws IOException {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        System.out.println("------------------CHECK PAGES------------------");
        COSDictionary pagesDict = (COSDictionary) catalog.getCOSObject().getDictionaryObject(COSName.PAGES);
//        System.out.println("namesDict2："+pagesDict);

        COSArray kidsArray = (COSArray) pagesDict.getDictionaryObject(COSName.KIDS);
//        System.out.println("kidsArray："+kidsArray);

        if (kidsArray.size() > 0) {
            for (COSBase one:kidsArray) {
                COSObject cosObject = (COSObject) one;

                COSDictionary cosDictionaryAA = (COSDictionary) cosObject.getDictionaryObject(COSName.AA);
                if (ObjectUtil.isNotEmpty(cosDictionaryAA)) {
                    COSDictionary cosDictionaryO = (COSDictionary) cosDictionaryAA.getDictionaryObject(COSName.O).getCOSObject();
                    COSBase cosBase = cosDictionaryO.getDictionaryObject(COSName.JS);
                    String jsString = ((COSString) cosBase).getString();
                    checkJsStr(jsString);
                }

                // 后续处理这些类型中的JS文件
                // 对象格式
//                COSDictionary cosDictionaryCONTENTS = (COSDictionary) cosObject.getDictionaryObject(COSName.CONTENTS);
//                COSDictionary cosDictionaryPARENT = (COSDictionary) cosObject.getDictionaryObject(COSName.PARENT);
//                COSDictionary cosDictionaryRESOURCES = (COSDictionary) cosObject.getDictionaryObject(COSName.RESOURCES);

                // 数组格式
//                COSDictionary cosDictionaryANNOTS = (COSDictionary) cosObject.getDictionaryObject(COSName.ANNOTS);
 //                COSDictionary cosDictionaryMEDIA_BOX = (COSDictionary) cosObject.getDictionaryObject(COSName.MEDIA_BOX);

                // COSNAME格式
                //                COSDictionary cosDictionaryTYPE = (COSDictionary) cosObject.getDictionaryObject(COSName.TYPE);
                System.out.println("------------------CHECK PAGES END------------------");


            }
        }



    }


    /**
     * Check TYPE
     * @param document
     * @throws IOException
     */
    private static void checkJavaScriptInType(PDDocument document) throws IOException {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        System.out.println("------------------CHECK Type------------------");
        COSBase cosBaseType = catalog.getCOSObject().getDictionaryObject(COSName.TYPE);
        if (ObjectUtil.isNotEmpty(cosBaseType)) {
            if (cosBaseType instanceof COSName) {
                COSName cosName = (COSName) cosBaseType;
                String jsObj = cosName.getName();
                checkJsStr(jsObj);
            }
        }
    }



    /**
     * Check Names
     * @param document
     * @throws IOException
     */
    private static void checkJavaScriptInNames(PDDocument document) throws IOException {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        System.out.println("------------------CHECK NAMES------------------");
        COSDictionary namesDict = (COSDictionary) catalog.getCOSObject().getDictionaryObject(COSName.NAMES);

        if (ObjectUtil.isNotEmpty(namesDict)) {
            COSDictionary javaScriptDict = (COSDictionary) namesDict.getDictionaryObject(COSName.JAVA_SCRIPT);
            if (ObjectUtil.isNotEmpty(javaScriptDict)) {
                COSArray namesArray = (COSArray) javaScriptDict.getDictionaryObject(COSName.NAMES);
//            System.out.println("namesArray里面共有："+namesArray.size());
                if (namesArray.size() > 0) {
                    for (COSBase one:namesArray) {
                        if (one instanceof COSObject) {
                            COSObject jsObject = (COSObject) one;
                            COSBase jsValue = jsObject.getObject();
                            COSDictionary jsActionDict = (COSDictionary) jsValue;
                            String jsObj = jsActionDict.getString(COSName.JS);
                            checkJsStr(jsObj);
                        } else if (one instanceof COSString) {
                            String jsString = ((COSString) one).getString();
                            checkJsStr(jsString);
                        }
                    }
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
        if (ObjectUtil.isNotEmpty(openAction)) {
            if (openAction instanceof PDActionJavaScript) {
                COSDictionary actionDict = openAction.getCOSObject();
                String jsCode = actionDict.getString(COSName.JS);
                checkJsStr(jsCode);
            }
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
                throw new IOException("请注意，PDF中可能存在恶意脚本！");
            }
        }

    }






}
