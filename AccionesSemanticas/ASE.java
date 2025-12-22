package AccionesSemanticas;
import CompiladoresMain.*;

public class ASE extends AccionSemantica{
    @Override
    public void ejecutar(Token token, char c) {
        
        
        String ubicacion = "Linea " + AnalizadorLexico.numero_linea +
                         " / Posicion " + (AnalizadorLexico.indice_caracter_leer - 1);

        String mensajeError = "";

        switch (AnalizadorLexico.estado_actual) {
            case 0:
                
                if (c == '%') {
                    mensajeError = "ERROR: El simbolo '%' solo es valido dentro de un identificador, no al inicio.";
                } else if (c == '!') {
                    mensajeError = "ERROR: El simbolo '!' solo es valido como parte del operador '!='.";
                } else {
                    mensajeError = "ERROR: '" + c + "' no es un caracter valido dentro del lenguaje.";
                }
                break;
            
            case 4:
                
                mensajeError = "ERROR: Se esperaba un '=' para formar el operador de asignacion ':=', se encontro '" + c + "'.";
                break;

            case 7:
                
                mensajeError = "ERROR: Constante numerica mal formada. Se esperaba un digito, un punto '.', o el sufijo 'L', se encontro '" + c + "'.";
                break;

            case 8:
                
                mensajeError = "ERROR: Formato de constante flotante invalido. Luego del punto debe seguir un digito.";
                break;
            
            case 10:
                
                mensajeError = "ERROR: Formato de exponente invalido. Luego de 'D' debe seguir un signo '+' o '-'.";
                break;

            case 11:
                
                mensajeError = "ERROR: Formato de exponente invalido. Luego del signo debe seguir al menos un digito.";
                break;

            case 13:
                if (c == '\n') {
                    mensajeError = "ERROR: Salto de linea inesperado. Las cadenas de caracteres (Tema 7) no pueden ser multilineas.";
                } else {
                    mensajeError = "ERROR: Cadena mal cerrada. Se esperaba '\"'."; 
                }
                break;
                
            case 14:
                mensajeError = "ERROR: Comentario mal formado (Tema 33). Se esperaba un segundo '#' para iniciar un comentario '##', se encontro '" + c + "'.";
                break;

            default:
                
                mensajeError = "ERROR: Error lexico desconocido en estado " + AnalizadorLexico.estado_actual + " al leer '" + c + "'.";
                break;
        }

        
        AnalizadorLexico.errores_y_warnings.add(ubicacion + " - " + mensajeError);
    }

    @Override
    public String toString() {
        return "ASE";
    }
}

