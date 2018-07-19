package cloud.cinder.ethereum.abi;

import cloud.cinder.ethereum.abi.domain.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


public class AbiDecoderTest {

    private AbiDecoder decoder = new AbiDecoder();

    @Test
    public void example1() throws Exception {
        final String example = getExample("TokenWhitelistPrecondition.json");
        AbiContract decode = decoder.decode(example);

        assertThat(decode.getElements()).hasSize(13);

        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractFunction).count()).isEqualTo(9);
        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractConstructor).count()).isEqualTo(1);
        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractFallback).count()).isEqualTo(1);
        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractEvent).count()).isEqualTo(2);
    }

    @Test
    public void example2() throws Exception {
        final String example = getExample("FundRequestToken.json");
        AbiContract decode = decoder.decode(example);

        assertThat(decode.getElements()).hasSize(33);

        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractFunction).count()).isEqualTo(27);
        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractConstructor).count()).isEqualTo(1);
        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractFallback).count()).isEqualTo(1);
        assertThat(decode.getElements().stream().filter(x -> x instanceof AbiContractEvent).count()).isEqualTo(4);
    }

    private String getExample(final String example) throws Exception {
        final URL url = getFile("abi-examples/" + example);
        return readFile(url.getPath());
    }

    private static URL getFile(final String file) {
        return Thread.currentThread().getContextClassLoader().getResource(file);
    }

    private static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
}