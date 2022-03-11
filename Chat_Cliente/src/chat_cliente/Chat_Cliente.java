
/**
 * Parte de la aplicacion de Chat que se encarga de los clientes
 */

package chat_cliente;
/**
 * Imports requeridos
 */
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import javax.swing.JOptionPane;
/**
 *Clase en la que se gestiona la comunicacion del cliente
 * @author kevin
 */
public class Chat_Cliente extends Thread {
    /**
     * Socket para la comunicacion con el servidor
     */
    private Socket socket;
    /**
     * Stream para el envio de obj al server
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * Stream para la interaccion con el servidor
     */
    private ObjectInputStream objectInputStream;
    /**
     * Ventana utilizada para la interfaz grafica
     */
    private final VentanaCl ventana;
    /**
     * Identificador unico de cada cliente
     */
    private String ID;
    /**
     * Variable que determina si el cliente se comunica o NO
     * con el servidor
     */
    private boolean escuchando;
    /**
     * Variable que almacena la ip host
     */
    private final String host;
    /**
     * Variable que almacena el puerto
     */
    private final int puerto;
    
    
    
    
    /**
     * Conmstructor de la clase Chat_Cliente
     * @param ventana
     * @param host
     * @param puerto
     * @param nombre 
     */

    Chat_Cliente(VentanaCl ventana, String host, Integer puerto, String nombre) {
        this.ventana=ventana;        
        this.host=host;
        this.puerto=puerto;
        this.ID=nombre;
        escuchando=true;
        this.start();
    }
    /**
     * Metodo que corre el hilo de la comunicacion del lado cliente
     */
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
    /**
     * Metodo que cierra el socket y los streams
     */
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
    
    /**
     * Metodo que envia notificaciones al servidor
     * @param cliente_receptor
     * @param mensaje 
     */

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
    /**
     * Metodo que recive constantemente la info del servidor
     */
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
                    
                   
    /**
     * Metodo que ejecuta una serie de comandos dependiendo de lo que el cliente reciba 
     * @param lista 
     */
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
    
        
     /**
      * Este metodo envia una solicitud para que el servidor lo agregue
      * a la lista de clientes
      * @param ID 
      */   
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
    
    
    
    /**
     * Metodo que retorna el ID del cliente en el chat
     * @return 
     */
    String getID() {
        return ID;
    }
}
