package herman.friendsmanagement.service;

import herman.friendsmanagement.exception.UserBlockUpdatesException;
import herman.friendsmanagement.exception.UserNotFoundException;
import herman.friendsmanagement.model.IdEmail;
import herman.friendsmanagement.model.User;
import herman.friendsmanagement.model.UserConnection;
import herman.friendsmanagement.repository.UserConnectionRepository;
import herman.friendsmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            throw new UserNotFoundException(friendEmail);
        }

        UserConnection existingUserConnection = userConnectionRepository.findByUserAndFriend(user, friend);
        if (existingUserConnection != null) {
            if (existingUserConnection.isBlockUpdates()) {
                throw new UserBlockUpdatesException(userEmail, friendEmail);
            } else {
                existingUserConnection.setConnected(true);
            }
        }

        //check the reverse
        UserConnection existingFriendUserConnection = userConnectionRepository.findByUserAndFriend(friend, user);
        if (existingFriendUserConnection != null) {
            if (existingFriendUserConnection.isBlockUpdates()) {
                throw new UserBlockUpdatesException(friendEmail, userEmail);
            } else {
                existingFriendUserConnection.setConnected(true);
            }
        }

        if (existingUserConnection != null) {
            return true;
        } else {
            // create new user connection
            UserConnection userConnection1 = createUserConnection(user, friend);
            userConnection1.setConnected(true);
            UserConnection userConnection2 = createUserConnection(friend, user);
            userConnection2.setConnected(true);

            List<UserConnection> result = userConnectionRepository.saveAll(Arrays.asList(userConnection1, userConnection2));

            return result.size() == 2;
        }
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

        List<IdEmail> results = userConnectionRepository.findAllUsersWithCommonFriend(user1.getId(), user2.getId());
        List<User> commonFriends = new ArrayList<User>();
        for (IdEmail row : results) {
            commonFriends.add(new User(row.getId(), row.getEmail()));
        }

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
        userConnection.setBlockUpdates(false);

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
        userConnection.setReceiveUpdates(false);

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

        List<UserConnection> results = userConnectionRepository.findAllByFriendAndBlockUpdatesAndConnectedOrReceiveUpdates(sender, false, true, true);
        List<User> users = new ArrayList<User>();
        for (UserConnection uc : results) {
            users.add(uc.getUser());
        }

        // parse text if contains emails add to users
        Set<String> emails = findEmailAddresses(text);
        for (String email : emails) {
            User user = userRepository.findByEmail(email);
            if (user != null && !users.contains(user)) {
                UserConnection uc = userConnectionRepository.findByUserAndFriend(user, sender);
                if (uc != null && uc.isBlockUpdates()) {
                    // do not send if it is block updates
                    break;
                } else {
                    // no friend connection also add to list sender
                    users.add(user);
                }

            }
        }

        return users;
    }

    private Set<String> findEmailAddresses(String input) {
        Pattern p = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(input);
        Set<String> emails = new HashSet<String>();
        while (matcher.find()) {
            emails.add(matcher.group());
        }

        return emails;
    }
}
