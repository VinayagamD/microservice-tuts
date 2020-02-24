package com.vinay.microservicetuts.resources;

import com.vinay.microservicetuts.exceptions.UserNotFoundException;
import com.vinay.microservicetuts.models.Post;
import com.vinay.microservicetuts.models.User;
import com.vinay.microservicetuts.repositories.PostRepository;
import com.vinay.microservicetuts.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("jpa/users")
public class UserJPAResource {

    private final UserRepository repository;
    private final PostRepository postRepository;

    public UserJPAResource(UserRepository repository, PostRepository postRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
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

    @GetMapping("{id}/posts")
    public List<Post>  retrieveAllUsersPost(@PathVariable int id){
         Optional<User> optionalUser = repository.findById(id);
         if(!optionalUser.isPresent()){
             throw new UserNotFoundException("id = " + id);
         }
         return optionalUser.get().getPosts();
    }

    //
    // input - details of user
    // output - CREATED & Return the created URI
    @PostMapping("{id}/createPost")
    @Transactional
    public ResponseEntity<Object> createUserPost(@PathVariable int id, @RequestBody Post post){
        Optional<User> optionalUser = repository.findById(id);
        if(!optionalUser.isPresent()){
            throw new UserNotFoundException("id = " + id);
        }
        post.setUser(optionalUser.get());
        Post savedPost = postRepository.save(post);
        // CREATED
        // /users/{id} savedUser.getId()
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
