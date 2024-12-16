package com.DressRental.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ClientRatingDTO {
    private UUID ratingId;
    private UUID clientId;
    private String clientName;
    private Integer rating;
    private String comment;
    private LocalDate date;

    public ClientRatingDTO(UUID ratingId, UUID clientId, String clientName, Integer rating, String comment, LocalDate date) {
        this.ratingId = ratingId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public ClientRatingDTO() {}

    public UUID getRatingId() {
        return ratingId;
    }

    public void setRatingId(UUID ratingId) {
        this.ratingId = ratingId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
