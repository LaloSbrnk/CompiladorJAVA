package AccionesSemanticas;
import CompiladoresMain.*;


public class AS2 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        
        String lexema = String.valueOf(c);
        token.setLexema(lexema);

        
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            
            token.setId(idToken);
        } else {
            
            
            
            token.setId(c);
        }
        
        
        
    }

    @Override
    public String toString() {
        return "AS2";
    }
}
