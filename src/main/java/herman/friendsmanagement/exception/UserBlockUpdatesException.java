package herman.friendsmanagement.exception;

public class UserBlockUpdatesException extends RuntimeException {
    public UserBlockUpdatesException(String userEmail, String friendEmail) {
        super(String.format("User email %s has block updates from user email %s.", userEmail, friendEmail));
    }
}
