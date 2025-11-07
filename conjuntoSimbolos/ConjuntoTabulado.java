package conjuntoSimbolos;
public class ConjuntoTabulado extends ConjuntoSimbolos{
    @Override
    public boolean contieneSimbolo(char simbolo){
        return (simbolo == '\t');
    }
}
