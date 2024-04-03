package com.example.passwordgeneration.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class WebsiteRequest {
    String websiteName;
    private List<UserRequest> users;
}
