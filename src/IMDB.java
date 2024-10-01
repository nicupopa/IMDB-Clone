package org.example;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class IMDB {
    private static IMDB instance;
    List<User<AccountType>> userList;
    List<Actor> actorList;
    List<Request> requestList;
    List<Production> prodList;

    private IMDB() {
        userList = new ArrayList<>();
        actorList = new ArrayList<>();
        requestList = new ArrayList<>();
        prodList = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null)
            instance = new IMDB();
        return instance;
    }

    private static List<String> parseStringArray(JSONArray jsonArray) {
        List<String> result = new ArrayList<>();
        if (jsonArray != null) {
            for (Object obj : jsonArray) {
                result.add((String) obj);
            }
        }
        return result;
    }

    public void run() throws Exception {

        JSONParser parser = new JSONParser();

        // read actors.json

        try (FileReader reader = new FileReader("C:\\Users\\Nicu\\Desktop\\PROIECT\\POO-Tema-2023-checker\\src\\main\\resources\\input\\actors.json")) {
            JSONArray actorsArray = (JSONArray) parser.parse(reader);

            for (Object iterator : actorsArray) {
                JSONObject actorsObject = (JSONObject) iterator;
                String actorName = (String) actorsObject.get("name");
                String bio = (String) actorsObject.get("biography");

                JSONArray performances = (JSONArray) actorsObject.get("performances");
                Actor actor = null;

                if (performances != null) {
                    List<Pair> productionsToAdd = new ArrayList<>();

                    for (Object performancesObject : performances) {
                        JSONObject performancesJSONObject = (JSONObject) performancesObject;
                        String title = (String) performancesJSONObject.get("title");
                        String type = (String) performancesJSONObject.get("type");

                        Pair productionPair = new Pair(title, type);
                        productionsToAdd.add(productionPair);
                    }

                    actor = new Actor(actorName, productionsToAdd, bio);
//                    actor.prodNameType.addAll(productionsToAdd);
                }
                actorList.add(actor);
            }

        } catch (IOException | ParseException e) {
            System.out.println("Parsing error: " + e.toString());
        }

        // read production.json

        try (FileReader reader = new FileReader("C:\\Users\\Nicu\\Desktop\\PROIECT\\POO-Tema-2023-checker\\src\\main\\resources\\input\\production.json")) {
            JSONArray productionArray = (JSONArray) parser.parse(reader);

            for (Object iterator : productionArray) {
                JSONObject productionObject = (JSONObject) iterator;

                String title = (String) productionObject.get("title");
                String type = (String) productionObject.get("type");
                String plot = (String) productionObject.get("plot");
                Double avgRating = (Double) productionObject.get("averageRating");

                JSONArray directors = (JSONArray) productionObject.get("directors");
                List<String> parsedDirectors = parseStringArray(directors);

                JSONArray actors = (JSONArray) productionObject.get("actors");
                List<String> parsedActors = parseStringArray(actors);

                JSONArray genres = (JSONArray) productionObject.get("genres");
                List<Genre> parsedGenres = new ArrayList<>();
                for (Object obj : genres) {
                    Genre genre = Genre.valueOf(obj.toString());
                    parsedGenres.add(genre);
                }

                JSONArray ratings = (JSONArray) productionObject.get("ratings");
                List<Rating> parsedRatings = new ArrayList<>();
                for (Object obj : ratings) {
                    JSONObject ratingObject = (JSONObject) obj;

                    String username = (String) ratingObject.get("username");
                    Integer rating = ((Long) ratingObject.get("rating")).intValue();
                    String comm = (String) ratingObject.get("comment");

                    Rating newRating = new Rating(username, rating, comm);
                    parsedRatings.add(newRating);
                }

                if ("Movie".equals(type)) {
                    String duration = (String) productionObject.get("duration");

                    if (productionObject.get("releaseYear") != null) {
                        Integer releaseYear = ((Long) productionObject.get("releaseYear")).intValue();
                        Production movie = new Movie(title, parsedDirectors, parsedActors,
                                parsedGenres, parsedRatings, plot, avgRating, duration, releaseYear);
                        prodList.add(movie);
                    } else {
                        Production movie = new Movie(title, parsedDirectors, parsedActors,
                                parsedGenres, parsedRatings, plot, avgRating, duration, null);
                        prodList.add(movie);
                    }
                }
                else if ("Series".equals(type)) {
                    Integer numSeasons = ((Long) productionObject.get("numSeasons")).intValue();

                    Map<String, List<Episode>> seasons = new HashMap<>();
                    JSONObject seasonsObject = (JSONObject) productionObject.get("seasons");

                    for (Object seasonKey : seasonsObject.keySet()) {
                        String seasonNr = (String) seasonKey;
                        JSONArray episodes = (JSONArray) seasonsObject.get(seasonNr);

                        List<Episode> episodesList = new ArrayList<>();
                        for (Object episodeObj : episodes) {
                            JSONObject episode = (JSONObject) episodeObj;

                            String episodeName = (String) episode.get("episodeName");
                            String duration = (String) episode.get("duration");

                            Episode episodeItem = new Episode(episodeName, duration);
                            episodesList.add(episodeItem);
                        }

                        seasons.put(seasonNr, episodesList);
                    }

                        if (productionObject.get("releaseYear") != null) {
                            Integer releaseYear = ((Long) productionObject.get("releaseYear")).intValue();
                            Series series = new Series(title, parsedDirectors, parsedActors, parsedGenres, parsedRatings, plot, avgRating, releaseYear, numSeasons, seasons);
                            prodList.add(series);
                        } else {
                            Series series = new Series(title, parsedDirectors, parsedActors, parsedGenres, parsedRatings, plot, avgRating, null, numSeasons, seasons);
                        }

                }
            }

        } catch (IOException | ParseException e) {
            System.out.println("Parsing error: " + e.toString());
        }

        // read accounts.json
        try (FileReader reader = new FileReader("C:\\Users\\Nicu\\Desktop\\PROIECT\\POO-Tema-2023-checker\\src\\main\\resources\\input\\accounts.json")) {
            JSONArray accountsArray = (JSONArray) parser.parse(reader);

            for (Object iterator : accountsArray) {
                JSONObject accountsObject = (JSONObject) iterator;
                String userType = (String) accountsObject.get("userType");

                JSONObject information = (JSONObject) accountsObject.get("information");

                JSONObject jsonCredentials = (JSONObject) information.get("credentials");
                String email = (String) jsonCredentials.get("email");
                String password = (String) jsonCredentials.get("password");
                if (email == null || password == null) {
                    throw new Exceptions.InformationIncompleteException();
                }
                Credentials credentials = new Credentials(email, password);
                String dateOfBirthJson = (String) information.get("birthDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime dateOfBirth = LocalDate.parse(dateOfBirthJson, formatter).atStartOfDay();

                if (information.get("name") == null) {
                    throw new Exceptions.InformationIncompleteException();
                }
                String name = (String) information.get("name");
                String country = (String) information.get("country");
                Integer age = ((Long) information.get("age")).intValue();
                Character gen = ((String) information.get("gender")).charAt(0);

                User.Information userInfo = new User.Information.
                        InformationBuilder(credentials, dateOfBirth, name, country, age, gen).build();

                String username = (String) accountsObject.get("username");

                Long XP = null;
                if (accountsObject.get("experience") != null) {
                    XP = Long.valueOf((String) accountsObject.get("experience"));
                }

                Integer xpValue = 0;
                if (XP != null) {
                    xpValue = XP.intValue();
                }

                JSONArray notificationsArray = (JSONArray) accountsObject.get("notifications");

                if ("Contributor".equals(userType)) {

                    List<String> notificationsList = new ArrayList<>();

                    if (notificationsArray != null) {
                        for (Object notification : notificationsArray) {
                            notificationsList.add((String) notification);
                        }
                    }

                    User contributor = UserFactory.getUser(userInfo, AccountType.CONTRIBUTOR, username, xpValue, notificationsList);

                    JSONArray productionsContribution = (JSONArray) accountsObject.get("productionsContribution");
                    JSONArray actorsContribution = (JSONArray) accountsObject.get("actorsContribution");
                    JSONArray favoriteProductions = (JSONArray) accountsObject.get("favoriteProductions");
                    JSONArray favoriteActors = (JSONArray) accountsObject.get("favoriteActors");

                    if (productionsContribution != null) {
                        SortedSet<Production> contributionsToAdd = new TreeSet<>();

                        for (Object contributionsObject : productionsContribution) {

                            String title = (String) contributionsObject;
                            for (Production prodIterator : prodList) {
                                if (title.equals(prodIterator.title)) {
                                    contributionsToAdd.add(prodIterator);
                                }
                            }

                            ((Contributor) contributor).contribution.addAll(contributionsToAdd);
                        }
                    }

                    if (actorsContribution != null) {
                        SortedSet<Actor> contributionsToAdd = new TreeSet<>();

                        for (Object contributionsObject : actorsContribution) {

                            String actorName = (String) contributionsObject;
                            for (Actor actorIterator : actorList) {
                                if (actorName.equals(actorIterator.name)) {
                                    contributionsToAdd.add(actorIterator);
                                }
                            }

                            ((Contributor) contributor).contribution.addAll(contributionsToAdd);
                        }
                    }

                    if (favoriteProductions != null) {

                        for (Object favoritesObject : favoriteProductions) {

                            String title = (String) favoritesObject;
                            for (Production prodIterator : prodList) {
                                if (title.equals(prodIterator.title)) {
                                    contributor.addFav(prodIterator);
                                }
                            }
                        }
                    }

                    if (favoriteActors != null) {

                        for (Object favoritesObject : favoriteProductions) {

                            String actorName = (String) favoritesObject;
                            for (Actor actorIterator : actorList) {
                                if (actorName.equals(actorIterator.name)) {
                                    contributor.addFav(actorIterator);
                                }
                            }
                        }
                    }

                    userList.add(contributor);
                } else if ("Regular".equals(userType)) {

                    List<String> notificationsList = new ArrayList<>();

                    if (notificationsArray != null) {
                        for (Object notification : notificationsArray) {
                            notificationsList.add((String) notification);
                        }
                    }

                    User regular = UserFactory.getUser(userInfo, AccountType.REGULAR, username, xpValue, notificationsList);

                    JSONArray favoriteProductions = (JSONArray) accountsObject.get("favoriteProductions");
                    JSONArray favoriteActors = (JSONArray) accountsObject.get("favoriteActors");

                    if (favoriteProductions != null) {
//                        for (Object favProduction : favoriteProductions) {
//                            admin.addFav(favProduction);
//                        }

                        for (Object favoritesObject : favoriteProductions) {

                            String title = (String) favoritesObject;
                            for (Production prodIterator : prodList) {
                                if (title.equals(prodIterator.title)) {
                                    regular.addFav(prodIterator);
                                }
                            }
                        }
                    }

                    if (favoriteActors != null) {
//                        for (Object favActor : favoriteActors) {
//                            admin.addFav(favActor);
//                        }
                        for (Object favoritesObject : favoriteProductions) {

                            String actorName = (String) favoritesObject;
                            for (Actor actorIterator : actorList) {
                                if (actorName.equals(actorIterator.name)) {
                                    regular.addFav(actorIterator);
                                }
                            }
                        }
                    }

                    userList.add(regular);
                } else if ("Admin".equals(userType)) {

                    List<String> notificationsList = new ArrayList<>();

                    if (notificationsArray != null) {
                        for (Object notification : notificationsArray) {
                            notificationsList.add((String) notification);
                        }
                    }

                    User<AccountType> admin = UserFactory.getUser(userInfo, AccountType.ADMIN, username, xpValue, notificationsList);

                    JSONArray productionsContribution = (JSONArray) accountsObject.get("productionsContribution");
                    JSONArray actorsContribution = (JSONArray) accountsObject.get("actorsContribution");
                    JSONArray favoriteProductions = (JSONArray) accountsObject.get("favoriteProductions");
                    JSONArray favoriteActors = (JSONArray) accountsObject.get("favoriteActors");

                    if (productionsContribution != null) {
//                        ((Contributor) contributor).contribution.addAll(productionsContribution);
                        SortedSet<Production> contributionsToAdd = new TreeSet<>();

                        for (Object contributionsObject : productionsContribution) {

                            String title = (String) contributionsObject;
                            for (Production prodIterator : prodList) {
                                if (title.equals(prodIterator.title)) {
                                    contributionsToAdd.add(prodIterator);
                                }
                            }

                            ((Admin) admin).contribution.addAll(contributionsToAdd);
                        }
                    }

                    if (actorsContribution != null) {
//                        ((Contributor) contributor).contribution.addAll(actorsContribution);
                        SortedSet<Actor> contributionsToAdd = new TreeSet<>();

                        for (Object contributionsObject : actorsContribution) {

                            String actorName = (String) contributionsObject;
                            for (Actor actorIterator : actorList) {
                                if (actorName.equals(actorIterator.name)) {
                                    contributionsToAdd.add(actorIterator);
                                }
                            }

                            ((Admin) admin).contribution.addAll(contributionsToAdd);
                        }
                    }

                    if (favoriteProductions != null) {
//                        for (Object favProduction : favoriteProductions) {
//                            admin.addFav(favProduction);
//                        }

                        for (Object favoritesObject : favoriteProductions) {

                            String title = (String) favoritesObject;
                            for (Production prodIterator : prodList) {
                                if (title.equals(prodIterator.title)) {
                                    admin.addFav(prodIterator);
                                }
                            }
                        }
                    }

                    if (favoriteActors != null) {
//                        for (Object favActor : favoriteActors) {
//                            admin.addFav(favActor);
//                        }
                        for (Object favoritesObject : favoriteProductions) {

                            String actorName = (String) favoritesObject;
                            for (Actor actorIterator : actorList) {
                                if (actorName.equals(actorIterator.name)) {
                                    admin.addFav(actorIterator);
                                }
                            }
                        }
                    }

                    userList.add(admin);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Parsing error: " + e.toString());
        }

        // read requests.json
        try (FileReader reader = new FileReader("C:\\Users\\Nicu\\Desktop\\PROIECT\\POO-Tema-2023-checker\\src\\main\\resources\\input\\requests.json")) {
            JSONArray requestsArray = (JSONArray) parser.parse(reader);

            for (Object iterator : requestsArray) {
                JSONObject requestsObject = (JSONObject) iterator;

                RequestType type = RequestType.valueOf((String) requestsObject.get("type"));

                String createdDateJson = (String) requestsObject.get("createdDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime createdDate = LocalDateTime.parse(createdDateJson, formatter);

                String requesterUsername = (String) requestsObject.get("username");
                String solverUsername = (String) requestsObject.get("to");
                String description = (String) requestsObject.get("description");

                if (RequestType.ACTOR_ISSUE.equals(type)) {
                    String name = (String) requestsObject.get("actorName");
                    Request request = new Request(type, createdDate, name, description,
                            requesterUsername, solverUsername);
                    requestList.add(request);

                    for (User<AccountType> contributorIter : IMDB.getInstance().userList) {
                        if (contributorIter instanceof Staff) {
                            if (request.solverUsername.equalsIgnoreCase(contributorIter.username)) {
                                ((Staff) contributorIter).requests.add(request);
                            }
                        }
                    }
                } else if (RequestType.MOVIE_ISSUE.equals(type)) {
                    String name = (String) requestsObject.get("movieTitle");
                    Request request = new Request(type, createdDate, name, description,
                            requesterUsername, solverUsername);
                    requestList.add(request);

                    for (User<AccountType> contributorIter : IMDB.getInstance().userList) {
                        if (contributorIter instanceof Staff) {
                            if (request.solverUsername.equalsIgnoreCase(contributorIter.username)) {
                                ((Staff) contributorIter).requests.add(request);
                            }
                        }
                    }
                } else {
                    Request request = new Request(type, createdDate, null, description,
                            requesterUsername, solverUsername);
                    requestList.add(request);

                    for (User<AccountType> adminIter : IMDB.getInstance().userList) {
                        if (adminIter instanceof Admin) {
                            ((Admin) adminIter).requests.add(request);
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Parsing error: " + e.toString());
        }
    }
}
