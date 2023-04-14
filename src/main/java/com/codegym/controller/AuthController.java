package com.codegym.controller;

import com.codegym.model.DTO.ICountRole;
import com.codegym.model.Role;
import com.codegym.model.RoleName;
import com.codegym.model.request.SignupForm;
import com.codegym.response.ResponseMessage;
import com.codegym.service.JwtService;
import com.codegym.service.role.IRoleService;
import com.codegym.service.user.IUserService;
import com.codegym.model.JwtResponse;
import com.codegym.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByUsername(user.getUsername()).get();
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), currentUser.getFullName(), userDetails.getAuthorities()));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupForm signupForm){
        if (userService.existsByUsername(signupForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("the username existed!"), HttpStatus.OK);
        }
        User user = new User(signupForm.getUsername(), signupForm.getPassword(), signupForm.getFullName());
        Set<String> strRoles = signupForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role){
                case "ROLE_ADMIN" -> {
                    Role adminRole = roleService.findByName(String.valueOf(RoleName.ROLE_ADMIN));
                    roles.add(adminRole);
                }
                case "ROLE_USER" ->{
                    Role userRole = roleService.findByName(String.valueOf(RoleName.ROLE_USER));
                    roles.add(userRole);
                }
            }
        });
        user.setRoles(roles);
        userService.save1(user);
        return new ResponseEntity<>(new ResponseMessage("Create success!"), HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public ResponseEntity<Iterable<ICountRole>> getRoleNumber() {
        Iterable<ICountRole> countRoles = userService.getRole();
        return new ResponseEntity<>(countRoles, HttpStatus.OK);
    }
}