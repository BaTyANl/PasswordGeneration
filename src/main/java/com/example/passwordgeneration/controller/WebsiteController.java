package com.example.passwordgeneration.controller;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.request.WebsiteRequest;
import com.example.passwordgeneration.dto.response.WebsiteResponse;
import com.example.passwordgeneration.service.WebsiteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/website")
public class WebsiteController {
    private final WebsiteService websiteService;

    @GetMapping("/all")
    public List<WebsiteResponse> getAllWebsites(){
        return websiteService.getAllWebsites();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<WebsiteResponse> getWebsiteById(@PathVariable Long id){
        WebsiteResponse websiteResponse = websiteService.getWebsiteById(id);
        if(websiteResponse==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else{
            return ResponseEntity.ok(websiteResponse);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<WebsiteResponse> createWebsite(@RequestBody WebsiteRequest websiteRequest){
        WebsiteResponse websiteResponse = websiteService.createWebsite(websiteRequest);
        if(websiteResponse!=null){
            return ResponseEntity.ok(websiteResponse);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping("/add_user/{id}")
    public ResponseEntity<WebsiteResponse> addIp(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        WebsiteResponse websiteResponse = websiteService.addUser(id, userRequest);
        if(websiteResponse != null) {
            return ResponseEntity.ok(websiteResponse);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping("/remove_user/{id}")
    public ResponseEntity<WebsiteResponse> removeIp(@PathVariable Long id, @RequestParam String username) {
        WebsiteResponse websiteResponse = websiteService.removeUser(id, username);
        if(websiteResponse != null) {
            return ResponseEntity.ok(websiteResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteWebsite(@PathVariable Long id){
        boolean isExist = websiteService.deleteWebsite(id);
        if(isExist){
            return HttpStatus.OK;
        }else{
            return HttpStatus.NOT_FOUND;
        }
    }
}

