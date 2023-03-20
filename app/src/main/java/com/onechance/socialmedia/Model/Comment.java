package com.onechance.socialmedia.Model;

public class Comment {
    private String commentBody,commentBy;
    private Long commentAt;

    public Comment() {
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public Long getCommentAt() {
        return commentAt;
    }

    public void setCommentAt(Long commentAt) {
        this.commentAt = commentAt;
    }
}
