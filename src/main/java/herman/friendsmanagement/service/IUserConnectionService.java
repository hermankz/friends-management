package herman.friendsmanagement.service;

import herman.friendsmanagement.model.User;

import java.util.List;

public interface IUserConnectionService {
    boolean createConnection(String userEmail, String friendEmail);

    List<User> getFriendsByEmail(String email);

    List<User> getCommonFriends(String email1, String email2);

    boolean subscribeUpdates(String requestorEmail, String targetEmail);

    boolean blockUpdates(String requestorEmail, String targetEmail);

    List<User> retrieveEmailCanReceiveUpdates(String senderEmail, String text);
}
