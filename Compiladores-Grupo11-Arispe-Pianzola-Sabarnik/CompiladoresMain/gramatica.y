%{

%}

// --- Declaración de Tokens ---
%token <sval> IDENTIFICADOR CADENA
%token <ival> CTE_LONG
%token <dval> CTE_DFLOAT

// Palabras reservadas y operadores que devuelve el léxico.
%token IF ELSE ENDIF PRINT RETURN
%token LONG DFLOAT VAR DO UNTIL
%token CV SL LE
%token ASIGNACION MAYOR_IGUAL MENOR_IGUAL DISTINTO IGUAL FLECHA

// --- Precedencia y Asociatividad ---
%left '+' '-'
%left '*' '/'
%right UMINUS // Precedencia para el menos unario.

%%

/* --- Reglas de la Gramática --- */

programa
    : IDENTIFICADOR '{' lista_sentencias '}'
        { System.out.println("Programa completo reconocido."); }
    ;

lista_sentencias
    : /* vacío */
    | lista_sentencias sentencia
    ;

sentencia
    : declarativa ';'
    | ejecutable ';'
    | funcion_def
    // Regla para recuperación de errores.
    | error ';' { System.err.println("ERROR Sintáctico recuperado en línea " + lexer.numero_linea); }
    ;


/* --- Definiciones --- */

// Tema 10: Declaraciones (explícita e inferencia).
declarativa
    : tipo lista_variables
        { System.out.println("Línea " + lexer.numero_linea + ": Declaración de variables con tipo explícito."); }
    | VAR asignacion
        { System.out.println("Línea " + lexer.numero_linea + ": Declaración de variable por inferencia."); }
    ;

lista_variables
    : IDENTIFICADOR
    | lista_variables ',' IDENTIFICADOR
    ;

tipo
    : LONG
    | DFLOAT
    ;

// Tema 20: Funciones con múltiples tipos de retorno.
funcion_def
    : lista_tipos IDENTIFICADOR '(' parametros_formales_opt ')' '{' cuerpo_funcion '}'
        { System.out.println("Línea " + lexer.numero_linea + ": Definición de función '" + $2 + "' reconocida."); }
    ;

lista_tipos
    : tipo
    | lista_tipos ',' tipo
    ;

parametros_formales_opt
    : /* vacío */
    | parametros_formales
    ;

parametros_formales
    : parametro_formal
    | parametros_formales ',' parametro_formal
    ;

// Tema 24: Pasaje de parámetros (cv sl, cv le).
parametro_formal
    : opt_sem_pasaje tipo IDENTIFICADOR
    ;

opt_sem_pasaje
    : /* vacío */
    | CV SL
    | CV LE
    ;

cuerpo_funcion
    : lista_sentencias
    ;


/* --- Instrucciones Ejecutables --- */

ejecutable
    : asignacion
    | asignacion_multiple
    | if_statement
    | do_until_statement
    | print_statement
    | return_statement
    | lambda_inline
    | expresion
    ;

asignacion
    : lhs ASIGNACION expresion
        { System.out.println("Línea " + lexer.numero_linea + ": Asignación simple (:=) reconocida."); }
    ;

// Tema 23: Soporte para prefijos ID.ID.
lhs
    : IDENTIFICADOR
    | IDENTIFICADOR '.' IDENTIFICADOR
    ;

// Tema 16: Asignación múltiple con el literal '='.
asignacion_multiple
    : lista_lhs '=' lista_rhs
        { System.out.println("Línea " + lexer.numero_linea + ": Asignación múltiple (=) reconocida."); }
    ;

lista_lhs
    : lhs
    | lista_lhs ',' lhs
    ;

lista_rhs
    : expresion
    | lista_rhs ',' expresion
    ;


/* --- Estructuras de Control --- */

if_statement
    : IF '(' condicion ')' bloque_ejecutable else_opt ENDIF
        { System.out.println("Línea " + lexer.numero_linea + ": Sentencia IF reconocida."); }
    ;

else_opt
    : /* vacío */
    | ELSE bloque_ejecutable
    ;

// Tema 13: do-until.
do_until_statement
    : DO bloque_ejecutable UNTIL '(' condicion ')'
        { System.out.println("Línea " + lexer.numero_linea + ": Sentencia DO-UNTIL reconocida."); }
    ;

bloque_ejecutable
    : ejecutable
    | '{' lista_sentencias '}'
    ;


/* --- Condiciones y Expresiones --- */

condicion
    : expresion comparador expresion
    ;

comparador
    : '>' | '<' | MAYOR_IGUAL | MENOR_IGUAL | IGUAL | DISTINTO
    ;

expresion
    : termino
    | expresion '+' termino
    | expresion '-' termino
    // Regla para el menos unario (ej: -5).
    | '-' expresion %prec UMINUS
    ;

termino
    : factor
    | termino '*' factor
    | termino '/' factor
    ;

factor
    : lhs
    | CTE_LONG
    | CTE_DFLOAT
    | invocacion
    ;


/* --- Invocaciones y Otras Sentencias --- */

invocacion
    : IDENTIFICADOR '(' lista_param_real_opt ')'
    ;

lista_param_real_opt
    : /* vacío */
    | lista_param_real
    ;

lista_param_real
    : parametro_real
    | lista_param_real ',' parametro_real
    ;

parametro_real
    : expresion FLECHA IDENTIFICADOR
    ;

print_statement
    : PRINT '(' CADENA ')'
        { System.out.println("Línea " + lexer.numero_linea + ": Sentencia PRINT con cadena reconocida."); }
    | PRINT '(' expresion ')'
        { System.out.println("Línea " + lexer.numero_linea + ": Sentencia PRINT con expresión reconocida."); }
    ;

// Tema 20: RETURN con soporte para múltiples valores.
return_statement
    : RETURN '(' lista_rhs ')'
        { System.out.println("Línea " + lexer.numero_linea + ": Sentencia RETURN reconocida."); }
    ;

// Tema 27: Expresión Lambda en línea.
lambda_inline
    : '(' tipo IDENTIFICADOR ')' '{' lista_sentencias '}' '(' argumento ')'
        { System.out.println("Línea " + lexer.numero_linea + ": Expresión Lambda en línea reconocida."); }
    ;

argumento
    : IDENTIFICADOR | CTE_LONG | CTE_DFLOAT
    ;

%%

/* --- Código Java Auxiliar --- */

private AnalizadorLexico lexer;

public Parser(AnalizadorLexico lexer) {
    this.lexer = lexer;
}

private int yylex() {
    int tokenId = lexer.yylex();
    this.yylval = lexer.yylval;
    return tokenId;
}

public void yyerror(String s) {
    System.err.println("ERROR Sintáctico en línea " + lexer.numero_linea + ": " + s);
}

public static void main(String[] args) {
    try {
        AnalizadorLexico lexer = new AnalizadorLexico(args[0]);
        Parser parser = new Parser(lexer);
        parser.yyparse();
    } catch (Exception e) {
        System.err.println("Error general: " + e.getMessage());
    }
}