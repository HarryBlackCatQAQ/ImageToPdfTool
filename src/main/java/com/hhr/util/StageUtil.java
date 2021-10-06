package com.hhr.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * @Author: Harry
 * @Date: 2021/10/5 22:45
 * @Version 1.0
 */
public class StageUtil {
    /**
     * 读取fxml
     * @param fxmlPath fxml路径
     */
    public static Parent loadFxml(URL fxmlPath){
        Parent root = null;
        try {
            root = FXMLLoader.load(fxmlPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }
    public static Parent loadFxml(String fxmlPath){
        return loadFxml(ResourcesPathUtil.getPathOfUrl(fxmlPath));
    }


}
