package com.hhr.javaFx;

import com.hhr.util.ResourcesPathUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * @Author: Harry
 * @Date: 2021/10/5 0:14
 * @Version 1.0
 */
public class MyStage {

    private static MyStage instance;

    private Stage stage;

    /**
     * 单例模式
     * @return
     */
    public static MyStage getInstance() {
        if (instance == null) {
            synchronized (MyStage.class) {
                if (instance == null) {
                    instance = new MyStage();
                }
            }
        }
        return instance;
    }

    private MyStage(){}

    /**
     * Stage设置
     * @param stage
     */
    public void stageSetting(Stage stage){
        setScene(stage);
        stage.setTitle("图片转PDF工具");
        stage.getIcons().add(new Image(ResourcesPathUtil.getPathOfString("/icon/ImagesToPdfIcon.png")));
        stage.setResizable(false);
        setOnCloseRequest(stage);
        this.stage = stage;
    }

    /**
     * Scene设置
     */
    private void setScene(Stage stage){
        Parent root = null;
        try {
            root = FXMLLoader.load(ResourcesPathUtil.getPathOfUrl("/fxml/main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);

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

    public Stage getStage() {
        return stage;
    }
}
