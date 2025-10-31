package AccionesSemanticas;
import CompiladoresMain.*;

public class AS6 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        token.setLexema(token.getLexema() + c);
        AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(token.getLexema());

        if (atributos != null) {
            token.setId(atributos.getIdToken());
            atributos.incrementarCantidad();
        } else {
            // Si no se encuentra, solo se registra el error.
            AnalizadorLexico.errores_y_warnings.add("Error interno: Operador '" + token.getLexema() + "' no encontrado en la tabla de símbolos.");
        }
    }

    @Override
    public String toString() {
        return "AS6";
    }
}