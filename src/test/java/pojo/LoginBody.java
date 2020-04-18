package pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginBody {

    private String email;
    private String password;

}
