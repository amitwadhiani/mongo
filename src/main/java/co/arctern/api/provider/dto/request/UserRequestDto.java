package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.constant.Gender;
import co.arctern.api.provider.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * user request body for user creation by Admin.
 */
@Data
@NoArgsConstructor
public class UserRequestDto {

    private Long userId;
    private String name;
    private String username;
    private String phone;
    private Integer age;
    private Date dateOfBirth;
    private String password;
    private String email;
    private Gender gender;
    private String profilePic;
    private Boolean isActive;
    private List<Long> roleIds;
    private List<Long> areaIds;
    private List<Long> offeringIds;
    private Long clusterId;

    public void toUserRequestDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
