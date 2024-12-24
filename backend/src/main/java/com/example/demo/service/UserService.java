package com.example.demo.service;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    //@Autowired
    //UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    //@Autowired
    //BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }

    public UserEntity findUserById(Long userId) {
        Optional<UserEntity> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new UserEntity());
    }

    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(UserRegistrationDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return false; // Пользователь с таким именем уже существует
        }
    
        UserEntity newUser = new UserEntity();
        newUser.setUsername(userDto.getUsername());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword())); // Хеширование пароля
        userRepository.save(newUser);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<UserEntity> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM users u WHERE u.id > :paramId", UserEntity.class)
                .setParameter("paramId", idMin).getResultList();
    }

    public void registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(newUser);
    }

    public void validateUser(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }
}