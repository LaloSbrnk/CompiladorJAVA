package AccionesSemanticas;
import CompiladoresMain.*;

// ---  Identificar tokens de palabras reservadas e indentificadores ---

public class AS4 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema();

        if (lexema.length() > 20) {
            lexema = lexema.substring(0, 20);
            token.setLexema(lexema);

            // Se genera el WARNING correspondiente.
            String warningMsg = "WARNING en línea " + AnalizadorLexico.numero_linea +
                                ": El identificador es demasiado largo, fue truncado a '" + lexema + "'";
            AnalizadorLexico.errores_y_warnings.add(warningMsg);
        }

        AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);
        
        if (atributos != null) {
   
            atributos.incrementarCantidad();
            token.setId(atributos.getIdToken());
        } else {
        	
            atributos = new AtributosTokens(1, TiposToken.IDENTIFICADOR);
            AnalizadorLexico.tablaSimbolos.put(lexema, atributos);
            token.setId(TiposToken.IDENTIFICADOR);
        }
    }

    @Override
    public String toString() {
        return "AS4";
    }
}