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
            System.out.println("DEBUG: Iniciando chequeo semantico del programa '" + nombrePrograma + "'");

            // Establece el nombre del ámbito global
            TdA.abrirAmbito(this.nombrePrograma); // Pila -> ["PROGRAMA%1"]

            // El chequeo principal se hace en el bloque hijo
            bloquePrincipal.chequear(TdA);

            System.out.println("DEBUG: Fin chequeo semantico del programa '" + nombrePrograma + "'");
            
            return "void"; 
        }
}