package com.hhr.view;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * @Author: Harry
 * @Date: 2021/10/13 2:22
 * @Version 1.0
 */
@Getter
public abstract class TaskInformationView {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox informationVBox;
}
