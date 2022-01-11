package com.binus.mp.wish.models;

import android.app.Application;

public class Auth extends Application {
    private Account account;

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

}
