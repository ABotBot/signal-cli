package org.asamk.signal.manager;

public class UserAlreadyExists extends Exception {

    private final String username;
    private final String description;

    public UserAlreadyExists(String username, String description) {
        this.username = username;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }
}
