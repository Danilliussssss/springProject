package com.spring.springProject.Controllers;

import com.spring.springProject.JPAEntities.Category;
import com.spring.springProject.JPAEntities.Product;
import com.spring.springProject.JPAEntities.Rating;
import com.spring.springProject.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CreateController {
    @Autowired
    private ProductService service;
    //Метод, срабатываемый при переходе на страницу создания нового метода
     @GetMapping("/create")
     public String start(Model model){

         return "create";
     }
    //Создать новый товар
     @PostMapping("/createProduct")
     public String create(Model model, String title, String category, double price, String description, Rating rating){
         Category category1 = new Category(category);
         Product newProduct = new Product(title,category1,price,description,rating);
         System.out.println(newProduct.getTitle());
         service.createProduct(newProduct);
         return "main";
     }
}
