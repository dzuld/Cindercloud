package cloud.cinder.web.block.repository;

import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.common.infrastructure.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BlockRepository extends JpaRepository<Block, String> {

    @Query("select block from Block block where uncle = false order by height desc")
    Slice<Block> findAllBlocksOrderByHeightDescAsPage(Pageable pageable);

    @Query("select block from Block block where uncle = false order by height desc")
    List<Block> findAllBlocksOrderByHeightDescAsList(Pageable pageable);

    @Query("select block from Block block where uncle = true order by height desc")
    Slice<Block> findAllUnclesOrderByHeightDesc(Pageable pageable);

    @Query("select block from Block block where (hash like %:searchKey% or height LIKE %:searchKey%) and minedBy LIKE %:minedBy% and uncle = false order by height desc")
    Slice<Block> searchBlocks(@Param("searchKey") final String searchKey, @Param("minedBy") final String minedBy, final Pageable pageable);

    @Query("select block from Block block where (hash like %:searchKey% or height LIKE %:searchKey%) and uncle = true order by height desc")
    Slice<Block> searchUncles(@Param("searchKey") final String searchKey, final Pageable pageable);

    @Query("select block from Block block where timestampDateTime >= :since and uncle = false")
    Stream<Block> findAllBlocksSince(@Param("since") final Date date);

    @Query("select block from Block block where uncle = true and hash = :hash")
    Optional<Block> findUncle(@Param("hash") final String hash);

    @Query("select block from Block block where uncle = false and hash LIKE :hash")
    Optional<Block> findBlock(@Param("hash") final String hash);

    @Query("select block from Block block where minedBy LIKE :address")
    Slice<Block> findBlocksAndUnclesByMiner(@Param("address") final String address, final Pageable pageable);
}
