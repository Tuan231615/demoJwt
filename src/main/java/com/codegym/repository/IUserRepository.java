package com.codegym.repository;

import com.codegym.model.DTO.ICountRole;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query(nativeQuery = true, value = "select r.name, count(name) as Number from roles r inner join users_roles ur on r.id=ur.role_id group by r.name")
    Iterable<ICountRole> getRoleNumber();
    Boolean existsByUsername(String username);
}