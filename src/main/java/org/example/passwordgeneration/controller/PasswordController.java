package org.example.passwordgeneration.controller;

import lombok.AllArgsConstructor;
import org.example.passwordgeneration.dto.Requests.PasswordRequest;
import org.example.passwordgeneration.model.Password;
import org.example.passwordgeneration.service.PasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/password")
@AllArgsConstructor
public class PasswordController {
    private final PasswordService service;
    @GetMapping("/all")
    public List<Password> getAllPasswords(){
        return service.getAllPasswords();
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<Password> getPasswordById(@PathVariable Long id){
        Password password = service.getPasswordById(id);
        if(password==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(password, HttpStatus.OK);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<Password> createPass(@RequestParam("length") int length,
                                       @RequestParam("excludeNumbers") boolean excludeNumbers,
                                       @RequestParam("excludeSpecialChars") boolean excludeSpecialChars) {
        return new ResponseEntity<>(service.createPass(length, excludeNumbers, excludeSpecialChars), HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Password> updatePassword(@PathVariable Long id, @RequestBody PasswordRequest passwordRequest){
        Password password = service.updatePassword(id, passwordRequest);
        if (password == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(password, HttpStatus.OK);
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