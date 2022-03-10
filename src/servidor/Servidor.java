
package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;
/**
 *
 * @author kevin
 */
public class Servidor extends Thread{
    
    private ServerSocket serverSocket;
    
    
    LinkedList<Hilo_Cliente> clientes;
    
    
    private final VentanaSe ventana;
    
    
    private final String puerto;
    
    static int ClienteS;
    /**
     * Constructor del servidor.
     * @param puerto
     * @param ventana 
     */
    public Servidor(String puerto, VentanaSe ventana) {
        ClienteS=0;
        this.puerto=puerto;
        this.ventana=ventana;
        clientes=new LinkedList<>();
        this.start();
    }
    
   
    public void run(){
        try {
            serverSocket = new ServerSocket(Integer.valueOf(puerto));
            ventana.ServerOn();
            while(true){
                Hilo_Cliente h;
                Socket socket;
                socket = serverSocket.accept();
                System.out.println("Nuevo Usuario: "+socket);
                h=new Hilo_Cliente(socket, this);               
                h.start();
                
            }
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "Error al iniciar el servidor,\n"
                                                 + "Compruebe que el puerto sea el correcto.");
            System.exit(0);
        }                
    }    

    LinkedList<String> getUsers() {
        LinkedList<String>usuariosConectados=new LinkedList<>();
        clientes.stream().forEach(c -> usuariosConectados.add(c.getID()));
        return usuariosConectados;
    }
    
    void newLog(String texto) {
        ventana.newLog(texto);
    }
}    
    
    
    
    
    
    
    

    
    

