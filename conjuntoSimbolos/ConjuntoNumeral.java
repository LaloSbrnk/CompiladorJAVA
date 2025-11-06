package conjuntoSimbolos;
public class ConjuntoNumeral extends ConjuntoSimbolos{
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == '#');
    }
}
