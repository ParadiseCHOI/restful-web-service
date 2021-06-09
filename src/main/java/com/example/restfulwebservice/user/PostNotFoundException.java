package com.example.restfulwebservice.user;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(int id) {
        super(String.format("Post[%s] not found", id));
    }
}
