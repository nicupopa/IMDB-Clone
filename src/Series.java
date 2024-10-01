package org.example;
import java.util.List;
import java.util.Map;

public class Series extends Production {
    Integer year;
    Integer sNumber;
    private Map<String, List<Episode>> season;

    public Series(String title, List<String> directorList, List<String> actorList, List<Genre> genreList, List<Rating> ratingList, String description, Double rating) {
        super(title, directorList, actorList, genreList, ratingList, description, rating);
    }

    public Series(String title, List<String> directorList, List<String> actorList,
                  List<Genre> genreList, List<Rating> ratingList, String description, Double rating,
                  Integer year, Integer sNumber, Map<String, List<Episode>> season) {
        super(title, directorList, actorList, genreList, ratingList, description, rating);
        this.year = year;
        this.sNumber = sNumber;
        this.season = season;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + title);
        System.out.println("Directors: " + directorList);
        System.out.println("Actors: " + actorList);
        System.out.println("Genres: " + genreList);
        System.out.println("Ratings: ");
        if (rating != null) {
            for (Rating rating : ratingList) {
                System.out.println("\tUsername: " + rating.getUsername());
                System.out.println("\tRating: " + rating.getRating());
                System.out.println("\tComment: " + rating.getComm());
            }
        }
        System.out.println("Description: " + description);
        System.out.println("Rating: " + rating);
        System.out.println("Year: " + year);
        System.out.println("Season Number: " + sNumber);

        for (Map.Entry<String, List<Episode>> entry : season.entrySet()) {
            System.out.println(entry.getKey() + ":");
            List<Episode> episodes = entry.getValue();
            for (Episode episode : episodes) {
                System.out.println("\t" + episode.getTitle());
                System.out.println("\t" + episode.getDuration());
                System.out.println("+++++++++++++++++++++++++++++");
            }
        }
    }
}
