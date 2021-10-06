package com.hhr.javaFx.tableview;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: Harry
 * @Date: 2021/10/5 21:59
 * @Version 1.0
 */
public class TableViewTask implements Serializable {
    private final SimpleStringProperty imageFolderPath;
    private final SimpleStringProperty pdfFileOutputPath;
    private final SimpleStringProperty pdfFileName;
    private final SimpleStringProperty isFinished;

    public TableViewTask(String imageFolderPath, String pdfFileOutputPath, String pdfFileName,int isFinished) {
        this.imageFolderPath = new SimpleStringProperty(imageFolderPath);
        this.pdfFileOutputPath = new SimpleStringProperty(pdfFileOutputPath);
        this.pdfFileName = new SimpleStringProperty(pdfFileName);
        this.isFinished = new SimpleStringProperty(String.valueOf(isFinished) + "%");
    }

    public String getImageFolderPath() {
        return imageFolderPath.get();
    }

    public String getPdfFileOutputPath() {
        return pdfFileOutputPath.get();
    }

    public String getPdfFileName() {
        return pdfFileName.get();
    }

    public String getIsFinished() {
        return isFinished.get();
    }

    public void setImageFolderPath(String imageFolderPath) {
        this.imageFolderPath.set(imageFolderPath);
    }

    public void setPdfFileOutputPath(String pdfFileOutputPath) {
        this.pdfFileOutputPath.set(pdfFileOutputPath);
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName.set(pdfFileName);
    }

    public void setIsFinished(String isFinished) {
        this.isFinished.set(isFinished + "%");
    }

    public void setIsFinished(int isFinished) {
        this.isFinished.set(isFinished + "%");
    }

    @Override
    public String toString() {
        return "TableViewTask{" +
                "imageFolderPath=" + imageFolderPath +
                ", pdfFileOutputPath=" + pdfFileOutputPath +
                ", pdfFileName=" + pdfFileName +
                ", isFinished=" + isFinished +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableViewTask that = (TableViewTask) o;
        return Objects.equals(imageFolderPath, that.imageFolderPath) && Objects.equals(pdfFileOutputPath, that.pdfFileOutputPath) && Objects.equals(pdfFileName, that.pdfFileName) && Objects.equals(isFinished, that.isFinished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageFolderPath, pdfFileOutputPath, pdfFileName, isFinished);
    }
}
