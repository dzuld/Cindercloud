package cloud.cinder.web.addressbook.service;

import cloud.cinder.common.addressbook.domain.Contact;
import cloud.cinder.web.addressbook.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.web3j.crypto.WalletUtils.isValidAddress;

@Service
public class AddressBookService {

    private final ContactRepository contactRepository;

    public AddressBookService(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional(readOnly = true)
    public List<Contact> getContacts(final String owner) {
        return contactRepository.findAllByOwner(owner);
    }

    @Transactional
    public void addContact(final String owner, final String address, final String nickname) {
        if (isValidAddress(address)) {
            if (contactRepository.existsByOwnerAndAddress(owner.toLowerCase(), address.toLowerCase())) {
                throw new IllegalArgumentException("The provided address is already a contact of yours.");
            }
            contactRepository.save(
                    Contact.builder()
                            .address(address.toLowerCase())
                            .owner(owner.toLowerCase())
                            .nickname(nickname)
                            .build()
            );
        } else {
            throw new IllegalArgumentException("The provided address was not valid.");
        }
    }

    @Transactional
    public void updateNickname(final String owner, final String address, final String nickname) {
        contactRepository.findByOwnerAndAddress(owner, address)
                .ifPresent(contact -> {
                    contact.setNickname(nickname);
                    contactRepository.save(contact);
                });
    }

    @Transactional
    public void deleteContact(final String owner, final Long id) {
        Optional<Contact> foundOne = contactRepository.findOne(id);
        foundOne
                .filter(contact -> contact.getOwner().equalsIgnoreCase(owner))
                .ifPresent(contact -> {
                    contactRepository.delete(id);
                });
    }
}
