package com.example.passwordgeneration.controller;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.response.UserResponse;
import com.example.passwordgeneration.service.UserService;
import lombok.AllArgsConstructor;
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
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse = userService.getUserById(id);
        if(userResponse==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else{
            return ResponseEntity.ok(userResponse);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        if(userResponse!=null){
            return ResponseEntity.ok(userResponse);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest){
        UserResponse userResponse = userService.updateUser(id, userRequest);
        if (userResponse == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else{
            return ResponseEntity.ok(userResponse);
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

