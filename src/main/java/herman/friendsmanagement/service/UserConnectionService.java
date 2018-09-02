package herman.friendsmanagement.service;

import herman.friendsmanagement.exception.UserBlockUpdatesException;
import herman.friendsmanagement.exception.UserNotFoundException;
import herman.friendsmanagement.model.User;
import herman.friendsmanagement.model.UserConnection;
import herman.friendsmanagement.repository.UserConnectionRepository;
import herman.friendsmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserConnectionService implements IUserConnectionService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Override
    public boolean createConnection(String userEmail, String friendEmail) {
        //find user
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException(userEmail);
        }

        //find friend
        User friend = userRepository.findByEmail(friendEmail);
        if (friend == null) {
            throw new UserNotFoundException(userEmail);
        }

        UserConnection existingUserConnection = userConnectionRepository.findByUserAndFriend(user, friend);
        if (existingUserConnection != null) {
            if (existingUserConnection.isBlockUpdates()) {
                throw new UserBlockUpdatesException(userEmail, friendEmail);
            } else {
                //check the reverse
                UserConnection existingFriendUserConnection = userConnectionRepository.findByUserAndFriend(friend, user);
                if (existingFriendUserConnection != null && existingFriendUserConnection.isBlockUpdates()) {
                    throw new UserBlockUpdatesException(friendEmail, userEmail);
                }
            }
        }

        UserConnection userConnection1 = createUserConnection(user, friend);
        userConnection1.setConnected(true);
        UserConnection userConnection2 = createUserConnection(friend, user);
        userConnection2.setConnected(true);

        List<UserConnection> result = userConnectionRepository.saveAll(Arrays.asList(userConnection1, userConnection2));

        return result.size() == 2;
    }

    private UserConnection createUserConnection(User user, User friend) {
        UserConnection userConnection = new UserConnection();
        userConnection.setUser(user);
        userConnection.setFriend(friend);
        return userConnection;
    }

    @Override
    public List<User> getFriendsByEmail(String email) {
        //find user
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }

        List<User> friends = new ArrayList<User>();

        List<UserConnection> userConnections = userConnectionRepository.findAllByUser(user);
        for (UserConnection userConnection : userConnections) {
            friends.add(userConnection.getFriend());
        }

        return friends;
    }

    @Override
    public List<User> getCommonFriends(String email1, String email2) {
        //find user
        User user1 = userRepository.findByEmail(email1);
        if (user1 == null) {
            throw new UserNotFoundException(email1);
        }

        //find user
        User user2 = userRepository.findByEmail(email2);
        if (user2 == null) {
            throw new UserNotFoundException(email2);
        }

        List<User> friends1 = new ArrayList<User>();
        List<UserConnection> userConnections1 = userConnectionRepository.findAllByUser(user1);
        for (UserConnection userConnection : userConnections1) {
            friends1.add(userConnection.getFriend());
        }

        List<User> friends2 = new ArrayList<User>();
        List<UserConnection> userConnections2 = userConnectionRepository.findAllByUser(user1);
        for (UserConnection userConnection : userConnections2) {
            friends2.add(userConnection.getFriend());
        }

        List<User> commonFriends = friends1.stream()
                .filter(e -> friends2.stream().anyMatch(id -> id.equals(e.getId())))
                .collect(Collectors.toList());

        return commonFriends;
    }

    @Override
    public boolean subscribeUpdates(String requestorEmail, String targetEmail) {
        //find user
        User requestor = userRepository.findByEmail(requestorEmail);
        if (requestor == null) {
            throw new UserNotFoundException(requestorEmail);
        }

        //find user
        User target = userRepository.findByEmail(targetEmail);
        if (target == null) {
            throw new UserNotFoundException(targetEmail);
        }

        UserConnection userConnection = userConnectionRepository.findByUserAndFriend(requestor, target);
        if (userConnection == null) {
            userConnection = createUserConnection(requestor, target);
        }
        userConnection.setReceiveUpdates(true);

        userConnectionRepository.save(userConnection);
        return true;
    }

    @Override
    public boolean blockUpdates(String requestorEmail, String targetEmail) {
        //find user
        User requestor = userRepository.findByEmail(requestorEmail);
        if (requestor == null) {
            throw new UserNotFoundException(requestorEmail);
        }

        //find user
        User target = userRepository.findByEmail(targetEmail);
        if (target == null) {
            throw new UserNotFoundException(targetEmail);
        }

        UserConnection userConnection = userConnectionRepository.findByUserAndFriend(requestor, target);
        if (userConnection == null) {
            userConnection = createUserConnection(requestor, target);
        }
        userConnection.setBlockUpdates(true);

        userConnectionRepository.save(userConnection);
        return true;
    }

    @Override
    public List<User> retrieveEmailCanReceiveUpdates(String senderEmail, String text) {
        //find user
        User sender = userRepository.findByEmail(senderEmail);
        if (sender == null) {
            throw new UserNotFoundException(senderEmail);
        }

        List<User> users = userConnectionRepository.findAllByBlockUpdatesAndConnectedOrReceiveUpdates(false, true, true);

        //TODO parse text if contains emails add to users

        return users;
    }
}
