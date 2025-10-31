package ArbolSintactico;

import java.util.HashMap;

import CompiladoresMain.TablaDeAmbitos;

public class NodoCondicion extends Nodo {
    private Nodo izq;
    private Nodo der;
    private String op; // Guarda ">", "==", etc.

    public NodoCondicion(Nodo izq, Nodo der, String op) {
        this.izq = izq;
        this.der = der;
        this.op = op;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        String tipoIzq = izq.chequear(TdA);
        String tipoDer = der.chequear(TdA);

        if (tipoIzq.equals("error") || tipoDer.equals("error")) {
            return "error";
        }

        // Solo permitimos comparar números entre si
        if (! (tipoIzq.equals("long") || tipoIzq.equals("dfloat")) ) {
            System.err.println("ERROR Semantico: Tipo incompatible en la comparacion. No se puede comparar un '" + tipoIzq + "'.");
            return "error";
        }
        if (! (tipoDer.equals("long") || tipoDer.equals("dfloat")) ) {
            System.err.println("ERROR Semantico: Tipo incompatible en la comparacion. No se puede comparar un '" + tipoDer + "'.");
            return "error";
        }
        
        return "boolean"; 
    }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Comparacion: " + op);
        izq.imprimir(prefijo + "  " + "Izq: ");
        der.imprimir(prefijo + "  " + "Der: ");
    }
}