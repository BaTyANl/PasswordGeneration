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
  private final WebsiteService websiteService;

  @GetMapping("/all")
  public List<WebsiteResponse> getAllWebsites() {
    return websiteService.getAllWebsites();
  }

  /**
   * Get website by id.
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<WebsiteResponse> getWebsiteById(@PathVariable Long id) {
    return ResponseEntity.ok(websiteService.getWebsiteById(id));
  }

  /**
   * Create website.
   */
  @PostMapping("/create")
  public ResponseEntity<WebsiteResponse> createWebsite(@RequestBody WebsiteRequest websiteRequest) {
    return ResponseEntity.ok(websiteService.createWebsite(websiteRequest));
  }

  /**
   * Add user in website.
   */
  @PutMapping("/add_user/{id}")
  public ResponseEntity<WebsiteResponse> addUser(@PathVariable Long id,
                                        @RequestBody UserRequest userRequest) {
    return ResponseEntity.ok(websiteService.addUser(id, userRequest));
  }

  /**
   * Remove user from website.
   */
  @PutMapping("/remove_user/{id}")
  public ResponseEntity<WebsiteResponse> removeUser(@PathVariable Long id,
                                                    @RequestParam String username) {
    return ResponseEntity.ok(websiteService.removeUser(id, username));
  }

  /**
   * Delete website.
   */
  @DeleteMapping("/delete/{id}")
  public HttpStatus deleteWebsite(@PathVariable Long id) {
    websiteService.deleteWebsite(id);
    return HttpStatus.OK;
  }
}

