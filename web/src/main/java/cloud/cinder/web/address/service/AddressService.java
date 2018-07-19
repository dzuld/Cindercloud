package cloud.cinder.web.address.service;

import cloud.cinder.web.address.repository.SpecialAddressRepository;
import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import rx.Observable;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class AddressService {

    private Web3jGateway web3j;
    private SpecialAddressRepository specialAddressRepository;

    public AddressService(final Web3jGateway web3j, final SpecialAddressRepository specialAddressRepository) {
        this.web3j = web3j;
        this.specialAddressRepository = specialAddressRepository;
    }

    public Observable<String> getCode(final String hash) {
        return web3j.web3j().ethGetCode(hash, DefaultBlockParameterName.LATEST).observable().map(EthGetCode::getCode);
    }

    public Observable<BigInteger> getBalance(final String address) {
        return web3j.web3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).observable().map(
                EthGetBalance::getBalance);
    }

    public Observable<BigInteger> getTransactionCount(final String address) {
        return web3j.web3j().ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).observable()
                .map(EthGetTransactionCount::getTransactionCount);
    }

    public Optional<SpecialAddress> findByAddress(final String address) {
        return specialAddressRepository.findByAddress(address);
    }

    public boolean isContract(final String address) {
        try {
            final String code = web3j.web3j().ethGetCode(address, DefaultBlockParameterName.LATEST).observable().map(EthGetCode::getCode)
                    .toBlocking().first();
            return code != null && code.length() > 2;
        } catch (final Exception ex) {
            return false;
        }
    }
}
