package uz.pdp.task2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task2.entity.Users;
import uz.pdp.task2.repository.UsersRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersRepository usersRepository;
    @GetMapping
    public HttpEntity<?> getUsers(){
        List<Users> all = usersRepository.findAll();
        return ResponseEntity.ok(all);
    }
    @GetMapping("/{id}")
    public HttpEntity<?> getOneUser(@PathVariable Integer id){
        Optional<Users> optionalUsers = usersRepository.findById(id);
        if (optionalUsers.isPresent()){
            Users users = optionalUsers.get();
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public HttpEntity<?> addUser(@RequestBody Users users){
        Users save = usersRepository.save(users);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteUser(@PathVariable Integer id){
        Optional<Users> optionalUsers = usersRepository.findById(id);
        if (optionalUsers.isPresent()){
            usersRepository.deleteById(id);
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.notFound().build();
    }


}
