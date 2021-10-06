package com.hhr.javaFx.dialog;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @Author: Harry
 * @Date: 2021/10/6 21:00
 * @Version 1.0
 */
public class DialogBaseModule {
    public static class MyText extends Text {
        public MyText(String text){
            this.setText(text);
            this.setWrappingWidth(125.3);
            this.fontProperty().set(Font.font(15));
        }
    }

    public static class MyTextField extends TextField {
        public MyTextField(){
            this.setPrefWidth(452);
            this.setPrefHeight(23);
        }
    }
}
