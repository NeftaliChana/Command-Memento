import java.io.*;
import java.util.*;

public class MementoHandler implements  java.io.Serializable{
  public static final String ID_FILE = "ID.txt";

  private ControladorSecuencia.Memento objMemento = null;

  public ControladorSecuencia.Memento getMemento() {
	 
    ObjectInputStream objStream = null;
    FileUtil util = new FileUtil();

    if (util.isFileExists(ID_FILE)) {
      //read the object from the file
      try {
        objStream = new ObjectInputStream(
                      new FileInputStream(new File(ID_FILE)));

        objMemento = (ControladorSecuencia.Memento)
                     objStream.readObject();
        objStream.close();

      } catch (Exception e) {
        System.out.println("Error Reading Memento");
        System.exit(1);
      }
      
      util.deleteFile(ID_FILE);
    }
    return objMemento;
  }

  public void setMemento(ControladorSecuencia.Memento memento) {
    ObjectOutputStream objStream = null;
   
    //write the object to the file
    try {
      objStream = new ObjectOutputStream(new FileOutputStream(new File(ID_FILE)));
      objStream.writeObject(memento);
      objStream.close();

    } catch (Exception e) {
    	System.out.println(e);
      System.out.println("Error Writing Memento");
      System.exit(1);
    }
  }

}// end of class

