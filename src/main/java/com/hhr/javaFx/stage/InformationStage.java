package com.hhr.javaFx.stage;

import com.hhr.controller.TaskInformationController;
import com.hhr.javaFx.ImageToPdfToolInfo;
import com.hhr.jf.SingletonFactory;
import com.hhr.jf.annotation.JfAutowired;
import com.hhr.jf.annotation.JfComponent;
import com.hhr.jf.util.ResourcesPathUtil;
import com.hhr.jf.util.StageUtil;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @Author: Harry
 * @Date: 2021/10/5 22:49
 * @Version 1.0
 */
@JfComponent(weakReference = true)
public class InformationStage extends BaseStage implements ImageToPdfToolInfo{
    private final Stage stage;

    @JfAutowired
    private TaskInformationController taskInformationController;

    /**
     * 单例模式 使用
     * @return
     */
    public InformationStage(){
        stage = new Stage();
        Parent root = StageUtil.loadFxml(ImageToPdfToolInfo.INFORMATION_VIEW);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stageSetting(stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                close();
            }
        });
    }

    /**
     * Stage设置
     */
    private void stageSetting(Stage stage){
        stage.setTitle("图片转PDF工具---转换详情");
        stage.getIcons().add(new Image(ResourcesPathUtil.getPathOfString(ICON_PATH)));
        stage.setResizable(false);
    }

    public void show(){
        this.stage.show();
    }

    private void close(){
        this.stage.close();
//        TaskInformationController.clearVBox();
        taskInformationController.clearVBox();
        System.gc();
    }
}
