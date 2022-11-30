package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;;

    @Autowired
    public UserServiceImp(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, RoleRepository roleRepository) {

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public boolean save(User user) {
        if (!userRepository.findByUsername(user.getUsername()).isEmpty()) {
            return false;
        }
        Role role = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(role));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public void update(User user) {
        user.setRoles(userRepository.findById(user.getId()).get().getRoles());
        userRepository.save(user);
    }

    @Transactional()
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void save(Role role) {
        if (roleRepository.findByName(role.getName()).isEmpty()) {
            roleRepository.save(role);
        } else{
            role = roleRepository.findByName(role.getName()).get();
        }
    }

    @Override
    public void save(User user, Role role) {
        Role role1 = roleRepository.findByName(role.getName()).get();
        user.setRoles(Collections.singleton(role1));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean contains(String username) {
        return !userRepository.findByUsername(username).isEmpty();
    }


    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        for (Role role: user.get().getRoles()){
            System.out.println(role.getName());
        }
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return user.get();
    }


}
