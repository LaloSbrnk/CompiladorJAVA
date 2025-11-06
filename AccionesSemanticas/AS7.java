package AccionesSemanticas;
import CompiladoresMain.*;

/*
    MODIFICADO: Esta acción AHORA SÓLO valida palabras reservadas
    consultando el mapa estático de TiposToken.
    Si no es una palabra reservada, es un ERROR LÉXICO (un ID
    en minúscula no permitido).
*/
public class AS7 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // 1. Retrocede el puntero para no consumir el carácter 'c'.
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema(); // Ej. "if" o "ifx"

        // 2. Busca el lexema en el mapa estático de TiposToken.
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            // --- Caso Exitoso: Es una palabra reservada ("if", "long", etc.) ---
            token.setId(idToken);
        } else {
            // --- Caso de Error: Es una palabra en minúsculas pero no es reservada ---
            // (Los identificadores válidos se manejan en AS4).
            String mensajeError = "ERROR en línea " + AnalizadorLexico.numero_linea + 
                                  ": La palabra en minúsculas '" + lexema + "' no es una palabra reservada válida.";
            AnalizadorLexico.errores_y_warnings.add(mensajeError);

            token.setId(Parser.YYERRCODE);
        }
        
        // 3. Nota: Ya no se interactúa con AnalizadorLexico.tablaSimbolos.
    }

    @Override
    public String toString() {
        return "AS7";
    }
}