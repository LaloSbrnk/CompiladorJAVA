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
        String scopeActual = TdA.getMangledScope(); 
        for (String nombreVar : variables) {
            String mangledName = nombreVar + scopeActual; 
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(mangledName);
            if (attrs != null && attrs.getUso() != null) {
                System.err.println("ERROR Semantico: Redeclaracion de variable '" + nombreVar + "' en el ambito " + scopeActual);
            } else {
                if (attrs == null) {
                    attrs = new AtributosTokens(TiposToken.IDENTIFICADOR);
                }
                attrs.setTipoDato(this.tipo);
                attrs.setUso("variable");
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

    
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        
        
        return null;
    }
}
