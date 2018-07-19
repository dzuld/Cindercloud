package cloud.cinder.ethereum.parity.domain;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;

import java.math.BigInteger;

import static java.util.Collections.singletonList;

public class ParitySignatureRegistry extends Contract {

    public ParitySignatureRegistry(final String contractAddress,
                                   final Web3j web3j) {
        super(null, contractAddress, web3j, Credentials.create(ECKeyPair.create(BigInteger.ZERO)), BigInteger.ZERO, BigInteger.ZERO);
    }

    public String entries(final byte[] bytes) {
        try {
            final Function function = new Function("entries",
                    singletonList(new Bytes4(bytes)),
                    singletonList(new TypeReference<Utf8String>() {
                    }));
            return executeRemoteCallSingleValueReturn(function, String.class).send();
        } catch (final Exception ex) {
            return "";
        }
    }
}
