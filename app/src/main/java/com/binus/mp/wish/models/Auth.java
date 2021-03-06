package com.binus.mp.wish.models;

import android.app.Application;
import android.location.Location;

import java.util.List;

public class Auth extends Application {
    private Account account;
    List<Location> locations;

    private static Auth instance;

    public synchronized static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }
        return instance;
    }

    public synchronized static void deleteInstance() {
        instance = null;
    }

    public Auth() {
        account = new Account();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public static void setInstance(Auth instance) {
        Auth.instance = instance;
    }
}
