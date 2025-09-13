package com.spring.springProject.JPAEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter @Getter @Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("category")

    private String category;
    public Category(String category){
        this.category = category;
    }
    public Category(){}
}
