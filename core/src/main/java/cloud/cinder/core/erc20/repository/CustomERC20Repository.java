package cloud.cinder.core.erc20.repository;

import cloud.cinder.ethereum.erc20.domain.CustomERC20;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomERC20Repository extends JpaRepository<CustomERC20, Long> {

    @Query("select erc20 from CustomERC20 erc20 where lower(addedBy) LIKE :address")
    List<CustomERC20> findAllByAddedBy(@Param("address") final String address);

}
