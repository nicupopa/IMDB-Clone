package org.example;
import java.util.*;
public abstract class Production implements Comparable<Object>, Subject {
    String title;
    List<String> directorList;
    List<String> actorList;
    List<Genre> genreList;
    List<Rating> ratingList;
    String description;
    Double rating;
    String message;
    List<User<AccountType>> observersList = new ArrayList<>();

    public Production(String title, List<String> directorList, List<String> actorList,
                      List<Genre> genreList, List<Rating> ratingList, String description, Double rating) {
        this.title = title;
        this.directorList = directorList;
        this.actorList = actorList;
        this.genreList = genreList;
        this.ratingList = ratingList;
        this.description = description;
        this.rating = rating;
    }

    public abstract void displayInfo();

    public int getReviewsNumber() {
        int number = 0;

        for (Rating rating : ratingList) {
            number++;
        }

        return number;
    }

    @Override
    public int compareTo(Object other) {
        if (other == null) {
            throw new NullPointerException();
        }

        if (other instanceof Actor) {
            return (this.title.compareTo(((Actor) other).name));
        } else if (other instanceof Production) {
            return (this.title.compareTo(((Production) other).title));
        } else {
            throw new UnsupportedOperationException("Unsupported comparison");
        }
    }

    public void updateDescription(String newDesc) {
        this.description = newDesc;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    // Subject implementation (Observer pattern)
    @Override
    public void register(User<AccountType> observer, Production production) {
        if (observer == null) {
            throw new NullPointerException();
        }
        production.observersList.add(observer);
    }

    @Override
    public void unregister(User<AccountType> observer, Production production) {
        production.observersList.remove(observer);
    }

    @Override
    public void notifyObservers(Production production, String reviewerUsername) {
        if (production.observersList != null && !production.observersList.isEmpty()) {
            for (User<AccountType> observer : production.observersList) {
                if (!observer.username.equals(reviewerUsername)) {
                    observer.update(message, observer);
                }
            }
        } else {
            System.out.println("No observers to notify!!!");
        }
    }

    @Override
    public Object getUpdate() {
        return message;
    }

    @Override
    public void postMessage(String title, String reviewerUsername, Production production) {
        this.message = reviewerUsername + " added a review for: " + title;
        notifyObservers(production, reviewerUsername);
    }
}
