package com.onechance.socialmedia.Model;

public class Follow {
    private String followedBy;
    private Long followedAt;

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public Long getFollowedAt() {
        return followedAt;
    }

    public Follow() {
    }

    public void setFollowedAt(Long followedAt) {
        this.followedAt = followedAt;
    }
}
