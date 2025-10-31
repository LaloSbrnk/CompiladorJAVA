package AccionesSemanticas;
import CompiladoresMain.*;

/*
    AS2: Establece el carácter como lexema y retorna el token asociado
    para símbolos de un solo carácter como ',' ';' '+' etc.
 */
public class AS2 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // 1. El carácter 'c' es el lexema completo.
        String lexema = String.valueOf(c);
        token.setLexema(lexema);

        // 2. Busca el lexema en la tabla de símbolos.
        AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);

        if (atributos != null) {
            // 3. Si se encuentra, se asigna el ID predefinido.
            token.setId(atributos.getIdToken());
            atributos.incrementarCantidad();
        } else {
            // 4. Plan B (Fallback): Si por alguna razón no está en la tabla,
            // se usa el código ASCII del propio carácter como ID. Esto es robusto.
            token.setId(c);
        }
    }

    @Override
    public String toString() {
        return "AS2";
    }
}
