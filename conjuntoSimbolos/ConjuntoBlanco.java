package conjuntoSimbolos;
public class ConjuntoBlanco extends ConjuntoSimbolos {
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == ' ');
    }
}