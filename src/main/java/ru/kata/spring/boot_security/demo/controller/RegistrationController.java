package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String printWelcome() {
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
        System.out.println("1");
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        System.out.println("1");

        if (!userForm.getPassword().equals(userForm.getConfirm())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        System.out.println("12");

        if (!userService.save(userForm)) {
            System.out.println("2");
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        System.out.println("13");

        return "redirect:/login";
    }
    @GetMapping("/roles")
    public String RolesCreater() {
        return "roles";
    }
    @GetMapping("/roles/user")
    public String CreatUserRole(Model model) {
        userService.save(new Role("ROLE_USER"));
        return "roles";
    }
    @GetMapping("/roles/admin")
    public String CreatAdminRole(Model model) {
        Role role = new Role("ROLE_ADMIN");
        userService.save(role);
        if (!userService.contains("admin")){
            User user = new User("admin", "admin");
            userService.save(user, role);
        }

        return "roles";
    }


}

