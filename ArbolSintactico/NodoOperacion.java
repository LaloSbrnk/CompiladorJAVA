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
        String tipoIzq = izq.chequear(TdA);
        String tipoDer = (der != null) ? der.chequear(TdA) : null; 
        if (this.op.equals("UMINUS")) {
            if (tipoIzq.equals("long") || tipoIzq.equals("dfloat")) {
                return tipoIzq;
            } else {
                System.err.println("ERROR Semantico: Operador 'menos unario' no aplicable a tipo '" + tipoIzq + "'.");
                return "error";
            }
        }
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
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Operacion: " + op);
        izq.imprimir(prefijo + "  " + "Izq: ");
        if (der != null) {
            der.imprimir(prefijo + "  " + "Der: ");
        }
    }
}