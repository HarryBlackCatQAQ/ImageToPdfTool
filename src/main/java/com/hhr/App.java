package com.hhr;

import com.hhr.javaFx.stage.MyStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MyStage myStage = MyStage.getInstance();
        myStage.stageSetting(primaryStage);
        primaryStage.show();
    }
}
