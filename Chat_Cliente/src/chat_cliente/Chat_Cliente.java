
package chat_cliente;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import javax.swing.JOptionPane;
/**
 *
 * @author kevin
 */
public class Chat_Cliente extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final VentanaCl ventana;
    private String ID;
    private boolean escuchando;
    private final String host;
    private final int puerto;
    
    

    

    Chat_Cliente(VentanaCl ventana, String host, Integer puerto, String nombre) {
        this.ventana=ventana;        
        this.host=host;
        this.puerto=puerto;
        this.ID=nombre;
        escuchando=true;
        this.start();
    }
    
    public void run(){
        try {
            socket=new Socket(host, puerto);
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectInputStream=new ObjectInputStream(socket.getInputStream());
            System.out.println("Conexion aceptada");
            this.enviarSolicitudConexion(ID);
            this.escuchar();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(ventana, "Se perdio la conexion con el servidor.");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ventana, "Se perdio la conexion con el servidor,\n"
                                                
                                                 + "Esta aplicación se cerrará.");
            System.exit(0);
        }

    }
    
    public void desconectar(){
        try {
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();  
            escuchando=false;
        } catch (Exception e) {
            System.err.println("Error");
        }
    }
    
    

    public void enviarMensaje(String cliente_receptor, String mensaje) {
        LinkedList<String> lista=new LinkedList<>();       
        lista.add("MENSAJE");        
        lista.add(ID);       
        lista.add(cliente_receptor);
        lista.add(mensaje);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error");
        }
    }

    public void escuchar() {
       try {
            while (escuchando) {
                Object aux = objectInputStream.readObject();
                if (aux != null) {
                    if (aux instanceof LinkedList) {
                        //Si se recibe una LinkedList entonces se procesa
                        exe((LinkedList<String>)aux);
                    } else {
                        System.err.println("Se recibió un Objeto desconocido a través del socket");
                    }
                } else {
                    System.err.println("Se recibió un null a través del socket");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "La comunicación con el servidor se ha perdido,\n"
                                                 + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }
                    
                   

    public void exe(LinkedList<String> lista) {
         // 0 - El primer elemento de la lista es siempre el tipo
        String tipo=lista.get(0);
        switch (tipo) {
            case "CONEXION_ACEPTADA":
                // 1      - ID exclusivo del nuevo usuario
                // 2 .. n - ID de los clientes conectados actualmente
                ID=lista.get(1);
                ventana.sesionIniciada(ID);
                for(int i=2;i<lista.size();i++){
                    ventana.addContacto(lista.get(i));
                }
                break;
            case "NUEVO_USUARIO_CONECTADO":
                // 1      - ID exclusivo del nuevo usuario
                ventana.addContacto(lista.get(1));
                break;
            case "USUARIO_DESCONECTADO":
                // 1      - ID exclusivo del nuevo usuario
                ventana.eliminarContacto(lista.get(1));
                break;                
            case "MENSAJE":
                // 1      - Cliente emisor
                // 3      - Mensaje
                ventana.addMensaje(lista.get(1), lista.get(3));
                break;
            default:
                break;
        }
    }
    
        
        
    private void enviarSolicitudConexion(String ID) {
        LinkedList<String> lista=new LinkedList<>();
        lista.add("SOLICITUD_CONEXION");
        lista.add(ID);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error de lectura.");
        }
    
        
    }
    
    
    
    
    String getID() {
        return ID;
    }
}
