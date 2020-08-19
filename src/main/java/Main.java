import DeadClass_Interface.Book;
import DeadClass_Interface.BookStore;
import File_Writer.Writer_Demo;
import File_Writer.Xml_writer;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws JAXBException {

        // Working portion
        /*
        String source = "D:\\work\\src";
        List<String> paths = new ArrayList<>();
        Reader reader = new Reader();

        //paths = reader.readPath(source);
        reader.readPath(source);
        reader.readFile(); */

        // Testing portion
        Writer_Demo demo = new Writer_Demo();
        demo.write();

    }

    /* Next Step:
    *   - Create method for storing data in XML file.
    *   - Create method/class for storing classes name ( or create class by deadcode types ).
    *   - Search for the name in class to detect dead class/interface.
    *       -> Check that if no. when counting name in file is more than 1 or not. If count is 1 then dead,else not.
    * */


}