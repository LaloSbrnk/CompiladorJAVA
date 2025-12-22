%{
    package CompiladoresMain;
    import ArbolSintactico.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.HashMap;
    /* Necesario*/
    import CompiladoresMain.AnalizadorLexico;
    import CompiladoresMain.AtributosTokens;
    import CompiladoresMain.TiposToken;
    import CompiladoresMain.TablaDeAmbitos; 
    import java.io.IOException;
    /* Para el main*/
    import java.io.FileWriter; 
    import java.io.BufferedWriter;
%}

// --- Declaración de Tokens ---
%token <sval> IDENTIFICADOR CADENA CTE_LONG CTE_DFLOAT

%type <obj> programa lista_sentencias sentencia declarativa ejecutable
%type <obj> asignacion asignacion_multiple lista_lhs lista_rhs
%type <obj> if_statement else_opt do_until_statement bloque_ejecutable
%type <obj> condicion
%type <obj> expresion termino factor
%type <obj> invocacion lista_param_real_opt lista_param_real parametro_real
%type <obj> print_statement return_statement
%type <obj> lambda_inline argumento
%type <obj> funcion_def cuerpo_funcion
%type <obj> lista_tipos parametros_formales_opt parametros_formales parametro_formal
%type <obj> lista_variables
%type <obj> accion_media_funcion

// --- Tipos para reglas que devuelven String ---
%type <sval> lhs tipo comparador 

// Palabras reservadas y operadores
%token IF ELSE ENDIF PRINT RETURN
%token LONG DFLOAT VAR DO UNTIL
%token CV SL LE
%token ASIGNACION MAYOR_IGUAL MENOR_IGUAL DISTINTO IGUAL FLECHA

// --- Precedencia ---
%left '+' '-'
%left '*' '/'
%right UMINUS

%%

/* --- Reglas de la Gramática --- */

programa
    : IDENTIFICADOR '{' lista_sentencias '}'
        {
            String nombreProg = val_peek(3).sval;
            NodoBloque bloqueProg = (NodoBloque)val_peek(1).obj;
            yyval = new ParserVal(new NodoPrograma(nombreProg, bloqueProg));
            System.out.println("Programa completo reconocido y AST construido.");
        }    
    ;

lista_sentencias
    : /* vacío */
        {
            yyval = new ParserVal(new NodoBloque());
        }
    | lista_sentencias sentencia
        {
            NodoBloque bloque = (NodoBloque)val_peek(1).obj;
            Nodo nuevaSentencia = (Nodo)val_peek(0).obj;
            bloque.agregarSentencia(nuevaSentencia);
            yyval = new ParserVal(bloque);
        }
    ;

sentencia
    : declarativa ';'
        {
            yyval = new ParserVal(val_peek(1).obj);
        }
    | ejecutable ';'
        {   
            yyval = new ParserVal(val_peek(1).obj);
        }
    | funcion_def
        {
            yyval = new ParserVal(val_peek(0).obj);
        }
    | error ';'
        { 
            System.err.println("ERROR Sintactico recuperado en línea " + lexer.numero_linea);
            yyval = new ParserVal(null); 
        }
    ;


/* --- Definiciones --- */

// CORRECCIÓN CRÍTICA: Usamos 'lista_tipos' en lugar de 'tipo' para evitar
// el conflicto Shift/Reduce con la definición de funciones.
declarativa
    : lista_tipos lista_variables
        {
            ArrayList<String> tipos = (ArrayList<String>)val_peek(1).obj;
            ArrayList<String> variables = (ArrayList<String>)val_peek(0).obj;
            
            // Validación Semántica Rápida: Variables solo pueden tener un tipo.
            if (tipos.size() > 1) {
                yyerror("Error Semantico: No se pueden declarar variables con multiples tipos.");
                yyval = new ParserVal(null); // O manejar error
            } else {
                String tipo_de_datos = tipos.get(0);
                yyval = new ParserVal(new NodoDeclaracion(tipo_de_datos, variables));
            }
        }    
    | VAR asignacion
        {
            NodoAsignacion nodoAsig = (NodoAsignacion)val_peek(0).obj;
            nodoAsig.setEsInferencia();
            yyval = new ParserVal(nodoAsig);
        }  
    ;

lista_variables
    : IDENTIFICADOR
        {
            ArrayList<String> lista = new ArrayList<>();
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
    | lista_variables ',' IDENTIFICADOR
        {
            ArrayList<String> lista = (ArrayList<String>)val_peek(2).obj;
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
    ;

tipo
    : LONG   { yyval = new ParserVal("long"); }
    | DFLOAT { yyval = new ParserVal("dfloat"); }
    ;

// lista_tipos se usa tanto para funciones como para declaraciones ahora
lista_tipos
    : tipo
        {
            ArrayList<String> lista = new ArrayList<>();
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
    | lista_tipos ',' tipo
        {
            ArrayList<String> lista = (ArrayList<String>)val_peek(2).obj;
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
    ;

funcion_def
    : lista_tipos IDENTIFICADOR '(' parametros_formales_opt ')' accion_media_funcion '{' cuerpo_funcion '}'
      { 
          NodoFuncionDef nodoFunc = (NodoFuncionDef)val_peek(3).obj;
          NodoBloque cuerpoFunc = (NodoBloque)val_peek(1).obj;
          nodoFunc.setCuerpo(cuerpoFunc);
          yyval = new ParserVal(nodoFunc);
          
          String nombreFuncion = nodoFunc.getNombre();
          System.out.println("Linea " + lexer.numero_linea + ": Definicion de funcion '" + nombreFuncion + "' reconocida.");
          funcionActual = null;
      }
    ;

accion_media_funcion :
      { 
          List<String> tiposRetorno = (ArrayList<String>)val_peek(4).obj;
          String nombreFuncion = val_peek(3).sval;
          ArrayList<NodoParametro> params = (ArrayList<NodoParametro>)val_peek(1).obj;

          // Crear atributos pero NO agregar a la tabla todavía (lo hace el chequear)
          AtributosTokens attrsFuncion = new AtributosTokens(TiposToken.IDENTIFICADOR);
          
          attrsFuncion.setUso("funcion");
          attrsFuncion.setTiposRetorno(tiposRetorno);
          attrsFuncion.setParametros(params);
          
          NodoFuncionDef nodoFunc = new NodoFuncionDef(nombreFuncion, tiposRetorno, params, null, attrsFuncion);
          funcionActual = nodoFunc; // IMPORTANTE: Seteamos funcionActual aquí
          yyval = new ParserVal(nodoFunc);
      }
    ;

parametros_formales_opt
    : /* vacío */ { yyval = new ParserVal(null); }
    | parametros_formales { yyval = new ParserVal(val_peek(0).obj); }
    ;

parametros_formales
    : parametro_formal
        {
            ArrayList<NodoParametro> lista = new ArrayList<>();
            lista.add((NodoParametro)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
    | parametros_formales ',' parametro_formal
        {
            ArrayList<NodoParametro> lista = (ArrayList<NodoParametro>)val_peek(2).obj;
            lista.add((NodoParametro)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
    ;

parametro_formal
    : opt_sem_pasaje tipo IDENTIFICADOR
        {
            yyval = new ParserVal(new NodoParametro((String)val_peek(0).sval, (String)val_peek(1).sval, (String)val_peek(2).sval));
        }
    ;

opt_sem_pasaje
    : /* vacío */ { yyval = new ParserVal(null); }
    | CV SL { yyval = new ParserVal("cv sl"); }
    | CV LE { yyval = new ParserVal("cv le"); }
    ;

cuerpo_funcion
    : lista_sentencias { yyval = new ParserVal(val_peek(0).obj); }
    ;

/* --- Instrucciones Ejecutables --- */

ejecutable
    : asignacion { yyval = new ParserVal(val_peek(0).obj); }
    | asignacion_multiple { yyval = new ParserVal(val_peek(0).obj); }
    | if_statement { yyval = new ParserVal(val_peek(0).obj); }
    | do_until_statement { yyval = new ParserVal(val_peek(0).obj); }
    | print_statement { yyval = new ParserVal(val_peek(0).obj); }
    | return_statement { yyval = new ParserVal(val_peek(0).obj); }
    | lambda_inline { yyval = new ParserVal(val_peek(0).obj); }
    | expresion { yyval = new ParserVal(val_peek(0).obj); }
    ;

asignacion
    : lhs ASIGNACION expresion
        {
            NodoVariable var = new NodoVariable(val_peek(2).sval);
            Nodo expr = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoAsignacion(var, expr));
        }   
    ;

lhs
    : IDENTIFICADOR { yyval = new ParserVal(val_peek(0).sval); }
    | IDENTIFICADOR '.' IDENTIFICADOR { yyval = new ParserVal(val_peek(2).sval + "." + val_peek(0).sval); }
    ;

asignacion_multiple
    : lista_lhs '=' lista_rhs
        {
            ArrayList<NodoVariable> vars = (ArrayList<NodoVariable>)val_peek(2).obj;
            ArrayList<Nodo> exprs = (ArrayList<Nodo>)val_peek(0).obj;
            yyval = new ParserVal(new NodoAsignacionMultiple(vars, exprs));
        }    
    ;

lista_lhs
    : lhs
        {
            ArrayList<NodoVariable> lista = new ArrayList<>();
            lista.add(new NodoVariable(val_peek(0).sval)); 
            yyval = new ParserVal(lista);
        }
    | lista_lhs ',' lhs
        {
            ArrayList<NodoVariable> lista = (ArrayList<NodoVariable>)val_peek(2).obj;
            lista.add(new NodoVariable(val_peek(0).sval));
            yyval = new ParserVal(lista);
        }
    ;

lista_rhs
    : expresion
        {
            ArrayList<Nodo> listaNodos = new ArrayList<>();
            listaNodos.add((Nodo)val_peek(0).obj);
            yyval = new ParserVal(listaNodos);
        }
    | lista_rhs ',' expresion
        {
            ArrayList<Nodo> listaNodos = (ArrayList<Nodo>)val_peek(2).obj;
            listaNodos.add((Nodo)val_peek(0).obj);
            yyval = new ParserVal(listaNodos);
        }
    ;

/* --- Estructuras de Control --- */

if_statement
    : IF '(' condicion ')' bloque_ejecutable else_opt ENDIF
        {
            Nodo nodo_cond = (Nodo)val_peek(4).obj;
            NodoBloque bloque_true = (NodoBloque)val_peek(2).obj;
            NodoBloque bloque_false = (NodoBloque)val_peek(1).obj;
            yyval = new ParserVal(new NodoIf(nodo_cond, bloque_true, bloque_false));
        }    
    ;

else_opt
    : /* vacío */ { yyval = new ParserVal(null); }
    | ELSE bloque_ejecutable { yyval = new ParserVal(val_peek(0).obj); }
    ;

do_until_statement
    : DO bloque_ejecutable UNTIL '(' condicion ')'
        {
            NodoBloque bloque_do = (NodoBloque)val_peek(4).obj;
            Nodo nodo_cond = (Nodo)val_peek(1).obj;
            yyval = new ParserVal(new NodoDoUntil(bloque_do, nodo_cond));
        }    
    ;

bloque_ejecutable
    : ejecutable
        {
            NodoBloque bloque = new NodoBloque();
            bloque.agregarSentencia((Nodo)val_peek(0).obj);
            yyval = new ParserVal(bloque);
        }
    | '{' lista_sentencias '}'
        {
            yyval = new ParserVal(val_peek(1).obj);
        }
    ;

/* --- Condiciones y Expresiones --- */

condicion
    : expresion comparador expresion
        {
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            String op = val_peek(1).sval;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoCondicion(nodo_izq, nodo_der, op));
        }
    ;

comparador
    : '>'           { yyval = new ParserVal(">"); }
    | '<'           { yyval = new ParserVal("<"); }
    | MAYOR_IGUAL   { yyval = new ParserVal(">="); }
    | MENOR_IGUAL   { yyval = new ParserVal("<="); }
    | IGUAL         { yyval = new ParserVal("=="); }
    | DISTINTO      { yyval = new ParserVal("!="); }
    ;

expresion
    : termino { yyval = new ParserVal(val_peek(0).obj); }
    | expresion '+' termino
        {
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("+", nodo_izq, nodo_der));
        }
    | expresion '-' termino
        {
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("-", nodo_izq, nodo_der));
        }
    | '-' expresion %prec UMINUS
        {
            Nodo nodo_hijo = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("UMINUS", nodo_hijo, null));
        }
    ;

termino
    : factor { yyval = new ParserVal(val_peek(0).obj); }
    | termino '*' factor
        {
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("*", nodo_izq, nodo_der));
        }
    | termino '/' factor
        {
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("/", nodo_izq, nodo_der));
        }
    ;

factor
    : lhs { yyval = new ParserVal(new NodoVariable(val_peek(0).sval)); }
    | CTE_LONG
        {
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoConstante(attrs.getValor(), "long"));
        }
    | CTE_DFLOAT
        {
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoConstante(attrs.getValor(), "dfloat"));
        }
    | invocacion { yyval = new ParserVal(val_peek(0).obj); }
    ;

invocacion
    : IDENTIFICADOR '(' lista_param_real_opt ')'
        {
            yyval = new ParserVal(new NodoInvocacion(val_peek(3).sval, (ArrayList<NodoParametroReal>)val_peek(1).obj));
        }
    ;

lista_param_real_opt
    : /* vacío */ { yyval = new ParserVal(null); }
    | lista_param_real { yyval = new ParserVal(val_peek(0).obj); }
    ;

lista_param_real
    : parametro_real
        {   
            ArrayList<NodoParametroReal> lista = new ArrayList<>();
            lista.add((NodoParametroReal)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
    | lista_param_real ',' parametro_real
        {
            ArrayList<NodoParametroReal> lista = (ArrayList<NodoParametroReal>)val_peek(2).obj;
            lista.add((NodoParametroReal)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
    ;

parametro_real
    : expresion FLECHA IDENTIFICADOR
        {
            yyval = new ParserVal(new NodoParametroReal((Nodo)val_peek(2).obj, val_peek(0).sval));
        }
    ;

print_statement
    : PRINT '(' CADENA ')'
        {
            String lexemaCadena = val_peek(1).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCadena);
            NodoConstante nodoCadena = new NodoConstante(attrs.getValor(), "string");
            yyval = new ParserVal(new NodoPrint(nodoCadena));
        }    
    | PRINT '(' expresion ')'
        {
            yyval = new ParserVal(new NodoPrint((Nodo)val_peek(1).obj));
        }    
    ;

return_statement
    : RETURN '(' lista_rhs ')'
        {
            ArrayList<Nodo> expresiones = (ArrayList<Nodo>)val_peek(1).obj;
            if (funcionActual == null) {
                yyerror("Sentencia RETURN fuera de una funcion.");
                yyval = new ParserVal(null);
            } else {
                yyval = new ParserVal(new NodoReturn(expresiones, funcionActual));
            }
        }
    ;

lambda_inline
    : '(' tipo IDENTIFICADOR ')' '{' lista_sentencias '}' '(' argumento ')'
        {
            String tipoParamLambda = val_peek(8).sval;
            String nombreParamLambda = val_peek(7).sval;
            NodoBloque cuerpoLambda = (NodoBloque)val_peek(4).obj;
            NodoArgumento argLambda = (NodoArgumento)val_peek(1).obj;
            yyval = new ParserVal(new NodoLambdaInline(tipoParamLambda, nombreParamLambda, cuerpoLambda, argLambda));
            System.out.println("Linea " + lexer.numero_linea + ": Expresión Lambda en línea reconocida.");
        }    
    ;

argumento
    : IDENTIFICADOR
        {
            yyval = new ParserVal(new NodoArgumento(val_peek(0).sval, "id"));
        }
    | CTE_LONG
        {
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoArgumento(attrs.getValor(), "long"));
        }
    | CTE_DFLOAT
        {
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoArgumento(attrs.getValor(), "dfloat"));
        }
    ;

%%

private NodoFuncionDef funcionActual = null;
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
    System.err.println("ERROR Sintactico en línea " + lexer.numero_linea + ": " + s);
}

public static void main(String[] args) {
    if (args.length == 0) {
        System.err.println("Error: Debes pasar la ruta del archivo a compilar como argumento.");
        return;
    }
    
    String rutaFuente = args[0];
    String rutaAsm = rutaFuente.replaceAll("\\..*$", ".asm");

    try {
        AnalizadorLexico lexer = new AnalizadorLexico(rutaFuente);
        Parser parser = new Parser(lexer);

        parser.yyparse(); 
        
        Nodo arbol = (Nodo) parser.yyval.obj;
        TablaDeAmbitos tablaDeAmbitos = new TablaDeAmbitos(AnalizadorLexico.tablaSimbolos);

        if (arbol != null && parser.yynerrs == 0) { 
            System.out.println("\n--- Iniciando Chequeo Semantico ---");
            arbol.chequear(tablaDeAmbitos);
            System.out.println("--- Chequeo Semantico Finalizado ---");
        } else {
            System.out.println("\n--- Chequeo Semantico Omitido (Errores Sintacticos o AST nulo) ---");
        }

        if (arbol != null && parser.yynerrs == 0) {
            System.out.println("\n--- Iniciando Generacion de Codigo Assembler ---");
            GeneradorAssembler generador = new GeneradorAssembler(tablaDeAmbitos);
            arbol.generarCodigo(generador, tablaDeAmbitos);
            generador.escribirArchivo(rutaAsm);
            System.out.println("--- Generacion de Codigo Finalizada ---");
            System.out.println("Archivo Assembler generado en: " + rutaAsm);
        } else {
             System.out.println("\n--- Generacion de Codigo Omitida ---");
        }
        
        AnalizadorLexico.printReporteLexico();
        System.out.println("\n--- 2. Representacion Intermedia (Arbol Sintactico) ---");
        if (arbol != null) {
            arbol.imprimir("");
        } else {
            System.out.println("Arbol no generado.");
        }

        lexer.printTablaSimbolos();

    } catch (IOException e) {
        System.err.println("Error de I/O al leer el archivo: " + e.getMessage());
    } catch (Exception e) { 
        System.err.println("Error general durante la compilacion: " + e.getMessage());
        e.printStackTrace(); 
    }
}