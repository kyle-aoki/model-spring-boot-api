package unc.book_trader.core;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import unc.book_trader.core.input.*;
import unc.book_trader.core.output.*;
import unc.book_trader.library.PasswordHasher;
import unc.book_trader.library.RandomString;

import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/UserService")
public class UserService {

    @Autowired public UserDao userDao;

    @RequestMapping("/CreateUserV1")
    public CreateUserOutputV1 createUserV1(@Valid @RequestBody CreateUserInputV1 createUserInputV1) throws Exception {
        log.info("{}", createUserInputV1);
        if (createUserInputV1.username.length() < 2) {
            return CreateUserOutputV1.builder().outcome(CreateUserOutputV1.Outcome.USERNAME_TOO_SHORT).build();
        }
        if (createUserInputV1.password.length() < 12) {
            return CreateUserOutputV1.builder().outcome(CreateUserOutputV1.Outcome.PASSWORD_TOO_SHORT).build();
        }
        if (userDao.userAlreadyExists(createUserInputV1.username)) {
            return CreateUserOutputV1.builder().outcome(CreateUserOutputV1.Outcome.USERNAME_ALREADY_TAKEN).build();
        }
        String hashedPassword = PasswordHasher.hash(createUserInputV1.password);
        CreateUserOutputV1.User user = userDao.createUser(createUserInputV1.username, hashedPassword);
        return CreateUserOutputV1.builder()
                .outcome(CreateUserOutputV1.Outcome.OK)
                .user(user)
                .build();
    }

    @RequestMapping("/ListUsersV1")
    public ListUsersOutputV1 listUsersV1(@Valid @RequestBody ListUsersInputV1 listUsersInputV1) {
        log.info("{}", listUsersInputV1);
        return ListUsersOutputV1.builder()
                .users(userDao.listUsers(listUsersInputV1.size, listUsersInputV1.page * listUsersInputV1.size))
                .build();
    }

    @CacheEvict(value = CacheType.GroupCache, allEntries = true)
    @RequestMapping("/CreateGroupV1")
    public CreateGroupOutputV1 createGroupV1(@Valid @RequestBody CreateGroupInputV1 createGroupInputV1) {
        log.info("{}", createGroupInputV1);
        String formattedGroupname = createGroupInputV1.groupname.toUpperCase();
        if (userDao.groupAlreadyExists(formattedGroupname)) {
            return CreateGroupOutputV1.builder()
                    .outcome(CreateGroupOutputV1.Outcome.GROUP_ALREADY_EXISTS)
                    .build();
        }
        userDao.createGroup(formattedGroupname);
        return CreateGroupOutputV1.builder()
                .outcome(CreateGroupOutputV1.Outcome.OK)
                .build();
    }

    @Cacheable(CacheType.GroupCache)
    @RequestMapping("/ListGroupsV1")
    public ListGroupsOutputV1 listGroupsV1() {
        return ListGroupsOutputV1.builder().groups(userDao.selectAllGroups()).build();
    }

    @Cacheable(value = CacheType.UserGroupCache, key = "#findUserGroupsInputV1.username")
    @RequestMapping("/FindUserGroupsV1")
    public FindUserGroupsOutputV1 FindUserGroupsV1(@Valid @RequestBody FindUserGroupsInputV1 findUserGroupsInputV1) {
        return FindUserGroupsOutputV1.builder()
                .userGroups(userDao.findUserGroupsByUsername(findUserGroupsInputV1.username))
                .build();
    }

    @CacheEvict(value = CacheType.UserGroupCache, key = "#enrollUserInGroupInputV1.username")
    @RequestMapping("/EnrollUserInGroupV1")
    public EnrollUserInGroupOutputV1 EnrollUserInGroupV1(@Valid @RequestBody EnrollUserInGroupInputV1 enrollUserInGroupInputV1) {
        List<String> groups = userDao.findUserGroupsByUsername(enrollUserInGroupInputV1.username);
        if (groups.contains(enrollUserInGroupInputV1.groupname)) {
            return EnrollUserInGroupOutputV1.builder()
                    .outcome(EnrollUserInGroupOutputV1.Outcome.USER_ALREADY_IN_GROUP)
                    .build();
        }
        userDao.addUserToGroup(enrollUserInGroupInputV1.username, enrollUserInGroupInputV1.groupname);
        return EnrollUserInGroupOutputV1.builder()
                .outcome(EnrollUserInGroupOutputV1.Outcome.OK)
                .build();
    }

    @RequestMapping("/LogInV1")
    public ResponseEntity<LogInOutputV1> LogInV1(@Valid @RequestBody LogInInputV1 logInInputV1) throws Exception {
        String hashedPassword = PasswordHasher.hash(logInInputV1.password);
        if (!userDao.isCorrectUsernameAndHashedPassword(logInInputV1.username, hashedPassword)) {
            return ResponseEntity.ok(LogInOutputV1.builder()
                    .outcome(LogInOutputV1.Outcome.WRONG_USERNAME_OR_PASSWORD)
                    .build());
        }
        String session = RandomString.ofLength(64);
        userDao.createSession(session, logInInputV1.username);
        HttpCookie httpCookie = ResponseCookie
                .from("book_trader", session)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(365 * 100))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .body(LogInOutputV1.builder()
                        .outcome(LogInOutputV1.Outcome.OK)
                        .sessionId(logInInputV1.sendSessionInResponseBody ? session : null)
                        .build());
    }

    @Cacheable(value = CacheType.GetUserCache, key = "#sessionId")
    @RequestMapping("/GetUserV1")
    public GetUserOutputV1 GetUserV1(@CookieValue("book_trader") String sessionId) {
        log.info("finding user for {}", sessionId);
        if (sessionId == null || sessionId.isBlank()) {
            return GetUserOutputV1.builder().outcome(GetUserOutputV1.Outcome.MISSING_SESSION).build();
        }
        var username = userDao.isValidSession(sessionId);
        if (username.isEmpty()) {
            return GetUserOutputV1.builder().outcome(GetUserOutputV1.Outcome.INVALID_SESSION).build();
        }
        List<String> userGroups = userDao.findUserGroupsByUsername(username.get());
        return GetUserOutputV1.builder()
                .outcome(GetUserOutputV1.Outcome.OK)
                .username(username.get())
                .groups(userGroups)
                .build();
    }
}
