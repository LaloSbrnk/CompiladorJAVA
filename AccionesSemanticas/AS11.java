package AccionesSemanticas;
import CompiladoresMain.*;
public class AS11 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        AnalizadorLexico.numero_linea++;
    }
    @Override
    public String toString() {
        return "AS11";
    }
}