package service.bpp;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class BasicPrivilegeProfileDeserializerTest {

    @Test
    public void deserialize_returnsExpected_whenBppVersion101() throws JAXBException {
        // Arrange
        String privilegeListXml = "<?xml version='1.0' encoding='UTF-8'?><bpp:PrivilegeList xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:bpp='http://itst.dk/oiosaml/basic_privilege_profile'><PrivilegeGroup Scope='urn:dk:gov:saml:CvrNumberIdentifier:34051178'><Privilege>http://bpp101.dk</Privilege></PrivilegeGroup></bpp:PrivilegeList>";
        String expectedScope = "urn:dk:gov:saml:CvrNumberIdentifier:34051178";
        String expectedPrivilege = "http://bpp101.dk";

        // Act
        PrivilegeListType list = BasicPrivilegeProfileDeserializer.deserializeBase64EncodedPrivilegeList(
                Base64.getEncoder().encodeToString(privilegeListXml.getBytes(StandardCharsets.UTF_8)));

        // Assert
        assertEquals(list.getPrivilegeGroup().size(), 1);
        assertEquals(list.getPrivilegeGroup().get(0).getPrivilege().size(), 1);
        assertEquals(list.getPrivilegeGroup().get(0).getPrivilege().get(0), expectedPrivilege);
        assertEquals(list.getPrivilegeGroup().get(0).getScope(), expectedScope);
    }

    @Test
    public void deserialize_returnsExpected_whenBppVersion12() throws JAXBException {
        // Arrange
        String privilegeListXml = "<?xml version='1.0' encoding='UTF-8'?><bpp:PrivilegeList xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:bpp='http://digst.dk/oiosaml/basic_privilege_profile'><PrivilegeGroup Scope='urn:dk:gov:saml:CvrNumberIdentifier:34051178'><Privilege>http://bpp12.dk</Privilege></PrivilegeGroup></bpp:PrivilegeList>";
        String expectedScope = "urn:dk:gov:saml:CvrNumberIdentifier:34051178";
        String expectedPrivilege = "http://bpp12.dk";

        // Act
        PrivilegeListType list = BasicPrivilegeProfileDeserializer.deserializeBase64EncodedPrivilegeList(
                Base64.getEncoder().encodeToString(privilegeListXml.getBytes(StandardCharsets.UTF_8)));

        // Assert
        assertEquals(list.getPrivilegeGroup().size(), 1);
        assertEquals(list.getPrivilegeGroup().get(0).getPrivilege().size(), 1);
        assertEquals(list.getPrivilegeGroup().get(0).getPrivilege().get(0), expectedPrivilege);
        assertEquals(list.getPrivilegeGroup().get(0).getScope(), expectedScope);
    }
}