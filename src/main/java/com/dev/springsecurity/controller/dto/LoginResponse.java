package com.dev.springsecurity.controller.dto;

public record LoginResponse( String accessToken, Long expiresIn) {
}
