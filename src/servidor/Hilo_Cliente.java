
package servidor;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 *
 * @author kevin
 */
public class Hilo_Cliente extends Thread{
    
    private final Socket socket;
    
    private ObjectInputStream objectInputStream;
    
    private ObjectOutputStream objectOutputStream;
    
    private final Servidor server;
    
    private String ID;
    
    private boolean Receptor;
            
           
    
    
    
    

 

   

    
    
}
