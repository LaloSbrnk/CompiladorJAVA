package conjuntoSimbolos;
public class ConjuntoDigito extends ConjuntoSimbolos {
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return Character.isDigit(simbolo);
    }
}
