package com.next.webserver.repository;

import com.next.webserver.domain.User;

import java.util.HashMap;
import java.util.Map;

public class DataBase {

    private static final Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }
}
