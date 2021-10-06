package com.hhr.javaFx.dialog;

import com.hhr.javaFx.stage.MyStage;
import com.hhr.javaFx.tableview.MyTableViewData;
import com.hhr.javaFx.tableview.TableViewTask;
import com.hhr.thread.MyJavaFxThreadPool;
import com.hhr.util.SingletonFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;


/**
 * @Author: Harry
 * @Date: 2021/10/5 19:11
 * @Version 1.0
 */
public class TaskAddDialog extends MainDialog{
    private final DialogBaseModule.MyTextField imageFolderPathTextField;
    private final DialogBaseModule.MyTextField pdfFileOutputPathTextField;
    private final DialogBaseModule.MyTextField pdfFileNameTextField;
    private final DialogBaseModule.MyText tipsText;

    public TaskAddDialog(){
        Window window = SingletonFactory.getWeakInstace(MyStage.class).getWindow();
        //创建提示窗口
        dialogBuilder = new DialogBuilder(window);
        //设置标题
        dialogBuilder.setTitle("图片转PDF任务添加");

        dialogBuilder.getLayout().setPrefWidth(710);

        VBox vBox = dialogBuilder.getLayoutContentVBox();
        vBox.setSpacing(25);

        HBox imageFolderPathHBox = new HBox();
        imageFolderPathHBox.setSpacing(5);
        DialogBaseModule.MyText imageFolderPathText = new DialogBaseModule.MyText("图片文件夹路径:");
        imageFolderPathTextField = new DialogBaseModule.MyTextField();
        Button imageFolderPathButton = new Button("路径选择");
        setButtonAction(imageFolderPathButton,imageFolderPathTextField);
        imageFolderPathHBox.getChildren().addAll(imageFolderPathText,imageFolderPathTextField,imageFolderPathButton);


        HBox pdfFileOutputPathHBox = new HBox();
        pdfFileOutputPathHBox.setSpacing(5);
        DialogBaseModule.MyText pdfFileOutputPathText = new DialogBaseModule.MyText("PDF输出路径:");
        pdfFileOutputPathTextField = new DialogBaseModule.MyTextField();
        Button pdfFileOutputPathButton = new Button("路径选择");
        setButtonAction(pdfFileOutputPathButton,pdfFileOutputPathTextField);
        pdfFileOutputPathHBox.getChildren().addAll(pdfFileOutputPathText,pdfFileOutputPathTextField,pdfFileOutputPathButton);


        HBox pdfFileNameTextHBox = new HBox();
        pdfFileNameTextHBox.setSpacing(5);
        DialogBaseModule.MyText pdfFileNameText = new DialogBaseModule.MyText("PDF文件名:");
        pdfFileNameTextField = new DialogBaseModule.MyTextField();
        pdfFileNameTextHBox.getChildren().addAll(pdfFileNameText,pdfFileNameTextField);

        tipsText = new DialogBaseModule.MyText("");
        tipsText.setFill(Color.RED);

        vBox.getChildren().addAll(imageFolderPathHBox,pdfFileOutputPathHBox,pdfFileNameTextHBox,tipsText);

        //添加textField监听
        textFieldAddListener(imageFolderPathTextField);
        textFieldAddListener(pdfFileOutputPathTextField);
        textFieldAddListener(pdfFileNameTextField);


        dialogBuilder.setNegativeBtn("取消", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                System.gc();
            }
        });

        dialogBuilder.setPositiveBtn("添加", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                SingletonFactory.getInstace(MyTableViewData.class).getData().add(
                        new TableViewTask(imageFolderPathTextField.getText(),
                                pdfFileOutputPathTextField.getText(),
                                pdfFileNameTextField.getText(),
                                0)
                );
                System.gc();
            }
        });

        SingletonFactory.getInstace(MyJavaFxThreadPool.class).javaFxExecute(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.getPositiveBtn().setDisable(true);
            }
        });
    }

    /**
     * 判断TextField中是否为空
     * @param folderPathOrFileName 文件路径或者文件名
     * @return 返回是否为空
     */
    private boolean isTextFieldEmpty(String folderPathOrFileName){
        return folderPathOrFileName == null || "".equals(folderPathOrFileName);
    }

    /**
     * 判断是否有禁止的字符存在文件名中 /\?*:"<>|
     */
    private static final String BAN_SYMBOL = "/\\?*:\"<>|";
    private boolean isHavingBanSymbol(String fileName){
        for(int i = 0;i < BAN_SYMBOL.length();i++){
            if(fileName.indexOf(BAN_SYMBOL.charAt(i)) >= 0){
                return true;
            }
        }

        return false;
    }

    /**
     * 添加textField监听事件
     */
    private void textFieldAddListener(TextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                isOk();
            }
        });
    }

    /**
     * 每个textField符合条件才可以启用添加按钮
     */
    private void isOk(){
        boolean check = true;
        String tips = "";
        if(isTextFieldEmpty(this.getImageFolderPathTextFieldText())){
            tips = "图片路径为空!";
        }
        else if(isTextFieldEmpty(this.getPdfFileOutputPathTextFieldText())){
            tips = "PDF输出路径为空!";
        }
        else if(isTextFieldEmpty(this.getPdfFileNameTextFieldText())){
            tips = "PDF名字为空!";
        }
        else if(isHavingBanSymbol(this.getPdfFileNameTextFieldText())){
            tips = "PDF名字中有违规字符:" + BAN_SYMBOL;
        }
        else{
            check = false;
        }

        dialogBuilder.getPositiveBtn().setDisable(check);

        tipsText.setText(tips);
    }

    /**
     * 设置 路径选择按钮事件
     */
    private void setButtonAction(Button button, final DialogBaseModule.MyTextField myTextField){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dirChooser = SingletonFactory.getWeakInstace(DirectoryChooser.class);
                File file = dirChooser.showDialog(SingletonFactory.getWeakInstace(MyStage.class).getStage());
                if(file == null){
                    return;
                }
                myTextField.setText(file.getPath());
            }
        });
    }




    public String getImageFolderPathTextFieldText() {
        return imageFolderPathTextField.getText();
    }

    public String getPdfFileOutputPathTextFieldText() {
        return pdfFileOutputPathTextField.getText();
    }

    public String getPdfFileNameTextFieldText() {
        return pdfFileNameTextField.getText();
    }
}
