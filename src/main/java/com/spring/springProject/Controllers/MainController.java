package com.spring.springProject.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springProject.ConnectServer;
import com.spring.springProject.JPAEntities.Category;
import com.spring.springProject.JPAEntities.Product;
import com.spring.springProject.JPAEntities.Rating;
import com.spring.springProject.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private ProductService service;
    private Optional<Product> product;
    private List<Product> products;
    List<Category> categories;
    private int pages;
    private int size;

    //Метод, срабатывающий при загрузке главной страницы
    @GetMapping("/")
    public String start(Model model) throws IOException, URISyntaxException, InterruptedException {

        products =  service.getAll();
        categories = service.getCategory();

        for(Product product: products) {
            service.createProduct(product);
            model.addAttribute("products", products);
            model.addAttribute("categories",categories);
        }
        uniqueCategory();
       return "main";
    }

    @GetMapping("/get")
    public String getDataToServer(Model model) throws IOException, URISyntaxException, InterruptedException {
        ConnectServer server = new ConnectServer();
        ObjectMapper mapper = new ObjectMapper();

        products =  mapper.readValue(server.getData(), new TypeReference<List<Product>>() {});

        for(Product product: products) {
            service.createProduct(product);
            model.addAttribute("products", products);
        }
        return "main";
    }
    //Метод, обрабатывающий Get-запрос на получение товара по id
    @GetMapping("/product")
    public String getProductById(Model model,Long id){
        if(id!=null) {
           product = service.getById(id);

            if (product.isPresent()) {
                model.addAttribute("id", id);
                model.addAttribute("product", product.get());
            }
        }
        return "main";
    }
    //Метод поиска товаров по категории
     public void findByCategory(String category){
        if(StringUtils.hasText(category)) {
            System.out.println("Выбранная категория: " + category);
            Iterator<Product> iterator = products.iterator();
            while (iterator.hasNext()){
                Product product1 = iterator.next();
                if(!category.equals( product1.getCategory().getCategory()))
                    iterator.remove();
            }
        }

     }
     //Пагинация товаров с фильтрацией по цене и категориям.
    @GetMapping("/page")
    public String page(Model model, Integer size,Integer page,Double minPrice, Double maxPrice,boolean isTrue,String category){

       products = service.getAll();



        List<Category> unique;
        Iterator<Product> iterator = products.iterator();
        if(isTrue) {
            unique =uniqueCategory();

            if(!unique.isEmpty())
               while (iterator.hasNext()){
                   Product product1 = iterator.next();
                   if(!unique.contains(product1.getCategory()))
                       iterator.remove();
               }
        }
        if(minPrice==null)
            minPrice=Double.MIN_VALUE;
        if(maxPrice==null)
           maxPrice =  Double.MAX_VALUE;
        findByCategory(category);
        if(size==null)
            size=products.size();
        if(page==null)
            page=1;
        if(size<= products.size()&&size>0) {
            this.size = size;
            this.pages = products.size() / size;
            if (this.pages * size < products.size())
                this.pages++;
            System.out.println(size);
            System.out.println(pages);
            List<List<Product>> listProducts = new ArrayList<>(pages);
            for (int i = 0; i < pages; i++)
                listProducts.add(new ArrayList<>());
            int j = 0;
            for (int i = 0; i < products.size(); i++) {
                if (i > 0 && i % size == 0)
                    j++;
                if(products.get(i).getPrice()>minPrice&&products.get(i).getPrice()<maxPrice)
                    listProducts.get(j).add(products.get(i));
            }


            if (page > 0 && page <= pages)
                model.addAttribute("products", listProducts.get(page - 1));

        }
       // }
        return "main";
    }

    public List<Category> uniqueCategory(){
       categories =  service.getCategory();
        List<Category> result = new ArrayList<>();
        service.count();

        for(Category category: categories) {
            long count = 0;
            for (Product product1 : products)
                if (product1.getCategory().equals(category))
                    count++;
            if(count==1)
                result.add(category);

        }
        for(Category category: result)
            System.out.println(category.getCategory());
        return result;
    }
    // Удалить выбранный товар
    @PostMapping("/delete")
    public String delete(Model model){
        System.out.println(product.get().getId());
        if (product.get().getId()!=null)
            service.deleteById(product.get().getId());
        return "main";

    }
    //Редактировать существующий товар
    @PostMapping("/save")
    public String save(Model model, String title,String category,double price,String description,Rating rating){
         product.get().setTitle(title);
         Category category1 = new Category(category);
         product.get().setCategory(category1);
         product.get().setPrice(price);
         product.get().setDescription(description);
         product.get().setRating(rating);
        service.createProduct(product.get());
        return "main";
    }
    //Создать новый товар(метод переводит на другую страницу)
    @PostMapping("/create")
    public String create(){
        return "redirect:/create";
    }

}
