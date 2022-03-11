/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
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
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

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
        LinkedList<String> lista = new LinkedList <> ();
        lista.add("MSJ");
        lista.add(ID);
        lista.add(cliente_receptor);
        lista.add(mensaje);
        try {
            objectOutputStream.writeObject(lista);
            
        } catch(IOException ex){
            System.err.println("Error");
        }
    }

    void confirmarDesconexion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    String getID() {
        return ID;
    }

    private void enviarSolicitudConexion(String ID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void escuchar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
