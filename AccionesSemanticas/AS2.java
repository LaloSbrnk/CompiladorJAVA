package AccionesSemanticas;
import CompiladoresMain.*;

/*
    AS2: Establece el carácter como lexema y retorna el token asociado
    para símbolos de un solo carácter como ',' ';' '+' etc.
 */
public class AS2 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        String lexema = String.valueOf(c);
        token.setLexema(lexema);

        AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);

        if (atributos != null) {
            token.setId(atributos.getIdToken());
            atributos.incrementarCantidad();
        } else {
            token.setId(c);
        }
    }

    @Override
    public String toString() {
        return "AS2";
    }
}
