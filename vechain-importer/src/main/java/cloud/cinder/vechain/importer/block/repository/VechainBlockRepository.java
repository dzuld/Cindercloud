package cloud.cinder.vechain.importer.block.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.vechain.block.domain.VechainBlock;

import java.util.Optional;

public interface VechainBlockRepository extends JpaRepository<VechainBlock, String> {

}
