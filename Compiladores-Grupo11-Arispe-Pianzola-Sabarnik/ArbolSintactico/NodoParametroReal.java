package ArbolSintactico;

import CompiladoresMain.TablaDeAmbitos;

public class NodoParametroReal extends Nodo {
    private Nodo expresion;
    private String nombreFormal; // El ID despues de '->'

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
        // El parametro real se chequea en el contexto de la invocacion
        // Solo chequeamos la expresion que contiene
        return expresion.chequear(TdA);
    }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Parametro Real (-> " + nombreFormal + "):");
        expresion.imprimir(prefijo + "  ");
    }
}