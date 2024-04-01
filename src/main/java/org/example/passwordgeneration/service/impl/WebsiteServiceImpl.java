package org.example.passwordgeneration.service.impl;

import lombok.AllArgsConstructor;
import org.example.passwordgeneration.dto.Requests.WebsiteRequest;
import org.example.passwordgeneration.model.Website;
import org.example.passwordgeneration.repository.WebsiteRepository;
import org.example.passwordgeneration.service.WebsiteService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteRepository websiteRepository;

    @Override
    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
    }

    @Override
    public Website getWebsiteById(Long id) {
        return websiteRepository.findById(id).orElseThrow(() -> new RuntimeException("Website wasn't found"));
    }

    @Override
    public Website createWebsite(WebsiteRequest websiteRequest) {
        return websiteRepository.save(new Website(websiteRequest.getWebsiteName()));
    }

    @Override
    public Website updateWebsite(@PathVariable Long id, WebsiteRequest websiteRequest) {
        Optional<Website> existWebsite = websiteRepository.findById(id);
        if (existWebsite.isEmpty()) {
            return null;
        }
        Website website = new Website(websiteRequest.getWebsiteName());
        website.setId(id);
        return websiteRepository.save(website);
    }

    @Override
    public boolean deleteWebsite(Long id) {
        Optional<Website> existWebsite = websiteRepository.findById(id);
        if(existWebsite.isEmpty()){
            return false;
        }
        websiteRepository.delete(existWebsite.get());
        return true;
    }
}
