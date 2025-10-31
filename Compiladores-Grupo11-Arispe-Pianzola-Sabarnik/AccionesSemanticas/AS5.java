package AccionesSemanticas;
import CompiladoresMain.*;

public class AS5 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // 1. Retrocede el puntero. El carácter 'c' no es parte de este token.
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema();

        // 2. Busca el lexema (ej. "<") en la tabla de símbolos.
        AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);

        if (atributos != null) {
            // 3. Si se encuentra, se asigna el ID predefinido.
            token.setId(atributos.getIdToken());
            atributos.incrementarCantidad();
        } else {
            // 4. Plan B (Fallback): Si no está, se usa el código ASCII.
            token.setId(lexema.charAt(0));
        }
    }

    @Override
    public String toString() {
        return "AS5"; // Corregido el toString que antes decía AS6
    }
}