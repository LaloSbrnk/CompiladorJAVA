package AccionesSemanticas;
import CompiladoresMain.*;


public class AS5 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema(); 

        
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            
            token.setId(idToken);
        } else {
            
            
            token.setId(lexema.charAt(0));
        }
        
        
    }

    @Override
    public String toString() {
        return "AS5";
    }
}
