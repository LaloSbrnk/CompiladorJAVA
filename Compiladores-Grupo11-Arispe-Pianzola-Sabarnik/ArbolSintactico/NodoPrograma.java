package ArbolSintactico;

import CompiladoresMain.*; 
import java.util.HashMap;

public class NodoPrograma extends Nodo {
    private String nombrePrograma; // El ID inicial
    private NodoBloque bloquePrincipal;

    public NodoPrograma(String nombre, NodoBloque bloque) {
        this.nombrePrograma = nombre;
        this.bloquePrincipal = bloque;
    }

    @Override
        public String chequear(TablaDeAmbitos TdA) {

            // Establece el nombre del ámbito global
            TdA.abrirAmbito(this.nombrePrograma); // Pila -> ["PROGRAMA%1"]

            // El chequeo principal se hace en el bloque hijo
            bloquePrincipal.chequear(TdA);

            
            return "void"; 
        }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Raiz (Programa: " + nombrePrograma + ")");
        bloquePrincipal.imprimir(prefijo + "  ");
    }
}