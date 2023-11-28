package com.example.Task5.Repository;

import com.example.Task5.Classes.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>{
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);
    Optional<Product>findByName(String name);
    boolean existsByName(String name);
}