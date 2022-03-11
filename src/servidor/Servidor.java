
package servidor;
/**
 * Imports requeridos por esta clase
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;


/**
 * Clase en la que se maneja la comuniacion con el servidor
 * @author kevin
 */
public class Servidor extends Thread{
    /**
     * Socket servidor que cumple con la funcion de incluir 
     * a los nuevos clientes en el chat
     */
    private ServerSocket serverSocket;
    /**
     * Lista de todos los hilos que se encargan de la comunicacion 
     * con cada uno de los clientes.
     */
    LinkedList<Hilo_Cliente> clientes;
    /**
     * Variable en la que se almacena la ventana
     * que gestiona la interfaz grafica del server
     */
    private final VentanaSe ventana;
    /**
     * Variable en la que se guarda el puerto por el que 
     * el servidor se comunicara
     */
    private final String puerto;
    /**
     * Diferenciador que se encarga, valga la redondancia
     * de diferencia entre los multiples clientes conectados
     * por ejemplo si dos usuarios ingresan con el mismo nick
     */
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
    
    /**
     * Metodo sobre el que corre el bucle infinito
     * en el que se espera por nuevas conexiones 
     * de clientes con el servidor
     * Llama a la clase hilo_cliente
     */
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
    /**
     *Este ciclo retorna la lista con los ID de todos los clientes conectados
     * @return 
     */
    LinkedList<String> getUsers() {
        LinkedList<String>usuariosConectados=new LinkedList<>();
        clientes.stream().forEach(c -> usuariosConectados.add(c.getID()));
        return usuariosConectados;
    }
    /**
     * Metodo que notifica nuevas conexiones.
     * @param texto 
     */
    void newLog(String texto) {
        ventana.newLog(texto);
    }
}    
    
    
    
    
    
    
    

    
    

