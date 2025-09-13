package com.spring.springProject.JPAEntities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Product {
    @Id
    @JsonProperty("id")


    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("price")
    private double price;
    @JsonProperty("description")
    @Column(columnDefinition = "text")
    private String description;
    @JsonProperty("rating")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    private Rating rating;
    @JsonProperty("category")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @JsonProperty("image")
    private String image;
   public Product(){}
    public Product(String title,Category category,double price,String description,Rating rating){
       this.title = title;
       this.category = category;
       this.price = price;
       this.description = description;
       this.rating = rating;
    }
    @Override
   public String toString(){
       return "User{id="+id+", title="+title+", price="+price+", description="+description+"}";
   }

}
