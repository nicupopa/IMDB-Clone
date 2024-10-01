package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff<T extends Comparable<T>> extends User<AccountType> implements StaffInterface {

    List<Request> requests = new ArrayList<>();
    SortedSet<T> contribution = new TreeSet<>();
    public Staff(Information userInfo, AccountType accType, String username, int XP,
                 List<String> notifList, List<Request> requests) {
        super(userInfo, accType, username, XP, notifList);
        this.requests = requests;
    }

    public Staff(Information userInfo, AccountType accType, String username, int XP, List<String> notifList) {
        super(userInfo, accType, username, XP, notifList);
    }

    public Object getContribution() {
        return contribution;
    }

}
