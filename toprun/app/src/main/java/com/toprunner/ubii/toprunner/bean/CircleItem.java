package com.toprunner.ubii.toprunner.bean;

import com.toprunner.ubii.toprunner.base.BaseBean;

import java.util.List;

/**
 * Created by ${赵鼎} on 2016/10/26 0026.
 */

public class CircleItem extends BaseBean {
    public final static String TYPE_URL = "1";
    public final static String TYPE_IMG = "2";
    public final static String TYPE_VIDEO = "3";
    private String id;//标示
    private String content;//内容
    private String createTime;//时间
    private String type;//1:链接  2:图片 3:视频
    private String linkImg;
    private String linkTitle;
    private List<String> photos;
    private User user;
    private String videoUrl;
    private String videoImgUrl;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLinkImg() {
        return linkImg;
    }
    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }
    public String getLinkTitle() {
        return linkTitle;
    }
    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }
    public List<String> getPhotos() {
        return photos;
    }
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImgUrl() {
        return videoImgUrl;
    }

    public void setVideoImgUrl(String videoImgUrl) {
        this.videoImgUrl = videoImgUrl;
    }
}
