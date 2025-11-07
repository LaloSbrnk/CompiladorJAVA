package AccionesSemanticas;
import CompiladoresMain.*;

public class AS10 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        String lexema = token.getLexema(); // <--- OBTENER LEXEMA
        AtributosTokens atributosTokens = AnalizadorLexico.tablaSimbolos.get(lexema);
        
        if (atributosTokens != null) {
            atributosTokens.incrementarCantidad();
            token.setId(atributosTokens.getCantidad());
        } else {
            atributosTokens = new AtributosTokens(1, TiposToken.CADENA);
            
            // --- INICIO DE LA CORRECCIÓN ---
            // Guardar el valor real de la cadena (sin las comillas)
            // El lexema del token actualmente incluye las comillas (ej: "hola")
            // por cómo funciona la AS3 en el estado 13.
            // Para el valor, las quitamos.
            String valorCadena = lexema;
            if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
                 valorCadena = lexema.substring(1, lexema.length() - 1);
            }
            atributosTokens.setValor(valorCadena); 
            // --- FIN DE LA CORRECCIÓN ---

            token.setId(atributosTokens.getIdToken());
            AnalizadorLexico.tablaSimbolos.put(lexema, atributosTokens); // Usar el lexema original ("hola") como clave
            token.setId(TiposToken.CADENA);
        }
    }
    
    @Override
    public String toString() {
        return "AS10";
    }
}