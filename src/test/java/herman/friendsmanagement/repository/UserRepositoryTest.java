package herman.friendsmanagement.repository;

import herman.friendsmanagement.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // given
        User user1 = new User();
        user1.setEmail("test@example.com");
        entityManager.persist(user1);
        entityManager.flush();

        // when
        User found = userRepository.findByEmail(user1.getEmail());

        // then
        assertEquals(found.getEmail(), user1.getEmail());
    }
}