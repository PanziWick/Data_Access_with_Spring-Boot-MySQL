package com.example.Task5.Service;

import com.example.Task5.Classes.Product;
import com.example.Task5.DTO.ApiResponse;
import com.example.Task5.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    public ResponseEntity<ApiResponse<List<Product>>> saveProduct(Product product){
        try{
            //Field Validation
            if(isProductDuplicate(product.getName())){
                return ResponseEntity.badRequest().body(new ApiResponse<>(400,"Product with the same name already exists", Collections.emptyList()));
            }
            //Save Product
            productRepository.save(product);
            return ResponseEntity.ok(new ApiResponse<>(201, "Product added successfully.", Collections.emptyList()));
        }catch (Exception e){
            //Log is internal error exist
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal server error.", Collections.emptyList()));
        }
    }

    public ResponseEntity<ApiResponse<List<Product>>> updateProduct(Long id, Product updatedProduct) {
        try {
            Optional<Product> existingProductOptional = productRepository.findById(id);
            // Check if the updated product name already exists
            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();

                // Update the existing product with the new information
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setPrice(updatedProduct.getPrice());

                // Save the updated product
                productRepository.save(existingProduct);

                return ResponseEntity.ok(new ApiResponse<>(200, "Product updated successfully", new ArrayList<>()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(404, "Product not found", new ArrayList<>()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "An error occurred while updating the product", null));
        }
    }

    public ResponseEntity<ApiResponse<List<Void>>> deleteProduct(Long id){
        try{
            //Check if the product exist in the database
            Optional<Product> exisitingProductOptional = productRepository.findById(id);
            if(exisitingProductOptional.isPresent()){
                Product product = exisitingProductOptional.get();
                //if product is present then delete
                productRepository.deleteById(id);
                return ResponseEntity.ok(new ApiResponse<>(200, "Product Deleted successfully", new ArrayList<>()));
            }else{
                return ResponseEntity.status(404).body(new ApiResponse<>(404, "Product not found", new ArrayList<>()));
            }

        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "An error occurred while deleting the product", new ArrayList<>()));
        }
    }

    private boolean isProductDuplicate(String productName) {
        return productRepository.existsByName(productName);
    }
}
