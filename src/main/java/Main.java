import DeadClass_Interface.Book;
import DeadClass_Interface.BookStore;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;


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

        var bookList = new ArrayList<Book>();

        // create books
        var book1 = new Book();
        book1.setIsbn("978-0060554736");
        book1.setName("The Game");
        book1.setAuthor("Neil Strauss");
        book1.setPublisher("Harpercollins");
        bookList.add(book1);

        var book2 = new Book();
        book2.setIsbn("978-3832180577");
        book2.setName("Feuchtgebiete");
        book2.setAuthor("Charlotte Roche");
        book2.setPublisher("Dumont Buchverlag");
        bookList.add(book2);

        var book3 = new Book();
        book3.setIsbn("978-3832180577");
        book3.setName("Feuchtgebiete");
        book3.setAuthor("Charlotte Roche");
        book3.setPublisher("Dumont Buchverlag");
        bookList.add(book3);

        var book4 = new Book();
        book4.setIsbn("978-0060554736");
        book4.setName("The Game");
        book4.setAuthor("Neil Strauss");
        book4.setPublisher("Harpercollins");
        bookList.add(book4);

        // create bookstore, assign books
        var bookstore = new BookStore();
        bookstore.setName("Fraport Bookstore");
        bookstore.setLocation("Livres belles");
        bookstore.setBookList(bookList);

        // Create writer
        Xml_writer writer = new Xml_writer();

        writer.write_xml(bookstore);
    }

    /* Next Step:
    *   - Create method for storing data in XML file.
    *   - Create method/class for storing classes name ( or create class by deadcode types ).
    *   - Search for the name in class to detect dead class/interface.
    *       -> Check that if no. when counting name in file is more than 1 or not. If count is 1 then dead,else not.
    * */


}