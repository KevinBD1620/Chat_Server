
package servidor;
/**
 * Imports requeridos por esta clase
 */
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Esta clase se encarga de gestionar todo lo relacionado con la interfaz grafica del servidor
 * Se muestra un historial de las conexines y desconexiones de los clientes.
 * Y se muestra un mensaje cuando el servidor esta corriendo de forma adecuada
 * @author kevin
 */
public class VentanaSe extends javax.swing.JFrame {
    private final String DEFAULT_PORT="10201";
    private final Servidor servidor;
    /**
     * Creates new form Ventana
     */
   public VentanaSe() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String puerto=getPuerto();
        servidor=new Servidor(puerto, this);
    }
        
        
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtClientes = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtClientes.setBackground(new java.awt.Color(255, 255, 0));
        txtClientes.setColumns(20);
        txtClientes.setRows(5);
        jScrollPane1.setViewportView(txtClientes);

        jLabel1.setText("Servidor");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaSe().setVisible(true);
            }
        });
    }
    
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtClientes;
    // End of variables declaration//GEN-END:variables

    
    /**
     * Agrega una linea de texto al log
     * @param texto 
     */
    void newLog(String texto) {
        txtClientes.append(texto);
       
    }
    /**
     * Metodo que despliega una ventana para que el usuario ingrese 
     * el puerto que quiere utilizar para comunicarce con el servidor
     * @return 
     */
    private String getPuerto() {
        String p=DEFAULT_PORT;
        JTextField puerto = new JTextField(20);
        puerto.setText(p);
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(2, 1));
        myPanel.add(new JLabel("Puerto de la conexi??n:"));
        myPanel.add(puerto);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Configuracion", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
                p=puerto.getText();
        }else{
            System.exit(0);
        }
        return p;
    }
    
    /**
     * Este metodo notifica cuando el eervidor se inicia correctamente.
     */
    void ServerOn() {
        txtClientes.setText("Iniciando conexion...");
    }      
    
        
}
