package org.example.passwordgeneration.controller;

import lombok.AllArgsConstructor;
import org.example.passwordgeneration.dto.Requests.UserRequest;
import org.example.passwordgeneration.model.User;
import org.example.passwordgeneration.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        if(user==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest){
        User user = userService.updateUser(id, userRequest);
        if (user == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteUser(@PathVariable Long id) {
        boolean isExist = userService.deleteUser(id);
        if(isExist){
            return HttpStatus.OK;
        }else{
            return HttpStatus.NOT_FOUND;
        }
    }
}