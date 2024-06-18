package edu.rice.comp322;

import java.util.Objects;

/**
 * A user.
 */
public class User {

    /**
     * GitHub username.
     */
    public String login;

    /**
     * Number of contributions made by this user.
     */
    public Integer contributions;

    public User(String username, Integer contributions) {
        this.login = username;
        this.contributions = contributions;
    }

    /**
     * Two users are equal if they have the same login username.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
