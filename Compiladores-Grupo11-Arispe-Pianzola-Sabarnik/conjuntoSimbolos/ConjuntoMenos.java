package conjuntoSimbolos;

public class ConjuntoMenos extends ConjuntoSimbolos {
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == '-');
    }
}