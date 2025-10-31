package ArbolSintactico;

import java.util.ArrayList;
import java.util.HashMap;

import CompiladoresMain.TablaDeAmbitos;

public class NodoBloque extends Nodo {
    private ArrayList<Nodo> sentencias;

    public NodoBloque() {
        this.sentencias = new ArrayList<>();
    }
    
    public void agregarSentencia(Nodo sentencia) {
        if (sentencia != null) { // (Las declaraciones pueden devolver null)
            this.sentencias.add(sentencia);
        }
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // Chequea cada sentencia dentro del bloque
        for (Nodo s : sentencias) {
            s.chequear(TdA);
        }
        return "void"; // Un bloque no tiene tipo
    }
    @Override
    public void imprimir(String prefijo) {
        // Un bloque en sí no se imprime, solo sus sentencias
        for (Nodo s : sentencias) {
            if (s != null) s.imprimir(prefijo);
        }
    }
}
