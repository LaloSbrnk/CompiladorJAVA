package ArbolSintactico;

import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.AnalizadorLexico; 
import CompiladoresMain.AtributosTokens; 
import CompiladoresMain.GeneradorAssembler; 
import java.util.Map; 
import CompiladoresMain.TiposToken;

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
        System.out.println(prefijo + "Constante (" + tipo + "): " + valor.toString());
    }

    // --- INICIO DE LA CORRECCIÓN (error A2004) ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        // Si la constante es 'long', devolvemos el valor inmediato.
        if ("long".equals(this.tipo)) {
            return this.valor.toString();
        }

        // Si es 'dfloat' o 'string', debemos devolver el nombre de la
        // variable en .DATA. Buscamos su lexema original en la TS.
        
        String lexema = null;
        for (Map.Entry<String, AtributosTokens> entry : AnalizadorLexico.tablaSimbolos.entrySet()) {
            AtributosTokens attrs = entry.getValue();
            if (attrs.getValor() != null && attrs.getValor().equals(this.valor)) {
                
                if ("dfloat".equals(this.tipo) && attrs.getIdToken() == TiposToken.CTE_DFLOAT) {
                    lexema = entry.getKey();
                    break;
                } else if ("string".equals(this.tipo) && attrs.getIdToken() == TiposToken.CADENA) {
                    lexema = entry.getKey(); 
                    break;
                }
            }
        }

        if (lexema == null) {
            // Fallback (no debería ocurrir en este caso, pero es seguro)
            lexema = this.valor.toString();
        }
        
        // Devolvemos el nombre de memoria (ej: _A_es_mayor_a_5)
        return G.getNombreAsm(lexema);
    }
    // --- FIN DE LA CORRECCIÓN ---
}