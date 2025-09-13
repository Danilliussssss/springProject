package com.spring.springProject;

import com.spring.springProject.JPAEntities.Category;
import com.spring.springProject.JPAEntities.Product;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    EntityManager manager;
    public void createProduct(Product product){
        Optional<Category> isExisting = categoryRepository.findByCategory(product.getCategory().getCategory());
        if(isExisting.isPresent()) {

            product.setCategory(isExisting.get());
        }
        else {

            Category category = categoryRepository.saveAndFlush(product.getCategory());
            System.out.println(category.getCategory());
            product.setCategory(category);

        }
        if(product.getId()==null) {
            Long id = ((Number) manager.createNativeQuery("SELECT nextval('product_id_seq')").getSingleResult()).longValue();
            product.setId(id);
            repository.save(product);
        }
        else repository.saveAndFlush(product);

    }

    public Optional<Product> getById(Long id){
        return repository.findById(id);
    }
    public List<Product> getAll(){

        return repository.findAll();
    }
    public long count(){
        return repository.count();
    }
    public void deleteById(Long id){
        repository.deleteById(id);
    }
    public List<Category> getCategory(){
        return categoryRepository.findAll();
    }

}
