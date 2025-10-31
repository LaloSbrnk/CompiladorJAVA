package AccionesSemanticas;
import CompiladoresMain.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AS9 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        // mantener comportamiento original (volver a leer el caracter)
        AnalizadorLexico.indice_caracter_leer--;

        // defensivo
        if (token == null || token.getLexema() == null) return;
        String lexema = token.getLexema();

        // Si ya está en la tabla, reutilizo
        AtributosTokens atributosTokens = AnalizadorLexico.tablaSimbolos.get(lexema);
        if (atributosTokens != null) {
            token.setId(atributosTokens.getIdToken());
            atributosTokens.incrementarCantidad();
            return;
        }
        // Separar parte base y (si hay) exponente
        int posD = lexema.indexOf('D'); 
        String baseStr = (posD != -1) ? lexema.substring(0, posD) : lexema;
        double base;
        int exponente = 0;

        try {
            // Double.parseDouble acepta "+.5", "5." y ".5"
            base = Double.parseDouble(baseStr);

            if (posD != -1) {
                String expStr = lexema.substring(posD + 1); // empieza con + o -
                // por seguridad comprobamos que el signo del exponente esté presente
                if (expStr.length() < 2 || (expStr.charAt(0) != '+' && expStr.charAt(0) != '-')) {
                    AnalizadorLexico.errores_y_warnings.add(
                        "Linea " + AnalizadorLexico.numero_linea +
                        " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                        " - ERROR: Exponente con D debe llevar signo obligatorio (+/-).");
                    return;
                }
                // parseo seguro del exponente (Integer es suficiente, si es muy grande el valor será Infinity)
                exponente = Integer.parseInt(expStr); // puede lanzar NumberFormatException
            }
        } catch (NumberFormatException ex) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Número inválido en parte base o exponente (" + lexema + ")");
            return;
        }

        // Calcular el valor: si hay D => base * 10^exponente; si no => base
        double valor;
        if (posD != -1) {
            valor = base * Math.pow(10.0, exponente);
        } else {
            valor = base;
        }

        // Validación de rango según tu imagen:
        //  2.2250738585072014D-308 < |x| < 1.7976931348623157D+308  OR  x == 0.0
        // Usamos Double.MIN_NORMAL (≈2.2250738585072014e-308) y Double.MAX_VALUE
        double abs = Math.abs(valor);
        boolean numeroValido = false;

        if (valor == 0.0) {
            numeroValido = true; // permitimos exactamente 0.0
        } else if (Double.isFinite(valor) && abs > Double.MIN_NORMAL && abs < Double.MAX_VALUE) {
            numeroValido = true;
        }

        if (!numeroValido) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Constante de punto flotante fuera de rango (" + lexema + ")");
            return;
        }

        // Si es válido, crear AtributosTokens y guardar
        atributosTokens = new AtributosTokens(1, TiposToken.CTE_DFLOAT);
        atributosTokens.setValor(valor);
        atributosTokens.setNombre_var("cte_" + AnalizadorLexico.cant_constantes);
        AnalizadorLexico.cant_constantes++;
        AnalizadorLexico.tablaSimbolos.put(lexema, atributosTokens);
        token.setId(atributosTokens.getIdToken());
    }

    @Override
    public String toString() {
        return "AS9";
    }
}
