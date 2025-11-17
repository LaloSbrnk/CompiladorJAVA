package AccionesSemanticas;
import CompiladoresMain.*;

/*
    MODIFICADO: Ya no consulta la tablaSimbolos global, sino el
    mapa estático de TiposToken.
*/
public class AS6 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        // 1. Concatena el último caracter
        token.setLexema(token.getLexema() + c);
        String lexema = token.getLexema(); // Ej. ":="

        // 2. Busca el lexema (ej. ":=") en el mapa estático de TiposToken.
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            // 3. Si se encuentra, se asigna el ID predefinido.
            token.setId(idToken);
        } else {
            // 4. Error: Esto no debería ocurrir si el autómata está bien definido
            //    (ej. si se forma un lexema como ":+" que no existe).
            AnalizadorLexico.errores_y_warnings.add(
                "Error Interno: El operador '" + lexema + "' no es un símbolo válido."
            );
            token.setId(Parser.YYERRCODE); // Devuelve error
        }
        
        // 5. Nota: Ya no se interactúa con AnalizadorLexico.tablaSimbolos.
    }

    @Override
    public String toString() {
        return "AS6";
    }
}