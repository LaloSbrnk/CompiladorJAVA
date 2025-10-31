package AccionesSemanticas;
import CompiladoresMain.*;

/*
    Accion Semantica 11:
    Incrementa el contador de lineas.
    Usado para \n cuando es consumido por el automata (whitespace o comentarios).
*/
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