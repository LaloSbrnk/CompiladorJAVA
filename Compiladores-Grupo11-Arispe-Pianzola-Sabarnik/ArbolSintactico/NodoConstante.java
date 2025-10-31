package ArbolSintactico;
import java.util.HashMap;

import CompiladoresMain.TablaDeAmbitos;

public class NodoConstante extends Nodo {
    private Object valor;
    private String tipo;

    public NodoConstante(Object valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        return this.tipo; 
    }
    @Override
    public void imprimir(String prefijo) {
        // Aplicamos sangría directamente a la misma línea
        System.out.println(prefijo + "Constante (" + tipo + "): " + valor.toString());
    }
}