package uz.pdp.task2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task2.entity.Attachment;
import uz.pdp.task2.entity.Product;
import uz.pdp.task2.payload.ProductDto;
import uz.pdp.task2.repository.AttachmentRepository;
import uz.pdp.task2.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AttachmentRepository attachmentRepository;

    @PreAuthorize(value = "hasAuthority('READ_ALL_PRODUCT')")
    @GetMapping
    public HttpEntity<?> getProducts(){
        List<Product> productList = productRepository.findAll();
        return ResponseEntity.ok(productList);
    }

    @PreAuthorize(value = "hasAuthority('READ_ONE_PRODUCT')")
    @GetMapping("/{id}")
    public HttpEntity<?> getProduct(@PathVariable Integer id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()){
            Product product1 = optionalProduct.get();
            return ResponseEntity.ok(product1);

        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize(value = "hasAuthority('ADD_PRODUCT')")
    @PostMapping
    public HttpEntity<?> addProduct(@RequestBody ProductDto productDto){
        Attachment attachment=new Attachment();
        Optional<Attachment> attachmentOptional = attachmentRepository.findById(productDto.getAttachmentId());
        if (attachmentOptional.isPresent()){
            attachment = attachmentOptional.get();
        }
        Product product=new Product();
        product.setAttachment(attachment);
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        Product save = productRepository.save(product);
        return ResponseEntity.ok(save);

    }

    @PreAuthorize(value = "hasAuthority('EDIT_PRODUCT')")
    @PutMapping("/{id}")
    public HttpEntity<?> editProduct(@PathVariable Integer id,@RequestBody ProductDto productDto){

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(productDto.getAttachmentId());
            Attachment attachment=new Attachment();
            if (optionalAttachment.isPresent()){
                 attachment = optionalAttachment.get();

            }
            product.setAttachment(attachment);
            product.setPrice(productDto.getPrice());
            product.setName(productDto.getName());
            productRepository.save(product);
            return ResponseEntity.ok("Edited");
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize(value = "hasAuthority('DELETE_PRODUCT')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable Integer id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()){
            productRepository.deleteById(id);
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.notFound().build();
    }

}
