package org.example.security.factory;

import org.example.security.UserManager;

import org.example.security.state.AuthenticatedState;
import org.example.security.state.UserState;

public class AuthenticatedStateFactory implements UserStateFactory {
    @Override
    public UserState createState(UserManager userManager) {
        return new AuthenticatedState(userManager);
    }
}