package conjuntoSimbolos;

public class ConjuntoMinus extends ConjuntoSimbolos{
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return Character.isLowerCase(simbolo);
    }
}