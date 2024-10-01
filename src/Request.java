package org.example;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject {
    private final RequestType requestType;
    private final LocalDateTime requestTime;
    String name;
    final String description;
    final String requesterUsername;
    final String solverUsername;
    private List<Observer> observersList = new ArrayList<>();
    private String message;

    public Request(RequestType requestType, LocalDateTime requestTime, String name,
                   String description, String requesterUsername, String solverUsername) {
        this.requestType = requestType;
        this.requestTime = requestTime;
        this.name = name;
        this.description = description;
        this.requesterUsername = requesterUsername;
        this.solverUsername = solverUsername;
    }

    public Request(RequestType requestType, LocalDateTime requestTime, String description,
                   String requesterUsername, String solverUsername) {
        this.requestType = requestType;
        this.requestTime = requestTime;
        this.description = description;
        this.requesterUsername = requesterUsername;
        this.solverUsername = solverUsername;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestType=" + requestType +
                ", requestTime=" + requestTime +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", requesterUsername='" + requesterUsername + '\'' +
                ", solverUsername='" + solverUsername + '\'' +
                '}';
    }
    public RequestType getRequestType() {
        return requestType;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public String getSolverUsername() {
        return solverUsername;
    }

    @Override
    public void register(User<AccountType> observer, Production production) {
        if (observer == null) {
            throw new NullPointerException();
        }
        observersList.add(observer);
    }

    public void register(User<AccountType> observer) {
        if (observer == null) {
            throw new NullPointerException();
        }
        observersList.add(observer);
    }

    @Override
    public void unregister(User<AccountType> observer, Production production) {
        observersList.remove(observer);
    }

    public void notifyObserversRequest(String reviewerUsername) {
        if (observersList != null && !observersList.isEmpty()) {
            for (Observer observer : observersList) {
                observer.update(message, (User<AccountType>) observer);
            }
        } else {
            System.out.println("No observers to notify!!!");
        }
    }
    @Override
    public void notifyObservers(Production production, String reviewerUsername) {
        if (observersList != null && !observersList.isEmpty()) {
            for (Observer observer : observersList) {
                observer.update(message, (User<AccountType>) observer);
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
//        System.out.println(message);
        this.message = "Your request has been solved";
        notifyObservers(production, reviewerUsername);
    }

    public void postMessageRequest(String reviewerUsername, String name, int type) {
        if (type == 1) {
            this.message = "Your request for " + name + " has been solved!";
        } else if (type == 2) {
            this.message = "Your request has been solved!";
        } else if (type == 3) {
            this.message = reviewerUsername + "has added a request for you!";
        }
        notifyObserversRequest(reviewerUsername);
    }
}
