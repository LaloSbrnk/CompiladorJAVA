package AccionesSemanticas;
import CompiladoresMain.*;

public class AS5 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema();

        AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);

        if (atributos != null) {
            token.setId(atributos.getIdToken());
            atributos.incrementarCantidad();
        } else {
            token.setId(lexema.charAt(0));
        }
    }

    @Override
    public String toString() {
        return "AS5"; 
    }
}