package cloud.cinder.web.token.service;

import cloud.cinder.ethereum.token.domain.TokenTransfer;
import cloud.cinder.web.token.listener.model.TokenEvent;
import cloud.cinder.web.token.listener.model.TokenEventType;
import cloud.cinder.web.token.repository.TokenTransferRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TokenTransferService {

    private final TokenTransferRepository tokenTransferRepository;

    public TokenTransferService(final TokenTransferRepository tokenTransferRepository) {
        this.tokenTransferRepository = tokenTransferRepository;
    }

    @Transactional
    public void saveTokenTransfer(final TokenTransfer tokenTransfer) {
        if (!tokenTransferRepository.findByTransactionHashAndLogIndex(tokenTransfer.getTransactionHash(), tokenTransfer.getLogIndex()).isPresent()) {
            tokenTransferRepository.save(tokenTransfer);
        }
    }

    public Optional<TokenEvent> getEventParameters(
            Event event, Log log) {
        final Optional<TokenEventType> eventType = getEventType(event);
        if (!eventType.isPresent()) {
            return Optional.empty();
        }
        final List<String> topics = log.getTopics();
        final String encodedEventSignature = EventEncoder.encode(event);
        if (!topics.get(0).equals(encodedEventSignature)) {
            return Optional.empty();
        }

        final List<Type> indexedValues = new ArrayList<>();
        final List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
                log.getData(), event.getNonIndexedParameters());

        final List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return Optional.of(new TokenEvent(eventType.get(), new EventValues(indexedValues, nonIndexedValues)));
    }

    public Optional<TokenEventType> getEventType(final Event event) {
        if (event != null && StringUtils.isNotBlank(event.getName())) {
            try {
                return Optional.of(TokenEventType.valueOf(event.getName().toUpperCase()));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
