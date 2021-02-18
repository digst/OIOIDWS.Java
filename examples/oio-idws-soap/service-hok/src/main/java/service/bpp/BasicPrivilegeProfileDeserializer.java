package service.bpp;

import org.apache.commons.codec.binary.Base64;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;

public class BasicPrivilegeProfileDeserializer {
    public static PrivilegeListType deserializeBase64EncodedPrivilegeList(String privilegeListBase64) throws JAXBException {
        byte[] privilegeBytes = Base64.decodeBase64(privilegeListBase64);

        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<PrivilegeListType> privilegeList = (JAXBElement<PrivilegeListType>) unmarshaller.unmarshal(new ByteArrayInputStream(privilegeBytes));

        return privilegeList.getValue();
    }
}
