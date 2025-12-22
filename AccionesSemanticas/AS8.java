package AccionesSemanticas;
import CompiladoresMain.*;

import java.math.BigInteger;
public class AS8 extends AccionSemantica {
    @Override
    public void ejecutar(Token token, char c) {
        java.math.BigInteger big = null;
        if (token == null || token.getLexema() == null) {
        token.setId(Parser.YYERRCODE);
        return;
        }
        String lexema = token.getLexema();
        AtributosTokens atributosTokens = AnalizadorLexico.tablaSimbolos.get(lexema);
        if (atributosTokens != null) {
            token.setId(atributosTokens.getIdToken());
            atributosTokens.incrementarCantidad();
            return;
        }
        boolean tieneL = lexema.endsWith("L") || lexema.endsWith("l");
        String digitos = tieneL ? lexema.substring(0, lexema.length() - 1) : lexema;
        if (digitos.isEmpty()) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Literal entero vacío antes de sufijo L");
                token.setId(Parser.YYERRCODE);
            return;
        }
        try {
            big = new BigInteger(digitos);
        } catch (NumberFormatException ex) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Formato numérico inválido para literal entero");
                token.setId(Parser.YYERRCODE);
            return;
        }
        BigInteger longMin = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger longMax = BigInteger.valueOf(Integer.MAX_VALUE);
        if (big.compareTo(longMin) < 0 || big.compareTo(longMax) > 0) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Literal entero con sufijo L fuera del rango de long");
            return;
        }
        long valorLong = big.longValue();
        atributosTokens = new AtributosTokens(1, TiposToken.CTE_LONG);
        atributosTokens.setValor(valorLong); 
        atributosTokens.setNombre_var("cte_" + AnalizadorLexico.cant_constantes);
        AnalizadorLexico.cant_constantes++;
        AnalizadorLexico.tablaSimbolos.put(lexema, atributosTokens);
        token.setId(atributosTokens.getIdToken());
    }
    @Override
    public String toString() {
        return "AS8";
    }
}

