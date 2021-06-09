package com.example.restfulwebservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// lombok
@Data
// 생성자 관련 Annotation
// 모든 arguments 를 포함한 생성자
@AllArgsConstructor
// default 생성자
@NoArgsConstructor

// Bean Class
public class HelloWorldBean {
    private String message;
}
