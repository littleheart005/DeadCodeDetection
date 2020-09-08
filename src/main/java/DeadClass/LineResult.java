package DeadClass;


import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

    /*
        Xml Example Result

        <Class>
            <Name> CheesePizza </Name>
            <Line> public class CheesePizza extends Pizza </Line>
            <File> CheesePizza.java </File>
         </Class>
         .
         .
         .
    */


public class LineResult {

    @XmlElementWrapper(name = "classes")
    @XmlElement(name = "class")
    private List<String> className;

    public List<String> getClassName() {
        return className;
    }

    public void setClassName(List<String> className) {
        this.className = className;
    }
}
