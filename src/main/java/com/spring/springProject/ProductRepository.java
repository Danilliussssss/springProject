package com.spring.springProject;

import com.spring.springProject.JPAEntities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
