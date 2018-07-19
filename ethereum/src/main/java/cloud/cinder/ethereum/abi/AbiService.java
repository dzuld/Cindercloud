package cloud.cinder.ethereum.abi;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.StringWriter;

@Component
@Slf4j
public class AbiService {

    public final String getERC20Abi() {
        try {
            final Resource resource = new ClassPathResource("abi/erc20.json");
            final InputStream resourceInputStream = resource.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(resourceInputStream, writer, "UTF-8");
            return writer.toString();
        } catch (final Exception ex) {
            log.debug("Unable to fetch ERC20 ABI");
            return "";
        }
    }
}
