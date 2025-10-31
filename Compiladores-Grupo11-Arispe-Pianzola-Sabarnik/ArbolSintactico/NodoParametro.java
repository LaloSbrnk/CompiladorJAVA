// En ArbolSintactico/NodoParametro.java
package ArbolSintactico;

import CompiladoresMain.*;
import java.util.HashMap; 

public class NodoParametro extends Nodo {
    private String nombre;
    private String tipo;

    public NodoParametro(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        //Creamos los atributos para la TS
        AtributosTokens attrs = new AtributosTokens(TiposToken.IDENTIFICADOR); 
        attrs.setTipoDato(this.tipo);
        attrs.setUso("parametro");

        //Intenta agregarlo al AMBITO ACTUAL (que es el de la funcion)
        if (!TdA.agregar(this.nombre, attrs)) {
            System.err.println("ERROR Semantico: Redeclaracion de parametro '" + this.nombre + "'.");
            return "error";
        } else {
            System.out.println("DEBUG: Registrado parametro '" + this.nombre + "' con tipo '" + this.tipo + "'");
        }
        return this.tipo; 
    }
}