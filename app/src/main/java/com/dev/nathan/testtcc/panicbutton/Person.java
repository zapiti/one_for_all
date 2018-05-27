package com.dev.nathan.testtcc.panicbutton;

import java.io.Serializable;


public class Person implements Serializable{
    private String number;
    private String name;

    public Person(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Person p) {
        return p.getName().equals(name) && p.getNumber().equals(number);
    }
}
