package CompiladoresMain;

import java.util.HashMap;

public class TiposToken {

    // --- Tokens de Categorías Dinámicas (van a la Tabla de Símbolos) ---
    public static final int IDENTIFICADOR = 257;
    public static final int CTE_LONG = 259;
    public static final int CTE_DFLOAT = 260;
    public static final int CADENA = 258;

    // --- Palabras Reservadas (Símbolos Estáticos) ---
    public static final int IF = 261;
    public static final int ELSE = 262;
    public static final int ENDIF = 263;
    public static final int PRINT = 264;
    public static final int RETURN = 265;
    public static final int LONG = 266;
    public static final int DFLOAT = 267;
    public static final int VAR = 268;
    public static final int DO = 269;
    public static final int UNTIL = 270;
    public static final int CV = 271;
    public static final int SL = 272;
    public static final int LE = 273;
    
    // --- Operadores (Símbolos Estáticos) ---
    public static final int FLECHA = 279;
    public static final int ASIGNACION = 274;      // :=
    public static final int MAYOR_IGUAL = 275;     // >=
    public static final int MENOR_IGUAL = 276;     // <=
    public static final int DISTINTO = 277;        // =! (Según tablaSimbolos.txt)
    public static final int IGUAL = 278;           // ==

    /**
     * Esta es la nueva estructura para almacenar SÍMBOLOS ESTÁTICOS.
     * Contiene todas las palabras reservadas, operadores y puntuación.
     * La Tabla de Símbolos global (en AnalizadorLexico) ya NO contendrá esto,
     * solo contendrá los símbolos dinámicos (IDs, Ctes, Cadenas).
     */
    private static final HashMap<String, Integer> SIMBOLOS_ESTATICOS = new HashMap<>();

    /**
     * Bloque estático para inicializar el HashMap de símbolos estáticos.
     * Esta información se toma del archivo 'tablaSimbolos.txt' original.
     */
    static {
        // Palabras Reservadas
        SIMBOLOS_ESTATICOS.put("if", IF);
        SIMBOLOS_ESTATICOS.put("else", ELSE);
        SIMBOLOS_ESTATICOS.put("endif", ENDIF);
        SIMBOLOS_ESTATICOS.put("print", PRINT);
        SIMBOLOS_ESTATICOS.put("return", RETURN);
        SIMBOLOS_ESTATICOS.put("long", LONG);
        SIMBOLOS_ESTATICOS.put("dfloat", DFLOAT);
        SIMBOLOS_ESTATICOS.put("var", VAR);
        SIMBOLOS_ESTATICOS.put("do", DO);
        SIMBOLOS_ESTATICOS.put("until", UNTIL);
        SIMBOLOS_ESTATICOS.put("cv", CV);
        SIMBOLOS_ESTATICOS.put("sl", SL);
        SIMBOLOS_ESTATICOS.put("le", LE);

        // Operadores de Múltiples Caracteres
        SIMBOLOS_ESTATICOS.put(":=", ASIGNACION);
        SIMBOLOS_ESTATICOS.put(">=", MAYOR_IGUAL);
        SIMBOLOS_ESTATICOS.put("<=", MENOR_IGUAL);
        SIMBOLOS_ESTATICOS.put("=!", DISTINTO);
        SIMBOLOS_ESTATICOS.put("==", IGUAL);
        SIMBOLOS_ESTATICOS.put("->", FLECHA);

        // Símbolos Simples (usamos su valor ASCII como ID, igual que en tablaSimbolos.txt)
        SIMBOLOS_ESTATICOS.put("\"", 34);
        SIMBOLOS_ESTATICOS.put("%", 37);
        SIMBOLOS_ESTATICOS.put("(", 40);
        SIMBOLOS_ESTATICOS.put(")", 41);
        SIMBOLOS_ESTATICOS.put("*", 42);
        SIMBOLOS_ESTATICOS.put("+", 43);
        SIMBOLOS_ESTATICOS.put(",", 44);
        SIMBOLOS_ESTATICOS.put("-", 45);
        SIMBOLOS_ESTATICOS.put(".", 46);
        SIMBOLOS_ESTATICOS.put("/", 47);
        SIMBOLOS_ESTATICOS.put(";", 59);
        SIMBOLOS_ESTATICOS.put("<", 60);
        SIMBOLOS_ESTATICOS.put("=", 61);
        SIMBOLOS_ESTATICOS.put(">", 62);
        SIMBOLOS_ESTATICOS.put("_", 95);
        SIMBOLOS_ESTATICOS.put("{", 123);
        SIMBOLOS_ESTATICOS.put("}", 125);
    }

    /**
     * Nuevo método público para que las Acciones Semánticas (AS)
     * consulten si un lexema es un símbolo estático.
     *
     * @param lexema El lexema a consultar.
     * @return El ID del token si es un símbolo estático, o -1 si no lo es.
     */
    public static int getIdEstatico(String lexema) {
        // Devuelve el ID si existe, o -1 (valor por defecto) si no se encuentra.
        return SIMBOLOS_ESTATICOS.getOrDefault(lexema, -1);
    }
}