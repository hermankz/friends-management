package herman.friendsmanagement.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(String.format("User with email %s is not found.", email));
    }
}
