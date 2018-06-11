package com.etermax.spacehorse.core.user.action;

import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserActionTest {

    @Test
    public void testFindByUserId() {
        UserRepository userRepository = mock(UserRepository.class);
        User userReturned = mock(User.class);
        when(userRepository.find(anyString())).thenReturn(userReturned);
        UserAction userAction = new UserAction(userRepository);
        String userId = "userId";
        User user = userAction.findByUserId(userId);

        assertThat(user).isEqualTo(userReturned);
    }

}
