package com.example.passwordgeneration.controller;

import com.example.passwordgeneration.dto.request.PasswordRequest;
import com.example.passwordgeneration.dto.response.PasswordResponse;
import com.example.passwordgeneration.service.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/password")
@AllArgsConstructor
public class PasswordController {
    private static final String PASSWORD_NOT_FOUND = "Password wasn't found";
    private final PasswordService service;
    @GetMapping("/all")
    public List<PasswordResponse> getAllPasswords(){
        return service.getAllPasswords();
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getPasswordById(@PathVariable Long id){
        PasswordResponse passwordResponse = service.getPasswordById(id);
        if(passwordResponse==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PASSWORD_NOT_FOUND);
        }else{
            return ResponseEntity.ok(passwordResponse);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<PasswordResponse> createPass(@RequestParam("length") int length,
                                               @RequestParam("excludeNumbers") boolean excludeNumbers,
                                               @RequestParam("excludeSpecialChars") boolean excludeSpecialChars) {
        return new ResponseEntity<>(service.createPass(length, excludeNumbers, excludeSpecialChars), HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updatePassword(@PathVariable Long id, @RequestBody PasswordRequest passwordRequest){
        PasswordResponse passwordResponse = service.updatePassword(id, passwordRequest);
        if (passwordResponse == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PASSWORD_NOT_FOUND);
        }else{
            return ResponseEntity.ok(passwordResponse);
        }
    }
    @DeleteMapping("/delete/{id}")
    public HttpStatus deletePassword(@PathVariable Long id){
        boolean isExist = service.deletePassword(id);
        if(isExist){
            return HttpStatus.OK;
        }else{
            return HttpStatus.NOT_FOUND;
        }
    }
}

