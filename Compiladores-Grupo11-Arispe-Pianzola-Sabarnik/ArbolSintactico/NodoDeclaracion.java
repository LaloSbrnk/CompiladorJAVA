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
        String scopeActual = TdA.getMangledScope(); // Ej: ":MAIN:F1"

        for (String nombreVar : variables) {
            
            String mangledName = nombreVar + scopeActual; // Ej: "X:MAIN:F1"

            // 1. Chequea si ya existe ESE nombre en la tabla general
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(mangledName);

            if (attrs != null && attrs.getUso() != null) {
                // Si existe y YA tiene un uso (variable, parametro, funcion), es redeclaracion
                System.err.println("ERROR Semantico: Redeclaracion de variable '" + nombreVar + "' en el ambito " + scopeActual);
            } else {
                // Si attrs es null, significa que el lexer no lo vio.
                // Si no es null, es una entrada del lexer (ID) que vamos a "promocionar"
                if (attrs == null) {
                    attrs = new AtributosTokens(TiposToken.IDENTIFICADOR);
                }
                
                // Seteamos sus propiedades
                attrs.setTipoDato(this.tipo);
                attrs.setUso("variable");
                
                // Lo ponemos (o sobreescribimos la entrada del lexer) en la tabla general
                // ¡Usamos TdA.agregar() que ya hace esto y setea el mangledName!
                TdA.agregar(nombreVar, attrs);
                
            }
        }
        return "void"; 
    }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Declaracion (Tipo: " + tipo + ")");
        for (String var : variables) {
            System.out.println(prefijo + "  " + "Variable: " + var);
        }
    }
}