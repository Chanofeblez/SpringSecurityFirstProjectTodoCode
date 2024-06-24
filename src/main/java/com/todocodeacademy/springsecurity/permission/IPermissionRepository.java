package com.todocodeacademy.springsecurity.permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<Permission, Long> {
}
