package conjuntoSimbolos;
public class ConjuntoDosPuntos extends ConjuntoSimbolos{
    @Override
    public boolean contieneSimbolo(char simbolo) {
        return (simbolo == ':');
    }
}
