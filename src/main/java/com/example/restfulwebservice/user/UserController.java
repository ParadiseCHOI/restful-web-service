package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    @Autowired
    private  UserDaoService service;

    // public UserController(UserDaoService service) {
    //     this.service = service;
    // }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        List<EntityModel<User>> models = new ArrayList<>();
        List<User> users = service.findAll();

        // HATEOAS
        for (User user : users) {
            EntityModel model = EntityModel.of(user);
            model.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel());
            models.add(model);
        }

        // JacksonFilter
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(models);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }

        // HATEOAS
        EntityModel<User> model = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        // JacksonFilter
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(model);
        mapping.setFilters(filters);
        return mapping;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null) {
            throw new UserNotFoundException(id);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable int id) {
        User updatedUser = service.update(user, id);

        if (updatedUser != null) {
            return ResponseEntity.noContent().build();
        }
        else {
            throw new UserNotFoundException(id);

        }
    }
}
