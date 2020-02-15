package com.vinay.microservicetuts.services;

import com.vinay.microservicetuts.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserDoaService {

    private static List<User> users = new ArrayList<>();

    private static int userCount = 3;

    static {
        users.add(new User(1, "Adam", new Date()));
        users.add(new User(2, "Eve", new Date()));
        users.add(new User(3, "Jack", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user){
        if(user.getId() == null){
            user.setId(++userCount);
        }
        users.add(user);
        return user;
    }

    public User findOne(int id){
       return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public User deleteById(int id){
        return users.stream().filter(user -> user.getId() == id).findFirst().map(user -> {
            users.remove(user);
            return user;
        }).orElse(null);

    }
}
