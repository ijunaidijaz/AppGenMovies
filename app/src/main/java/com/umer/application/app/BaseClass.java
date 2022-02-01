package com.umer.application.app;


public class BaseClass {
    public String string(int id) {
        return MainApp.getAppContext().getString(id);
    }
}
