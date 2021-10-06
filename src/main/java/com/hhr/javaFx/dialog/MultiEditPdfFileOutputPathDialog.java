package com.hhr.javaFx.dialog;

import com.hhr.controller.MainController;
import com.hhr.javaFx.stage.MyStage;
import com.hhr.javaFx.tableview.MyTableViewData;
import com.hhr.javaFx.tableview.TableViewTask;
import com.hhr.thread.MyJavaFxThreadPool;
import com.hhr.util.SingletonFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * @Author: Harry
 * @Date: 2021/10/6 20:46
 * @Version 1.0
 */
public class MultiEditPdfFileOutputPathDialog extends MainDialog {

    public MultiEditPdfFileOutputPathDialog(){
        Window window = SingletonFactory.getWeakInstace(MyStage.class).getWindow();
        //创建提示窗口
        dialogBuilder = new DialogBuilder(window);
        //设置标题
        dialogBuilder.setTitle("批量修改PDF文件输出路径");

        dialogBuilder.getLayout().setPrefWidth(700);

        VBox vBox = dialogBuilder.getLayoutContentVBox();
        vBox.setSpacing(25);

        HBox pdfFileOutputPathHBox = new HBox();
        pdfFileOutputPathHBox.setSpacing(5);
        final DialogBaseModule.MyText pdfFileOutputPathText = new DialogBaseModule.MyText("PDF输出路径:");

        final DialogBaseModule.MyTextField pdfFileOutputPathTextField = new DialogBaseModule.MyTextField();
        pdfFileOutputPathTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                boolean check = false;

                if (pdfFileOutputPathText.getText() == null || "".equals(pdfFileOutputPathText.getText())){
                    check = true;
                }

                dialogBuilder.getPositiveBtn().setDisable(check);
            }
        });

        Button pdfFileOutputPathButton = new Button("路径选择");
        pdfFileOutputPathButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dirChooser = new DirectoryChooser();
                File file = dirChooser.showDialog(SingletonFactory.getWeakInstace(MyStage.class).getStage());
                if(file == null){
                    return;
                }
                pdfFileOutputPathTextField.setText(file.getPath());
            }
        });

        pdfFileOutputPathHBox.getChildren().addAll(pdfFileOutputPathText,pdfFileOutputPathTextField,pdfFileOutputPathButton);

        vBox.getChildren().addAll(pdfFileOutputPathHBox);

        dialogBuilder.setNegativeBtn("取消", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                System.gc();
            }
        });

        dialogBuilder.setPositiveBtn("修改", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                ObservableList<TableViewTask> data = SingletonFactory.getInstace(MyTableViewData.class).getData();

                for(int i = 0;i < data.size();i++){
                    data.get(i).setPdfFileOutputPath(pdfFileOutputPathTextField.getText());
                }

                MainController.refresh();
            }
        });

        SingletonFactory.getInstace(MyJavaFxThreadPool.class).javaFxExecute(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.getPositiveBtn().setDisable(true);
            }
        });
    }
}
