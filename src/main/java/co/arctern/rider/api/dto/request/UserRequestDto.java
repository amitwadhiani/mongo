package co.arctern.rider.api.dto.request;

import co.arctern.rider.api.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

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
