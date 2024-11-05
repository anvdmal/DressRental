package com.DressRental.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_rating")
public class UserRating extends BaseEntity {
    private User user;
    private int rating;
    private String comment;
    private LocalDate reviewDate;

    public UserRating(User user, int rating, String comment) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDate.now();
    }

    protected UserRating() {
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "rating", nullable = false)
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name = "review_date", nullable = false)
    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
}
