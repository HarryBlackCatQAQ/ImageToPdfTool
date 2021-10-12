package com.hhr.controller;

import com.hhr.jf.annotation.JfController;
import com.hhr.thread.MyJavaFxThreadPool;
import com.hhr.jf.SingletonFactory;
import com.hhr.view.TaskInformationView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author: Harry
 * @Date: 2021/10/5 22:29
 * @Version 1.0
 */

@JfController
public class TaskInformationController extends TaskInformationView implements Initializable {
    private static VBox informationVBox2;
    private static ScrollPane scrollPane2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        informationVBox2 = this.getInformationVBox();
        scrollPane2 = this.getScrollPane();
    }

    public  void addVBoxInformation(final String information){
        SingletonFactory.getInstance(MyJavaFxThreadPool.class).javaFxExecute(new Runnable() {
            @Override
            public void run() {
                Text informationText = new Text(information);
                informationText.setWrappingWidth(666);
                informationVBox2.getChildren().add(informationText);
                scrollPane2.setVvalue(1.0);
            }
        });
    }

    public void clearVBox(){
        informationVBox2.getChildren().clear();
    }
}
