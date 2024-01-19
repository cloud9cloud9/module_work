package org.example.security.factory;

import org.example.security.UserManager;
import org.example.security.state.UserState;

public interface UserStateFactory {
    UserState createState(UserManager userManager);
}
