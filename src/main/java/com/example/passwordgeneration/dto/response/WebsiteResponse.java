package com.example.passwordgeneration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
public class WebsiteResponse {
    private String websiteName;
    private List<String> users;
}
