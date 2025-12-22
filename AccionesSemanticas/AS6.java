package AccionesSemanticas;
import CompiladoresMain.*;


public class AS6 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        
        token.setLexema(token.getLexema() + c);
        String lexema = token.getLexema(); 

        
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            
            token.setId(idToken);
        } else {
            
            
            AnalizadorLexico.errores_y_warnings.add(
                "Error Interno: El operador '" + lexema + "' no es un símbolo válido."
            );
            token.setId(Parser.YYERRCODE); 
        }
        
        
    }

    @Override
    public String toString() {
        return "AS6";
    }
}
