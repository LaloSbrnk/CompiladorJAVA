package AccionesSemanticas;
import CompiladoresMain.*;
public class AS1 extends AccionSemantica{
    private String lexema;
    public AS1() {
    }
    @Override
    public void ejecutar(Token token, char c) {
        token.setLexema(String.valueOf(c));
    }
    @Override
    public String toString() {
        return "AS1";
    }
}

