package org.example;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.*;

public class Admin extends Staff {
    public Admin(Information userInfo, AccountType accType, String username, Integer XP, List<String> notifList, List list) {
        super(userInfo, accType, username, XP, notifList, list);
    }

    public Admin(Information userInfo, AccountType accType, String username, Integer XP, List<String> notifList) {
        super(userInfo, accType, username, XP, notifList);
    }

    @Override
    public void addProductionSystem(Scanner input, User<AccountType> user) throws InterruptedException {
        System.out.println("Enter production type (movie/series):");
        String productionType = input.nextLine().toLowerCase();

        System.out.println("Enter production title:");
        String productionTitle = input.nextLine();

        System.out.println("Enter directors (comma-separated):");
        List<String> directors = new ArrayList<>(List.of(input.nextLine().split(",\\s*")));

        System.out.println("Enter actors (comma-separated):");
        List<String> actors = new ArrayList<>(List.of(input.nextLine().split(",\\s*")));

        System.out.println("Enter genres (comma-separated):");
        for (Genre genreIter : Genre.values()) {
            System.out.println("->" + genreIter);
        }
        List<Genre> genres = new ArrayList<>();
        for (String genre : input.nextLine().split(",\\s*")) {
            String formattedGenre = genre.substring(0, 1).toUpperCase() + genre.substring(1).toLowerCase();
            genres.add(Genre.valueOf(formattedGenre));
        }

        System.out.println("Enter description:");
        String description = input.nextLine();

        if (productionType.equals("movie")) {
            System.out.println("Enter movie duration:");
            String duration = input.nextLine();

            System.out.println("Enter movie year:");
            Integer year = input.nextInt();
            input.nextLine();
            List<Rating> ratingList = new ArrayList<>();

            Movie newMovie = new Movie(productionTitle, directors, actors, genres, ratingList, description, null, duration, year);
            newMovie.register(user, newMovie);
            user.setSubject(newMovie);

            IMDB.getInstance().prodList.add(newMovie);
            System.out.println("Production added successfully!");
            Thread.sleep(3000);
        } else if (productionType.equals("series")) {
            System.out.println("Enter series year:");
            Integer year = input.nextInt();
            input.nextLine();

            System.out.println("Enter series season number:");
            Integer seasonNumber = input.nextInt();
            input.nextLine();

            Map<String, List<Episode>> episodes = new HashMap<>();

            for (int i = 1; i <= seasonNumber; i++) {
                System.out.println("Enter number of episodes for Season " + i + ":");
                int numEpisodes = input.nextInt();
                input.nextLine();

                List<Episode> episodeList = new ArrayList<>();

                for (int j = 1; j <= numEpisodes; j++) {
                    System.out.println("Enter details for Episode " + j + " of Season " + i + ":");
                    System.out.println("Enter episode title:");
                    String episodeTitle = input.nextLine();

                    System.out.println("Enter episode duration:");
                    String episodeDuration = input.nextLine();

                    episodeList.add(new Episode(episodeTitle, episodeDuration));
                }

                episodes.put("Season " + i, episodeList);
            }

            List<Rating> ratingList = new ArrayList<>();
            Series newSeries = new Series(productionTitle, directors, actors, genres, ratingList, description, null, year, seasonNumber, episodes);
            newSeries.register(user, newSeries);
            user.setSubject(newSeries);

            IMDB.getInstance().prodList.add(newSeries);
            System.out.println("Production added successfully!");
            Thread.sleep(3000);
        } else {
            System.out.println("Unsupported production type");
            Thread.sleep(3000);
        }
    }

    @Override
    public void addActorSystem(Scanner input) throws InterruptedException {
        System.out.println("Enter actor's name:");
        String actorName = input.nextLine();

        System.out.println("Enter actor's bio:");
        String actorBio = input.nextLine();

        List<Pair> performances = new ArrayList<>();

        while (true) {
            System.out.println("Enter movie title (or type 'done' to finish):");
            String movieTitle = input.nextLine();

            if (movieTitle.equalsIgnoreCase("done")) {
                break;
            }

            System.out.println("Enter movie type:");
            String movieType = input.nextLine();

            Pair performance = new Pair(movieTitle, movieType);
            performances.add(performance);
        }

        Actor actor = new Actor(actorName, performances, actorBio);
        IMDB.getInstance().actorList.add(actor);

        System.out.println("Actor added successfully!\n");
        Thread.sleep(3000);
    }

    @Override
    public void removeProductionSystem(String name) throws InterruptedException {
        boolean foundProduction = false;
        Production production = null;

        for (Production iterator : IMDB.getInstance().prodList) {
            if (iterator.title.equalsIgnoreCase(name)) {
                production = iterator;
                foundProduction = true;
            }
        }
        if (!foundProduction) {
            System.out.println("Production couldn't be found!");
            Thread.sleep(3000);
        } else {
            IMDB.getInstance().prodList.remove(production);
            System.out.println("Production removed successfully!");
            Thread.sleep(3000);
        }
    }

    @Override
    public void removeActorSystem(String name) throws InterruptedException {
        boolean actorFound = false;
        Actor actor = null;

        for (Actor iterator : IMDB.getInstance().actorList) {
            if (iterator.name.equalsIgnoreCase(name)) {
                actor = iterator;
                actorFound = true;
            }
        }
        if (!actorFound) {
            System.out.println("Actor couldn't be found.");
            Thread.sleep(3000);
        } else {
            IMDB.getInstance().actorList.remove(actor);
            System.out.println("Actor removed successfully!");
            Thread.sleep(3000);
        }
    }

    @Override
    public void updateProduction(Scanner input) throws InterruptedException {
        boolean productionFound = false;
        Production production = null;

        System.out.println("What production do you want to update?");
        Thread.sleep(1500);
        for (Production iter : IMDB.getInstance().prodList) {
            System.out.println("-> " + iter.title);
        }
        String title = input.nextLine();

        for (Production iterator : IMDB.getInstance().prodList) {
            if (iterator.title.equalsIgnoreCase(title)) {
                production = iterator;
                productionFound = true;
            }
        }

        if (!productionFound) {
            System.out.println("Production couldn't be found.");
            Thread.sleep(3000);
        } else {
            System.out.println("What do you want to update?");
            System.out.println("1) Title");
            System.out.println("2) Directors");
            System.out.println("3) Actors");
            System.out.println("4) Genres");
            System.out.println("5) Description");

            if (input.hasNextInt()) {
                int choice = input.nextInt();
                input.nextLine();

                if (choice >= 1 && choice <= 5) {
                    switch (choice) {
                        case 1:
                            System.out.println("Type new title:");
                            production.updateTitle(input.nextLine());
                            break;

                        case 2:
                            System.out.println("Pick action:");
                            System.out.println("1) Add new director(s)");
                            System.out.println("2) Delete existing director");

                            if (input.hasNextInt()) {
                                int choice2 = input.nextInt();
                                input.nextLine();

                                if (choice2 >= 1 && choice2 <= 2) {
                                    switch (choice2) {
                                        case 1:
                                            System.out.println("Enter the number of new directors:");
                                            int numDirectors = input.nextInt();
                                            input.nextLine();

                                            List<String> newDirectors = new ArrayList<>();

                                            for (int i = 0; i < numDirectors; i++) {
                                                System.out.println("Enter director name:");
                                                newDirectors.add(input.nextLine());
                                            }

                                            production.directorList.addAll(newDirectors);
                                            break;

                                        case 2:
                                            System.out.println("What director do you want to delete?");
                                            for (String director : production.directorList) {
                                                System.out.println("-> " + director);
                                            }

                                            production.directorList.remove(input.nextLine());
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

                        case 3:
                            System.out.println("Pick action:");
                            System.out.println("1) Add new actor(s)");
                            System.out.println("2) Delete existing actor");

                            if (input.hasNextInt()) {
                                int choice3 = input.nextInt();
                                input.nextLine();

                                if (choice3 >= 1 && choice3 <= 2) {
                                    switch (choice3) {
                                        case 1:
                                            System.out.println("Enter the number of new actors:");
                                            int numActors = input.nextInt();
                                            input.nextLine();

                                            List<String> newActors = new ArrayList<>();

                                            for (int i = 0; i < numActors; i++) {
                                                System.out.println("Enter actor name:");
                                                newActors.add(input.nextLine());
                                            }

                                            production.actorList.addAll(newActors);
                                            break;

                                        case 2:
                                            System.out.println("What actor do you want to delete?");
                                            for (String actor : production.actorList) {
                                                System.out.println("-> " + actor);
                                            }

                                            production.actorList.remove(input.nextLine());
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

                        case 4:
                            System.out.println("Pick action:");
                            System.out.println("1) Add new genre(s)");
                            System.out.println("2) Delete existing genre");

                            if (input.hasNextInt()) {
                                int choice4 = input.nextInt();
                                input.nextLine();

                                if (choice4 >= 1 && choice4 <= 2) {
                                    switch (choice4) {
                                        case 1:
                                            System.out.println("Enter the number of new genres:");
                                            int numGenres = input.nextInt();
                                            input.nextLine();

                                            List<Genre> newGenres = new ArrayList<>();

                                            for (int i = 0; i < numGenres; i++) {
                                                System.out.println("Enter genre name:");
                                                String genreName = input.nextLine();
                                                String formattedGenre = genreName.substring(0, 1).toUpperCase()
                                                        + genreName.substring(1).toLowerCase();
                                                newGenres.add(Genre.valueOf(formattedGenre));
                                            }

                                            production.genreList.addAll(newGenres);
                                            break;

                                        case 2:
                                            System.out.println("What genre do you want to delete?");
                                            for (Genre genre : production.genreList) {
                                                System.out.println("-> " + genre);
                                            }

                                            System.out.println("Enter genre name to delete:");
                                            for (Genre genreIter : Genre.values()) {
                                                System.out.println("->" + genreIter);
                                            }

                                            String genreToDelete = input.nextLine();
                                            String formattedGenre = genreToDelete.substring(0, 1).toUpperCase()
                                                    + genreToDelete.substring(1).toLowerCase();
                                            production.genreList.remove(Genre.valueOf(formattedGenre));
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


                        case 5:
                            System.out.println("Type new description:");
                            production.updateDescription(input.nextLine());
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
            System.out.println("Production updated successfully!");
            Thread.sleep(3000);
        }
    }

    @Override
    public void updateActor(Scanner input) throws InterruptedException {
        boolean actorFound = false;
        Actor actor = null;

        System.out.println("What Actor do you want to update?");
        Thread.sleep(1500);
        for (Actor iter : IMDB.getInstance().actorList) {
            System.out.println("-> " + iter.name);
        }
        String name = input.nextLine();

        for (Actor iterator : IMDB.getInstance().actorList) {
            if (iterator.name.equalsIgnoreCase(name)) {
                actor = iterator;
                actorFound = true;
            }
        }

        if (!actorFound) {
            System.out.println("Actor couldn't be found.");
            Thread.sleep(3000);
        } else {
            System.out.println("What do you want to update?");
            System.out.println("1) Name");
            System.out.println("2) Bio");
            System.out.println("3) Productions");

            if (input.hasNextInt()) {
                int choice = input.nextInt();
                input.nextLine();

                if (choice >= 1 && choice <= 3) {
                    switch (choice) {
                        case 1:
                            System.out.println("Type new name:");
                            actor.updateName(input.nextLine());
                            break;

                        case 2:
                            System.out.println("Type new bio:");
                            actor.updateBio(input.nextLine());
                            break;

                        case 3:
                            System.out.println("Pick action:");
                            System.out.println("1) Add new production(s)");
                            System.out.println("2) Delete existing production");

                            if (input.hasNextInt()) {
                                int choice2 = input.nextInt();
                                input.nextLine();

                                if (choice2 >= 1 && choice2 <= 2) {
                                    switch (choice2) {
                                        case 1:
                                            System.out.println("Enter the number of new productions:");
                                            int numProductions = input.nextInt();
                                            input.nextLine();

                                            List<Pair> newProductions = new ArrayList<>();

                                            for (int i = 0; i < numProductions; i++) {
                                                System.out.println("Enter production title:");
                                                String title = input.nextLine();

                                                System.out.println("Enter production type: (Movie or Series)");
                                                String type = input.nextLine();

                                                newProductions.add(new Pair(title, type));
                                            }
                                            actor.prodNameType.addAll(newProductions);
                                            break;

                                        case 2:
                                            System.out.println("What production do you want to delete?");
                                            for (Pair production : actor.prodNameType) {
                                                System.out.println("->" + production.getName());
                                            }

                                            actor.deleteProduction(input.nextLine());
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
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    Thread.sleep(3000);
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
                Thread.sleep(3000);
            }
            System.out.println("Actor updated successfully!");
            Thread.sleep(3000);
        }
    }

    @Override
    public User logOut(User user) {
        return null;
    }

    public void addUser(User user) {

    }

    public void removeUser(User user) {

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
