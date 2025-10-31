package AccionesSemanticas;
import CompiladoresMain.Token;

public abstract class AccionSemantica {
    public abstract void ejecutar(Token token, char c);
}
