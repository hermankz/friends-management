package herman.friendsmanagement.controller;

import herman.friendsmanagement.model.User;
import herman.friendsmanagement.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping(path="/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(path="/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping(path="/users/{email}")
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping(path="/users/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "email") String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path="/users/{email}")
    public User getUserByEmail(@PathVariable(value = "email") String email) {
        return userService.getUserByEmail(email);
    }
}
