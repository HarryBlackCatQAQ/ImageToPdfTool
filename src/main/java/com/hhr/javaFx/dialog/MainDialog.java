package com.hhr.javaFx.dialog;

public abstract class MainDialog {
    protected DialogBuilder dialogBuilder;

    public void show(){
        this.dialogBuilder.create();
    }

    public void close(){
        this.dialogBuilder.getAlert().close();
    }
}