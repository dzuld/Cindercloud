package cloud.cinder.web.token.service;

import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.web.token.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public List<Token> findAll() {
        return tokenRepository.findAll();
    }
}
