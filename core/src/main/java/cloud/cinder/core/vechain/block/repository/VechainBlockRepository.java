package cloud.cinder.core.vechain.block.repository;

import cloud.cinder.vechain.block.domain.VechainBlock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VechainBlockRepository extends JpaRepository<VechainBlock, String> {

    @Query("select block from VechainBlock block order by number desc")
    List<VechainBlock> findAllBlocksOrderByHeightDescAsList(Pageable pageRequest);

    @Query("select block from VechainBlock block order by number desc")
    Slice<VechainBlock> findAllBlocksOrderByHeightDesc(Pageable pageRequest);
}
