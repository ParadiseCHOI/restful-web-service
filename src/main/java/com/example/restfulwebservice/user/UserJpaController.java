package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/jpa", produces = "application/json")
public class UserJpaController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        List<EntityModel<User>> models = new ArrayList<>();
        List<User> users = userRepository.findAll();

        // HATEOAS
        for (User user : users) {
            EntityModel model = EntityModel.of(user);
            model.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel());
            models.add(model);
        }

        // JacksonFilter
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "posts");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(models);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        // HATEOAS
        EntityModel<User> model = EntityModel.of(user.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        // JacksonFilter
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "posts");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(model);
        mapping.setFilters(filters);
        return mapping;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return user.get().getPosts();
    }

    @GetMapping("/users/{userId}/posts/{id}")
    public Post retrievePostById(@PathVariable int userId, @PathVariable int id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundException(id);
        }
        if (post.get().getUser().getId() != userId) {
            throw new UserNotFoundException(userId);
        }
        return post.get();
    }

    @DeleteMapping ("/users/{userId}/posts/{id}")
    public void DeletePostById(@PathVariable int userId, @PathVariable int id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundException(id);
        }
        if (post.get().getUser().getId() != userId) {
            throw new UserNotFoundException(userId);
        }
        postRepository.deleteById(id);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        post.setUser(user.get());

        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/users/{userId}/posts/{id}")
    public ResponseEntity<Post> createPost(@PathVariable int userId, @PathVariable int id ,@RequestBody Post updatedPost) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundException(id);
        }

        if (post.get().getUser().getId() != userId) {
            throw new UserNotFoundException(userId);
        }

        post.get().setDescription(updatedPost.getDescription());

        Post savedPost = postRepository.save(post.get());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    // @PutMapping("/users/{id}")
    // public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable int id) {
    //     User updatedUser = userRepository.update(user, id);
    //
    //     if (updatedUser != null) {
    //         return ResponseEntity.noContent().build();
    //     }
    //     else {
    //         throw new UserNotFoundException(id);
    //
    //     }
    // }
}
