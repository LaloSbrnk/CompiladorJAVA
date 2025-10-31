package AccionesSemanticas;
import CompiladoresMain.*;

import java.math.BigInteger;

public class AS8 extends AccionSemantica {

    @Override
    public void ejecutar(Token token, char c) {
        java.math.BigInteger big = null;
        // Protecciones básicas
        if (token == null || token.getLexema() == null) return;

        // lexema tal cual viene (sin signo — según tu aclaración)
        String lexema = token.getLexema();

        // Si ya existe en la tabla de símbolos, reutilizo y retorno
        AtributosTokens atributosTokens = AnalizadorLexico.tablaSimbolos.get(lexema);
        if (atributosTokens != null) {
            token.setId(atributosTokens.getIdToken());
            atributosTokens.incrementarCantidad();
            return;
        }

        // Detectar sufijo L o l (aceptamos mayúscula y minúscula)
        boolean tieneL = lexema.endsWith("L") || lexema.endsWith("l");

        // Quitar sufijo L si existe, obteniendo la parte de dígitos
        String digitos = tieneL ? lexema.substring(0, lexema.length() - 1) : lexema;
        if (digitos.isEmpty()) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Literal entero vacío antes de sufijo L");
            return;
        }
        // Validar con BigInteger para detectar overflow
        try {
            big = new BigInteger(digitos);
        } catch (NumberFormatException ex) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Formato numérico inválido para literal entero");
            return;
        }
        // Chequear rango de long
        BigInteger longMin = BigInteger.valueOf(Long.MIN_VALUE);
        BigInteger longMax = BigInteger.valueOf(Long.MAX_VALUE);
        if (big.compareTo(longMin) < 0 || big.compareTo(longMax) > 0) {
            AnalizadorLexico.errores_y_warnings.add(
                "Linea " + AnalizadorLexico.numero_linea +
                " / Posicion " + (AnalizadorLexico.indice_caracter_leer - lexema.length()) +
                " - ERROR: Literal entero con sufijo L fuera del rango de long");
            return;
        }

        long valorLong = big.longValue();

        // Crear atributos y guardarlos en tabla usando la clave tal cual (sin signo por tu aclaración)
        atributosTokens = new AtributosTokens(1, TiposToken.CTE_LONG);
        atributosTokens.setValor(valorLong); // guardamos la magnitud (el semántico aplicará el signo cuando corresponda)
        atributosTokens.setNombre_var("cte_" + AnalizadorLexico.cant_constantes);
        AnalizadorLexico.cant_constantes++;
        AnalizadorLexico.tablaSimbolos.put(lexema, atributosTokens);

        // Asociamos el token al registro creado
        token.setId(atributosTokens.getIdToken());
    }

    @Override
    public String toString() {
        return "AS8";
    }
}

