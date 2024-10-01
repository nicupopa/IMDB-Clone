package org.example;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

//        List<Observer> observers = new ArrayList<>();
//        observers.addAll(IMDB.getInstance().userList);

        IMDB.getInstance().run();
        CLI.run();
//        List<User<AccountType>> userList = IMDB.getInstance().userList;
//        int count = 0;
//
//        for (User<AccountType> user : userList) {
//            count++;
//            System.out.println("Username: " + user.getUsername());
//            System.out.println("XP: " + user.getXP() + "\n");
//            System.out.println("Information: ");
//            System.out.println("Name: "  + user.getUserInfo().name);
//            System.out.println("Country: "  + user.getUserInfo().country);
//            System.out.println("Age: "  + user.getUserInfo().age);
//            System.out.println("Gender: "  + user.getUserInfo().gen);
//            System.out.println("Birthdate: "  + user.getUserInfo().getDateOfBirth());
//            System.out.println("Email: "  + user.getUserInfo().getCredentials().email);
//            System.out.println("Pass: "  + user.getUserInfo().getCredentials().password);
//            System.out.println("UserType: " + user.accType);
//            System.out.println("--------------");
//
//            System.out.println("Fav List: ");
//            for (Object fav : user.favList) {
//                if (fav instanceof Production) {
//                    System.out.println(" " + ((Production) fav).title);
//                } else if (fav instanceof Actor){
//                    System.out.println(" " + ((Actor) fav).name);
//                }
//            }
//
//            System.out.println("--------------");
//
//            if (user.notifList != null) {
//                System.out.println("Notif List: ");
//                for (Object notif : user.notifList) {
//                    System.out.println(" " + notif);
//                }
//            }
//
//            System.out.println("--------------");
//
//            if (user instanceof Staff staff) {
//
//                System.out.println("Contribution: ");
//                for (Object contribution : staff.contribution) {
//                    if (contribution instanceof Production) {
//                        System.out.println(" " + ((Production) contribution).title);
//                    } else if (contribution instanceof Actor){
//                        System.out.println(" " + ((Actor) contribution).name);
//                    }
//                }
//            }
//
//            System.out.println("\n" + "+++++++++++++++++++++++++++++++++" + "\n");
//        }
//        System.out.println(count);

//        List<Actor> actorList = IMDB.getInstance().actorList;
//        for (Actor actor : actorList) {
//            System.out.println("Name: " + actor.name);
//            System.out.println("Bio: " + actor.bio);
//
//            List<Pair> performances = actor.prodNameType;
//            System.out.println("Performances:");
//
//            for (Pair performance : performances) {
//
//                System.out.println("\tTitle: " + performance.getName());
//                System.out.println("\tType: " + performance.getType());
//            }
//            System.out.println("\n" + "+++++++++++++++++++++++++++++++++" + "\n");
//        }
//
//        List<Production> prodlist = IMDB.getInstance().prodList;
//        for (Production production : prodlist) {
//            production.displayInfo();
//            System.out.println("=================================");
//        }
//
//        List<Request> reqList = IMDB.getInstance().requestList;
//        for (Request req : reqList) {
//            System.out.println("Request Type: " + req.getRequestType());
//            System.out.println("Request Time: " + req.getRequestTime());
//            System.out.println("Name: " + req.getName());
//            System.out.println("Description: " + req.getDescription());
//            System.out.println("Requester Username: " + req.getRequesterUsername());
//            System.out.println("Solver Username: " + req.getSolverUsername());
//            System.out.println();
//        }
    }
}
