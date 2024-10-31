package at.studying;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDatabase {
    private Map<String, User> users = new HashMap<>();

    public User getUser(String username) {
        return users.get(username);
    }

    public void createUser(User user) {
        users.put(user.getUsername(), user);
    }

    public boolean doesUsernameExist(String username) {
        return users.containsKey(username);
    }

    public boolean updateUser(String username, User user) {
        if (!users.containsKey(username)) {
            return false;
        }
        users.put(username, user);
        return true;
    }
}