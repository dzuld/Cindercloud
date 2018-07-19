package cloud.cinder.web.address.repository;

import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.common.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpecialAddressRepository extends JpaRepository<SpecialAddress, Long> {


    @Query("select sa from SpecialAddress sa where address LIKE :address")
    Optional<SpecialAddress> findByAddress(@Param("address") final String address);

}
