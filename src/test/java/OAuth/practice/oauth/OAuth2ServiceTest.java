package OAuth.practice.oauth;

import OAuth.practice.oauth.domain.User;
import OAuth.practice.oauth.dto.UserProfile;
import OAuth.practice.oauth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OAuth2Service oAuth2Service;

    private OAuth2UserRequest userRequest;
    private OAuth2User OAuth2User;
    private UserProfile userProfile;
    private User user;

    @BeforeEach
    void setUp() throws IllegalArgumentException{
        // 예제 데이터 설정
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("email", "john.doe@example.com");

        userProfile = new UserProfile();
        user = new User(1L,"John Doe", "john.doe@example.com", "google");

        OAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "name"
        );

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token", null, null);
        userRequest = new OAuth2UserRequest(null, accessToken);
    }

    @Test
    void loadUser_WhenUserExists_ShouldReturnUser() {
        // UserRepository가 userProfile 정보로 User를 찾거나 생성할 때 해당 User 객체를 반환하도록 설정
        when(userRepository.findUserByEmailAndProvider(userProfile.getEmail(), userProfile.getProvider()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // OAuth2UserRequest를 처리할 때 실제 OAuth2User 객체를 반환하도록 설정
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Act - OAuth2Service의 loadUser 메서드를 호출
        OAuth2User result = oAuth2Service.loadUser(userRequest);

        // Assert - 반환된 OAuth2User가 null이 아니고, 예상된 권한과 속성을 가지고 있는지 검증
        assertNotNull(result);
        assertEquals("John Doe", result.getAttribute("name"));
        assertEquals("john.doe@example.com", result.getAttribute("email"));
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

}