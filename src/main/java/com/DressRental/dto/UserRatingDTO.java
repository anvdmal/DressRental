package com.DressRental.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public class UserRatingDTO {
    private String userEmail;
    private int rating;
    private String comment;
    private LocalDate reviewDate;

    public UserRatingDTO(String userEmail, int rating, String comment) {
        this.userEmail = userEmail;
        this.rating = rating;
        this.comment = comment;
    }

    public UserRatingDTO() {
    }

    @NotNull
    @NotEmpty
    @Email(message = "Неккоректный email!")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @NotNull
    @NotEmpty
    @Length(min = 25, message = "Слишком короткий комментарий!")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
}
