package cloud.cinder.core.etherscan;

import cloud.cinder.core.etherscan.dto.EtherscanResponse;
import feign.Param;
import feign.RequestLine;

public interface EtherscanClient {

    @RequestLine("GET /?module=account&action=txlist&address={address}&startblock=0&endblock=99999999&sort=asc")
    EtherscanResponse transactions(@Param("address") final String address);

}
