package OAuth.practice.oauth.dto;

import OAuth.practice.oauth.domain.User;
import lombok.Getter;

@Getter
public class UserProfile {
    private String username;
    private String email;
    private String provider;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .provider(this.provider)
                .build();
    }
}
