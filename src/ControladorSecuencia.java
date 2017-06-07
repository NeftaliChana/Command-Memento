
import java.awt.BorderLayout;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.DefaultListModel;

public class ControladorSecuencia implements java.io.Serializable {

    private PedidoGUI pedidoGUI;

    public static final String ARCHIVO_DATOS = "Data.txt";

    private long IDUltimoProcesado = 1;
    private long IDCmd = 0;
    private String ultimoCmd = "";
    long idSecuencia = 0;
    String comandoAnt = "";

    public ControladorSecuencia(PedidoGUI pg) {
        pedidoGUI = pg;
    }

    public Memento crearMemento() {
        return (new Memento(IDUltimoProcesado, IDCmd, ultimoCmd));
    }

    public void setMemento(Memento memento) {
        if (memento != null) {
            IDUltimoProcesado = memento.getIDLast();
            IDCmd = memento.getIDCmd();
            ultimoCmd = memento.getLastCmd();
        }
    }

    public long getIDUltimoProcesado() {
        return IDUltimoProcesado;
    }

    public void setIDUltimoProcesado(long ultimoID) {
        IDUltimoProcesado = ultimoID;
    }

    public long getLastCommandID() {
        return IDCmd;
    }

    public void setLastCommandID(long lastCmd) {
        IDCmd = lastCmd;
    }

    public String getLastCmd() {
        return ultimoCmd;
    }

    public void setLastCmd(long lastCmd) {
        IDCmd = lastCmd;
    }

    public long getIdSecuencia() {
        return idSecuencia;
    }

    public void setIdSecuencia(long idSecuencia) {
        this.idSecuencia = idSecuencia;
    }

    public boolean procesar(String secuencia) {

        boolean exitoso = true;
        String comando = "";
        int contComando = 0;

        //obtiene los valores del memento 
        long ultimoID = getIDUltimoProcesado();
        long ultimoCmdID = getLastCommandID();
        //String lastComando = getLastCmd();
        CommandInterface objComando = null;
        StringTokenizer st = new StringTokenizer(secuencia, " - ");
        idSecuencia = new Long(st.nextToken());
        contComando = 0;
        while (st.hasMoreTokens()) {
            comando = st.nextToken();
            contComando++;
            if (contComando >= ultimoCmdID) {
                System.out.println("ant: " + comandoAnt + "->" + comando);
                if (!(isValid(comandoAnt, comando))) {
                    System.out.println("NO SE HA SELECCIONADO UN COMANDO ADECUADO");
                    exitoso = false;
                    break;
                } else {
                    if (comando.equalsIgnoreCase("AGRANDAR")) {
                        objComando = pedidoGUI.new AgrandarPedido();
                    } else if (comando.equalsIgnoreCase("ELIMINAR")) {
                        objComando = pedidoGUI.new EliminarPedido();
                    } else if (comando.equalsIgnoreCase("ADICIONAR")) {
                        objComando = pedidoGUI.new AdicionarProducto();
                    }
                    comandoAnt = comando;
                    objComando.procesarEvento();
                }
            }
        }
         if (!exitoso) {
            //System.out.println("guardando contComando = " + contComando + "; idsecuencia = " + idSecuencia + "; comando = " + comando);
            this.IDCmd = contComando;
            this.IDUltimoProcesado = idSecuencia;
            this.ultimoCmd = comando;
        }
        return exitoso;
    }

    public boolean isValid(String comandoAnterior, String comando) {

        if (comando.equalsIgnoreCase("Agrandar")) {
            if (comandoAnterior.equalsIgnoreCase("Adicionar")) {
                return true;
            }
        } else if (comando.equalsIgnoreCase("Adicionar")) {
            if (comandoAnterior.equalsIgnoreCase("") || comandoAnterior.equalsIgnoreCase("ELIMINAR")) {
                return true;
            }
        } else if (comando.equalsIgnoreCase("Eliminar")) {
            if (comandoAnterior.equalsIgnoreCase("AGRANDAR") || comandoAnterior.equalsIgnoreCase("Adicionar")) {
                return true;
            }
        }
        return false;
    }

    class Memento implements java.io.Serializable {

        private long lastProcessedID;
        private long lastCommandID;
        private String lastCommand;

        private Memento(long IDLast, long IDCmd, String lastCmd) {
            lastProcessedID = IDLast;
            lastCommandID = IDCmd;
            lastCommand = lastCmd;
        }

        private long getIDLast() {
            return lastProcessedID;
        }

        private long getIDCmd() {
            return lastCommandID;
        }

        private String getLastCmd() {
            return lastCommand;
        }
    }
}
