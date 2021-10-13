package com.hhr.jf.util;

import com.hhr.jf.JfInstance;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;

/**
 * @Author: Harry
 * @Date: 2021/10/5 22:45
 * @Version 1.0
 */
@Slf4j
public class StageUtil {
    /**
     * 读取fxml
     * @param fxmlPath fxml路径
     */
    public static Parent loadFxml(URL fxmlPath,String packageName){
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlPath);
            root = fxmlLoader.load();

            log.info(LogUtil.getClassSimpleName(fxmlLoader.getController()) + " 注入开始");
            JfInstance.parseJfControllerAnnotation(fxmlLoader,packageName);
            log.info(LogUtil.getClassSimpleName(fxmlLoader.getController()) + " 注入结束");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    public static Parent loadFxml(String fxmlPath,String packageName){
        return loadFxml(ResourcesPathUtil.getPathOfUrl(fxmlPath),packageName);
    }

    public static Parent loadFxml(String fxmlPath){
        return loadFxml(ResourcesPathUtil.getPathOfUrl(fxmlPath),null);
    }



}
