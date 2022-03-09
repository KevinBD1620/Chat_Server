/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    
    static int clienteS;
    /**
     * Constructor del servidor.
     * @param puerto
     * @param ventana 
     */
    public Servidor(String puerto, VentanaSe ventana) {
        clienteS=0;
        this.puerto=puerto;
        this.ventana=ventana;
        clientes=new LinkedList<>();
        this.start();
    }
    
    public void run(){
        try {
            serverSocket = new ServerSocket(Integer.valueOf(puerto));
            ventana.addServidorIniciado();
            while(true){
                Hilo_Cliente h;
                Socket socket;
                socket = serverSocket.accept();
                System.out.println("Nueva conexion entrante: "+socket);
                h=new Hilo_Cliente(socket, this);               
                h.start();
                
            }catch (Exception e)
            
        }
    }
    
    
    
    
    
    

    
    
}
