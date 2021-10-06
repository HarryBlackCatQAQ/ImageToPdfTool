package com.hhr.controller;

import com.hhr.javaFx.dialog.TaskAddDialog;
import com.hhr.javaFx.stage.InformationStage;
import com.hhr.javaFx.tableview.MyTableViewData;
import com.hhr.javaFx.tableview.TableViewTask;
import com.hhr.service.ImageToPdfToolService;
import com.hhr.thread.MyThreadPool;
import com.lowagie.text.DocumentException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: Harry
 * @Date: 2021/10/5 0:08
 * @Version 1.0
 */
public class MainController implements Initializable {
    private static TableView<TableViewTask> imageToPdfTableView2;

    @FXML
    private TableView<TableViewTask> imageToPdfTableView;

    @FXML
    private TableColumn<TableViewTask, String> imageFolderPathColumn;

    @FXML
    private TableColumn<TableViewTask, String> pdfFileOutputColumn;

    @FXML
    private TableColumn<TableViewTask, String> pdfFileNameColumn;

    @FXML
    private TableColumn<TableViewTask, String> isFinishedColumn;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button cleanAllTaskButton;

    @FXML
    private Button startChangeButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageFolderPathColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("imageFolderPath"));
        pdfFileOutputColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("pdfFileOutputPath"));
        pdfFileNameColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("pdfFileName"));
        isFinishedColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("isFinished"));
        imageToPdfTableView.setItems(MyTableViewData.getInstance().getData());

        imageToPdfTableView2 = imageToPdfTableView;
    }


    @FXML
    private void addTaskButtonClicked(MouseEvent event) {
//        System.err.println("addTaskButtonClicked");
        TaskAddDialog taskAddDialog = new TaskAddDialog();
        taskAddDialog.show();
    }

    @FXML
    private void cleanAllTaskButtonClicked(MouseEvent event) {
//        System.err.println("cleanAllTaskButtonClicked");
        MyTableViewData.getInstance().getData().clear();
        System.gc();
    }

    @FXML
    private void startChangeButtonClicked(MouseEvent event) {
//        System.err.println("startChangeButtonClicked");

        InformationStage.getInstance().show();

        for(int i = 0;i < MyTableViewData.getInstance().getData().size();i++){
            TableViewTask tableViewTask = MyTableViewData.getInstance().getData().get(i);
            final String imageFolderPath = tableViewTask.getImageFolderPath();
            final String pdfFileOutputPath = tableViewTask.getPdfFileOutputPath();
            final String pdfName = tableViewTask.getPdfFileName();

            if(!"100%".equals(tableViewTask.getIsFinished())){
                final int finalI = i;
                MyThreadPool.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ImageToPdfToolService.toPdf(imageFolderPath,pdfFileOutputPath  + File.separator + pdfName + ".pdf",pdfName, finalI);
                            System.gc();
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
        System.gc();
    }

    /**
     * 若pdf名字为空提示用户填写pdf文件名字
     */
    private void showAlter(Alert.AlertType alterType, String information){
        Alert warning = new Alert(alterType,information);
        warning.initOwner(InformationStage.getInstance().getStage());
        warning.show();
    }


    public static void refresh(){
        imageToPdfTableView2.refresh();
    }

}
