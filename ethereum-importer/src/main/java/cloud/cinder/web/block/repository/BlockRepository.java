package cloud.cinder.web.block.repository;

import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.common.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, String> {

    @Query("select block from Block block where uncle = false and hash LIKE :hash")
    Optional<Block> findBlock(@Param("hash") final String hash);
}