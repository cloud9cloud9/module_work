package org.example.security.factory;

import org.example.security.UserManager;
import org.example.security.state.BlockedState;
import org.example.security.state.UserState;

public class BlockedStateFactory implements UserStateFactory {
    @Override
    public UserState createState(UserManager userManager) {
        return new BlockedState();
    }
}
