package com.agagrzebyk.user_jwt.controller;

import com.agagrzebyk.user_jwt.model.User;
import com.agagrzebyk.user_jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserResourceAssembler userAssembler;

    @Autowired
    public UserController(UserService userService,
                          UserResourceAssembler userAssembler){
        this.userService = userService;
        this.userAssembler = userAssembler;
    }


    @GetMapping
    Resources<Resource<User>> all() {
        List<Resource<User>> users = userService.getAllUsers()
                .stream()
                .map(userAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(users,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping
    ResponseEntity<?> addUser(@RequestBody User user) throws URISyntaxException {
        Resource<User> newUser = userAssembler.toResource(userService.addUser(user));
        return ResponseEntity.created(new URI(newUser.getId().expand().getHref())).body(newUser);
    }

    @GetMapping("/name/{username}")
    Resource<User> findByUsername(@PathVariable String username){
        User user = userService.findByUsername(username);
        return userAssembler.toResource(user);
    }

    @GetMapping("/{id}")
    Resource<User> oneUser(@PathVariable("id") long id) {

        User user = userService.getUserById(id);
        return userAssembler.toResource(user);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceUser(@RequestBody User user, @PathVariable("id") Long id) throws URISyntaxException {
        User updatedUser = userService.replaceUserById(user, id);
        Resource<User> newStudent = userAssembler.toResource(updatedUser);
        return ResponseEntity.created(new URI(newStudent.getId().expand().getHref())).body(newStudent);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
