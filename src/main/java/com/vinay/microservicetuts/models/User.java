package com.vinay.microservicetuts.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

public class User {

    private Integer id;
    @Size(min = 2, message = "Name Should be at least 2 characters")
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Past
    private Date date;

    public User() {
    }

    public User(Integer id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
