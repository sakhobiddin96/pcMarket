package uz.pdp.task2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task2.entity.Attachment;
import uz.pdp.task2.entity.Product;
import uz.pdp.task2.entity.ProductProperties;
import uz.pdp.task2.payload.ProductDto;
import uz.pdp.task2.payload.ProductPropertiesDto;
import uz.pdp.task2.repository.AttachmentRepository;
import uz.pdp.task2.repository.ProductPropertiesRepository;
import uz.pdp.task2.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productProperties")
public class ProductPropertiesController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductPropertiesRepository productPropertiesRepository;
    @PreAuthorize(value = "hasAuthority('READ_ALL_PRODUCT')")
    @GetMapping
    public HttpEntity<?> getProductProperties() {
        List<ProductProperties> productProperties = productPropertiesRepository.findAll();
        return ResponseEntity.ok(productProperties);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getProductProperty(@PathVariable Integer id) {
        Optional<ProductProperties> optionalProductProperties = productPropertiesRepository.findById(id);
        if (optionalProductProperties.isPresent()) {
            ProductProperties productProperties = optionalProductProperties.get();
            return ResponseEntity.ok(productProperties);

        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public HttpEntity<?> addProductProperty(@RequestBody ProductPropertiesDto productPropertiesDto) {
        ProductProperties productProperties=new ProductProperties();
        Optional<Product> optionalProduct = productRepository.findById(productPropertiesDto.getProductId());
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            productProperties.setProduct(product);
            productProperties.setKey(productPropertiesDto.getKey());
            productProperties.setKey(productPropertiesDto.getKey());
            productPropertiesRepository.save(productProperties);
            return ResponseEntity.ok("Saved");
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/{id}")
    public HttpEntity<?> editProductProperty(@PathVariable Integer id, @RequestBody ProductPropertiesDto productPropertiesDto) {

        Optional<ProductProperties> optionalProductProperties = productPropertiesRepository.findById(id);
        if (optionalProductProperties.isPresent()) {
            ProductProperties productProperties=new ProductProperties();
            Optional<Product> optionalProduct = productRepository.findById(productPropertiesDto.getProductId());
            if (optionalProduct.isPresent()){
                Product product = optionalProduct.get();
                productProperties.setProduct(product);
                productProperties.setValue(productPropertiesDto.getValue());
                productProperties.setKey(productPropertiesDto.getKey());
                productPropertiesRepository.save(productProperties);
                return ResponseEntity.ok("Edited");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProductProperty(@PathVariable Integer id) {
        Optional<ProductProperties> optionalProductProperties = productPropertiesRepository.findById(id);
        if (optionalProductProperties.isPresent()) {
            productPropertiesRepository.deleteById(id);
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.notFound().build();
    }

}
