package com.binus.mp.wish.models;

import android.app.Application;

public class Auth extends Application {
    private Account acc;

    private static Auth authUser;

    public synchronized static Auth getSession(){
        if(authUser == null){
            authUser = new Auth();
        }
        return authUser;
    }

    public Auth(){
        acc = new Account();
    }

    public Account getAcc() {
        return acc;
    }

    public void setAcc(Account acc) {
        this.acc = acc;
    }

}
