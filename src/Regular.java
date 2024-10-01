package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Regular extends User<AccountType> implements RequestsManager {

    public Regular(Information userInfo, AccountType accType, String username, Integer XP, List<String> notifList) {
        super(userInfo, accType, username, XP, notifList);
    }

    @Override
    public void createRequest(Request r) {
        IMDB.getInstance().requestList.add(r);
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.getInstance().requestList.remove(r);
    }

    public void addRating(User<AccountType> user, Scanner input) throws InterruptedException {
        String name = null;
        boolean entityExists = false;
        Production foundProduction = null;
        boolean validRating = false;

        do {
            System.out.println("What production do you want to rate?");

            List<Production> prodList = IMDB.getInstance().prodList;
            for (Production production : prodList) {
                System.out.println("-> " + production.title);
            }

            name = input.nextLine();

//            for (Production production : prodList) {
//                for (Rating rating : production.ratingList) {
//                    if (rating.username.equalsIgnoreCase(user.username)) {
//                        System.out.println("You can't add another review for this Production");
//                        Thread.sleep(3000);
//                        return;
//                    }
//                }
//            }

            for (Production production : prodList) {
                if (name.equalsIgnoreCase(production.title)) {
                    foundProduction = production;
                    entityExists = true;
                }
            }

            for (Rating rating : foundProduction.ratingList) {
                if (rating.username.equalsIgnoreCase(user.username)) {
                    System.out.println("You can't add another review for this Production");
                    Thread.sleep(3000);
                    return;
                }
            }

            if (!entityExists) {
                System.out.println("Typo or production doesn't exist!");
                Thread.sleep(3000);
            }

        } while (!entityExists);

        do {
            System.out.println("Rate 1-10:");
            if (input.hasNextInt()) {
                int rating = input.nextInt();
                input.nextLine();

                if (rating >= 1 && rating <= 10) {
                    validRating = true;
                    System.out.println("Add a comment:");
                    String comm = input.nextLine();

                    Rating r = new Rating(user.username, rating, comm);
                    foundProduction.ratingList.add(r);

                    r.register(user, foundProduction);
                    user.setSubject(r);

                    r.postMessage(foundProduction.title, user.username, foundProduction);

                    System.out.println("Rating added successfully!");
                    Thread.sleep(3000);
                } else {
                    System.out.println("Invalid rating. Please enter a number between 1 and 10.");
                    Thread.sleep(3000);
                }
            } else {
                System.out.println("Invalid rating. Please enter a number between 1 and 10.");
                input.nextLine();
                Thread.sleep(3000);
            }
        } while (!validRating);
    }

    public void deleteRating(User<AccountType> user, Scanner input) throws InterruptedException {
        boolean reviewFound = false;
        boolean validReview = false;
        String name = null;
        Rating foundReview = null;
        Production foundProduction = null;

        List<Production> prodList = IMDB.getInstance().prodList;
        List<Rating> userRatingList = new ArrayList<>();

        do {
            int index = 1;
            System.out.println("What review do you want to remove?");

            for (Production production : prodList) {
                for (Rating rating : production.ratingList) {
                    if (rating.username.equals(user.username)) {
                        System.out.println(index + ". " + production.title);
                        index++;
                        userRatingList.add(rating);
                        reviewFound = true;
                    }
                }
            }

            if (!reviewFound) {
                System.out.println("You don't have any reviews!");
                Thread.sleep(3000);
            }

            if (input.hasNextInt()) {
                int userInput = input.nextInt();
                input.nextLine();

                if (userInput >= 1 && userInput <= index - 1) {
                    validReview = true;
                    Rating selectedReview = userRatingList.get(userInput - 1);

                    for (Production production : prodList) {
                        if (production.ratingList.contains(selectedReview)) {
                            production.ratingList.remove(selectedReview);
                            selectedReview.unregister(user, production);
                        }
                    }
                    userRatingList.remove(selectedReview);
                    System.out.println("Rating removed successfully");
                    Thread.sleep(3000);
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    Thread.sleep(3000);
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
                Thread.sleep(3000);
            }

        } while (!validReview);
    }

    @Override
    public User logOut(User user) {
        return null;
    }

    // Observer pattern methods
    @Override
    public void update() {
        String newMessage = (String) subject.getUpdate();
        if (newMessage == null) {
            System.out.println("No new message received!");
        } else {
            System.out.println("New notification: " + newMessage);
        }
    }

    @Override
    public void update(String newNotification, User<AccountType> user) {
        if (newNotification == null) {
            System.out.println("No new notification received!");
        } else {
//            System.out.println("New notification: " + newNotification);
                user.notifList.add(newNotification);
        }
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
