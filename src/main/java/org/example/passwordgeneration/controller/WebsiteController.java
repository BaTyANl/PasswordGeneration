package org.example.passwordgeneration.controller;

import lombok.AllArgsConstructor;
import org.example.passwordgeneration.dto.requests.WebsiteRequest;
import org.example.passwordgeneration.model.Website;
import org.example.passwordgeneration.service.WebsiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class WebsiteController {
    private final WebsiteService websiteService;

    @GetMapping("/all")
    public List<Website> getAllWebsites(){
        return websiteService.getAllWebsites();
    }

    @GetMapping("/id/{id}")
        public ResponseEntity<Website> getWebsiteById(@PathVariable Long id){
        Website website = websiteService.getWebsiteById(id);
        if(website==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(website, HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Website> createWebsite(@RequestBody WebsiteRequest websiteRequest){
        return new ResponseEntity<>(websiteService.createWebsite(websiteRequest), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Website> updateWebsite(@PathVariable Long id, @RequestBody WebsiteRequest websiteRequest){
        Website website = websiteService.updateWebsite(id, websiteRequest);
        if (website == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(website, HttpStatus.OK);
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
