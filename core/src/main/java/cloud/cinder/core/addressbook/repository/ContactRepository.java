package cloud.cinder.core.addressbook.repository;

import cloud.cinder.common.addressbook.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByOwner(final @Param("owner") String owner);

    boolean existsByOwnerAndAddress(final @Param("owner") String owner, final @Param("address") String address);

}
