package ArbolSintactico;
import CompiladoresMain.TablaDeAmbitos;
public class NodoParametroReal extends Nodo {
    private Nodo expresion;
    private String nombreFormal; 
    public NodoParametroReal(Nodo expresion, String nombreFormal) {
        this.expresion = expresion;
        this.nombreFormal = nombreFormal;
    }
    public String getNombreFormal() {
        return nombreFormal;
    }
    public Nodo getExpresion() {
        return expresion;
    }
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        return expresion.chequear(TdA);
    }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Parametro Real (-> " + nombreFormal + "):");
        expresion.imprimir(prefijo + "  ");
    }
}