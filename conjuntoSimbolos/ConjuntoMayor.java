package conjuntoSimbolos;
public class ConjuntoMayor extends ConjuntoSimbolos {
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == '>');
    }
}