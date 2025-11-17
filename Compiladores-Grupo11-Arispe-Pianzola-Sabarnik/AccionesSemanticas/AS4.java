package AccionesSemanticas;
import CompiladoresMain.*;

/**
 * --- Acción Semántica 4 (MODIFICADA) ---
 * * Identifica un token como IDENTIFICADOR.
 * * Esta es una modificación crucial para el diseño correcto de la Tabla de Símbolos:
 * * 1.  **NO** interactúa con la `AnalizadorLexico.tablaSimbolos`. 
 * La responsabilidad de agregar identificadores a la tabla (y gestionar ámbitos)
 * es *exclusivamente* del Analizador Semántico (ej. NodoDeclaracion, NodoFuncionDef)
 * durante la recorrida del AST.
 * * 2.  Su único trabajo es:
 * a) Retroceder el puntero.
 * b) Validar la longitud del identificador (truncar y advertir si es > 20).
 * c) Asignar el ID de token `TiposToken.IDENTIFICADOR`.
 * * El `AnalizadorLexico.yylex()` será el encargado de tomar el lexema de este token
 * y pasarlo como "referencia" (en `yylval.sval`) al Parser.
 */
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

        // 3. Asignar el ID de token.
        //
        // A diferencia de la implementación original, ya NO buscamos 
        // en la tabla de símbolos ni agregamos el ID aquí.
        // Simplemente lo etiquetamos como un IDENTIFICADOR.
        token.setId(TiposToken.IDENTIFICADOR);
    }

    @Override
    public String toString() {
        return "AS4";
    }
}