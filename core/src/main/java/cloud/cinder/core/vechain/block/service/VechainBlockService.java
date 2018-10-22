package cloud.cinder.core.vechain.block.service;

import cloud.cinder.core.vechain.block.repository.VechainBlockRepository;
import cloud.cinder.vechain.block.domain.VechainBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
public class VechainBlockService {

    private VechainBlockRepository blockRepository;

    public VechainBlockService(final VechainBlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @Transactional(readOnly = true)
    public Slice<VechainBlock> getLastBlocks(final Pageable pageable) {
        return blockRepository.findAllBlocksOrderByHeightDesc(pageable);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<VechainBlock> getBlock(final String hash) {
        return blockRepository.findById(hash);
    }
}
