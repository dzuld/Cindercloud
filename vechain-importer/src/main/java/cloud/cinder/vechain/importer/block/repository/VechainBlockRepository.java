package cloud.cinder.vechain.importer.block.repository;

import cloud.cinder.vechain.block.domain.VechainBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VechainBlockRepository extends JpaRepository<VechainBlock, String> {

}
