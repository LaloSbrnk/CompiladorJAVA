package AccionesSemanticas;
import CompiladoresMain.*;


public class AS7 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema(); 

        
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            
            token.setId(idToken);
        } else {
            
            
            String mensajeError = "ERROR en línea " + AnalizadorLexico.numero_linea + 
                                  ": La palabra en minúsculas '" + lexema + "' no es una palabra reservada válida.";
            AnalizadorLexico.errores_y_warnings.add(mensajeError);

            token.setId(Parser.YYERRCODE);
        }
        
        
    }

    @Override
    public String toString() {
        return "AS7";
    }
}
