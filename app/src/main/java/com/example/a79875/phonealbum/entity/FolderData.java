package com.example.a79875.phonealbum.entity;

public class FolderData {
    private String latestPhotoUrl;
    private String folderName;
    private int count;
    private String folderFileUrl;

    public FolderData(String latestPhotoUrl, String folderUrl,int count) {
        this.latestPhotoUrl = latestPhotoUrl;
        this.folderName = folderUrl;
        this.folderFileUrl = folderUrl;
        this.count = count;
        newFolderName(folderUrl);
    }

    public String getFolderFileUrl() {
        return folderFileUrl;
    }

    public void setFolderFileUrl(String folderFileUrl) {
        this.folderFileUrl = folderFileUrl;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLatestPhotoUrl() {
        return latestPhotoUrl;
    }

    public void setLatestPhotoUrl(String latestPhotoUrl) {
        this.latestPhotoUrl = latestPhotoUrl;
    }

    public String getFolderName() {
        return folderName;
    }

    // 根据路径截取名字
    private void newFolderName(String folderUrl) {
        int start = folderUrl.lastIndexOf("/");
        if (start != -1) {
            String newName = folderUrl.substring(start + 1,folderUrl.length());
            this.folderName = newName;
        }else {
            this.folderName = folderUrl;
        }

    }
}
