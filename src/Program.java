/**
 * Created by Igor on 03.12.2016.
 */
import QueueGraph.*;

import javax.xml.bind.JAXBException;
import java.io.File;

public class Program {
    public static void main(String[] args) throws JAXBException {
        QueueSerialization serialization = QueueSerialization.fromFile(new File("examples/Test.xml"));
        QueueBuilder builder = new QueueBuilder(serialization);
        builder.buildQueue();

    }
}
