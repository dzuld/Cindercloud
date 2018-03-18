package cloud.cinder.cindercloud.token.service;

import cloud.cinder.cindercloud.token.domain.Token;
import cloud.cinder.cindercloud.token.dto.TokenTransferDto;
import cloud.cinder.cindercloud.token.repository.TokenRepository;
import cloud.cinder.cindercloud.token.repository.TokenTransferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private TokenRepository tokenRepository;
    private TokenTransferRepository tokenTransferRepository;

    public TokenService(final TokenRepository tokenRepository, final TokenTransferRepository tokenTransferRepository) {
        this.tokenRepository = tokenRepository;
        this.tokenTransferRepository = tokenTransferRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Token> findByAddress(final String address) {
        return tokenRepository.findByAddressLike(address);
    }

    @Transactional(readOnly = true)
    public List<Token> findAll() {
        return tokenRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Token> findAll(Pageable pageable) {
        return tokenRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return tokenRepository.count();
    }

    @Transactional(readOnly = true)
    public List<TokenTransferDto> findTransfersByFromOrTo(final String address) {
        return tokenTransferRepository.findByFromOrTo(address)
                .stream()
                .map(transfer -> {
                    final Optional<Token> tokenByAddress = findByAddress(transfer.getTokenAddress());
                    return TokenTransferDto.builder()
                            .blockHeight(transfer.getBlockHeight())
                            .blockTimestamp(transfer.getBlockTimestamp())
                            .from(transfer.getFromAddress())
                            .to(transfer.getToAddress())
                            .transactionHash(transfer.getTransactionHash())
                            .amount(transfer.getAmount())
                            .tokenAddress(transfer.getTokenAddress())
                            .decimals(tokenByAddress.map(Token::getDecimals).orElse(18))
                            .tokenSymbol(tokenByAddress.map(Token::getSymbol).orElse("UNKNOWN"))
                            .tokenName(tokenByAddress.map(Token::getName).orElse("Unknown"))
                            .build();
                }).collect(Collectors.toList());
    }
}
