package com.codegym.service.user;

import com.codegym.model.DTO.ICountRole;
import com.codegym.service.IGeneralService;
import com.codegym.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    Optional<User> findByUsername(String username);
    Iterable<ICountRole> getRole();
    Boolean existsByUsername(String username);

    User save1(User user);
}
