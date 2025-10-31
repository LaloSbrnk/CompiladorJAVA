package AccionesSemanticas;
import CompiladoresMain.*;

public class AS7 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // 1. Retrocede el puntero para no consumir el carácter 'c' que finalizó la palabra.
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema();

        // 2. Verifica si el lexema es una de las palabras reservadas conocidas.
        if (TiposToken.esReservada(lexema)) {
            // --- Caso Exitoso: Es una palabra reservada ---
            AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);
            
            // Verificación de seguridad (aunque no debería fallar si la tabla está bien cargada)
            if (atributos != null) {
                token.setId(atributos.getIdToken());
                atributos.incrementarCantidad();
            }

        } else {
            // --- Caso de Error: Es una palabra en minúsculas pero no es reservada ---
            String mensajeError = "ERROR en línea " + AnalizadorLexico.numero_linea + 
                                  ": La palabra en minúsculas '" + lexema + "' no es una palabra reservada válida.";
            AnalizadorLexico.errores_y_warnings.add(mensajeError);

            // --- CORRECCIÓN ---
            // Devolvemos el token especial de error de YACC (256)
            token.setId(Parser.YYERRCODE); // <-- CAMBIAR 0 POR Parser.YYERRCODE
        }
    }

    @Override
    public String toString() {
        return "AS7";
    }
}