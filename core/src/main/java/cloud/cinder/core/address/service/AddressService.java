package cloud.cinder.core.address.service;

import cloud.cinder.core.address.repository.SpecialAddressRepository;
import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Web3jGateway web3j;
    @Autowired
    private SpecialAddressRepository specialAddressRepository;


    public Flowable<String> getCode(final String hash) {
        return web3j.web3j().ethGetCode(hash, DefaultBlockParameterName.LATEST).flowable().map(EthGetCode::getCode);
    }

    public Flowable<BigInteger> getBalance(final String address) {
        return web3j.web3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).flowable().map(
                EthGetBalance::getBalance);
    }

    public Flowable<BigInteger> getTransactionCount(final String address) {
        return web3j.web3j().ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).flowable()
                .map(EthGetTransactionCount::getTransactionCount);
    }

    public Optional<SpecialAddress> findByAddress(final String address) {
        return specialAddressRepository.findByAddress(address);
    }

    public boolean isContract(final String address) {
        try {
            final String code = web3j.web3j().ethGetCode(address, DefaultBlockParameterName.LATEST).flowable().map(EthGetCode::getCode)
                    .blockingFirst();
            return code != null && code.length() > 2;
        } catch (final Exception ex) {
            return false;
        }
    }
}
