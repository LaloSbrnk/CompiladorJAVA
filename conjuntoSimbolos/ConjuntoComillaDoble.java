package conjuntoSimbolos;
public class ConjuntoComillaDoble extends ConjuntoSimbolos {
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == '"');
    }
}