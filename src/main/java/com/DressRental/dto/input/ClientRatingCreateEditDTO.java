package com.DressRental.dto.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class ClientRatingCreateEditDTO {
    Integer rating;
    String comment;

    public ClientRatingCreateEditDTO(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public ClientRatingCreateEditDTO() {}

    @NotNull(message = "Рейтинг обязателен")
    @Min(value = 1)
    @Max(value = 5)
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @NotEmpty
    @Length(min = 25, message = "Слишком короткий комментарий")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
