package AccionesSemanticas;
import CompiladoresMain.*;


public class AS4 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema();

        
        if (lexema.length() > 20) {
            
            lexema = lexema.substring(0, 20);
            token.setLexema(lexema);

            
            String warningMsg = "WARNING en línea " + AnalizadorLexico.numero_linea +
                                ": El identificador es demasiado largo, fue truncado a '" + lexema + "'";
            AnalizadorLexico.errores_y_warnings.add(warningMsg);
        }

        
        
        
        
        
        token.setId(TiposToken.IDENTIFICADOR);
    }

    @Override
    public String toString() {
        return "AS4";
    }
}
