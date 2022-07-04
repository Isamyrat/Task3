package root.developer.security.service;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.developer.security.jwt.JwtProvider;
import root.developer.security.model.JwtRequest;
import root.developer.security.model.JwtResponse;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull JwtRequest authRequest) {

        final String accessToken = jwtProvider.generateAccessToken(authRequest);
        return new JwtResponse(accessToken);
    }
}
