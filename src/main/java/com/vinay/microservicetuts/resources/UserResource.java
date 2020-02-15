package com.vinay.microservicetuts.resources;

import com.vinay.microservicetuts.exceptions.UserNotFoundException;
import com.vinay.microservicetuts.models.User;
import com.vinay.microservicetuts.services.UserDoaService;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
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
    public EntityModel retrieveUser(@PathVariable int id){
        User user = service.findOne(id);
        if(user == null){
            throw new UserNotFoundException("id = " + id);
        }

        // "all-uses", SERVER_PATH + "/users"
        // HATEOAS
        EntityModel<User> model = new EntityModel<>(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));
        return model;
    }

    //
    // input - details of user
    // output - CREATED & Return the created URI
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = service.save(user);
        // CREATED
        // /users/{id} savedUser.getId()
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id){
        User user = service.deleteById(id);
        if(user == null){
            throw new UserNotFoundException("id = "+id );
        }
    }
}
