package cloud.cinder.web.block.rest;

import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.web.block.service.BlockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/block")
@Slf4j
public class BlockRestController {

    @Autowired
    private BlockService blockService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Block> getPagedBlocks(Pageable pageable) {
        return blockService.getLastBlocks(pageable).getContent();
    }

    @RequestMapping(value = "/{hash}")
    public DeferredResult<Block> getBlock(@PathVariable("hash") final String hash) {
        final DeferredResult<Block> result = new DeferredResult<>();
        blockService.getBlock(hash).subscribe(result::setResult, e -> {
            log.debug("Unable to fetch block: {}", hash);
        });
        return result;
    }

    @RequestMapping(value = "/last/number")
    public final Long getLastBlock() {
        return blockService.getLastBlock().toBlocking().first().getBlockNumber().longValue();
    }
}
