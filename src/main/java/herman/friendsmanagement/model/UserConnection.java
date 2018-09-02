package herman.friendsmanagement.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "user_connection")
public class UserConnection {
    @Id
    @GeneratedValue(generator = "user_connection_id_generator")
    @SequenceGenerator(name = "user_connection_id_generator", sequenceName = "user_connection_id_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "friend_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User friend;

    private boolean connected;

    private boolean receiveUpdates;

    private boolean blockUpdates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isReceiveUpdates() {
        return receiveUpdates;
    }

    public void setReceiveUpdates(boolean receiveUpdates) {
        this.receiveUpdates = receiveUpdates;
    }

    public boolean isBlockUpdates() {
        return blockUpdates;
    }

    public void setBlockUpdates(boolean blockUpdates) {
        this.blockUpdates = blockUpdates;
    }
}
