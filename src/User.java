package org.example;
import java.time.LocalDateTime;
import java.util.*;
public abstract class User<AccountType> implements Observer {
    Information userInfo;
    AccountType accType;
    String username;
    Integer XP;
    List<String> notifList;
    SortedSet<Object> favList = new TreeSet<>();
    Subject subject;

    public User(Information userInfo, AccountType accType, String username, int XP, List<String> notifList) {
        this.userInfo = userInfo;
        this.accType = accType;
        this.username = username;
        this.XP = XP;
        this.notifList = notifList;
    }

    public User(Information userInfo, AccountType accType, String username) {
        this.userInfo = userInfo;
        this.accType = accType;
        this.username = username;
        this.XP = null;
        this.notifList = null;
        this.favList = null;

    }

    public Information getUserInfo() {
        return userInfo;
    }

    public String getUsername() {
        return username;
    }

    public AccountType getAccType() {
        return accType;
    }

    public int getXP() {
        return XP;
    }

    public void addFav(Object object) {
        favList.add(object);
    }

    public void deleteFav(Object object) {
        favList.remove(object);
    }

    public void addNotification(String notification) {
        notifList.add(notification);
    }

    public void updateXP(int XP) {
        this.XP = XP;
    }

    public abstract User logOut(User<AccountType> user);

    public static class Information {
        private Credentials credentials;
        private LocalDateTime dateOfBirth;
        String name;
        String country;
        Integer age;
        Character gen;

        public Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.dateOfBirth = builder.dateOfBirth;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gen = builder.gen;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public LocalDateTime getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDateTime dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public static class InformationBuilder {
            private final Credentials credentials;
            private final LocalDateTime dateOfBirth;
            String name;
            String country;
            Integer age;
            Character gen;

            public InformationBuilder(Credentials credentials, LocalDateTime dateOfBirth,
                                      String name, String country, Integer age, Character gen) {
                this.credentials = credentials;
                this.dateOfBirth = dateOfBirth;
                this.name = name;
                this.country = country;
                this.age = age;
                this.gen = gen;
            }
            public Information build() {
                return new Information(this);
            }
        }

    }
}
