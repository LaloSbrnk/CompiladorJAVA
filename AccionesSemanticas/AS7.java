package AccionesSemanticas;
import CompiladoresMain.*;

public class AS7 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        AnalizadorLexico.indice_caracter_leer--;

        String lexema = token.getLexema();

        if (TiposToken.esReservada(lexema)) {
            AtributosTokens atributos = AnalizadorLexico.tablaSimbolos.get(lexema);
            
            if (atributos != null) {
                token.setId(atributos.getIdToken());
                atributos.incrementarCantidad();
            }

        } else {
            String mensajeError = "ERROR en línea " + AnalizadorLexico.numero_linea + 
                                  ": La palabra en minúsculas '" + lexema + "' no es una palabra reservada válida.";
            AnalizadorLexico.errores_y_warnings.add(mensajeError);
        }
    }

    @Override
    public String toString() {
        return "AS7";
    }
}