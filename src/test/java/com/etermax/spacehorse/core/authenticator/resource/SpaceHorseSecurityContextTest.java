package com.etermax.spacehorse.core.authenticator.resource;

import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.authenticator.model.UserPrincipal;
import org.junit.Test;

import javax.ws.rs.core.SecurityContext;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpaceHorseSecurityContextTest {

    @Test
    public void testGetUserPrincipal() {
        String id = "id";
        Role role = Role.PLAYER;
        UserPrincipal userPrincipal = new UserPrincipal(id, role);
        SecurityContext securityContext = mock(SecurityContext.class);
        SpaceHorseSecurityContext spaceHorseSecurityContext = new SpaceHorseSecurityContext(userPrincipal, securityContext);

        Principal userPrincipalReturned = spaceHorseSecurityContext.getUserPrincipal();

        assertThat(userPrincipalReturned).isEqualTo(userPrincipal);
    }

    @Test
    public void testIsPlayerUserInPlayerRole() {
        String id = "id";
        Role role = Role.PLAYER;
        UserPrincipal userPrincipal = new UserPrincipal(id, role);
        SecurityContext securityContext = mock(SecurityContext.class);
        SpaceHorseSecurityContext spaceHorseSecurityContext = new SpaceHorseSecurityContext(userPrincipal, securityContext);

        boolean isUserInRole = spaceHorseSecurityContext.isUserInRole("PLAYER");
        assertThat(isUserInRole).isTrue();
    }

    @Test
    public void testTesterImpliesPlayer() {
        String id = "id";
        Role role = Role.TESTER;
        UserPrincipal userPrincipal = new UserPrincipal(id, role);
        SecurityContext securityContext = mock(SecurityContext.class);
        SpaceHorseSecurityContext spaceHorseSecurityContext = new SpaceHorseSecurityContext(userPrincipal, securityContext);

        boolean isUserInRole = spaceHorseSecurityContext.isUserInRole("PLAYER");
        assertThat(isUserInRole).isTrue();
    }

    @Test
    public void testAdminDoesNotImpliesTester() {
        String id = "id";
        Role role = Role.ADMIN;
        UserPrincipal userPrincipal = new UserPrincipal(id, role);
        SecurityContext securityContext = mock(SecurityContext.class);
        SpaceHorseSecurityContext spaceHorseSecurityContext = new SpaceHorseSecurityContext(userPrincipal, securityContext);

        boolean isUserInRole = spaceHorseSecurityContext.isUserInRole("TESTER");
        assertThat(isUserInRole).isFalse();
    }

    @Test
    public void testAdminDoesNotImpliesPlayer() {
        String id = "id";
        Role role = Role.ADMIN;
        UserPrincipal userPrincipal = new UserPrincipal(id, role);
        SecurityContext securityContext = mock(SecurityContext.class);
        SpaceHorseSecurityContext spaceHorseSecurityContext = new SpaceHorseSecurityContext(userPrincipal, securityContext);

        boolean isUserInRole = spaceHorseSecurityContext.isUserInRole("PLAYER");
        assertThat(isUserInRole).isFalse();
    }

    @Test
    public void testIsSecure() {
        String id = "id";
        Role role = Role.PLAYER;
        UserPrincipal userPrincipal = new UserPrincipal(id, role);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.isSecure()).thenReturn(true);
        SpaceHorseSecurityContext spaceHorseSecurityContext = new SpaceHorseSecurityContext(userPrincipal, securityContext);

        assertThat(spaceHorseSecurityContext.isSecure()).isTrue();
    }

    @Test
    public void testAuthenticationScheme() {
        String id = "id";
        Role role = Role.PLAYER;
        UserPrincipal userPrincipal = new UserPrincipal(id, role);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.isSecure()).thenReturn(true);
        SpaceHorseSecurityContext spaceHorseSecurityContext = new SpaceHorseSecurityContext(userPrincipal, securityContext);

        assertThat(spaceHorseSecurityContext.getAuthenticationScheme()).contains("CUSTOM_TOKEN");
    }
}
