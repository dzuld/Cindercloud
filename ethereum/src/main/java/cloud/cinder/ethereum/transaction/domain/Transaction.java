package cloud.cinder.ethereum.transaction.domain;

import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.common.utils.domain.PrettyAmount;
import cloud.cinder.ethereum.util.EthUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {


    @Id
    private String hash;
    private String blockHash;
    private BigInteger blockHeight;
    private BigInteger gasPrice;
    private BigInteger gas;
    private BigInteger transactionIndex;
    private BigInteger value;
    private String input;
    private BigInteger nonce;
    private String s;
    private String r;
    private Integer v;
    @Column(name = "from_address")
    private String fromAddress;
    @Column(name = "to_address")
    private String toAddress;
    private String creates;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.UNKNOWN;

    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockTimestamp;

    @Transient
    public SpecialAddress specialFrom;
    @Transient
    public SpecialAddress specialTo;

    public boolean isContractCreation() {
        return (StringUtils.isEmpty(toAddress) && !StringUtils.isEmpty(creates));
    }

    public PrettyAmount formattedValue() {
        return EthUtil.format(value);
    }

    public PrettyAmount formattedGasPrice() {
        return EthUtil.format(gasPrice);
    }

    public String prettyHash() {
        return hash.substring(0, 18) + "...";
    }

    public String prettyBlockHash() {
        return blockHash.substring(0, 18) + "...";
    }

    public String prettyFromAddress() {
        if (specialFrom != null) {
            return specialFrom.getName();
        } else {
            return fromAddress.substring(0, 18) + "...";
        }
    }

    public String prettyToAddress() {
        if (specialTo != null) {
            return specialTo.getName();
        } else {
            if (isContractCreation()) {
                return creates.substring(0, 18) + "... [contract creation]";
            }
            return toAddress.substring(0, 18) + "...";
        }
    }

    public String prettyBlockTimestamp() {
        final PrettyTime prettyTime = new PrettyTime(java.sql.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return prettyTime.format(blockTimestamp);
    }

    public Direction direction(final String address) {
        if (fromAddress.equals(address)) {
            return Direction.OUT;
        } else {
            return Direction.IN;
        }
    }

    public boolean hasInput() {
        return input != null && !input.equals("0x");
    }

    public String inputString() {
        try {
            return new String(Hex.decode(input.substring(2)));
        } catch (Exception extraDataString) {
            return "";
        }
    }

    public HistoricTransaction toHistoricTransaction() {
        return HistoricTransaction.builder()
                .blockHash(blockHash)
                .blockHeight(blockHeight)
                .blockTimestamp(blockTimestamp)
                .creates(creates)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .gas(gas)
                .hash(hash)
                .input(input)
                .nonce(nonce)
                .gasPrice(gasPrice)
                .value(value)
                .r(r)
                .s(s)
                .v(v)
                .transactionIndex(transactionIndex)
                .build();
    }

    public enum Direction {
        IN, OUT
    }
}

