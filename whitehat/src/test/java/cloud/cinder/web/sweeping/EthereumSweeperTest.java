package cloud.cinder.web.sweeping;

import cloud.cinder.whitehat.sweeping.EthereumSweeper;
import org.junit.Before;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class EthereumSweeperTest {

    private EthereumSweeper sweeper;

    @Before
    public void setUp() throws Exception {
        sweeper = new EthereumSweeper();
    }

    @org.junit.Test
    public void calculateNormalPriority() {
        BigInteger bigInteger = sweeper.calculatePriority(BigInteger.valueOf(10), BigInteger.valueOf(2));
        assertThat(bigInteger.intValue()).isEqualTo(1);
    }

    @org.junit.Test
    public void calcaulateHighPriority() {
        BigInteger bigInteger = sweeper.calculatePriority(BigInteger.valueOf(1000), BigInteger.valueOf(2));
        assertThat(bigInteger.intValue()).isEqualTo(20);
    }

    @org.junit.Test
    public void calculateSuperHighPriority() {
        BigInteger bigInteger = sweeper.calculatePriority(BigInteger.valueOf(250000), BigInteger.valueOf(20));
        assertThat(bigInteger.intValue()).isEqualTo(500);
    }
}