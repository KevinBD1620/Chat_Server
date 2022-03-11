
package servidor;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 *
 * @author kevin
 * Se importan los elementeos requeridos para esta clase
 */

/**
 * Esta clase esta compuesta por objetos que al iniciar, escuchan permanentemente la informacion brindada
 * por los clientes, se crean hilos para cada usuario nuevo.
 *
 */
public class Hilo_Cliente extends Thread{
    /**
     * Socket que se utiliza para la comunicacion con el cliente
     */
    private final Socket socket;
    
    /**
     * Stream para enviar objetos al servidor
     */
    private ObjectInputStream objectInputStream;
    /**
     * Stream para recivir objetos del servidor
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * Servidor al que pertenece este hilo
     */
    private final Servidor server;
    /**
     * ID para cada cliente con el que el hilo se esta comunicando
     */
    private String ID;
    /**
     * Booleano que almacena valores de verdad, cuando el hilo y el cliente se estan comunicando
     */
    private boolean Receptor;
    /**
     * Constructor de la clase Hilo_Cliente
     * @param socket
     * @param server 
     */
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
    
    /**
     * Aca es donde se sobreescribe el metodo Thread, en ciclo infinito
     */
    
    public void desconectar() {
        try {
            socket.close();
            Receptor=false;
        } catch (IOException ex) {
            System.err.println("Error al cerrar el socket de comunicación con el cliente.");
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
    
    /**
     * Metodo encargado de cerrar el socket al finalizar la comunicacion
     */        
     
    /**
     * Este metodo esta constantemente reciviendo la informacion del cliente con el que 
     * se esta comunicando
     */
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
    
            
    /**
     * Este metodo gestiona una serie de acciones dependiendo de lo que el socket de conexion 
     * haya recivido.
     * @param lista 
     */
    public void exe(LinkedList<String> lista){
        String tipo = lista.get(0);
        switch (tipo) {
            case "SOLICITUD_CONEXION":
                SetCon(lista.get(1));
                break;
            case "SOLICITUD_DESCONEXION":
                SetDiscon();
                break;
            case "MENSAJE":
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
    /**
     * Metodo para notificar al cliente atraves del socket
     * @param lista 
     */
    private void enviarMensaje(LinkedList<String> lista){
        try{
            objectOutputStream.writeObject(lista);
            
        } catch (Exception e) {
            System.err.println("Error");
        }
    }
    /**
     * Metodo que retorna un identificador unico para cada cliente
     * @return 
     */
    public String getID() {
        return ID;
    }
    /**
     * Este metodo se encarga de notificar a todos los clientes conectados
     * que se ha detectado un nuevo cliente. de esta manera este mismo
     * se puede agregar a la lista de destinatarios
     * @param ID 
     */
    private void SetCon(String ID) {
        Servidor.ClienteS++;
        this.ID=Servidor.ClienteS+" - "+ID;
        LinkedList<String> lista=new LinkedList<>();
        lista.add("CONEXION_ACEPTADA");
        lista.add(this.ID);
        lista.addAll(server.getUsers());
        enviarMensaje(lista);
        server.newLog("\nNuevo Usuario: "+this.ID);
        //enviar a todos los clientes el nombre del nuevo usuario conectado excepto a él mismo
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("NUEVO_USUARIO_CONECTADO");
        auxLista.add(this.ID);
        server.clientes
                .stream()
                .forEach(cliente -> cliente.enviarMensaje(auxLista));
        server.clientes.add(this);
    }
    
    /**
     * Este metodo se encarga de notificar a todos los usuarios atraves de los clientes
     * cuando uno de los clientes se ha desconectado.
     */
     private void SetDiscon() {
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("USUARIO_DESCONECTADO");
        auxLista.add(this.ID);
        server.newLog("\nEl cliente \""+this.ID+"\" se ha desconectado.");
        this.desconectar();
        for(int i=0;i<server.clientes.size();i++){
            if(server.clientes.get(i).equals(this)){
                server.clientes.remove(i);
                break;
            }
        }
        server.clientes
                .stream()
                .forEach(h -> h.enviarMensaje(auxLista));        
    }
}
    

