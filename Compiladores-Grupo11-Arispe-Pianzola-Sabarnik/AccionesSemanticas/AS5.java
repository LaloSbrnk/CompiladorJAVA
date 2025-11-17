package AccionesSemanticas;
import CompiladoresMain.*;

/*
    MODIFICADO: Ya no consulta la tablaSimbolos global, sino el
    mapa estático de TiposToken.
*/
public class AS5 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // 1. Retrocede el puntero. El carácter 'c' no es parte de este token.
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema(); // Ej. "<"

        // 2. Busca el lexema (ej. "<") en el mapa estático de TiposToken.
        int idToken = TiposToken.getIdEstatico(lexema);

        if (idToken != -1) {
            // 3. Si se encuentra, se asigna el ID predefinido.
            token.setId(idToken);
        } else {
            // 4. Plan B (Fallback): Si no está, se usa el código ASCII.
            //    (Ej. para el guion bajo '_', que también usa AS5)
            token.setId(lexema.charAt(0));
        }
        
        // 5. Nota: Ya no se interactúa con AnalizadorLexico.tablaSimbolos.
    }

    @Override
    public String toString() {
        return "AS5";
    }
}