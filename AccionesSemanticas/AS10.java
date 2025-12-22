package AccionesSemanticas;
import CompiladoresMain.*;

public class AS10 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        String lexema = token.getLexema(); 
        AtributosTokens atributosTokens = AnalizadorLexico.tablaSimbolos.get(lexema);
        
        if (atributosTokens != null) {
            atributosTokens.incrementarCantidad();
            token.setId(atributosTokens.getCantidad());
        } else {
            atributosTokens = new AtributosTokens(1, TiposToken.CADENA);
            
            
            
            
            
            
            String valorCadena = lexema;
            if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
                 valorCadena = lexema.substring(1, lexema.length() - 1);
            }
            atributosTokens.setValor(valorCadena); 
            

            token.setId(atributosTokens.getIdToken());
            AnalizadorLexico.tablaSimbolos.put(lexema, atributosTokens); 
            token.setId(TiposToken.CADENA);
        }
    }
    
    @Override
    public String toString() {
        return "AS10";
    }
}
