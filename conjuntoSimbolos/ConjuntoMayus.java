package conjuntoSimbolos;
public class ConjuntoMayus extends ConjuntoSimbolos{
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return Character.isUpperCase(simbolo);
    }
}