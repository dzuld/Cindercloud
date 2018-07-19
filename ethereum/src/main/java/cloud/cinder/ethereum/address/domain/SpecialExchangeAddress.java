package cloud.cinder.ethereum.address.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("exchange")
public class SpecialExchangeAddress extends SpecialAddress {
}
