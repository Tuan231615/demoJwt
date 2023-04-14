package com.codegym.service.role;

import com.codegym.model.RoleName;
import com.codegym.service.IGeneralService;
import com.codegym.model.Role;

import java.util.Optional;

public interface IRoleService extends IGeneralService<Role> {
    Role findByName(String name);

//    Optional<Role> findByName(RoleName roleAdmin);
}