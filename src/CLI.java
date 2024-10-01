package org.example;
import javax.swing.*;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CLI {

    public static void displayProductions(String filterGenre, boolean filterReviews) {
        List<Production> prodList = new ArrayList<>(IMDB.getInstance().prodList);
        String formattedGenre = null;

        if (filterGenre != null) {
            formattedGenre = filterGenre.substring(0, 1).toUpperCase()
                    + filterGenre.substring(1).toLowerCase();
        }

        if (filterReviews) {
            IMDB.getInstance().prodList.sort(Comparator.comparingInt(Production::getReviewsNumber).reversed());
        }

        for (Production production : prodList) {
            if (filterGenre != null && !production.genreList.contains(Genre.valueOf(formattedGenre))) {
                continue;
            }
            production.displayInfo();
//            System.out.println(production.getReviewsNumber());
        }
    }

    public static void displayActors(boolean sort) {
        List<Actor> actorList = new ArrayList<>(IMDB.getInstance().actorList);

        if (sort) {
            actorList.sort(Comparator.comparing(Actor::getName, String.CASE_INSENSITIVE_ORDER));
        }
        for (Actor actor : actorList) {
            actor.displayInfo();
        }
    }

    public static void search(String name) {
        List<Actor> actorList = IMDB.getInstance().actorList;
        List<Production> prodlist = IMDB.getInstance().prodList;

        boolean foundActor = false;
        boolean foundProduction = false;

        for (Actor iter : actorList) {
            if (iter.name.equalsIgnoreCase(name)) {
                foundActor = true;
                iter.displayInfo();
                break;
            }
        }

        for (Production iter : prodlist) {
            if (iter.title.equalsIgnoreCase(name)) {
                foundProduction = true;
                iter.displayInfo();
                break;
            }
         }

        if (!foundProduction && !foundActor) {
            System.out.println("Your search didn't match anything in out database");
        }
    }

    public static void displayNotifications(User<AccountType> user) {
        if (user.notifList != null) {
                System.out.println("Notification List: ");
                System.out.println("--------------");
                for (Object iter : user.notifList) {
                    System.out.println(" " + iter);
                    System.out.println("--------------");
                }
            }
    }

    public static void manageFavorites(User<AccountType> user, int index, String key) {
        List<Actor> actorList = IMDB.getInstance().actorList;
        List<Production> prodlist = IMDB.getInstance().prodList;

        boolean foundActor = false;
        boolean foundProduction = false;

        switch (index) {
            case 1:
                for (Actor iter : actorList) {
                    if (iter.name.equalsIgnoreCase(key)) {
                        foundActor = true;
                        user.addFav(iter);
                        System.out.println("Actor added successfully!");
                    }
                }

                for (Production iter : prodlist) {
                    if (iter.title.equalsIgnoreCase(key)) {
                        foundProduction = true;
                        user.addFav(iter);
                        System.out.println("Production added successfully!");
                    }
                }

                if (!foundProduction && !foundActor) {
                    System.out.println("Your inquiry didn't match anything in out database");
                }

                break;

            case 2:
                Actor act = null;
                Production prod = null;

                for (Object iter : user.favList) {
                    if (iter instanceof Actor) {
                        if (((Actor) iter).name.equalsIgnoreCase(key)) {
                            foundActor = true;
                            act = (Actor) iter;
//                            user.deleteFav(iter);
//                            System.out.println("Actor removed successfully!");
                        }
                    } else if (iter instanceof Production) {
                        if (((Production) iter).title.equalsIgnoreCase(key)) {
                            foundProduction = true;
                            prod = (Production) iter;
//                            user.deleteFav(iter);
//                            System.out.println("Production removed successfully!");
                        }
                    }
                }

                if (act != null) {
                    user.deleteFav(act);
                    System.out.println("Actor removed successfully!");
                } else if (prod != null) {
                    user.deleteFav(prod);
                    System.out.println("Production removed successfully!");
                }

                if (!foundProduction && !foundActor) {
                    System.out.println("Your inquiry didn't match anything from your favorites list");
                }

                break;
        }
    }

    public static void createRequest(User<AccountType> user, int type, Scanner input) throws InterruptedException {
        System.out.println("Describe your request:");
        String description = input.nextLine();
        LocalDateTime currentTime = LocalDateTime.now();
        RequestType requestType = null;
        String name = null;
        Staff staffMember = null;
        boolean entityExists = false;
        boolean validContributor = false;

        switch (type) {
            case 1:
                requestType = RequestType.ACTOR_ISSUE;
                List<Actor> actorList = IMDB.getInstance().actorList;

                do {
                    System.out.println("Select actor:");

                    for (Actor actor : actorList) {
                        System.out.println("-> " + actor.name);
                    }

                    name = input.nextLine();

                    if (user instanceof Staff) {
                        Staff staffUser = (Staff) user;

                        for (Object contributionIter : staffUser.contribution) {
                            if (contributionIter instanceof Actor) {
                                if (((Actor) contributionIter).name.equalsIgnoreCase(name)) {
                                    validContributor = true;
                                }
                            } else if (contributionIter instanceof Production) {
                                if (((Production) contributionIter).title.equalsIgnoreCase(name)) {
                                    validContributor = true;
                                }
                            }
                        }
                    }

                    for (Actor actor : actorList) {
                        if (name.equalsIgnoreCase(actor.name)) {
                            entityExists = true;
                        }
                    }

                    if (!entityExists) {
                        System.out.println("Typo or actor doesn't exist!");
                        Thread.sleep(3000);
                    }
                } while (!entityExists);

                if (validContributor) {
                    System.out.println("You can't create a request for your own contribution");
                    Thread.sleep(3000);
                    return;
                }
                break;

            case 2:
                requestType = RequestType.MOVIE_ISSUE;
                List<Production> prodList = IMDB.getInstance().prodList;

                do {
                    System.out.println("Select production:");
                    for (Production production : prodList) {
                        System.out.println("-> " + production.title);
                    }

                    name = input.nextLine();

                    for (Production production : prodList) {
                        if (name.equalsIgnoreCase(production.title)) {
                            entityExists = true;
                        }
                    }

                    if (!entityExists) {
                        System.out.println("Typo or production doesn't exist!");
                        Thread.sleep(3000);
                    }
                } while (!entityExists);

                entityExists = false;
                break;
            case 3:
                requestType = RequestType.DELETE_ACCOUNT;
                break;
            case 4:
                requestType = RequestType.OTHERS;
                break;
        }

        if (type == 1 || type == 2) {
            String contributorUsername = null;
            for (User<AccountType> userContributor : IMDB.getInstance().userList) {
                if (userContributor instanceof Staff) {
                    Staff staff = (Staff) userContributor;

                    for (Object contribution : staff.contribution) {
                        if (contribution instanceof Actor) {
                            if (((Actor) contribution).name.equalsIgnoreCase(name)) {
                                contributorUsername = staff.getUsername();
                                staffMember = (Staff) userContributor;
                            }
                        } else if (contribution instanceof Production) {
                            if (((Production) contribution).title.equalsIgnoreCase(name)) {
                                contributorUsername = staff.getUsername();
                                staffMember = (Staff) userContributor;
                            }
                        }
                    }
                }
            }

            Request request = new Request(requestType, currentTime, name, description, user.username, contributorUsername);

            request.register(user);
            user.setSubject(request);

            staffMember.requests.add(request);
            if (user instanceof Regular) {
                ((Regular) user).createRequest(request);
            } else if (user instanceof Contributor) {
                ((Contributor) user).createRequest(request);
            }
        } else if (type == 3 || type == 4) {
            Request request = new Request(requestType, currentTime, description, user.username, "ADMIN");
            if (type == 4) {
                request.register(user);
                user.setSubject(request);
            }

            for (User<AccountType> adminIter : IMDB.getInstance().userList) {
                if (adminIter instanceof Admin) {
                    ((Admin) adminIter).requests.add(request);
                }
            }

            if (user instanceof Regular) {
                ((Regular) user).createRequest(request);
            } else if (user instanceof Contributor) {
                ((Contributor) user).createRequest(request);
            }
            RequestsHolder.addRequest(request);
        }

        System.out.println("Request added successfully!");
        Thread.sleep(3000);
    }

    public static void removeRequest(User<AccountType> user, Scanner input) throws InterruptedException {
        boolean requestFound = false;
        boolean validRequest = false;
        List<Request> requestList = IMDB.getInstance().requestList;
        List<Request> userReqList = new ArrayList<>();

        do {
            int index = 1;
            System.out.println("What request do you want to remove?");
            for (Request req : requestList) {
                if (req.requesterUsername.equals(user.username)) {
                    System.out.println(index + ". " + req.description);
                    userReqList.add(req);
                    index++;
                    requestFound = true;
                }
            }

            if (requestFound == false) {
                System.out.println("You don't have any active requests.");
                Thread.sleep(3000);
            }

            if (input.hasNextInt()) {
                int userInput = input.nextInt();
                input.nextLine();

                if (userInput >= 1 && userInput <= index - 1) {
                    validRequest = true;
                    Request selectedRequest = userReqList.get(userInput - 1);

                    requestList.remove(selectedRequest);
                    if (user instanceof Regular) {
                        ((Regular) user).removeRequest(selectedRequest);
                    } else if (user instanceof Contributor) {
                        ((Contributor) user).removeRequest(selectedRequest);
                    }
                    System.out.println("Request removed successfully.");
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
        } while (!validRequest);
    }

    public static void manageRequests(User<AccountType> user, Scanner input) throws InterruptedException {
        boolean validRequest = false;
        boolean requestFound = false;
        List<Request> requestList = ((Staff)user).requests;
        List<Request> userReqList = new ArrayList<>();

        do {
            int reqIndex = 1;

            for (Request requestIter : requestList) {
                System.out.print(reqIndex + ". " + requestIter.getRequestType());
                System.out.println(": " + requestIter.getDescription());
                userReqList.add(requestIter);
                reqIndex++;
                requestFound = true;
            }

            if (requestFound == false) {
                System.out.println("There are no active requests");
                Thread.sleep(3000);
            }

            if (input.hasNextInt()) {
                int reqInput = input.nextInt();
                input.nextLine();

                if (reqInput >= 1 && reqInput <= reqIndex - 1) {
                    validRequest = true;
                    Request selectedRequest = userReqList.get(reqInput - 1);

                    if (selectedRequest.getRequestType().equals(RequestType.MOVIE_ISSUE) ||
                            selectedRequest.getRequestType().equals(RequestType.ACTOR_ISSUE) ||
                            selectedRequest.getRequestType().equals(RequestType.OTHERS)) {

                        System.out.println("1) Accept Request");
                        System.out.println("2) Deny Request");
                        int requestChoice = input.nextInt();
                        input.nextLine();

                        switch (requestChoice) {
                            case 1:
                                System.out.println("-> If you solved this request type 'done'");
                                System.out.println("-> If you want to solve this request press any key");

                                if (input.nextLine().equalsIgnoreCase("done")) {
                                    if (selectedRequest.getRequestType().equals(RequestType.MOVIE_ISSUE) ||
                                            selectedRequest.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                                        selectedRequest.postMessageRequest(user.username, selectedRequest.name, 1);
                                    } else if (selectedRequest.getRequestType().equals(RequestType.OTHERS)) {
                                        selectedRequest.postMessageRequest(user.username, selectedRequest.name, 2);
                                    }
                                    IMDB.getInstance().requestList.remove(selectedRequest);
                                }
                                break;

                            case 2:
                                IMDB.getInstance().requestList.remove(selectedRequest);
                                break;

                            default:
                                System.out.println("Invalid input. Enter a number between 1 and 2");
                                Thread.sleep(3000);
                                break;
                        }
                    } else if (selectedRequest.getRequestType().equals(RequestType.DELETE_ACCOUNT)){
                        System.out.println("1) Accept Request");
                        System.out.println("2) Deny Request");
                        int requestChoice = input.nextInt();
                        input.nextLine();

                        switch (requestChoice) {
                            case 1:
                                User<AccountType> userToDelete = null;
                                for (User<AccountType> userIter : IMDB.getInstance().userList) {
                                    if (userIter.username.equalsIgnoreCase(selectedRequest.getRequesterUsername())) {
                                        userToDelete = userIter;
                                    }
                                }
                                if (userToDelete == null) {
                                    System.out.println("User not found!");
                                } else if (userToDelete != null) {
                                    IMDB.getInstance().userList.remove(userToDelete);
                                    System.out.println("User deleted successfully");
                                    Thread.sleep(3000);
                                }
                                IMDB.getInstance().requestList.remove(selectedRequest);
                                break;

                            case 2:
                                IMDB.getInstance().requestList.remove(selectedRequest);
                                break;

                            default:
                                System.out.println("Invalid input. Enter a number between 1 and 2");
                                Thread.sleep(3000);
                                break;
                        }
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    Thread.sleep(3000);
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
                Thread.sleep(3000);
            }
        } while (!validRequest);
    }

    public static String generateUsername(String name, String surname) {
        String numbers = "0123456789";
        StringBuilder randomNumbers = new StringBuilder();

        name = name.toLowerCase();
        surname = surname.toLowerCase();

        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(numbers.length());
            randomNumbers.append(numbers.charAt(index));
        }

        return name + "_" + surname + "_" + randomNumbers;
    }

    public static String generatePassword() {
        String chars = "KLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int randomIndex = random.nextInt(chars.length());
            char randomChar = chars.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }

        return passwordBuilder.toString();
    }

    public static void run() throws InterruptedException {
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("Welcome Back! Enter your credentials!");

            Scanner input = new Scanner(System.in);
            int count = 0;
            boolean authenticationSuccessful = false;
            User user = null;

            System.out.print("email: ");
            String email = input.nextLine();

            while (!authenticationSuccessful && count < 3) {
                for (User<AccountType> iter : IMDB.getInstance().userList) {
                    if (email.equals(iter.userInfo.getCredentials().email)) {
                        System.out.print("password: ");
                        String pass = input.nextLine();
                        if (pass.equals(iter.userInfo.getCredentials().password)) {
                            System.out.println("Welcome back user " + iter.username + "!");
                            System.out.println("Username: " + iter.username);
                            user = iter;
                            authenticationSuccessful = true;
                        } else {
                            count++;
                        }
                    }
                }
                if (count == 3) {
                    System.out.println("You have exceeded the number of password attempts. please try again later.");
                }
            }

            if (!authenticationSuccessful) {
                System.exit(0);
            }

            while (user != null) {
                if (user.getAccType() == AccountType.REGULAR) {
                    System.out.println("User XP: " + user.getXP());
                    System.out.println("Choose action:");
                    System.out.println("\t1) View production details");
                    System.out.println("\t2) View actors details");
                    System.out.println("\t3) View notifications");
                    System.out.println("\t4) Search for actor/movie/series");
                    System.out.println("\t5) Add/Delete actor/movie/series to/from favorites");
                    System.out.println("\t6) Create/Delete request");
                    System.out.println("\t7) Add/Delete review");
                    System.out.println("\t8) Logout");

                    switch (input.nextInt()) {
                        case 1:
                            System.out.println("Do you want to filter productions?");
                            System.out.println("1) By genre");
                            System.out.println("2) By number of reviews");
                            System.out.println("3) No");

                            int choiceProduction = input.nextInt();
                            input.nextLine();

                            switch (choiceProduction) {
                                case 1:
                                    boolean genreVerify = false;

                                    System.out.println("Select genre: (First uppercase)");
                                    for (Genre genreIter : Genre.values()) {
                                        System.out.println("->" + genreIter);
                                    }

                                    String selectedGenre = input.nextLine();
                                    for (Genre genreIter : Genre.values()) {
                                        if (String.valueOf(genreIter).equals(selectedGenre)) {
                                            genreVerify = true;
                                        }
                                    }

                                    if (!genreVerify) {
                                        System.out.println("Enter a valid genre");
                                        Thread.sleep(3000);
                                    } else {
                                        displayProductions(selectedGenre, false);
                                        Thread.sleep(3000);
                                    }
                                    break;

                                case 2:
                                    displayProductions(null, true);
                                    Thread.sleep(3000);
                                    break;

                                case 3:
                                    displayProductions(null, false);
                                    break;

                                default:
                                    System.out.println("Please enter a number from 1 to 3");
                                    break;
                            }
                            break;

                        case 2:
                            System.out.println("Do you want to filter actors? (A-Z)");
                            System.out.println("1) Yes");
                            System.out.println("2) No");

                            int choiceActors = input.nextInt();
                            input.nextLine();

                            switch (choiceActors) {
                                case 1:
                                    displayActors(true);
                                    break;

                                case 2:
                                    displayActors(false);
                                    break;

                                case 3:
                                    System.out.println("Enter a number between 1 and 2");
                                    break;
                            }
                            break;

                        case 3:
                            displayNotifications(user);
                            break;

                        case 4:
                            System.out.println("What do you want to find?");
                            input.nextLine();
                            String key = input.nextLine();
                            search(key);
                            break;

                        case 5:
                            String name;
                            System.out.println("Pick action:");
                            System.out.println("\t1) Add to favorites");
                            System.out.println("\t2) Remove from favorites");
                            input.nextLine();

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice == 1 || choice == 2) {
                                    switch (choice) {
                                        case 1:
                                            System.out.println("What do you want to add to favorites?");
                                            Thread.sleep(1500);
                                            for (Actor actor : IMDB.getInstance().actorList) {
                                                System.out.println("-> " + actor.name);
                                            }
                                            for (Production prod : IMDB.getInstance().prodList) {
                                                System.out.println("-> " + prod.title);
                                            }

                                            name = input.nextLine();
                                            manageFavorites(user, choice, name);
                                            break;

                                        case 2:
                                            System.out.println("What do you want to remove from favorites?");
                                            for (Object iter : user.favList) {
                                                if (iter instanceof Production) {
                                                    System.out.println("->" + ((Production) iter).title);
                                                } else if (iter instanceof Actor) {
                                                    System.out.println("->" + ((Actor) iter).name);
                                                }
                                            }
                                            name = input.nextLine();
                                            manageFavorites(user, choice, name);
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;

                        case 6:
                            System.out.println("Pick action:");
                            System.out.println("\t1) Create Request");
                            System.out.println("\t2) Remove Request");
                            input.nextLine();
                            int reqIndex = input.nextInt();
                            input.nextLine();

                            if (reqIndex == 1) {
                                System.out.println("Select request type:");
                                System.out.println("\t1) Actor Issue");
                                System.out.println("\t2) Movie Issue");
                                System.out.println("\t3) Delete Account");
                                System.out.println("\t4) Others");
                                int type = input.nextInt();
                                input.nextLine();

                                createRequest(user, type, input);
                            } else if (reqIndex == 2) {
                                removeRequest(user, input);
                            }
                            break;

                        case 7:
                            System.out.println("Pick action:");
                            System.out.println("\t1) Add review");
                            System.out.println("\t2) Delete review");
                            input.nextLine();
                            int reviewIndex = input.nextInt();
                            input.nextLine();

                            if (reviewIndex == 1) {
                                ((Regular) user).addRating(user, input);
                            } else if (reviewIndex == 2) {
                                ((Regular) user).deleteRating(user, input);
                            }
                            break;

                        case 8:
                            System.out.println("Pick action:");
                            System.out.println("1) Log out");
                            System.out.println("2) Exit");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice == 1 || choice == 2) {
                                    switch (choice) {
                                        case 1:
                                            loggedIn = false;
                                            user = user.logOut(user);
                                            break;
                                        case 2:
                                            String filepath = "C:\\Users\\Nicu\\Desktop\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\main\\resources\\media\\(ORIGINAL) Change Da World My Final Message Goodbye.wav";
                                            AudioPlayer.playMusic(filepath);
                                            JOptionPane.showMessageDialog(null, "Press Ok to stop playing.");
                                            System.out.println("Change da world");
                                            System.out.println("My final message:");
                                            System.out.println("GOODBYE!");
                                            System.exit(0);
                                            break;
                                        default:
                                            System.out.println("Invalid input. Please enter 1 or 2.");
                                            Thread.sleep(3000);
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;
                    }
                } else if (user.getAccType() == AccountType.CONTRIBUTOR) {
                    System.out.println("User XP: " + user.getXP());
                    System.out.println("Choose action:");
                    System.out.println("\t1) View production details");
                    System.out.println("\t2) View actors details");
                    System.out.println("\t3) View notifications");
                    System.out.println("\t4) Search for actor/movie/series");
                    System.out.println("\t5) Add/Delete actor/movie/series to/from favorites");
                    System.out.println("\t6) Create/Delete request");
                    System.out.println("\t7) Add/Delete actor/movie/series to/from system");
                    System.out.println("\t8) View/Solve request");
                    System.out.println("\t9) Update Production/Actor");
                    System.out.println("\t10) Logout");

                    switch (input.nextInt()) {
                        case 1:
                            System.out.println("Do you want to filter productions?");
                            System.out.println("1) By genre");
                            System.out.println("2) By number of reviews");
                            System.out.println("3) No");

                            int choiceProduction = input.nextInt();
                            input.nextLine();

                            switch (choiceProduction) {
                                case 1:
                                    boolean genreVerify = false;

                                    System.out.println("Select genre: (First uppercase)");
                                    for (Genre genreIter : Genre.values()) {
                                        System.out.println("->" + genreIter);
                                    }

                                    String selectedGenre = input.nextLine();
                                    for (Genre genreIter : Genre.values()) {
                                        if (String.valueOf(genreIter).equals(selectedGenre)) {
                                            genreVerify = true;
                                        }
                                    }

                                    if (!genreVerify) {
                                        System.out.println("Enter a valid genre");
                                        Thread.sleep(3000);
                                    } else {
                                        displayProductions(selectedGenre, false);
                                        Thread.sleep(3000);
                                    }
                                    break;

                                case 2:
                                    displayProductions(null, true);
                                    Thread.sleep(3000);
                                    break;

                                case 3:
                                    displayProductions(null, false);
                                    break;

                                default:
                                    System.out.println("Please enter a number from 1 to 3");
                                    break;
                            }
                            break;

                        case 2:
                            System.out.println("Do you want to filter actors? (A-Z)");
                            System.out.println("1) Yes");
                            System.out.println("2) No");

                            int choiceActors = input.nextInt();
                            input.nextLine();

                            switch (choiceActors) {
                                case 1:
                                    displayActors(true);
                                    break;

                                case 2:
                                    displayActors(false);
                                    break;

                                case 3:
                                    System.out.println("Enter a number between 1 and 2");
                                    break;
                            }
                            break;

                        case 3:
                            displayNotifications(user);
                            break;

                        case 4:
                            System.out.println("What do you want to find?");
                            input.nextLine();
                            String key = input.nextLine();
                            search(key);
                            break;

                        case 5:
                            System.out.println("Pick action:");
                            System.out.println("\t1) Add to favorites");
                            System.out.println("\t2) Remove from favorites");
                            input.nextLine();
                            int index = input.nextInt();
                            input.nextLine();

                            if (index == 1) {
                                System.out.println("What do you want to add to favorites?");
                            } else if (index == 2) {
                                System.out.println("What do you want to remove from favorites?");
                            }
                            String name = input.nextLine();
                            manageFavorites(user, index, name);
                            break;

                        case 6:
                            System.out.println("Pick action:");
                            System.out.println("\t1) Create Request");
                            System.out.println("\t2) Remove Request");
                            input.nextLine();
                            int reqIndex = input.nextInt();
                            input.nextLine();

                            if (reqIndex == 1) {
                                System.out.println("Select request type:");
                                System.out.println("\t1) Actor Issue");
                                System.out.println("\t2) Movie Issue");
                                System.out.println("\t3) Delete Account");
                                System.out.println("\t4) Others");
                                int type = input.nextInt();
                                input.nextLine();

                                createRequest(user, type, input);
                            } else if (reqIndex == 2) {
                                removeRequest(user, input);
                            }
                            break;

                        case 7:
                            System.out.println("Select Action:");
                            System.out.println("1) Add Actor");
                            System.out.println("2) Delete Actor");
                            System.out.println("3) Add Production");
                            System.out.println("4) Delete Production");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice >= 1 && choice <= 4) {
                                    switch (choice) {
                                        case 1:
                                            ((Contributor)user).addActorSystem(input);
                                            break;

                                        case 2:
                                            System.out.println("What actor do you want to remove?");
                                            Thread.sleep(1500);
                                            for (Actor actor : IMDB.getInstance().actorList) {
                                                System.out.println("-> " + actor.name);
                                            }
                                            ((Contributor)user).removeActorSystem(input.nextLine());
                                            break;

                                        case 3:
                                            ((Contributor)user).addProductionSystem(input, user);
                                            break;

                                        case 4:
                                            System.out.println("What production do you want to remove?");
                                            Thread.sleep(1500);
                                            for (Production prod : IMDB.getInstance().prodList) {
                                                System.out.println("-> " + prod.title);
                                            }
                                            ((Contributor)user).removeProductionSystem(input.nextLine());
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;

                        case 8:
                            manageRequests(user, input);
                            break;

                        case 9:
                            System.out.println("Select Action:");
                            System.out.println("1) Update Actor");
                            System.out.println("2) Update Production");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice >= 1 && choice <= 2) {
                                    switch (choice) {
                                        case 1:
                                            ((Contributor)user).updateActor(input);
                                            break;

                                        case 2:
                                            ((Contributor)user).updateProduction(input);
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;

                        case 10:
                            System.out.println("Pick action:");
                            System.out.println("1) Log out");
                            System.out.println("2) Exit");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice == 1 || choice == 2) {
                                    switch (choice) {
                                        case 1:
                                            loggedIn = false;
                                            user = user.logOut(user);
                                            break;
                                        case 2:
                                            String filepath = "C:\\Users\\Nicu\\Desktop\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\main\\resources\\media\\(ORIGINAL) Change Da World My Final Message Goodbye.wav";
                                            AudioPlayer.playMusic(filepath);
                                            JOptionPane.showMessageDialog(null, "Press Ok to stop playing.");
                                            System.out.println("Change da world");
                                            System.out.println("My final message:");
                                            System.out.println("GOODBYE!");
                                            System.exit(0);
                                            break;
                                        default:
                                            System.out.println("Invalid input. Please enter 1 or 2.");
                                            Thread.sleep(3000);
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;
                    }
                } else if (user.getAccType() == AccountType.ADMIN) {
                    System.out.println("User XP: -");
                    System.out.println("Choose action:");
                    System.out.println("\t1) View production details");
                    System.out.println("\t2) View actors details");
                    System.out.println("\t3) View notifications");
                    System.out.println("\t4) Search for actor/movie/series");
                    System.out.println("\t5) Add/Delete actor/movie/series to/from favorites");
                    System.out.println("\t6) Add/Delete actor/movie/series to/from system");
                    System.out.println("\t7) View/Solve request");
                    System.out.println("\t8) Update Production/Actor");
                    System.out.println("\t9) Add/Delete user");
                    System.out.println("\t10) Logout");

                    switch (input.nextInt()) {
                        case 1:
                            System.out.println("Do you want to filter productions?");
                            System.out.println("1) By genre");
                            System.out.println("2) By number of reviews");
                            System.out.println("3) No");

                            int choiceProduction = input.nextInt();
                            input.nextLine();

                            switch (choiceProduction) {
                                case 1:
                                    boolean genreVerify = false;

                                    System.out.println("Select genre: (First uppercase)");
                                    for (Genre genreIter : Genre.values()) {
                                        System.out.println("->" + genreIter);
                                    }

                                    String selectedGenre = input.nextLine();
                                    for (Genre genreIter : Genre.values()) {
                                        if (String.valueOf(genreIter).equals(selectedGenre)) {
                                            genreVerify = true;
                                        }
                                    }

                                    if (!genreVerify) {
                                        System.out.println("Enter a valid genre");
                                        Thread.sleep(3000);
                                    } else {
                                        displayProductions(selectedGenre, false);
                                        Thread.sleep(3000);
                                    }
                                    break;

                                case 2:
                                    displayProductions(null, true);
                                    Thread.sleep(3000);
                                    break;

                                case 3:
                                    displayProductions(null, false);
                                    break;

                                default:
                                    System.out.println("Please enter a number from 1 to 3");
                                    break;
                            }
                            break;

                        case 2:
                            System.out.println("Do you want to filter actors? (A-Z)");
                            System.out.println("1) Yes");
                            System.out.println("2) No");

                            int choiceActors = input.nextInt();
                            input.nextLine();

                            switch (choiceActors) {
                                case 1:
                                    displayActors(true);
                                    break;

                                case 2:
                                    displayActors(false);
                                    break;

                                case 3:
                                    System.out.println("Enter a number between 1 and 2");
                                    break;
                            }
                            break;

                        case 3:
                            displayNotifications(user);
                            break;

                        case 4:
                            System.out.println("What do you want to find?");
                            input.nextLine();
                            String key = input.nextLine();
                            search(key);
                            break;

                        case 5:
                            System.out.println("Pick action:");
                            System.out.println("\t1) Add to favorites");
                            System.out.println("\t2) Remove from favorites");
                            input.nextLine();
                            int index = input.nextInt();
                            input.nextLine();

                            if (index == 1) {
                                System.out.println("What do you want to add to favorites?");
                            } else if (index == 2) {
                                System.out.println("What do you want to remove from favorites?");
                            }
                            String name = input.nextLine();
                            manageFavorites(user, index, name);
                            break;

                        case 6:
                            System.out.println("Select Action:");
                            System.out.println("1) Add Actor");
                            System.out.println("2) Delete Actor");
                            System.out.println("3) Add Production");
                            System.out.println("4) Delete Production");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice >= 1 && choice <= 4) {
                                    switch (choice) {
                                        case 1:
                                            ((Admin)user).addActorSystem(input);
                                            break;

                                        case 2:
                                            System.out.println("What actor do you want to remove?");
                                            Thread.sleep(1500);
                                            for (Actor actor : IMDB.getInstance().actorList) {
                                                System.out.println("-> " + actor.name);
                                            }
                                            ((Admin)user).removeActorSystem(input.nextLine());
                                            break;

                                        case 3:
                                            ((Admin)user).addProductionSystem(input, user);
                                            break;

                                        case 4:
                                            System.out.println("What production do you want to remove?");
                                            Thread.sleep(1500);
                                            for (Production prod : IMDB.getInstance().prodList) {
                                                System.out.println("-> " + prod.title);
                                            }
                                            ((Admin)user).removeProductionSystem(input.nextLine());
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;

                        case 7:
                            manageRequests(user, input);
                            break;

                        case 8:
                            System.out.println("Select Action:");
                            System.out.println("1) Update Actor");
                            System.out.println("2) Update Production");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice >= 1 && choice <= 2) {
                                    switch (choice) {
                                        case 1:
                                            ((Admin)user).updateActor(input);
                                            break;

                                        case 2:
                                            ((Admin)user).updateProduction(input);
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;

                        case 9:
                            System.out.println("Select Action:");
                            System.out.println("1) Add User");
                            System.out.println("2) Delete User");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice >= 1 && choice <= 2) {
                                    switch (choice) {
                                        case 1:
                                            System.out.println("Enter name:");
                                            String newName = input.nextLine();

                                            System.out.println("Enter surname:");
                                            String newSurname = input.nextLine();

                                            String finalName = newName + " " + newSurname;

                                            String newUsername = generateUsername(newName, newSurname);
                                            String newPassword = generatePassword();

                                            System.out.println("Enter email:");
                                            String newEmail = input.nextLine();

                                            Credentials credentials = new Credentials(newEmail, newPassword);

                                            System.out.println("Enter country:");
                                            String country = input.nextLine();

                                            System.out.println("Enter age:");
                                            int age = input.nextInt();

//                                            System.out.println("Enter date of birth (YYYY-MM-DD):");
////                                            String dobString = input.nextLine();
//                                            LocalDate dateOfBirth = LocalDate.parse(input.nextLine());


                                            System.out.println("Enter gender (M/F):");
                                            char gender = input.next().charAt(0);
                                            input.nextLine();

                                            User.Information userInfo = new User.Information.
                                                    InformationBuilder(credentials, null, newName,
                                                        country, age, gender).build();

                                            System.out.println("Enter account type:");
                                            String accType = input.nextLine();

                                            if (accType.equalsIgnoreCase("regular")) {
                                                User<AccountType> regular = new Regular(userInfo, AccountType.REGULAR, newUsername, 0, null);
                                                IMDB.getInstance().userList.add(regular);
                                            } else if (accType.equalsIgnoreCase("contributor")) {
                                                User<AccountType> contributor = new Contributor(userInfo, AccountType.CONTRIBUTOR, newUsername, 0, null);
                                                IMDB.getInstance().userList.add(contributor);
                                            } else if (accType.equalsIgnoreCase("admin")) {
                                                User<AccountType> admin = new Admin(userInfo, AccountType.ADMIN, newUsername, 0, null);
                                                IMDB.getInstance().userList.add(admin);
                                            }

//                                            for (User<AccountType> idk : IMDB.getInstance().userList) {
//                                                System.out.println("->" + idk.username);
//                                            }
                                            System.out.println("User added successfully!");
                                            System.out.println("------------------------");
                                            System.out.println("New user credentials:");
                                            System.out.println("Email: " + newEmail);
                                            System.out.println("Password: " + newPassword);
                                            System.out.println("------------------------");
                                            Thread.sleep(3000);
                                            break;

                                        case 2:
                                            boolean userFound = false;
                                            boolean validUser = false;
                                            List<User<AccountType>> userList = new ArrayList<>();
                                            SortedSet<Object> contributionList = new TreeSet<>();
                                            List<Request> requestList = new ArrayList<>();

                                            do {
                                                int userIndex = 1;
                                                System.out.println("What user do you want to remove?");
                                                for (User<AccountType> userIter : IMDB.getInstance().userList) {
                                                        System.out.println(userIndex + ". " + userIter.username);
                                                        userList.add(userIter);
                                                        userIndex++;
                                                }

                                                if (input.hasNextInt()) {
                                                    int userInput = input.nextInt();
                                                    input.nextLine();

                                                    if (userInput >= 1 && userInput <= userIndex - 1) {
                                                        validUser = true;
                                                        User<AccountType> selectedUser = userList.get(userInput - 1);

                                                        if (selectedUser instanceof Contributor) {
                                                            contributionList.addAll(((Contributor) selectedUser).contribution);
                                                            requestList.addAll(((Contributor) selectedUser).requests);

                                                            for (User<AccountType> adminIter : IMDB.getInstance().userList) {
                                                                if (adminIter instanceof Admin) {
                                                                    ((Admin) adminIter).contribution.addAll(contributionList);
                                                                }
                                                            }

                                                            for (User<AccountType> adminIter : IMDB.getInstance().userList) {
                                                                if (adminIter instanceof Admin) {
                                                                    ((Admin) adminIter).requests.addAll(requestList);
                                                                }
                                                            }
                                                        }

                                                        IMDB.getInstance().userList.remove(selectedUser);
                                                        System.out.println("User removed successfully.");
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
                                            } while (!validUser);
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;

                        case 10:
                            System.out.println("Pick action:");
                            System.out.println("1) Log out");
                            System.out.println("2) Exit");

                            if (input.hasNextInt()) {
                                int choice = input.nextInt();
                                input.nextLine();

                                if (choice == 1 || choice == 2) {
                                    switch (choice) {
                                        case 1:
                                            loggedIn = false;
                                            user = user.logOut(user);
                                            break;
                                        case 2:
                                            String filepath = "C:\\Users\\Nicu\\Desktop\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\main\\resources\\media\\(ORIGINAL) Change Da World My Final Message Goodbye.wav";
                                            AudioPlayer.playMusic(filepath);
                                            JOptionPane.showMessageDialog(null, "Press Ok to stop playing.");
                                            System.out.println("Change da world");
                                            System.out.println("My final message:");
                                            System.out.println("GOODBYE!");
                                            System.exit(0);
                                            break;
                                        default:
                                            System.out.println("Invalid input. Please enter 1 or 2.");
                                            Thread.sleep(3000);
                                            break;
                                    }
                                } else {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    Thread.sleep(3000);
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                input.nextLine();
                                Thread.sleep(3000);
                            }
                            break;
                    }
                }
            }
        }
    }
}
