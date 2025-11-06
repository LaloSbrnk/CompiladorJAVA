package AccionesSemanticas;
import CompiladoresMain.*;
public class AS10 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        AtributosTokens atributosTokens = AnalizadorLexico.tablaSimbolos.get(token.getLexema());
        if (atributosTokens != null) {
            atributosTokens.incrementarCantidad();
            token.setId(atributosTokens.getCantidad());
        } else {
            atributosTokens = new AtributosTokens(1, TiposToken.CADENA);
            token.setId(atributosTokens.getIdToken());
            AnalizadorLexico.tablaSimbolos.put(token.getLexema(), atributosTokens);
            token.setId(TiposToken.CADENA);
        }
    }
    @Override
    public String toString() {
        return "AS10";
    }
}
