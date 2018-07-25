package cloud.cinder.vechain.importer.block.continuous;

import cloud.cinder.vechain.block.domain.VechainBlock;
import cloud.cinder.vechain.importer.block.service.VechainBlockService;
import cloud.cinder.vechain.thorifyj.ThorifyjGateway;
import cloud.cinder.vechain.thorifyj.domain.ThorifyBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@EnableScheduling
public class BlockImporter {

    private ThorifyjGateway thorifyjGateway;
    private VechainBlockService vechainBlockService;

    public BlockImporter(final ThorifyjGateway thorifyjGateway,
                         final VechainBlockService vechainBlockService) {
        this.thorifyjGateway = thorifyjGateway;
        this.vechainBlockService = vechainBlockService;
    }

    @Value("${cloud.cinder.vechain.live-block-import:false}")
    private boolean autoBlockImport;

    @Scheduled(fixedRate = 5000)
    public void listenForBlocks() {
        if (autoBlockImport) {
            importNewestBlock();
        }
    }

    private void importNewestBlock() {
        Optional<ThorifyBlock> newBlock = getLatest();
        newBlock
                .filter(block -> !vechainBlockService.exists(block.getId()))
                .ifPresent(block -> {
                    vechainBlockService.save(VechainBlock.asVechainBlock(block));
                });
    }

    private Optional<ThorifyBlock> getLatest() {
        try {
            return Optional.ofNullable(thorifyjGateway.thorify().blocks().getLatest());
        } catch (final Exception ex) {
            return Optional.empty();
        }
    }
}
