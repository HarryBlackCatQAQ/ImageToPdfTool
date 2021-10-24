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
import java.util.*;

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


        List<File> fileList = Arrays.asList(files);

        sortFileList(fileList);

        //计算数量
        int imagesCounter = 0;
        for (File file1 : files) {
            if (isPicture(file1.getName())) {
                imagesCounter++;
            }
        }

        // 循环获取图片文件夹内的图片
        int dealCounter = 0;
        for (File file1 : files) {
            if (isPicture(file1.getName())) {
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
     * 对图片的名字进行排序
     */
    private void sortFileList(List<File> fileList){
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

                return changFileName(o1.getName()).compareTo(changFileName(o2.getName()));
            }

        });
    }

    /**
     * 对有数字标号的文件进行前置0的添加
     * @param fileName
     * @return
     */
    private String changFileName(String fileName){
        if(!fileName.contains(".")){
            return fileName;
        }

        fileName = fileName.substring(0,fileName.indexOf("."));

        StringBuilder name = new StringBuilder("");

        List<List<Character>> lists = new ArrayList<>();
        lists.add(new ArrayList<>());
        int preIndex = -1;
        for(int i = 0;i < fileName.length();i++){
            char c = fileName.charAt(i);

            if(isNumber(c)){
                int index = lists.size() - 1;
                if(preIndex == -1){
                    lists.get(index).add(c);
                }
                else if(i - preIndex == 1){
                    lists.get(index).add(c);
                }
                else{
                    lists.add(new ArrayList<>());
                    lists.get(index + 1).add(c);
                }

                preIndex = i;
            }
        }

        for(List<Character> list : lists){
            int size = 20 - list.size();

            for(int i = 0;i < size;i++){
                name.append(0);
            }

            for(char c : list){
                name.append(c);
            }
        }

        return name.toString();
    }

    private boolean isNumber(char c){
        return c >= '0' && c <= '9';
    }

    private int cal(int x,int sum){
        double ans = (x * 1.0) / (sum * 1.0) * 100;
        return (int)ans;
    }

    /**
     * 判断该图片是否是图片 以后缀为判断方法
     */
    private boolean isPicture(String fileName){
        return fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".tif");
    }

    /**
     * 返回没有后缀的图片名字
     */
    private String getPictureName(String fileName){
        return fileName.substring(0,fileName.indexOf('.'));
    }

}
