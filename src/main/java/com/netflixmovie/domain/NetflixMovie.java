package com.netflixmovie.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "netflix_show_details")
public class NetflixMovie {
    @Id
    @Column(name = "show_id")
    public String showId;
    public String type;
    public String title;
    public String director;
    public String cast;
    public String country;
    @Column(name="date_added")
    public LocalDate dateAdded;
    @Column(name="release_year")
    public Integer releaseYear;
    public String rating;
    public String duration;
    @Column(name="listed_in")
    public String listedIn;
    public String description;

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getListedIn() {
        return listedIn;
    }

    public void setListedIn(String listedIn) {
        this.listedIn = listedIn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NetflixMovie{" +
                "showId='" + showId + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", cast='" + cast + '\'' +
                ", country='" + country + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", releaseYear=" + releaseYear +
                ", rating='" + rating + '\'' +
                ", duration='" + duration + '\'' +
                ", listedIn='" + listedIn + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
