package com.free.newhdmovies.app;


public class BaseClass {
    public String string(int id) {
        return MainApp.getAppContext().getString(id);
    }
}
