package com.vinay.microservicetuts.resources;

import com.vinay.microservicetuts.exceptions.UserNotFoundException;
import com.vinay.microservicetuts.models.User;
import com.vinay.microservicetuts.services.UserDoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users/")
public class UserResource {

    private final UserDoaService service;

    public UserResource(UserDoaService service) {
        this.service = service;
    }

    // GET /users
    // retrieveAllUsers
    @GetMapping
    public List<User>  retrieveAllUsers(){
        return service.findAll();
    }

    // GET /users/{id}
    @GetMapping("{id}")
    public User retrieveUser(@PathVariable int id){
        User user = service.findOne(id);
        if(user == null){
            throw new UserNotFoundException("id = " + id);
        }
        return user;
    }

    //
    // input - details of user
    // output - CREATED & Return the created URI
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user){
        User savedUser = service.save(user);
        // CREATED
        // /users/{id} savedUser.getId()
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
