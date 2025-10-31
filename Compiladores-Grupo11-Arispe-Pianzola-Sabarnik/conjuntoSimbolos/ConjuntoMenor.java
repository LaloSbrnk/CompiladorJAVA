package conjuntoSimbolos;

public class ConjuntoMenor extends ConjuntoSimbolos {
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == '<');
    }
}