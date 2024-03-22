package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getAllUsers(ModelMap model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam("name") String name, @RequestParam("lastName") String lastName,
                          @RequestParam("age") int age) {
        userService.addUser(new User(name, lastName, age));
        return "redirect:/users";
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") int userId) {
        userService.deleteUser(userId);
        return "redirect:/users";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam("userId") int userId, @RequestParam("name") String name,
                             @RequestParam("lastName") String lastName, @RequestParam("age") String age) {
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setName(name.isEmpty() ? user.getName() : name);
            user.setLastName(lastName.isEmpty() ? user.getLastName() : lastName);
            user.setAge(age.isEmpty() ? user.getAge() : Integer.parseInt(age));
            userService.updateUser(user);
        }
        return "redirect:/users";
    }
}
