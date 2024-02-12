package unc.book_trader.core;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.spring5.JdbiRepository;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;
import unc.book_trader.core.output.CreateUserOutputV1;
import unc.book_trader.core.output.ListUsersOutputV1;

@Repository
@JdbiRepository
public interface UserDao {

    @RegisterBeanMapper(ListUsersOutputV1.User.class)
    @SqlQuery("select id, username from users limit :size offset :offset")
    List<ListUsersOutputV1.User> listUsers(@Bind int size, @Bind int offset);

    @SqlQuery("select count(1) from users where username = :username")
    boolean userAlreadyExists(@Bind String username);

    @RegisterBeanMapper(CreateUserOutputV1.User.class)
    @SqlQuery("""
                  insert into users (username, password_hash)
                  values (:username, :password_hash)
                  returning id, username
              """)
    CreateUserOutputV1.User createUser(@Bind String username, @Bind String password_hash);

    @SqlQuery("select count(1) from groups where groupname = :groupname")
    boolean groupAlreadyExists(@Bind String groupname);

    @SqlUpdate("insert into groups (groupname) values (:groupname)")
    void createGroup(@Bind String groupname);

    @SqlQuery("select groupname from groups")
    List<String> selectAllGroups();

    @SqlQuery("select groupname from user_groups where username = :username")
    List<String> findUserGroupsByUsername(@Bind String username);

    @SqlUpdate("insert into user_groups (username, groupname) values (:username, :groupname)")
    void addUserToGroup(@Bind String username, @Bind String groupname);

    @SqlQuery("select count(1) from users where username = :username and password_hash = :hashedPassword")
    boolean isCorrectUsernameAndHashedPassword(@Bind String username, @Bind String hashedPassword);

    @SqlUpdate("insert into session (session_id, username) VALUES (:sessionId, :username)")
    void createSession(@Bind String sessionId, @Bind String username);

    @SqlQuery("select username from session where session_id = :sessionId")
    Optional<String> isValidSession(@Bind String sessionId);
}
