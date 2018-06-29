package cloud.cinder.web.whitehat.ethernodes;

import cloud.cinder.ethereum.util.EthUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EthernodeTester {

    final ObjectMapper objectMapper = new ObjectMapper();

    private OkHttpClient httpClient;
    private Web3j cindercloud;

    @Before
    public void setUp() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        httpClient = builder.build();
        HttpService web3jService = new HttpService("https://parity.cinder.cloud",
                httpClient, false);
        cindercloud = Web3j.build(web3jService, 5L, new ScheduledThreadPoolExecutor(5));
    }

    @Test
    public void testEthernodes() throws Exception {
        Response execute = httpClient.newCall(new Request.Builder().url("https://www.ethernodes.org/network/1/data?draw=2&columns%5B0%5D%5Bdata%5D=id&columns%5B0%5D%5Bname%5D=&columns%5B0%5D%5Bsearchable%5D=true&columns%5B0%5D%5Borderable%5D=true&columns%5B0%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B0%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B1%5D%5Bdata%5D=host&columns%5B1%5D%5Bname%5D=&columns%5B1%5D%5Bsearchable%5D=true&columns%5B1%5D%5Borderable%5D=true&columns%5B1%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B1%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B2%5D%5Bdata%5D=port&columns%5B2%5D%5Bname%5D=&columns%5B2%5D%5Bsearchable%5D=true&columns%5B2%5D%5Borderable%5D=true&columns%5B2%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B2%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B3%5D%5Bdata%5D=country&columns%5B3%5D%5Bname%5D=&columns%5B3%5D%5Bsearchable%5D=true&columns%5B3%5D%5Borderable%5D=true&columns%5B3%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B3%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B4%5D%5Bdata%5D=clientId&columns%5B4%5D%5Bname%5D=&columns%5B4%5D%5Bsearchable%5D=true&columns%5B4%5D%5Borderable%5D=true&columns%5B4%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B4%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B5%5D%5Bdata%5D=client&columns%5B5%5D%5Bname%5D=&columns%5B5%5D%5Bsearchable%5D=true&columns%5B5%5D%5Borderable%5D=true&columns%5B5%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B5%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B6%5D%5Bdata%5D=clientVersion&columns%5B6%5D%5Bname%5D=&columns%5B6%5D%5Bsearchable%5D=true&columns%5B6%5D%5Borderable%5D=true&columns%5B6%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B6%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B7%5D%5Bdata%5D=os&columns%5B7%5D%5Bname%5D=&columns%5B7%5D%5Bsearchable%5D=true&columns%5B7%5D%5Borderable%5D=true&columns%5B7%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B7%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B8%5D%5Bdata%5D=lastUpdate&columns%5B8%5D%5Bname%5D=&columns%5B8%5D%5Bsearchable%5D=true&columns%5B8%5D%5Borderable%5D=true&columns%5B8%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B8%5D%5Bsearch%5D%5Bregex%5D=false&order%5B0%5D%5Bcolumn%5D=0&order%5B0%5D%5Bdir%5D=asc&start=0&length=100000&search%5Bvalue%5D=&search%5Bregex%5D=false").build()).execute();
        final EthernodeResponses $ = objectMapper.readValue(execute.body().string(), EthernodeResponses.class);
        $.getData()
                .forEach(this::check);
    }

    private void check(final Node node) {
        try {
            final String port;
            if (node.getClient().toLowerCase().contains("python")) {
                port = "4000";
            } else {
                port = "8545";
            }
            checkUrl("http://", node, port);
            checkUrl("https://", node, port);
        } catch (final Exception ex) {
            //System.out.println(ex.getMessage());
        }
    }

    private void checkUrl(final String protocol, final Node node, final String port) throws IOException {
        final String url = protocol + node.getHost() + ":" + port;
        httpClient.newCall(new Request.Builder().url(url).build()).execute();
        checkFunds(url);
    }

    private void checkFunds(final String url) {
        HttpService web3jService = new HttpService(url,
                httpClient, false);
        Web3j web3j = Web3j.build(web3jService, 5L, new ScheduledThreadPoolExecutor(5));
        try {
            EthAccounts send = web3j.ethAccounts().send();
            if (!send.hasError()) {
                System.out.println(String.format("%s has the following accounts", url));
                send.getAccounts().forEach(this::showInfo);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showInfo(final String account) {
        try {
            EthGetBalance balance = cindercloud.ethGetBalance(account, DefaultBlockParameterName.LATEST).send();
            EthGetTransactionCount txCount = cindercloud.ethGetTransactionCount(account, DefaultBlockParameterName.LATEST).send();
            String result = String.format("%s has a balance of %s and a txCount of %s", account, EthUtil.format(balance.getBalance()), txCount.getTransactionCount().longValue());
            System.out.println(result);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

    }
}
