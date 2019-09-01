package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * user request dto for user entity.
 */
@Data
@NoArgsConstructor
public class UserRequestDto {

    private String name;
    private String username;
    private String phone;
    private String password;
    private String email;
    private String profilePic;

    public void toUserRequestDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
