package cloud.cinder.vechain.thorifyj;

import cloud.cinder.vechain.thorifyj.domain.ThorifyBlock;
import cloud.cinder.vechain.thorifyj.repository.ThorifyjBlockRepository;
import org.springframework.stereotype.Component;

@Component
public class ThorifyBlockService {

    private ThorifyjBlockRepository thorifyjBlockRepository;

    public ThorifyBlockService(final ThorifyjBlockRepository thorifyjBlockRepository) {
        this.thorifyjBlockRepository = thorifyjBlockRepository;
    }

    public ThorifyBlock get(final Long number) {
        return thorifyjBlockRepository.getBlock(number.toString());
    }

    public ThorifyBlock get(final String id) {
        return thorifyjBlockRepository.getBlock(id);
    }

    public ThorifyBlock getLatest() {
        return thorifyjBlockRepository.getBlock("best");
    }
}
