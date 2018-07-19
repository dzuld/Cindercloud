package cloud.cinder.web.address.rest;

import cloud.cinder.web.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigInteger;

@RestController
@RequestMapping(value = "/rest/address/{address}")
public class AddressRestController {

    @Autowired
    private AddressService addressService;

    /*
     * Balance in weihr
     */
    @RequestMapping(value = "/balance")
    public DeferredResult<BigInteger> getBalance(@PathVariable("address") final String address) {
        final DeferredResult<BigInteger> balanceResult = new DeferredResult<>();
        addressService.getBalance(address).subscribe(balanceResult::setResult);
        return balanceResult;
    }

    /*
     * only defines the outgoing transactions
     */
    @RequestMapping(value = "/transactioncount")
    public DeferredResult<BigInteger> getTransactionCount(@PathVariable("address") final String address) {
        final DeferredResult<BigInteger> balanceResult = new DeferredResult<>();
        addressService.getTransactionCount(address).subscribe(balanceResult::setResult);
        return balanceResult;
    }
}
