package org.example.passwordgeneration.service;

import org.example.passwordgeneration.dto.Requests.WebsiteRequest;
import org.example.passwordgeneration.model.Website;

import java.util.List;

public interface WebsiteService {
    List<Website> getAllWebsites();
    Website getWebsiteById(Long id);
    Website createWebsite(WebsiteRequest websiteRequest);
    Website updateWebsite(Long id, WebsiteRequest websiteRequest);
    boolean deleteWebsite(Long id);
}
