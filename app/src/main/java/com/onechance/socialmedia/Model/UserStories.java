package com.onechance.socialmedia.Model;

public class UserStories {
    private String image;
    private Long storyAt;

    public UserStories() {
    }

    public UserStories(String image, Long storyAt) {
        this.image = image;
        this.storyAt = storyAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(Long storyAt) {
        this.storyAt = storyAt;
    }
}
