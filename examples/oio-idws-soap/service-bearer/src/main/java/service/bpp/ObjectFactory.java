package service.bpp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private final static String Bpp101Namespace = "http://itst.dk/oiosaml/basic_privilege_profile";
    private final static String Bpp12Namespace = "http://digst.dk/oiosaml/basic_privilege_profile";

    public ObjectFactory() { }

    @XmlElementDecl(namespace = Bpp101Namespace, name = "PrivilegeList")
    public JAXBElement<PrivilegeListType> createBpp101PrivilegeList(PrivilegeListType value) {
        return new JAXBElement<PrivilegeListType>(new QName(Bpp101Namespace), PrivilegeListType.class, value);
    }

    @XmlElementDecl(namespace = Bpp12Namespace, name = "PrivilegeList")
    public JAXBElement<PrivilegeListType> createBpp12PrivilegeList(PrivilegeListType value) {
        return new JAXBElement<PrivilegeListType>(new QName(Bpp12Namespace), PrivilegeListType.class, value);
    }
}
