package cloud.cinder.ethereum.util;

import cloud.cinder.common.utils.domain.PrettyAmount;
import org.web3j.utils.Convert;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class EthUtil {

    public static double asEth(final BigInteger weiBalance) {
        return new BigDecimal(weiBalance).divide(BigDecimal.valueOf(Math.pow(10, 18)), 8, RoundingMode.HALF_DOWN).doubleValue();
    }

    public static PrettyAmount format(final BigInteger value) {
        if (value.equals(BigInteger.ZERO)) {
            return new PrettyAmount("0", "wei");
        } else if (isSmallerThan(value, Convert.toWei("0.01", Convert.Unit.ETHER).toBigInteger())) {
            if (isSmallerThan(value, Convert.toWei("0.01", Convert.Unit.FINNEY).toBigInteger())) {
                if (isSmallerThan(value, Convert.toWei("0.01", Convert.Unit.GWEI).toBigInteger())) {
                    return new PrettyAmount(value.toString(), "wei");
                } else {
                    return new PrettyAmount(Convert.fromWei(new BigDecimal(value), Convert.Unit.GWEI).toPlainString(), "gwei");
                }
            } else {
                return new PrettyAmount(Convert.fromWei(new BigDecimal(value), Convert.Unit.FINNEY).toPlainString(), "finney");
            }
        } else {
            return new PrettyAmount(Convert.fromWei(new BigDecimal(value), Convert.Unit.ETHER).toPlainString(), "ether");
        }
    }

    public static String prettifyAddress(final @Nonnull String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }

    private static boolean isSmallerThan(final BigInteger aBigInt, final BigInteger val) {
        return aBigInt.compareTo(val) < 0;
    }
}
