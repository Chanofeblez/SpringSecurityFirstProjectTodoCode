package com.todocodeacademy.springsecurity.user;

import com.todocodeacademy.springsecurity.role.IRoleService;
import com.todocodeacademy.springsecurity.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<List<UserSec>> getAllUsers(){
        List<UserSec> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id){
        Optional<UserSec> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserSec> createUser(@RequestBody UserSec userSec){
        Set<Role> roleList = new HashSet<Role>();
        Role readRole;

        //Recuperar la Permission/s por su ID
        for(Role role : userSec.getRolesList()){
            readRole = roleService.findById(role.getId()).orElse(null);
            if (readRole != null ){
                //si encuentro, guardo en la lista
                roleList.add(readRole);
            }
        }

        if (!roleList.isEmpty()) {
            userSec.setRolesList(roleList);

            UserSec newUser = userService.save(userSec);
            return ResponseEntity.ok(newUser);
        }
        return null;
    }
}
