package cloud.cinder.vechain.importer.block.service;

import cloud.cinder.vechain.block.domain.VechainBlock;
import cloud.cinder.vechain.importer.block.repository.VechainBlockRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class VechainBlockService {

    private VechainBlockRepository vechainBlockRepository;

    public VechainBlockService(final VechainBlockRepository vechainBlockRepository) {
        this.vechainBlockRepository = vechainBlockRepository;
    }


    @Transactional(readOnly = true)
    public boolean exists(final String id) {
        return vechainBlockRepository.existsById(id);
    }

    @Transactional
    public void save(final VechainBlock vechainBlock) {
        vechainBlockRepository.save(vechainBlock);
    }

}
