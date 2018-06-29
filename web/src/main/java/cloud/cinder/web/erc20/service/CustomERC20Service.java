package cloud.cinder.web.erc20.service;

import cloud.cinder.ethereum.erc20.domain.CustomERC20;
import cloud.cinder.ethereum.token.domain.HumanStandardToken;
import cloud.cinder.web.erc20.repository.CustomERC20Repository;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomERC20Service {

    private final Web3jGateway web3j;
    private final CustomERC20Repository customERC20Repository;

    public CustomERC20Service(final Web3jGateway web3j,
                              final CustomERC20Repository customERC20Repository) {
        this.web3j = web3j;
        this.customERC20Repository = customERC20Repository;
    }

    @Transactional(readOnly = true)
    public List<CustomERC20> findAll(final String address) {
        return customERC20Repository.findAllByAddedBy(address);
    }

    @Transactional
    public boolean add(final String address, final String addedBy) {
        Optional<CustomERC20> recognize = recognize(address, addedBy);
        if (recognize.isPresent()) {
            customERC20Repository.save(recognize.get());
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void delete(final Long id, final String addedBy) {
        customERC20Repository.findOne(id).filter(x -> x.getAddedBy().toLowerCase().equalsIgnoreCase(addedBy))
                .ifPresent(customERC20Repository::delete);
    }

    private Optional<CustomERC20> recognize(final String address, final String addedBy) {
        try {
            final HumanStandardToken token = HumanStandardToken.load(address, web3j.web3j());
            return Optional.of(CustomERC20.builder()
                    .addedBy(addedBy)
                    .address(address)
                    .decimals(token.decimals().send().intValue())
                    .symbol(token.symbol().send())
                    .name(token.name().send())
                    .build());
        } catch (final Exception ex) {
            log.debug("Unable to recognize {} as token", address);
            return Optional.empty();
        }
    }
}
