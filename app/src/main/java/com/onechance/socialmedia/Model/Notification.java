package com.onechance.socialmedia.Model;

public class Notification {
   private String notificationBy;
   private String type;
   private Long notificationAt;

    public String getNotificationBy() {
        return notificationBy;
    }

    public void setNotificationBy(String notificationBy) {
        this.notificationBy = notificationBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(Long notificationAt) {
        this.notificationAt = notificationAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public boolean isCheckOpen() {
        return checkOpen;
    }

    public void setCheckOpen(boolean checkOpen) {
        this.checkOpen = checkOpen;
    }

    private String postId;
   private String postedBy;
   private boolean checkOpen;

}
