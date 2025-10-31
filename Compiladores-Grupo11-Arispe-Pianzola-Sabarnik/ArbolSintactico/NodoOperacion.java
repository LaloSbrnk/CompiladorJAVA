package ArbolSintactico;
import java.util.HashMap;

import CompiladoresMain.TablaDeAmbitos;

public class NodoOperacion extends Nodo {
    private String op;
    private Nodo izq;
    private Nodo der;

    public NodoOperacion(String op, Nodo izq, Nodo der) {
        this.op = op;
        this.izq = izq;
        this.der = der;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) { 
        // 1. Chequea el sub-árbol izquierdo
        String tipoIzq = izq.chequear(TdA);
        
        // 2. Chequea el sub-árbol derecho
        String tipoDer = (der != null) ? der.chequear(TdA) : null; // (Manejo para UMINUS)

        // --- Lógica de UMINUS ---
        if (this.op.equals("UMINUS")) {
            if (tipoIzq.equals("long") || tipoIzq.equals("dfloat")) {
                return tipoIzq;
            } else {
                System.err.println("ERROR Semantico: Operador 'menos unario' no aplicable a tipo '" + tipoIzq + "'.");
                return "error";
            }
        }
        
        // Lógica de Operación Binaria
        if (tipoIzq.equals("error") || tipoDer.equals("error")) {
            return "error";
        }
        
        if ((tipoIzq.equals("long") || tipoIzq.equals("dfloat")) &&
            (tipoDer.equals("long") || tipoDer.equals("dfloat"))) {
            
            if (tipoIzq.equals("dfloat") || tipoDer.equals("dfloat")) {
                return "dfloat";
            } else {
                return "long";
            }
        }
        
        System.err.println("ERROR Semantico: Tipos incompatibles en la operacion '" + op + "': " + tipoIzq + ", " + tipoDer);
        return "error";
    }
}