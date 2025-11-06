package conjuntoSimbolos;
public class ConjuntoMas extends ConjuntoSimbolos {
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == '+');
    }
}