package ArbolSintactico;

import java.util.HashMap;

import CompiladoresMain.TablaDeAmbitos;

public class NodoIf extends Nodo {
    private Nodo condicion;
    private NodoBloque bloqueTrue;
    private NodoBloque bloqueFalse; // Puede ser null

    public NodoIf(Nodo condicion, NodoBloque bloqueTrue, NodoBloque bloqueFalse) {
        this.condicion = condicion;
        this.bloqueTrue = bloqueTrue;
        this.bloqueFalse = bloqueFalse;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        condicion.chequear(TdA);
        
        bloqueTrue.chequear(TdA); 
        
        if (bloqueFalse != null) {
            bloqueFalse.chequear(TdA); 
        }
        
        return "void"; 
    }
}