package com.DressRental.models.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_rating")
public class ClientRating extends BaseEntity {
    private User user;
    private Rental rental;
    private int rating;
    private String comment;
    private LocalDate reviewDate;

    public ClientRating(User user, Rental rental, int rating, String comment) {
        this.user = user;
        this.rental = rental;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDate.now();
    }

    protected ClientRating() {
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id", nullable = false)
    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
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
