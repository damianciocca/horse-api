package com.etermax.spacehorse.core.authenticator.resource;

import com.etermax.spacehorse.core.authenticator.model.Credentials;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.authenticator.model.UserPrincipal;
import com.etermax.spacehorse.core.login.error.InvalidCredentialsException;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.User;
import io.dropwizard.auth.AuthenticationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAuthenticatorTest {
    String sessionToken = "sessionToken";
    String loginId = "loginId";
    UserAction userAction = mock(UserAction.class);
    User user = mock(User.class);

    @Before
    public void setup() {
        when(user.getSessionToken()).thenReturn(sessionToken);
        when(user.getUserId()).thenReturn(loginId);
        Role role = Role.PLAYER;
        when(user.getRole()).thenReturn(role);
        when(userAction.findByUserId(anyString())).thenReturn(user);
    }

    @Test
    public void testAuthenticate() {
        Credentials credentials = new Credentials(loginId, sessionToken);
        UserAuthenticator userAuthenticator = new UserAuthenticator(userAction);
        try {
            Optional<UserPrincipal> authenticate = userAuthenticator.authenticate(credentials);

            assertThat(authenticate.isPresent()).isTrue();
            assertThat(authenticate.get()).hasFieldOrPropertyWithValue("id", loginId)
                                            .hasFieldOrPropertyWithValue("role", Role.PLAYER);

        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAuthenticateWithInvalidCredentialsThrowsInvalidCredentialsException() {
        String invalidToken = "invalidToken";
        Credentials credentials = new Credentials(loginId, invalidToken);
        UserAuthenticator userAuthenticator = new UserAuthenticator(userAction);

        Throwable thrown = catchThrowable(() -> userAuthenticator.authenticate(credentials));
        assertThat(thrown).isInstanceOf(InvalidCredentialsException.class)
                            .hasMessage("Invalid Credentials Exception");
    }
}
