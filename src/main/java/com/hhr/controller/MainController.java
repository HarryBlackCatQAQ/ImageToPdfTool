package com.hhr.controller;

import com.hhr.javaFx.dialog.MultiEditPdfFileOutputPathDialog;
import com.hhr.javaFx.dialog.TaskAddDialog;
import com.hhr.javaFx.stage.InformationStage;
import com.hhr.javaFx.tableview.MyTableViewData;
import com.hhr.javaFx.tableview.TableViewTask;
import com.hhr.jf.annotation.JfAutowired;
import com.hhr.jf.annotation.JfController;
import com.hhr.service.ImageToPdfToolService;
import com.hhr.thread.MyFixedThreadPool;
import com.hhr.jf.SingletonFactory;
import com.hhr.view.MainView;
import com.lowagie.text.DocumentException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: Harry
 * @Date: 2021/10/5 0:08
 * @Version 1.0
 */
@JfController
public class MainController extends MainView implements Initializable {
    private static TableView<TableViewTask> imageToPdfTableView2;

    @JfAutowired
    private ImageToPdfToolService imageToPdfToolService;

    @JfAutowired
    private MultiEditPdfFileOutputPathDialog multiEditPdfFileOutputPathDialog;

    @JfAutowired
    private InformationStage informationStage;

    @JfAutowired
    private TaskAddDialog taskAddDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getImageFolderPathColumn().setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("imageFolderPath"));
        setTableViewColumnEditAction(this.getImageFolderPathColumn(),TableViewTask.IMAGE_FOLDER_PATH);
        this.getPdfFileOutputColumn().setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("pdfFileOutputPath"));
        setTableViewColumnEditAction(this.getPdfFileOutputColumn(),TableViewTask.PDF_FILE_OUTPUT_PATH);
        this.getPdfFileNameColumn().setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("pdfFileName"));
        setTableViewColumnEditAction( this.getPdfFileNameColumn(),TableViewTask.PDF_FILE_NAME);
        this.getIsFinishedColumn().setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("isFinished"));
        this.getImageToPdfTableView().setItems(SingletonFactory.getInstance(MyTableViewData.class).getData());

        imageToPdfTableView2 =  this.getImageToPdfTableView();
    }


    @FXML
    private void addTaskButtonClicked(MouseEvent event) {
//        System.err.println("addTaskButtonClicked");
//        TaskAddDialog taskAddDialog = new TaskAddDialog();
        taskAddDialog.show();
    }

    @FXML
    private void cleanAllTaskButtonClicked(MouseEvent event) {
//        System.err.println("cleanAllTaskButtonClicked");
        SingletonFactory.getInstance(MyTableViewData.class).getData().clear();
        System.gc();
    }

    @FXML
    private void startChangeButtonClicked(MouseEvent event) {
//        System.err.println("startChangeButtonClicked");

        informationStage.show();

        int data_size = SingletonFactory.getInstance(MyTableViewData.class).getData().size();
        ObservableList<TableViewTask> myTableViewData = SingletonFactory.getInstance(MyTableViewData.class).getData();
        for(int i = 0;i < data_size;i++){
            TableViewTask tableViewTask = myTableViewData.get(i);
            final String imageFolderPath = tableViewTask.getImageFolderPath();
            final String pdfFileOutputPath = tableViewTask.getPdfFileOutputPath();
            final String pdfName = tableViewTask.getPdfFileName();

            if(!"100%".equals(tableViewTask.getIsFinished())){
                final int finalI = i;
                SingletonFactory.getInstance(MyFixedThreadPool.class).execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            imageToPdfToolService.toPdf(imageFolderPath,pdfFileOutputPath  + File.separator + pdfName + ".pdf",pdfName, finalI);
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

    @FXML
    private void multiSelectionButtonClicked(MouseEvent event) {
        SingletonFactory.getInstance(MyFixedThreadPool.class).execute(new Runnable() {
            @Override
            public void run() {
                multiSelectionButtonClicked();
            }
        });
    }
    private void multiSelectionButtonClicked(){
        JFileChooser jFileChooser = SingletonFactory.getWeakInstance(JFileChooser.class);
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setMultiSelectionEnabled(true);
        jFileChooser.setDialogTitle("选择文件夹");
//        System.err.println(1);
        jFileChooser.showOpenDialog(null);
//        System.err.println(2);
        for (File dir : jFileChooser.getSelectedFiles()) {
            SingletonFactory.getInstance(MyTableViewData.class).getData().add(
                    new TableViewTask(dir.getPath(),
                            "",
                            "",
                            0)
            );
//            System.out.println(dirs[i].getPath());
        }
//        System.err.println("3");
        refresh();
    }

    @FXML
    private void multiEditPdfFileOutputPathButtonClicked(MouseEvent event) {
//        MultiEditPdfFileOutputPathDialog multiEditPdfFileOutputPathDialog = new MultiEditPdfFileOutputPathDialog();
        multiEditPdfFileOutputPathDialog.show();
    }

    /**
     * 若pdf名字为空提示用户填写pdf文件名字
     */
    private void showAlter(Alert.AlertType alterType, String information){
        Alert warning = new Alert(alterType,information);
        warning.initOwner(SingletonFactory.getWeakInstance(InformationStage.class).getStage());
        warning.show();
    }

    /**
     * 设置可编辑列
     */
    private void setTableViewColumnEditAction(TableColumn<TableViewTask, String> tableColumn, final String columnName){
        tableColumn.setCellFactory(TextFieldTableCell.<TableViewTask>forTableColumn());
        tableColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TableViewTask, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TableViewTask, String> t) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setColumnValue(columnName,t.getNewValue());
                    }
                }
        );
    }


    public void refresh(){
        imageToPdfTableView2.refresh();
    }

}
