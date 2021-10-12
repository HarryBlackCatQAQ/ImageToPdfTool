package com.hhr.javaFx.stage;

import com.hhr.javaFx.ImageToPdfToolInfo;
import com.hhr.jf.JfScan;
import com.hhr.jf.util.ResourcesPathUtil;
import com.hhr.jf.util.StageUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @Author: Harry
 * @Date: 2021/10/5 0:14
 * @Version 1.0
 */
public class MyStage extends BaseStage implements ImageToPdfToolInfo {

    private static MyStage instance;

    /**
     * 单例模式 使用
     * @return
     */
    public MyStage(){}

    /**
     * Stage设置
     * @param stage
     */
    public void stageSetting(Stage stage){
        setScene(stage);
        stage.setTitle("图片转PDF工具");
        stage.getIcons().add(new Image(ResourcesPathUtil.getPathOfString(ICON_PATH)));
        stage.setResizable(false);
        setOnCloseRequest(stage);
        this.stage = stage;
    }

    /**
     * Scene设置
     */
    private void setScene(Stage stage){
        Parent root = StageUtil.loadFxml(MAIN_VIEW,"com.hhr");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(ResourcesPathUtil.getPathOfUrl("/css/jfoenix-components.css").toExternalForm());
        stage.setScene(scene);
    }

    /**
     * 设置关闭舞台动作
     */
    private void setOnCloseRequest(Stage stage){
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
}
