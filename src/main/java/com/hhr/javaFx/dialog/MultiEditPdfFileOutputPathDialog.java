package com.hhr.javaFx.dialog;

import com.hhr.controller.MainController;
import com.hhr.javaFx.stage.MyStage;
import com.hhr.javaFx.tableview.MyTableViewData;
import com.hhr.javaFx.tableview.TableViewTask;
import com.hhr.jf.annotation.JfAutowired;
import com.hhr.jf.annotation.JfComponent;
import com.hhr.thread.MyJavaFxThreadPool;
import com.hhr.jf.SingletonFactory;
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
@JfComponent(weakReference = true)
public class MultiEditPdfFileOutputPathDialog extends MainDialog {
    private HBox pdfFileOutputPathHBox;

    @JfAutowired
    private MainController mainController;

    public MultiEditPdfFileOutputPathDialog(){
        //创建提示窗口
        dialogBuilder = new DialogBuilder();
        //设置标题
        dialogBuilder.setTitle("批量修改PDF文件输出路径");

        dialogBuilder.getLayout().setPrefWidth(710);

        VBox vBox = dialogBuilder.getLayoutContentVBox();
        vBox.setSpacing(25);

        pdfFileOutputPathHBox = new HBox();
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
                DirectoryChooser dirChooser = SingletonFactory.getWeakInstance(DirectoryChooser.class);
                File file = dirChooser.showDialog(SingletonFactory.getWeakInstance(MyStage.class).getStage());
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
                pdfFileOutputPathTextField.setText("");
                System.gc();
            }
        });

        dialogBuilder.setPositiveBtn("修改", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                ObservableList<TableViewTask> data = SingletonFactory.getInstance(MyTableViewData.class).getData();

                for(int i = 0;i < data.size();i++){
                    data.get(i).setPdfFileOutputPath(pdfFileOutputPathTextField.getText());
                }

                mainController.refresh();
//                MainController.refresh();
                pdfFileOutputPathTextField.setText("");
            }
        });

    }

    @Override
    public void show() {
        SingletonFactory.getInstance(MyJavaFxThreadPool.class).javaFxExecute(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.getPositiveBtn().setDisable(true);
            }
        });

        Window window = SingletonFactory.getWeakInstance(MyStage.class).getWindow();
        this.dialogBuilder.setWindow(window);

        super.show();
    }
}
