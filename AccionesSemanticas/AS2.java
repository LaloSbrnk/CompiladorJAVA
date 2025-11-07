package AccionesSemanticas;
import CompiladoresMain.*;

/*
    AS2: Establece el carácter como lexema y retorna el token asociado
    para símbolos de un solo carácter como ',' ';' '+' etc.
    
    MODIFICADO: Ya no consulta la tablaSimbolos global, sino el
    mapa estático de TiposToken.
 */
public class AS2 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // 1. El carácter 'c' es el lexema completo.
        String lexema = String.valueOf(c);
        token.setLexema(lexema);

        // 2. Busca el lexema en el mapa estático de TiposToken.
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            // 3. Si se encuentra, se asigna el ID predefinido.
            token.setId(idToken);
        } else {
            // 4. Plan B (Fallback): Si por alguna razón no está en el mapa estático,
            // (esto no debería pasar si TiposToken está completo)
            // se usa el código ASCII del propio carácter como ID.
            token.setId(c);
        }
        
        // 5. Nota: Ya no se interactúa con AnalizadorLexico.tablaSimbolos
        //    ni se incrementa ninguna cantidad aquí.
    }

    @Override
    public String toString() {
        return "AS2";
    }
}