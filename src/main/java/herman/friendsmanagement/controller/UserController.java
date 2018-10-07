package herman.friendsmanagement.controller;

import herman.friendsmanagement.model.User;
import herman.friendsmanagement.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping(path="/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(path="/")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping(path="/{email}")
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping(path="/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "email") String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path="/{email}")
    public User getUserByEmail(@PathVariable(value = "email") String email) {
        return userService.getUserByEmail(email);
    }
}
