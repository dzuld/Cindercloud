package cloud.cinder.ethereum.address.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("crowdsale")
public class SpecialCrowdsaleAddress extends SpecialAddress {

}
