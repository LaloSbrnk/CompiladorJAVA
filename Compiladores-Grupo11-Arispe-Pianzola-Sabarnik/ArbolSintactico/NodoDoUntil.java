package ArbolSintactico;

import java.util.HashMap;

import CompiladoresMain.TablaDeAmbitos;

public class NodoDoUntil extends Nodo {
    private NodoBloque bloque;
    private Nodo condicion;

    public NodoDoUntil(NodoBloque bloque, Nodo condicion) {
        this.bloque = bloque;
        this.condicion = condicion;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // Chequea el bloque 'do'
        bloque.chequear(TdA); 
        
        // Chequea la condición 'until'
        condicion.chequear(TdA);
        
        return "void"; 
    }
}