package com.livematch.livesportstv.app;


public class BaseClass {
    public String string(int id) {
        return MainApp.getAppContext().getString(id);
    }
}
