package com.example.parkingsystem;

import java.util.Properties;

public interface Command {
    String getCommandName();

    String getDisplayName();

    String execute(Properties params);
}