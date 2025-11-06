package ArbolSintactico;
import CompiladoresMain.*; 
import java.util.HashMap;
public class NodoPrograma extends Nodo {
    private String nombrePrograma; 
    private NodoBloque bloquePrincipal;
    public NodoPrograma(String nombre, NodoBloque bloque) {
        this.nombrePrograma = nombre;
        this.bloquePrincipal = bloque;
    }
    @Override
        public String chequear(TablaDeAmbitos TdA) {
            TdA.abrirAmbito(this.nombrePrograma); 
            bloquePrincipal.chequear(TdA);
            return "void"; 
        }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Raiz (Programa: " + nombrePrograma + ")");
        bloquePrincipal.imprimir(prefijo + "  ");
    }
}