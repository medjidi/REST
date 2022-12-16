package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public ModelAndView getPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users.html");
        return modelAndView;
    }

    @GetMapping()
    public Pair<Pair<UserDTO, User>, Pair<List<UserDTO>, List<Role>>> printAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO admin = convertToDTO((User) authentication.getPrincipal());
        List<Role> roles = userService.getAllRoles();
        for (Role r : roles) {
            System.out.println(r.getAuthority());
        }
        List<UserDTO> users = userService.getListOfUsers().stream()
                .map(this::convertToDTO).collect(Collectors.toList());

        User user = new User();
        return Pair.of(Pair.of(admin, user), Pair.of(users, roles));
    }
//    @GetMapping()
//    public List<UserDTO> printAllUsers() {
//        List<UserDTO> users = userService.getListOfUsers().stream()
//                .map(this::convertToDTO).collect(Collectors.toList());
//        for (UserDTO user : users) {
//            System.out.println(user);
//        }
//        return users;
//    }

    @PostMapping()
    public Pair<Pair<UserDTO, User>, Pair<List<UserDTO>, List<Role>>> creat(@RequestBody User userDTO) {
        userService.save(userDTO);
        return printAllUsers();
    }


    @PatchMapping("/{id}")
    public Pair<Pair<UserDTO, User>, Pair<List<UserDTO>, List<Role>>> update(@RequestBody User user) {
        userService.update(user);
        return printAllUsers();
    }

    @DeleteMapping("/{id}")
    public Pair<Pair<UserDTO, User>, Pair<List<UserDTO>, List<Role>>> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return printAllUsers();
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private RoleDTO convertToDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

}