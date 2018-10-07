package herman.friendsmanagement.repository;

import herman.friendsmanagement.model.IdEmail;
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

    @Query(
            value = "select u.id, u.email from user_connection uc " +
                    "inner join user_account u on u.id = uc.friend_id " +
                    "where uc.user_id = ?1 " +
                    "and uc.friend_id in (select friend_id from user_connection where user_id = ?2)",
            nativeQuery = true
    )
    List<IdEmail> findAllUsersWithCommonFriend(Long userId1, Long userId2);

    List<UserConnection> findAllByFriendAndBlockUpdatesAndConnectedOrReceiveUpdates(User friend, boolean blockUpdates, boolean connected, boolean receiveUpdates);
}
