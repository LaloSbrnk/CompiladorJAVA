package conjuntoSimbolos;

public class ConjuntoExclamasion extends ConjuntoSimbolos{
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == '!');
    }
}
