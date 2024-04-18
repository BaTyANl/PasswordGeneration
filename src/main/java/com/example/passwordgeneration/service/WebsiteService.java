package com.example.passwordgeneration.service;

import com.example.passwordgeneration.dto.request.UserRequest;
import com.example.passwordgeneration.dto.request.WebsiteRequest;
import com.example.passwordgeneration.dto.response.WebsiteResponse;
import java.util.List;

/**
 * Interface of website's service.
 */
public interface WebsiteService {

  List<WebsiteResponse> getAllWebsites();

  WebsiteResponse getWebsiteById(Long id);

  WebsiteResponse createWebsite(WebsiteRequest websiteRequest);

  WebsiteResponse addUser(Long id, UserRequest userRequest);

  WebsiteResponse removeUser(Long id, String username);

  void deleteWebsite(Long id);
}
