
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.sun.java.swing.plaf.windows.*;
import java.util.*;

public class PedidoGUI extends JFrame {

    public static final String newline = "\n";
    public static final String SECUENCIA = "Correr Secuencia";
    public static final String AGRANDAR = "Actualizar";
    public static final String EXIT = "Salir";

    private JPanel pnlPedido;
    private JComboBox listaProductos;
    private JComboBox listaComandos;
    private JList listaPedido;

    private DefaultListModel defListaPedido;
    private SecuenciaButton btnSecuencia;
    private ActualizarListaSecuencia btnActualizarLista;
    private ExitButton btnExit;
    private ControladorSecuencia ctrlSec;
    private MementoHandler mementoHandler;

    public PedidoGUI() throws Exception {
        super("Comidas Rapidas IsWar");
        listaProductos = new JComboBox();
        listaComandos = new JComboBox();
        // Create controls
        defListaPedido = new DefaultListModel();
        listaPedido = new JList(defListaPedido);
        pnlPedido = new JPanel();
        listaPedido.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPedido.setSelectedIndex(-1);
        JScrollPane spListaPedido = new JScrollPane(listaPedido);

        // Create Labels
        JLabel lbllistaPedido = new JLabel("Productos:");
        //JLabel lblSpacer = new JLabel("         ");

        //create CommandButtons
        btnActualizarLista = new ActualizarListaSecuencia("Actualizar Lista");
        btnSecuencia = new SecuenciaButton(PedidoGUI.SECUENCIA);
        btnExit = new ExitButton(PedidoGUI.EXIT);

        //Create and Add ActionListeners
        buttonHandler vf = new buttonHandler();
        btnActualizarLista.addActionListener(vf);
        btnSecuencia.addActionListener(vf);
        btnExit.addActionListener(vf);

        JPanel lstPanel = new JPanel();
        GridBagLayout gridbag2 = new GridBagLayout();
        lstPanel.setLayout(gridbag2);
        GridBagConstraints gbc2 = new GridBagConstraints();

        //lstPanel.add(listaProductos);
        lstPanel.add(lbllistaPedido);
        lstPanel.add(spListaPedido);

        //lstPanel.add(lblSpacer);

        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gridbag2.setConstraints(lbllistaPedido, gbc2);
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        //gridbag2.setConstraints(lblSpacer, gbc2);

        // -----------------------------------
        // For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel();

        // ----------------------------------------------
        GridBagLayout gridbag = new GridBagLayout();
        buttonPanel.setLayout(gridbag);
        GridBagConstraints gbc = new GridBagConstraints();
        buttonPanel.add(lstPanel);
        buttonPanel.add(listaComandos);
        buttonPanel.add(btnActualizarLista);
        buttonPanel.add(btnSecuencia);
        buttonPanel.add(btnExit);

        gbc.insets.top = 5;
        gbc.insets.bottom = 5;
        gbc.insets.left = 5;
        gbc.insets.right = 5;

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;

        gridbag.setConstraints(btnSecuencia, gbc);
        gbc.gridx = 5;
        gbc.gridy = 0;

        gridbag.setConstraints(listaComandos, gbc);
        gbc.gridx = 7;
        gbc.gridy = 0;
        gridbag.setConstraints(btnExit, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gridbag.setConstraints(lstPanel, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets.left = 2;
        gbc.insets.right = 2;
        gbc.insets.top = 40;

        // ****************************************************
        // Add the buttons and the log to the frame
        Container contentPane = getContentPane();
        contentPane.add(lstPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        ctrlSec = new ControladorSecuencia(this);
        mementoHandler = new MementoHandler();
        initialize();
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            SwingUtilities.updateComponentTreeUI(PedidoGUI.this);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public JList getListaPedido() {
        return listaPedido;
    }

    private void initialize() {
        listaProductos.addItem("Hamburguesa");
        listaProductos.addItem("Papas");
        listaProductos.addItem("Gaseosa");
        llenarListaSecuencia();
    }

    public void llenarListaSecuencia() {
        String lineaEntrada = "";
        listaComandos.removeAllItems();
        FileUtil util = new FileUtil();
        Vector vector = new Vector();
        vector = util.fileToVector("secuencia.txt");
        for(int i=0; i < vector.size(); i++){
            listaComandos.addItem(vector.elementAt(i));
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new PedidoGUI();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(650, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class buttonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CommandInterface objComando = (CommandInterface) e.getSource();
            objComando.procesarEvento();
        }
    }

    class SecuenciaButton extends JButton implements CommandInterface {
        public void procesarEvento() {
            //System.out.println("------------------------------------------------------");
            ctrlSec.setMemento(mementoHandler.getMemento());
            if (!ctrlSec.procesar(listaComandos.getSelectedItem().toString())) {
                JOptionPane.showMessageDialog(null, "Se ha presentado un error en la secuencia ejecutada");
            } else {
                ctrlSec.setLastCommandID(0);
            } 
            mementoHandler.setMemento(ctrlSec.crearMemento());
            ctrlSec.setMemento(mementoHandler.getMemento());
        }
        public SecuenciaButton(String name) {
            super(name);
        }
    }

    class ActualizarListaSecuencia extends JButton implements CommandInterface {
        public void procesarEvento() {
            llenarListaSecuencia();
            JOptionPane.showMessageDialog(null, "Lista de Secuencias Actualizada correctamente");
        }
        public ActualizarListaSecuencia(String name) {
            super(name);
        }
    }

    class ExitButton extends JButton implements CommandInterface {
        public void procesarEvento() {
            System.exit(1);
        }
        public ExitButton(String name) {
            super(name);
        }
    }

    class AgrandarPedido implements CommandInterface {
        public AgrandarPedido() {
        }
        public void procesarEvento() {
            for (int i = 0; i < listaProductos.getItemCount(); i++) {
                ((DefaultListModel) listaPedido.getModel()).addElement(listaProductos.getItemAt(i).toString() + " AGRANDADO");
            }
        }
    }

    class AdicionarProducto implements CommandInterface{
        public AdicionarProducto() {
        }
        public void procesarEvento() {
            ((DefaultListModel) listaPedido.getModel()).addElement("---Combo Adicionado---");
        }
    }

    class EliminarPedido implements CommandInterface {
        public EliminarPedido() {
        }

        public void procesarEvento() {
            ((DefaultListModel) listaPedido.getModel()).addElement("---Combo Eliminado---");
        }

    }
}// end of class
