package com.hhr.controller;

import com.hhr.javaFx.dialog.MultiEditPdfFileOutputPathDialog;
import com.hhr.javaFx.dialog.TaskAddDialog;
import com.hhr.javaFx.stage.InformationStage;
import com.hhr.javaFx.tableview.MyTableViewData;
import com.hhr.javaFx.tableview.TableViewTask;
import com.hhr.service.ImageToPdfToolService;
import com.hhr.thread.MyFixedThreadPool;
import com.hhr.util.SingletonFactory;
import com.lowagie.text.DocumentException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
public class MainController implements Initializable {
    private static TableView<TableViewTask> imageToPdfTableView2;

    @FXML
    private Pane mainPane;

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

    @FXML
    private Button multiSelectionButton;

    @FXML
    private Button multiEditPdfFileOutputPathButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageFolderPathColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("imageFolderPath"));
        setTableViewColumnEditAction(imageFolderPathColumn,TableViewTask.IMAGE_FOLDER_PATH);
        pdfFileOutputColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("pdfFileOutputPath"));
        setTableViewColumnEditAction(pdfFileOutputColumn,TableViewTask.PDF_FILE_OUTPUT_PATH);
        pdfFileNameColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("pdfFileName"));
        setTableViewColumnEditAction(pdfFileNameColumn,TableViewTask.PDF_FILE_NAME);
        isFinishedColumn.setCellValueFactory(new PropertyValueFactory<TableViewTask, String>("isFinished"));
        imageToPdfTableView.setItems(SingletonFactory.getInstace(MyTableViewData.class).getData());

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
        SingletonFactory.getInstace(MyTableViewData.class).getData().clear();
        System.gc();
    }

    @FXML
    private void startChangeButtonClicked(MouseEvent event) {
//        System.err.println("startChangeButtonClicked");

        SingletonFactory.getWeakInstace(InformationStage.class).show();

        int data_size = SingletonFactory.getInstace(MyTableViewData.class).getData().size();
        ObservableList<TableViewTask> myTableViewData = SingletonFactory.getInstace(MyTableViewData.class).getData();
        for(int i = 0;i < data_size;i++){
            TableViewTask tableViewTask = myTableViewData.get(i);
            final String imageFolderPath = tableViewTask.getImageFolderPath();
            final String pdfFileOutputPath = tableViewTask.getPdfFileOutputPath();
            final String pdfName = tableViewTask.getPdfFileName();

            if(!"100%".equals(tableViewTask.getIsFinished())){
                final int finalI = i;
                SingletonFactory.getInstace(MyFixedThreadPool.class).execute(new Runnable() {
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

    @FXML
    private void multiSelectionButtonClicked(MouseEvent event) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setMultiSelectionEnabled(true);
        jFileChooser.setDialogTitle("选择文件夹");
        jFileChooser.showOpenDialog(null);

        for (File dir : jFileChooser.getSelectedFiles()) {
            SingletonFactory.getInstace(MyTableViewData.class).getData().add(
                    new TableViewTask(dir.getPath(),
                            "",
                            "",
                            0)
            );
//            System.out.println(dirs[i].getPath());
        }
        refresh();
    }

    @FXML
    private void multiEditPdfFileOutputPathButtonClicked(MouseEvent event) {
        MultiEditPdfFileOutputPathDialog multiEditPdfFileOutputPathDialog = new MultiEditPdfFileOutputPathDialog();
        multiEditPdfFileOutputPathDialog.show();
    }

    /**
     * 若pdf名字为空提示用户填写pdf文件名字
     */
    private void showAlter(Alert.AlertType alterType, String information){
        Alert warning = new Alert(alterType,information);
        warning.initOwner(SingletonFactory.getWeakInstace(InformationStage.class).getStage());
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


    public static void refresh(){
        imageToPdfTableView2.refresh();
    }

}
