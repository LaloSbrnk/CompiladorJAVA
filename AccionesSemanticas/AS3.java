package AccionesSemanticas;
import CompiladoresMain.*;

/*
    Concatenar caracter
 */
public class AS3 extends AccionSemantica{
    @Override
    public void ejecutar(Token token, char c) {
        token.setLexema(token.getLexema() + c);
    }

    @Override
    public String toString() {
        return "AS3";
    }
}
