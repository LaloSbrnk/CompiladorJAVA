package AccionesSemanticas;
import CompiladoresMain.*;

// ---  Identificar tokens de palabras reservadas e indentificadores ---

public class AS4 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // 1. Retroceder el puntero para no consumir el carácter 'c'.
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema();

        // 2. Verificar la longitud del identificador.
        if (lexema.length() > 20) {
            // Si excede los 20 caracteres, se trunca.
            lexema = lexema.substring(0, 20);
            token.setLexema(lexema);

            // Se genera el WARNING correspondiente.
            String warningMsg = "WARNING en línea " + AnalizadorLexico.numero_linea +
                                ": El identificador es demasiado largo, fue truncado a '" + lexema + "'";
            AnalizadorLexico.errores_y_warnings.add(warningMsg);
        }

        // 3. Buscar el lexema (ya truncado, si fue el caso) en la Tabla de Símbolos.
        AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);
        
        if (atributos != null) {
            // --- Caso Exitoso: El lexema ya estaba en la tabla ---
            // (Podría ser una palabra reservada en mayúsculas si el lenguaje lo permitiera,
            // o un identificador ya visto).
            atributos.incrementarCantidad();
            token.setId(atributos.getIdToken());
        } else {
            // --- Caso Nuevo: Es un identificador que no se había visto antes ---
            // Se crea una nueva entrada en la tabla para este identificador.
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