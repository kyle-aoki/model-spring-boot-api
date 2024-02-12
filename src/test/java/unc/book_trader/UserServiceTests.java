package unc.book_trader;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unc.book_trader.core.UserService;
import unc.book_trader.core.input.*;
import unc.book_trader.core.output.*;
import unc.book_trader.library.RandomString;

import java.util.Objects;

@Slf4j
@SpringBootTest
class UserServiceTests extends BaseTest {

    @Autowired
    UserService userService;

    @Test
    void test_create_user() throws Exception {
        var req1 = userService.createUserV1(CreateUserInputV1.builder()
                .username("kyle")
                .password("123123123123")
                .build());
        assert req1.outcome == CreateUserOutputV1.Outcome.OK;
        var req2 = userService.listUsersV1(
                ListUsersInputV1.builder().page(0).size(5).build());
        assert !req2.users.isEmpty();
    }

    @Test
    void test_fail_to_create_user() throws Exception {
        var req1 = userService.createUserV1(
                CreateUserInputV1.builder().username("kyle").password("123").build());
        assert req1.outcome == CreateUserOutputV1.Outcome.PASSWORD_TOO_SHORT;
        var req2 = userService.createUserV1(CreateUserInputV1.builder()
                .username("unique_username")
                .password("123456789101112")
                .build());
        assert req2.outcome == CreateUserOutputV1.Outcome.OK;
        var req3 = userService.createUserV1(CreateUserInputV1.builder()
                .username("unique_username")
                .password("123456789101112")
                .build());
        assert req3.outcome == CreateUserOutputV1.Outcome.USERNAME_ALREADY_TAKEN;
    }

    @Test
    void test_groups() throws Exception {
        String username = RandomString.ofLength(6);
        String password = RandomString.ofLength(12);
        String adminGroup = RandomString.ofLength(12);

        var r1 = userService.createUserV1(CreateUserInputV1.builder()
                .username(username)
                .password(password)
                .build());
        assert r1.outcome == CreateUserOutputV1.Outcome.OK;

        var r2 = userService.createGroupV1(CreateGroupInputV1.builder()
                .groupname(adminGroup)
                .build());
        assert r2.outcome == CreateGroupOutputV1.Outcome.OK;

        var r3 = userService.EnrollUserInGroupV1(EnrollUserInGroupInputV1.builder()
                .username(username)
                .groupname(adminGroup)
                .build());
        assert r3.outcome == EnrollUserInGroupOutputV1.Outcome.OK;

        var r4 = userService.LogInV1(LogInInputV1.builder()
                .username(username)
                .password(password)
                .sendSessionInResponseBody(true)
                .build());
        LogInOutputV1 logInOutputV1 = Objects.requireNonNull(r4.getBody());
        assert logInOutputV1.outcome == LogInOutputV1.Outcome.OK;

        var r5 = userService.GetUserV1(logInOutputV1.sessionId);
        assert r5.outcome == GetUserOutputV1.Outcome.OK &&
               r5.username.equals(username) &&
               r5.groups.contains(adminGroup);
    }
}
