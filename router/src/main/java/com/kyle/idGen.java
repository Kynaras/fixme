package com.kyle;

public class idGen {
    private int id = 000000;

    public idGen() {
        //Add if necessary
    }

    public int generateId() {
        this.id++;
        return this.id;
    }
    
}
