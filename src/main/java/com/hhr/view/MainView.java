package com.hhr.view;

import com.hhr.javaFx.tableview.TableViewTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import lombok.Getter;

/**
 * @Author: Harry
 * @Date: 2021/10/13 2:18
 * @Version 1.0
 */
@Getter
public abstract class MainView {
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
}
