package com.binus.mp.wish.models;

public class Auth {
    private Account acc;

    private static Auth authUser;



    public synchronized static Auth getSession(){
        if(authUser == null){
            authUser = new Auth();
        }
        return authUser;
    }
    private Auth(){
        acc = new Account();
    }

    public Account getAcc() {
        return acc;
    }

    public void setAcc(Account acc) {
        this.acc = acc;
    }
}
