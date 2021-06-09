package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@ApiModel(description = "사용자가 작성한 게시물의 상세 정보를 위한 도메인 객체")
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    // User : Post -> 1 : (0~N), Main : Sub -> Parent : Child
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
}
