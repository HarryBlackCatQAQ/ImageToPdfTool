package com.hhr.service;

import com.hhr.controller.MainController;
import com.hhr.controller.TaskInformationController;
import com.hhr.javaFx.tableview.MyTableViewData;
import com.hhr.jf.annotation.JfAutowired;
import com.hhr.jf.annotation.JfService;
import com.hhr.thread.MyJavaFxThreadPool;
import com.hhr.jf.SingletonFactory;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: Harry
 * @Date: 2021/10/5 1:38
 * @Version 1.0
 */

@JfService
public class ImageToPdfToolService {

    @JfAutowired
    private MainController mainController;

    @JfAutowired
    private TaskInformationController taskInformationController;

    /**
     *
     * @param imageFolderPath
     *            图片文件夹地址
     * @param pdfPath
     *            PDF文件保存地址
     *
     */
    public void toPdf(String imageFolderPath, String pdfPath,String pdfName,int dataIndex) throws IOException, DocumentException {
        long time1 = System.currentTimeMillis();

        // 图片地址
        String imagePath = null;

        // 输入流
        FileOutputStream fos = new FileOutputStream(pdfPath);
        // 创建文档
        Document doc = new Document(null, 0, 0, 0, 0);
        // 写入PDF文档
        PdfWriter.getInstance(doc, fos);
        // 读取图片流
        BufferedImage img = null;
        // 实例化图片
        Image image = null;
        // 获取图片文件夹对象
        File file = new File(imageFolderPath);
        File[] files = file.listFiles();


        List fileList = Arrays.asList(files);

        Collections.sort(fileList, new Comparator() {
            @Override
            public int compare(Object _o1, Object _o2) {
                File o1 = (File)_o1;
                File o2 = (File)_o2;

                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }

                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }

//                System.err.println(changFileName(o1.getName()) +  " " + changFileName(o2.getName()));
                return changFileName(o1.getName()).compareTo(changFileName(o2.getName()));
            }

        });

        //计算数量
        int imagesCounter = 0;
        for (File file1 : files) {
            if (file1.getName().endsWith(".png")
                    || file1.getName().endsWith(".jpg")
                    || file1.getName().endsWith(".gif")
                    || file1.getName().endsWith(".jpeg")
                    || file1.getName().endsWith(".tif")) {
                imagesCounter++;
            }
        }

        // 循环获取图片文件夹内的图片
        int dealCounter = 0;
        for (File file1 : files) {
            if (file1.getName().endsWith(".png")
                    || file1.getName().endsWith(".jpg")
                    || file1.getName().endsWith(".gif")
                    || file1.getName().endsWith(".jpeg")
                    || file1.getName().endsWith(".tif")) {
//                 System.out.println(file1.getName());
                imagePath = imageFolderPath + File.separator + file1.getName();
//                System.out.println(file1.getName());
//                TaskInformationController.addVBoxInformation(pdfName + ".pdf文件添加图片" + file1.getName());
                taskInformationController.addVBoxInformation(pdfName + ".pdf文件添加图片" + file1.getName());

                // 读取图片流
                img = ImageIO.read(new File(imagePath));
                // 根据图片大小设置文档大小
                doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                // 实例化图片
                image = Image.getInstance(imagePath);
                // 添加图片到文档
                doc.open();
                doc.add(image);

                dealCounter++;

//                System.err.println(MyTableViewData.getInstance().getData().get(dataIndex));
                SingletonFactory.getInstance(MyTableViewData.class).getData().get(dataIndex).setIsFinished(cal(dealCounter,imagesCounter));
                SingletonFactory.getInstance(MyJavaFxThreadPool.class).javaFxExecute(new Runnable() {
                    @Override
                    public void run() {
                        mainController.refresh();
//                        MainController.refresh();
                    }
                });
            }
        }
        // 关闭文档
        doc.close();

        long time2 = System.currentTimeMillis();
        int time = (int) ((time2 - time1) / 1000);
//        TaskInformationController.addVBoxInformation("执行了:" + time + "秒!" + "  共" + imagesCounter + "张图片," + pdfName + ".pdf生成成功!");
        taskInformationController.addVBoxInformation("执行了:" + time + "秒!" + "  共" + imagesCounter + "张图片," + pdfName + ".pdf生成成功!");
    }

    /**
     * 对有数字标号的文件进行前置0的添加
     * @param fileName
     * @return
     */
    private String changFileName(String fileName){
        int startIndex = 0;
        for(int i = 0;i < fileName.length();i++){
            char c = fileName.charAt(i);
            if(isNumber(c)){
                startIndex = i;
                break;
            }
        }

        if(isNumber(fileName.charAt(startIndex + 1))){
            return fileName;
        }

        return fileName.substring(0,startIndex) + "0" + fileName.substring(startIndex);
    }

    private boolean isNumber(char c){
        return c >= '0' && c <= '9';
    }

    private int cal(int x,int sum){
        double ans = (x * 1.0) / (sum * 1.0) * 100;
        return (int)ans;
    }
}
