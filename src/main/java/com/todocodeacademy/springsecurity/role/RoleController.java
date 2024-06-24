package com.todocodeacademy.springsecurity.role;

import com.todocodeacademy.springsecurity.permission.IPermissionService;
import com.todocodeacademy.springsecurity.permission.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id){
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        Set<Permission> permiList = new HashSet<Permission>();
        Permission readPermission;

        //Recuperar la Permission/s por su ID
        for (Permission per : role.getPermissionsList()){
            readPermission = permissionService.findById(per.getId()).orElse(null);
            if (readPermission != null){
                //si encuentro, guardo en la lista
                permiList.add(readPermission);
            }
        }

        role.setPermissionsList(permiList);
        Role newRole = roleService.save(role);
        return ResponseEntity.ok(newRole);
    }
}
