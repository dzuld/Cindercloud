package cloud.cinder.ethereum.abi;

import cloud.cinder.ethereum.abi.domain.AbiContract;
import cloud.cinder.ethereum.abi.domain.AbiContractElement;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbiDecoder {

    private ObjectMapper mapper;

    public AbiDecoder() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public AbiContract decode(final String json) {
        try {
            return new AbiContract(mapper.readValue(json, new TypeReference<List<AbiContractElement>>() {
            }));
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Unable to decode json");
        }
    }
}
