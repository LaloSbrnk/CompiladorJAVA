package AccionesSemanticas;
import CompiladoresMain.*;

public class ASE extends AccionSemantica{
    @Override
    public void ejecutar(Token token, char c) {
        switch (AnalizadorLexico.estado_actual) {
            case 0:
                AnalizadorLexico.errores_y_warnings.add("Linea " + AnalizadorLexico.numero_linea +
                        " / Posicion " + (AnalizadorLexico.indice_caracter_leer - 1) +
                        " - ERROR: '" + c + "' no es un caracter valido dentro del lenguaje");
                break;
            case 3:
                AnalizadorLexico.errores_y_warnings.add("Linea " + AnalizadorLexico.numero_linea +
                        " / Posicion " + (AnalizadorLexico.indice_caracter_leer - 1) +
                        " - ERROR: Formato de constante numerica flotante invalido. Luego del punto debe seguir un digito.");
                break;
            case 5:
                AnalizadorLexico.errores_y_warnings.add("Linea " + AnalizadorLexico.numero_linea +
                        " / Posicion " + (AnalizadorLexico.indice_caracter_leer - 1) +
                        " - ERROR: Formato de constante numerica flotante invalido. Luego del signo 'd' debe seguir el signo o la parte numerica del exponente (al menos un digito)");
                break;
            case 7:
            case 8:
                AnalizadorLexico.errores_y_warnings.add("Linea " + AnalizadorLexico.numero_linea +
                        " / Posicion " + (AnalizadorLexico.indice_caracter_leer - 1) +
                        " - ERROR: Formato de constante numerica octal invalido.");
                break;
            case 13:
                AnalizadorLexico.errores_y_warnings.add("Linea " + AnalizadorLexico.numero_linea +
                        " / Posicion " + (AnalizadorLexico.indice_caracter_leer - 1) +
                        " - ERROR: Se esperaba simbolo '=', se encontro '" + token.getLexema().charAt(token.getLexema().length() - 1) +
                        "'.");
                break;
            case 14:
                AnalizadorLexico.errores_y_warnings.add("Linea " + AnalizadorLexico.numero_linea +
                        " / Posicion " + (AnalizadorLexico.indice_caracter_leer - 1) +
                        " - ERROR: Se esperaba simbolo '}' previo al salto de linea.");
            break;
        }
    }

    @Override
    public String toString() {
        return "ASE";
    }
}