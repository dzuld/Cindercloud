package cloud.cinder.web.vechain.block.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.vechain.block.domain.VechainBlock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rx.Single;

import java.util.List;
import java.util.Optional;

public interface VechainBlockRepository extends JpaRepository<VechainBlock, String> {

    @Query("select block from VechainBlock block order by number desc")
    List<VechainBlock> findAllBlocksOrderByHeightDescAsList(Pageable pageRequest);

    @Query("select block from VechainBlock block order by number desc")
    Slice<VechainBlock> findAllBlocksOrderByHeightDesc(Pageable pageRequest);
}
