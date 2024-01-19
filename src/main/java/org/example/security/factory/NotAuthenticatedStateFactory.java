package org.example.security.factory;

import org.example.security.UserManager;
import org.example.security.state.NotAuthenticatedState;
import org.example.security.state.UserState;

public class NotAuthenticatedStateFactory implements UserStateFactory {
    @Override
    public UserState createState(UserManager userManager) {
        return new NotAuthenticatedState(userManager);
    }
}
