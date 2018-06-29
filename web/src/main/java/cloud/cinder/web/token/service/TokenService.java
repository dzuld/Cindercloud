package cloud.cinder.web.token.service;

import cloud.cinder.common.queue.QueueSender;
import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.web.token.dto.TokenTransferDto;
import cloud.cinder.web.token.dto.UserTokenRequest;
import cloud.cinder.web.token.repository.TokenRepository;
import cloud.cinder.web.token.repository.TokenTransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenService {

    @Value("${cloud.cinder.queue.user-token-import}")
    private String userTokenImportQueue;
    @Autowired
    private QueueSender $;
    @Autowired
    private ObjectMapper objectMapper;

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
    public List<TokenTransferDto> findByToken(final String tokenHash) {
        return tokenTransferRepository.findByTokenAddress(tokenHash.toLowerCase(), new PageRequest(0, 20))
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

    @Transactional(readOnly = true)
    public List<TokenTransferDto> findTransfersByFromOrTo(final String address) {
        return tokenTransferRepository.findByFromOrTo(address.toLowerCase(), new PageRequest(0, 30))
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

    @Transactional(readOnly = true)
    public boolean hasTokenTransfersFromOrTo(final String address) {
        return tokenTransferRepository.existsByFromOrTo(address);
    }

    public void importAddress(final String address) {
        try {
            $.send(userTokenImportQueue, objectMapper.writeValueAsString(new UserTokenRequest(address)));
        } catch (final Exception ex) {
            log.debug("Unable to send to user token import queue", ex);
        }
    }
}
