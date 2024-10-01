package org.example;

import java.util.List;

public class UserFactory {

    public static User<AccountType> getUser(User.Information userInfo, AccountType accType,
                               String username, Integer XP, List<String> notifList) {
        if (AccountType.REGULAR.equals(accType)) {
            return new Regular(userInfo, AccountType.REGULAR, username, XP, notifList);
        }
        if (AccountType.CONTRIBUTOR.equals(accType)) {
            return new Contributor(userInfo, AccountType.CONTRIBUTOR, username, XP, notifList);
        }
        if (AccountType.ADMIN.equals(accType)) {
            return new Admin(userInfo, AccountType.ADMIN, username, XP, notifList);
        }

        return null;
    }
}
