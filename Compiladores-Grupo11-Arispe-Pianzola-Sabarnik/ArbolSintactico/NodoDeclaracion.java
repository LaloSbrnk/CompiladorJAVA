package ArbolSintactico;

import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NodoDeclaracion extends Nodo {
    private String tipo;
    private ArrayList<String> variables;

    public NodoDeclaracion(String tipo, ArrayList<String> variables) {
        this.tipo = tipo;
        this.variables = variables;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        for (String nombreVar : variables) {
            
            // 1. Chequea si ya existe Y TIENE USO en el AMBITO ACTUAL
            AtributosTokens attrsEnAmbitoActual = TdA.getAmbitoActual().get(nombreVar);

            if (attrsEnAmbitoActual != null && attrsEnAmbitoActual.getUso() != null) {
                // Si existe y YA tiene un uso (variable, parametro, funcion), es redeclaracion
                System.err.println("ERROR Semantico: Redeclaracion de variable '" + nombreVar + "'.");
            } else {
                // No existe en el ambito actual, o existe pero sin uso (del lexer)
                // Obtenemos el atributo global (del lexer) para actualizarlo
                AtributosTokens attrsGlobal = AnalizadorLexico.tablaSimbolos.get(nombreVar);
                if (attrsGlobal == null) { // No deberia pasar si el lexer funciona bien
                    attrsGlobal = new AtributosTokens(TiposToken.IDENTIFICADOR);
                    AnalizadorLexico.tablaSimbolos.put(nombreVar, attrsGlobal);
                }
                
                // Seteamos sus propiedades
                attrsGlobal.setTipoDato(this.tipo);
                attrsGlobal.setUso("variable");
                
                // Lo ponemos (o sobreescribimos) en el ambito actual
                TdA.getAmbitoActual().put(nombreVar, attrsGlobal);
                System.out.println("DEBUG: Declarada variable '" + nombreVar + "' con tipo '" + this.tipo + "'");
            }
        }
        return "void"; 
    }
}