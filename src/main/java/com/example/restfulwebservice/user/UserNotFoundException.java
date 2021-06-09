package com.example.restfulwebservice.user;

// HTTP Status Code
// 2XX > OK
// 4XX > Client
// 존재하지 않는 리소스 요청, 권한, 작동하지 않는 메소드 호출
// 5XX > Server
// Sever 측 Programming 상 문제, 서버의 외부 리소스와의 연결 문제

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super(String.format("ID[%s] not found", id));
    }
}
