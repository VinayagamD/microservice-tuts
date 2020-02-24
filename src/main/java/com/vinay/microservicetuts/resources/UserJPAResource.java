package com.vinay.microservicetuts.resources;

import com.vinay.microservicetuts.exceptions.UserNotFoundException;
import com.vinay.microservicetuts.models.User;
import com.vinay.microservicetuts.repositories.UserRepository;
import com.vinay.microservicetuts.services.UserDoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("jpa/users")
public class UserJPAResource {

    private final UserRepository repository;

    public UserJPAResource(UserRepository repository) {
        this.repository = repository;
    }

    // GET /users
    // retrieveAllUsers
    @GetMapping
    public List<User>  retrieveAllUsers(){
        return repository.findAll();
    }

    // GET /users/{id}
    @GetMapping("{id}")
    public User retrieveUser(@PathVariable int id){
        Optional<User> user = repository.findById(id);
        if(!user.isPresent()){
            throw new UserNotFoundException("id = " + id);
        }

        // "all-uses", SERVER_PATH + "/users"
        // HATEOAS
       /* EntityModel<User> model = new EntityModel<>(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));*/
        return user.get();
    }

    //
    // input - details of user
    // output - CREATED & Return the created URI
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user){
        System.out.println(user);
        User savedUser = repository.save(user);
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
        repository.deleteById(id);
    }
}
