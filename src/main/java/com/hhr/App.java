package com.hhr;

import com.hhr.javaFx.stage.MyStage;
import com.hhr.jf.SingletonFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MyStage myStage = SingletonFactory.getWeakInstance(MyStage.class);
        myStage.stageSetting(primaryStage);
        primaryStage.show();
    }
}
