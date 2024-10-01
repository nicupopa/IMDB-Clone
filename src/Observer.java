package org.example;

public interface Observer {
    void update();

    void update(String notification, User<AccountType> user);

    void setSubject(Subject subject);
}
