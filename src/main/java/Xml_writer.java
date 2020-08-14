// Import JAXB dependencies
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.File;
import java.util.ArrayList;


public class Xml_writer<T> {


    public <E> void testJAXB(T object_token) throws JAXBException {

        String XML_OUTPUT = "src/main/resources/"+object_token.getClass().getName()+".xml";

        // create JAXB context and marsheller
        var contex = JAXBContext.newInstance(object_token.getClass());
        var m = contex.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to System.out
        m.marshal(object_token, System.out);

        //Write to File
        m.marshal(object_token, new File(XML_OUTPUT));

    }

}
