//package com.hhr.thread;
//
//import com.hhr.javaFx.stage.MyStage;
//import com.hhr.javaFx.dialog.TaskAddDialog;
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TextField;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import javafx.stage.DirectoryChooser;
//
//import java.io.File;
//import java.net.URL;
//import java.util.ResourceBundle;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * @Author: Harry
// * @Date: 2021/10/5 0:08
// * @Version 1.0
// */
//public class MainController2 implements Initializable {
//    private static VBox informationVBox2;
//    private static ScrollPane scrollPane2;
//    public static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
//
//    @FXML
//    private VBox informationVBox;
//
//    @FXML
//    private TextField imageFolderPathTextField;
//
//    @FXML
//    private TextField pdfFileOutputPathTextField;
//
//    @FXML
//    private TextField pdfFileNameTextField;
//
//    @FXML
//    private Button imageFolderPathButton;
//
//    @FXML
//    private Button pdfFileOutputPathButton;
//
//    @FXML
//    private Button startChangeButton;
//
//    @FXML
//    private ScrollPane scrollPane;
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        informationVBox2 = informationVBox;
//        scrollPane2 = scrollPane;
//    }
//
//
//    @FXML
//    private void imageFolderPathButtonClicked(MouseEvent event) {
//        DirectoryChooser dirChooser = new DirectoryChooser();
//        File file = dirChooser.showDialog(MyStage.getInstance().getStage());
//        if(file == null){
//            return;
//        }
//        imageFolderPathTextField.setText(file.getPath());
//    }
//
//    @FXML
//    private void pdfFileOutputPathButtonClicked(MouseEvent event) {
//        DirectoryChooser dirChooser = new DirectoryChooser();
//        File file = dirChooser.showDialog(MyStage.getInstance().getStage());
//        if(file == null){
//            return;
//        }
//        pdfFileOutputPathTextField.setText(file.getPath());
//    }
//
//    @FXML
//    private void startChangeButtonClicked(MouseEvent event) {
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                Platform.runLater(new Runnable() {
////                    @Override
////                    public void run() {
////                        Stage stage = new Stage();
////                        stage.show();
////                    }
////                });
////            }
////        }).start();
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                TaskAddDialog taskAddDialog = new TaskAddDialog();
//                taskAddDialog.show();
//            }
//        });
////        final String imageFolderPath = imageFolderPathTextField.getText();
////        final String pdfFileOutputPath = pdfFileOutputPathTextField.getText();
////        final String pdfName = pdfFileNameTextField.getText();
////
////        if(isTextFieldEmpty(imageFolderPath)){
////            showAlter(Alert.AlertType.WARNING,"??????????????????!");
////        }
////        else if(isTextFieldEmpty(pdfFileOutputPath)){
////            showAlter(Alert.AlertType.WARNING,"PDF??????????????????!");
////        }
////        else if(isTextFieldEmpty(pdfName)){
////            showAlter(Alert.AlertType.WARNING,"PDF????????????!");
////        }
////        else if(isHavingBanSymbol(pdfName)){
////            showAlter(Alert.AlertType.WARNING,"PDF????????????????????????:" + BAN_SYMBOL);
////        }
////        else{
//////            System.err.println(pdfFileOutputPath  + File.separator + pdfName + ".pdf");
////            informationVBox.getChildren().clear();
////            fixedThreadPool.execute(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        ImageToPdfToolService.toPdf(imageFolderPath,pdfFileOutputPath  + File.separator + pdfName + ".pdf",pdfName);
////                    } catch (IOException | DocumentException e) {
////                        e.printStackTrace();
////                        Platform.runLater(new Runnable() {
////                            @Override
////                            public void run() {
////                                showAlter(Alert.AlertType.ERROR,"????????????!\n" + e.getMessage());
////                            }
////                        });
////                    }
////                }
////            });
////
////        }
//    }
//
//    /**
//     * ???pdf??????????????????????????????pdf????????????
//     */
//    private void showAlter(AlertType alterType, String information){
//        Alert warning = new Alert(alterType,information);
//        warning.initOwner(MyStage.getInstance().getStage());
//        warning.show();
//    }
//
//    /**
//     * ??????TextField???????????????
//     * @param folderPathOrFileName ???????????????????????????
//     * @return ??????????????????
//     */
//    private boolean isTextFieldEmpty(String folderPathOrFileName){
//        return folderPathOrFileName == null || "".equals(folderPathOrFileName);
//    }
//
//    /**
//     * ???????????????????????????????????????????????? /\?*:"<>|
//     */
//    private static final String BAN_SYMBOL = "/\\?*:\"<>|";
//    private boolean isHavingBanSymbol(String fileName){
//        for(int i = 0;i < BAN_SYMBOL.length();i++){
//            if(fileName.indexOf(BAN_SYMBOL.charAt(i)) >= 0){
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public static void addImageToPdfTask(){
//
//    }
//
//    public static void addVBoxInformation(final String information){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                Text informationText = new Text(information);
//                informationText.setWrappingWidth(666);
//                informationVBox2.getChildren().add(informationText);
//                scrollPane2.setVvalue(1.0);
//            }
//        });
//    }
//}
