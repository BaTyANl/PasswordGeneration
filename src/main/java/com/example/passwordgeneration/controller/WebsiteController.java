package com.example.passwordgeneration.controller;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.request.WebsiteRequest;
import com.example.passwordgeneration.dto.response.WebsiteResponse;
import com.example.passwordgeneration.service.WebsiteService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Website controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/website")
public class WebsiteController {
  private static final String WEBSITE_NOT_FOUND = "Website wasn't found";
  private final WebsiteService websiteService;

  @GetMapping("/all")
  public List<WebsiteResponse> getAllWebsites() {
    return websiteService.getAllWebsites();
  }

  /**
   * Get website by id.
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<Object> getWebsiteById(@PathVariable Long id) {
    WebsiteResponse websiteResponse = websiteService.getWebsiteById(id);
    if (websiteResponse == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WEBSITE_NOT_FOUND);
    } else {
      return ResponseEntity.ok(websiteResponse);
    }
  }

  /**
   * Create website.
   */
  @PostMapping("/create")
  public ResponseEntity<Object> createWebsite(@RequestBody WebsiteRequest websiteRequest) {
    WebsiteResponse websiteResponse = websiteService.createWebsite(websiteRequest);
    if (websiteResponse != null) {
      return ResponseEntity.ok(websiteResponse);
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
  }

  /**
   * Add user in website.
   */
  @PutMapping("/add_user/{id}")
  public ResponseEntity<Object> addUser(@PathVariable Long id,
                                        @RequestBody UserRequest userRequest) {
    WebsiteResponse websiteResponse = websiteService.addUser(id, userRequest);
    if (websiteResponse != null) {
      return ResponseEntity.ok(websiteResponse);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  /**
   * Remove user from website.
   */
  @PutMapping("/remove_user/{id}")
  public ResponseEntity<Object> removeUser(@PathVariable Long id, @RequestParam String username) {
    WebsiteResponse websiteResponse = websiteService.removeUser(id, username);
    if (websiteResponse != null) {
      return ResponseEntity.ok(websiteResponse);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Information wasn't correct");
    }
  }

  /**
   * Delete website.
   */
  @DeleteMapping("/delete/{id}")
  public HttpStatus deleteWebsite(@PathVariable Long id) {
    boolean isExist = websiteService.deleteWebsite(id);
    if (isExist) {
      return HttpStatus.OK;
    } else {
      return HttpStatus.NOT_FOUND;
    }
  }
}

