
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
    
    public Hilo_Cliente (Socket socket, Servidor server){
        this.server = server;
        this.socket = socket;
        try{
            objectOutputStream = new ObjectOutputStream (socket.getOutputStream());
            objectInputStream  = new ObjectInputStream  (socket.getInputStream());
            
        } catch (IOException ex) {
            System.err.println("Error");
        }
    }
            
    public void desconectar() {
        try {
            socket.close();
            Receptor=false;
            
        }catch (IOException ex) {
            System.err.println("Error al cortar la comunicacion con el usuario");
        }
    }       
    
    public void escuchar(){
        Receptor = true;
        while(Receptor){
            try {
                Object aux=objectInputStream.readObject();
                if (aux instanceof LinkedList){
                    exe((LinkedList<String>)aux);
                }
            } catch (Exception e) {
                System.err.println("Error");
            }
        }
    }
    
    public void run() {
        try{
            escuchar(); 
        } catch (Exception ex) {
            System.err.println("Error en el readline");
        }
        desconectar ();
    }
    
    public void exe(LinkedList<String> lista){
        String tipo = lista.get(0);
        switch (tipo) {
            case "Solicitud_Con":
                SetCon(lista.get(1));
                break;
            case "SOLICITUD_DISCON":
                SetDiscon();
                break;
            case "MSJ":
                String destinatario = lista.get(2);
                server.clientes
                        .stream()
                        .filter(h -> (destinatario.equals(h.getID())))
                        .forEach((h)-> h.enviarMensaje(lista));
                break;
            default:
                break;         
        }
    }
    
    private void enviarMensaje(LinkedList<String> lista){
        
    }

    public String getID() {
        return ID;
    }
    
    private void SetCon(String ID) {
        
    }
    
   
     private void SetDiscon() {
         
     }
    

   

    
    
}
