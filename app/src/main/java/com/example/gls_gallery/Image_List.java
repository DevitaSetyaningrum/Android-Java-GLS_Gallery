package com.example.gls_gallery;

public class Image_List {
    private String imageName = "";
    private String folderName = "";
    private String imagePath = "";

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image_List(String imageName, String imagePath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    public Image_List(String folderName){
        this.folderName = folderName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
