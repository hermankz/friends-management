package herman.friendsmanagement.repository;

import herman.friendsmanagement.model.User;
import herman.friendsmanagement.model.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {
    List<UserConnection> findAllByUser(User user);

    UserConnection findByUserAndFriend(User user, User friend);

    List<UserConnection> findAllByFriend(User friend);

    // select u.id, u.name from user_connection uc
    // inner join user u on u.id = uc.friend_id
    // where uc.user_id = userId1
    // and uc.friend_id in (select friend_id from user_connection where user_id = userId2)
//    @Query()
//    List<User> findAllUsersWithCommonFriend(User user1, User user2);

    List<User> findAllByBlockUpdatesAndConnectedOrReceiveUpdates(boolean blockUpdates, boolean connected, boolean receiveUpdates);

//    public List<User> getUsersCanReceiveUpdates(User sender) {
//        return null;
//    }
}
