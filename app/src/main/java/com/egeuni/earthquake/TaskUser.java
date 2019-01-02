package com.egeuni.earthquake;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class TaskUser {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String surname;
    private String place;
    private String relation;
    private String gender;

    @Ignore
    public TaskUser(String name, String surname, String place, String relation, String gender) {
        this.name = name;
        this.surname = surname;
        this.place = place;
        this.relation = relation;
        this.gender = gender;
    }

    public TaskUser(int id, String name, String surname, String place, String relation, String gender) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.place = place;
        this.relation = relation;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
