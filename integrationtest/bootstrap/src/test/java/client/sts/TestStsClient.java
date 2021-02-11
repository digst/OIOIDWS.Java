package client.sts;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.Bus;
import org.w3c.dom.Element;

import java.nio.charset.Charset;

/**
 * Test STS client able to use bootstrap token provided by test runner.
 */
public class TestStsClient extends DigstSTSClient {

    //TODO change to non-static by using applicationContext.getbean...
    private static String bootStrapToken;

    public TestStsClient(Bus b) {
        super(b);
    }

    @Override
    public Element getActAsToken() throws Exception {
        byte[] rawAssertion = Base64.decodeBase64(bootStrapToken);
        String actAsToken = new String(rawAssertion, Charset.forName("UTF-8"));

        return getDelegationSecurityToken(actAsToken);
    }

    public static void setBootStrapToken(String bootStrapToken) {
        TestStsClient.bootStrapToken = bootStrapToken;
    }
}
