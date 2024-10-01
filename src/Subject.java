package org.example;

public interface Subject {
    void register(User<AccountType> observer, Production production);

    void unregister(User<AccountType> observer, Production production);

    void notifyObservers(Production production, String reviewerUsername);

    Object getUpdate();

    void postMessage(String title, String reviewerUsername, Production production);
}
