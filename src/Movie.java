package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Movie extends Production{
    String duration;
    Integer year;

    public Movie(String title, List<String> directorList, List<String> actorList, List<Genre> genreList,
                 List<Rating> ratingList, String description, Double rating) {
        super(title, directorList, actorList, genreList, ratingList, description, rating);
    }

    public Movie(String title, List<String> directorList, List<String> actorList, List<Genre> genreList,
                 List<Rating> ratingList, String description, Double rating, String duration, Integer year) {
        super(title, directorList, actorList, genreList, ratingList, description, rating);
        this.duration = duration;
        this.year = year;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + title);
        System.out.println("Directors: " + directorList);
        System.out.println("Actors: " + actorList);
        System.out.println("Genres: " + genreList);
        System.out.println("Ratings: ");
        if (ratingList != null) {
            for (Rating rating : ratingList) {
                System.out.println("\tUsername: " + rating.getUsername());
                System.out.println("\tRating: " + rating.getRating());
                System.out.println("\tComment: " + rating.getComm());
                System.out.println("-----------");
            }
        }
        System.out.println("Description: " + description);
        System.out.println("Overall Rating: " + rating);
        System.out.println("Duration: " + duration);
        System.out.println("Year: " + year);
        System.out.println("+++++++++++++++++++++++++++++");
    }
}
