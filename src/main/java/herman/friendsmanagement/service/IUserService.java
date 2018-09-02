package herman.friendsmanagement.service;

import herman.friendsmanagement.model.User;

import java.util.List;

public interface IUserService {

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteByEmail(String email);

    User getUserByEmail(String email);
}
