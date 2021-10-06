package com.hhr.javaFx.tableview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @Author: Harry
 * @Date: 2021/10/5 21:58
 * @Version 1.0
 */
public class MyTableViewData {
    private final ObservableList<TableViewTask> data;

    /**
     * 单例模式 使用
     * @return
     */

    public MyTableViewData(){
        data = FXCollections.observableArrayList();
    }

    public ObservableList<TableViewTask> getData() {
        return data;
    }
}
