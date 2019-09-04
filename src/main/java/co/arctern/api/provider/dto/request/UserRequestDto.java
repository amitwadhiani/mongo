package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.domain.User;
import com.amazonaws.services.polly.model.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * user request body for user creation by Admin.
 */
@Data
@NoArgsConstructor
public class UserRequestDto {

    private String name;
    private String username;
    private String phone;
    private Integer age;
    private String password;
    private String email;
    private Gender gender;
    private String profilePic;
    private List<Long> roleIds;
    private List<Long> areaIds;

    public void toUserRequestDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
