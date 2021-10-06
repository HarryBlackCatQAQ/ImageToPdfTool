package com.hhr.controller;

import com.hhr.javaFx.MyStage;
import com.hhr.service.ImageToPdfToolService;
import com.lowagie.text.DocumentException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Harry
 * @Date: 2021/10/5 0:08
 * @Version 1.0
 */
public class MainController implements Initializable {
    private static VBox informationVBox2;
    private static ScrollPane scrollPane2;
    public static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

    @FXML
    private VBox informationVBox;

    @FXML
    private TextField imageFolderPathTextField;

    @FXML
    private TextField pdfFileOutputPathTextField;

    @FXML
    private TextField pdfFileNameTextField;

    @FXML
    private Button imageFolderPathButton;

    @FXML
    private Button pdfFileOutputPathButton;

    @FXML
    private Button startChangeButton;

    @FXML
    private ScrollPane scrollPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        informationVBox2 = informationVBox;
        scrollPane2 = scrollPane;
    }


    @FXML
    private void imageFolderPathButtonClicked(MouseEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(MyStage.getInstance().getStage());
        if(file == null){
            return;
        }
        imageFolderPathTextField.setText(file.getPath());
    }

    @FXML
    private void pdfFileOutputPathButtonClicked(MouseEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(MyStage.getInstance().getStage());
        if(file == null){
            return;
        }
        pdfFileOutputPathTextField.setText(file.getPath());
    }

    @FXML
    private void startChangeButtonClicked(MouseEvent event) {
        final String imageFolderPath = imageFolderPathTextField.getText();
        final String pdfFileOutputPath = pdfFileOutputPathTextField.getText();
        final String pdfName = pdfFileNameTextField.getText();

        if(isTextFieldEmpty(imageFolderPath)){
            showAlter(Alert.AlertType.WARNING,"图片路径为空!");
        }
        else if(isTextFieldEmpty(pdfFileOutputPath)){
            showAlter(Alert.AlertType.WARNING,"PDF输出路径为空!");
        }
        else if(isTextFieldEmpty(pdfName)){
            showAlter(Alert.AlertType.WARNING,"PDF名字为空!");
        }
        else if(isHavingBanSymbol(pdfName)){
            showAlter(Alert.AlertType.WARNING,"PDF名字中有违规字符:" + BAN_SYMBOL);
        }
        else{
//            System.err.println(pdfFileOutputPath  + File.separator + pdfName + ".pdf");
            informationVBox.getChildren().clear();
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ImageToPdfToolService.toPdf(imageFolderPath,pdfFileOutputPath  + File.separator + pdfName + ".pdf",pdfName);
                    } catch (IOException | DocumentException e) {
                        e.printStackTrace();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                showAlter(Alert.AlertType.ERROR,"转换失败!\n" + e.getMessage());
                            }
                        });
                    }
                }
            });

        }
    }

    /**
     * 若pdf名字为空提示用户填写pdf文件名字
     */
    private void showAlter(Alert.AlertType alterType, String information){
        Alert warning = new Alert(alterType,information);
        warning.initOwner(MyStage.getInstance().getStage());
        warning.show();
    }

    /**
     * 判断TextField中是否为空
     * @param folderPathOrFileName 文件路径或者文件名
     * @return 返回是否为空
     */
    private boolean isTextFieldEmpty(String folderPathOrFileName){
        return folderPathOrFileName == null || "".equals(folderPathOrFileName);
    }

    /**
     * 判断是否有禁止的字符存在文件名中 /\?*:"<>|
     */
    private static final String BAN_SYMBOL = "/\\?*:\"<>|";
    private boolean isHavingBanSymbol(String fileName){
        for(int i = 0;i < BAN_SYMBOL.length();i++){
            if(fileName.indexOf(BAN_SYMBOL.charAt(i)) >= 0){
                return true;
            }
        }

        return false;
    }

    public static void addVBoxInformation(final String information){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Text informationText = new Text(information);
                informationText.setWrappingWidth(666);
                informationVBox2.getChildren().add(informationText);
                scrollPane2.setVvalue(1.0);
            }
        });
    }
}
