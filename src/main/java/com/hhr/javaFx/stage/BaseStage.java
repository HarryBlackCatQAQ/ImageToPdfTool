package com.hhr.javaFx.stage;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @Author: Harry
 * @Date: 2021/10/5 23:17
 * @Version 1.0
 */
public abstract class BaseStage {
    protected Stage stage;

    public Stage getStage() {
        return stage;
    }

    public Scene getScene(){
        return stage.getScene();
    }

    public Window getWindow(){
        return getScene().getWindow();
    }
}
