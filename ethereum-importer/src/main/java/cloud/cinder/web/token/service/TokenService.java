package cloud.cinder.web.token.service;


import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.web.token.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(final TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional(readOnly = true)
    public List<Token> findAll() {
        return tokenRepository.findAll();
    }
}
