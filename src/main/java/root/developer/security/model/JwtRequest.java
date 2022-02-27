package root.developer.security.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class JwtRequest {

    private Set<Role> roles;
    private String issuer;
}
