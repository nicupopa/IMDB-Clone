package org.example;

import java.util.ArrayList;
import java.util.List;

public class Rating implements Subject {
    String username;
    Integer rating;
    String comm;
    String message;
    List<String> notification = new ArrayList<>();

    public Rating(String username, Integer rating, String comm) {
        this.username = username;
        this.rating = rating;
        this.comm = comm;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", comm='" + comm + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComm() {
        return comm;
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
