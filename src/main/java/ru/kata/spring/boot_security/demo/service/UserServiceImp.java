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
public class UserServiceImp implements UserService, UserDetailsService{

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
        if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public void update(User user) {
        String pass = user.getPassword();
        if (pass.isEmpty()){
            user.setPassword(userRepository.findById(user.getId()).get().getPassword());
        }
        else{
            user.setPassword(bCryptPasswordEncoder.encode(pass));
        }
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
    @Transactional(readOnly = true)
    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getReferenceById(id);
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
        return !userRepository.findByEmail(username).isEmpty();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return user.get();
    }

}
