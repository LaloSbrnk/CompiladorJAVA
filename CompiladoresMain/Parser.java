


















    package CompiladoresMain;
    import ArbolSintactico.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.HashMap;
    
    import CompiladoresMain.AnalizadorLexico;
    import CompiladoresMain.AtributosTokens;
    import CompiladoresMain.TiposToken;
    import CompiladoresMain.TablaDeAmbitos; 
    import java.io.IOException;
    
    import java.io.FileWriter; 
    import java.io.BufferedWriter;





public class Parser
{

boolean yydebug;        
int yynerrs;            
int yyerrflag;          
int yychar;             





void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}


final static int YYSTACKSIZE = 500;  
int statestk[] = new int[YYSTACKSIZE]; 
int stateptr;
int stateptrmax;                     
int statemax;                        



final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}



final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}



void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}






String   yytext;
ParserVal yyval; 
ParserVal yylval;
ParserVal valstk[];
int valptr;



void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}

public final static short IDENTIFICADOR=257;
public final static short CADENA=258;
public final static short CTE_LONG=259;
public final static short CTE_DFLOAT=260;
public final static short IF=261;
public final static short ELSE=262;
public final static short ENDIF=263;
public final static short PRINT=264;
public final static short RETURN=265;
public final static short LONG=266;
public final static short DFLOAT=267;
public final static short VAR=268;
public final static short DO=269;
public final static short UNTIL=270;
public final static short CV=271;
public final static short SL=272;
public final static short LE=273;
public final static short ASIGNACION=274;
public final static short MAYOR_IGUAL=275;
public final static short MENOR_IGUAL=276;
public final static short DISTINTO=277;
public final static short IGUAL=278;
public final static short FLECHA=279;
public final static short UMINUS=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    2,    3,    3,   31,
   31,   34,   34,   27,   27,   25,   32,   28,   28,   29,
   29,   30,   36,   36,   36,   26,    4,    4,    4,    4,
    4,    4,    4,    4,    5,   33,   33,    6,    7,    7,
    8,    8,    9,   10,   10,   11,   12,   12,   13,   35,
   35,   35,   35,   35,   35,   14,   14,   14,   14,   15,
   15,   15,   16,   16,   16,   16,   17,   18,   18,   19,
   19,   20,   21,   21,   22,   23,   24,   24,   24,
};
final static short yylen[] = {                            2,
    4,    0,    2,    2,    2,    1,    2,    2,    2,    1,
    3,    1,    1,    1,    3,    9,    0,    0,    1,    1,
    3,    3,    0,    2,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    1,    3,    3,    1,    3,
    1,    3,    7,    0,    2,    6,    1,    3,    3,    1,
    1,    1,    1,    1,    1,    1,    3,    3,    2,    1,
    3,    3,    1,    1,    1,    1,    4,    0,    1,    1,
    3,    3,    4,    4,    4,   10,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,   64,   65,    0,    0,
    0,   12,   13,    0,    0,    0,    1,    0,    3,    0,
    0,   27,   28,    0,   29,   30,    0,    0,   60,   66,
   31,   32,   33,    6,    0,    0,   14,    7,    0,    0,
    0,    0,    0,    0,    9,    0,    2,   47,    0,   59,
   63,    0,    4,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   70,   37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   40,    0,
    0,    0,   61,   62,    0,   15,    0,    0,    0,   67,
    0,    0,   52,   53,   55,   54,   50,   51,    0,   73,
   74,    0,   75,   48,    0,    0,    0,    0,    0,   20,
    0,   11,   72,   71,    0,    0,    0,    0,    2,   24,
   25,   17,    0,    0,    0,    0,   46,    0,    0,   21,
   22,   45,   43,    0,    2,    0,    0,    0,   77,   78,
   79,    0,   16,   76,
};
final static short yydgoto[] = {                          2,
    4,   19,   20,   21,   22,   23,   24,   74,   25,  126,
   26,   49,   70,   27,   28,   29,   30,   66,   67,   68,
   31,   32,   33,  142,   34,  138,   35,  108,  109,  110,
   63,  129,   51,   37,   99,  111,
};
final static short yysindex[] = {                      -212,
  -70,    0,    0,   25,   -4,   17,    0,    0,   33,   41,
   49,    0,    0, -165,   80,  101,    0, -240,    0,   42,
   43,    0,    0,   -9,    0,    0,   19,   24,    0,    0,
    0,    0,    0,    0,  -29, -178,    0,    0,  101, -154,
  101,  -34,  101,   58,    0, -178,    0,    0, -164,    0,
    0, -152,    0,    0, -165,  101, -182, -182, -182, -182,
   70, -240,   71,  101,  -38,   73,   72,    0,    0,   76,
  -21,   77,   31,   44,   19,   39,   81,   82,    0,   78,
   24,   24,    0,    0, -160,    0, -130,   19, -129,    0,
  101,   80,    0,    0,    0,    0,    0,    0,  101,    0,
    0,  101,    0,    0,  101,    8, -235,   91,   90,    0,
 -240,    0,    0,    0, -127,   19,   19,   99,    0,    0,
    0,    0, -160, -115,   80, -120,    0,   53,   22,    0,
    0,    0,    0,  107,    0, -177,   67,   26,    0,    0,
    0,  108,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -51,  -31,    0,    0,
    0,    0,    0,    0,    0,   94,    0,    0,  111,    0,
    0,    0,    0,    6,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   10,    0,   95,    0,    0,    0,  115,    0,    0,    0,
    0,    0,    0,    0,   85,    0,    0,    0,    0,    9,
  -11,   -1,    0,    0,  -24,    0,    0,  100,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  117,    0,
    0,    0,    0,    0, -102,  121,   89,    0,    0,    0,
    0,    0, -176,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   38,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -22,    0,    0,   -6,  151,    0,    0,  110,    0,    0,
    0,  -69,   62,  326,   37,   40,    0,    0,    0,   79,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   45,
    0,    0,   32,   -2,    0,    0,
};
final static int YYTABLESIZE=431;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         36,
   36,   36,   36,   36,   57,   36,   58,   34,   48,   56,
   16,   56,   56,   56,   62,   52,   18,   36,   36,   36,
   36,   57,  115,   58,   76,   12,   13,   56,   56,   57,
   56,   57,   57,   57,   55,   36,  120,  121,   98,   58,
   97,   58,   58,   58,    1,   46,   36,   57,   57,   36,
   57,   56,    3,   10,   38,  132,   39,   58,   58,   86,
   58,   57,   40,   58,   18,   59,   36,   38,   10,   16,
   60,  101,   41,   57,    6,   58,    7,    8,   18,  139,
   42,  140,  141,   16,  103,   48,   79,  102,   43,   23,
   23,   44,   18,   81,   82,   64,  128,   16,   83,   84,
   53,   54,   69,   40,   78,   77,   18,   36,  124,   85,
  107,   16,  137,   90,   87,   91,   92,  100,   48,   18,
  105,  102,  106,   36,   16,   41,  112,  113,   41,   42,
  119,  122,   42,  123,  125,   63,   63,   39,   63,  127,
   63,  131,  133,   41,  135,   16,  136,   42,  144,   17,
  143,   68,   63,    8,   39,   69,   36,   19,   35,   36,
   44,   49,   26,  104,   45,   80,  118,  130,   36,  114,
    0,    0,    0,    0,    0,    0,    0,  134,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
   34,   34,    0,    0,    0,    0,    0,    0,   34,    0,
   36,   36,    6,   72,    7,    8,    0,   61,   36,    0,
   56,   56,   36,   36,   36,   36,   36,   36,   56,    0,
   89,   23,   23,   56,   56,   56,   56,   56,    0,    0,
   57,   57,    0,   93,   94,   95,   96,    0,   57,    0,
   58,   58,    0,   57,   57,   57,   57,   57,   58,    0,
   38,   38,    0,   58,   58,   58,   58,   58,   38,   36,
    5,    6,    0,    7,    8,    9,    0,    0,   10,   11,
   12,   13,   14,   15,    5,    6,    0,    7,    8,    9,
    0,    0,   10,   11,   12,   13,   14,   15,    5,    6,
    0,    7,    8,    9,    0,    0,   10,   11,   12,   13,
   14,   15,    5,    6,    0,    7,    8,    9,    0,    0,
   10,   11,   12,   13,   14,   15,    6,    0,    7,    8,
    9,   50,    0,   10,   11,    0,   41,   41,   15,    0,
   42,   42,    0,    0,   41,   63,   63,    6,   42,    7,
    8,   35,   35,   63,   65,    0,   71,   73,   75,   35,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   75,    0,    0,    0,    0,    0,    0,    0,   88,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   65,    0,    0,    0,
    0,    0,    0,    0,  116,    0,    0,  117,    0,    0,
   71,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   43,   47,   45,   59,   15,   41,
   45,   43,   44,   45,   44,   18,   41,   59,   60,   61,
   62,   43,   92,   45,   47,  266,  267,   59,   60,   41,
   62,   43,   44,   45,   44,    4,  272,  273,   60,   41,
   62,   43,   44,   45,  257,   14,   15,   59,   60,   44,
   62,   61,  123,   44,   59,  125,   40,   59,   60,   62,
   62,   43,   46,   45,   40,   42,   61,   59,   59,   45,
   47,   41,   40,   43,  257,   45,  259,  260,   40,  257,
   40,  259,  260,   45,   41,   92,   55,   44,   40,  266,
  267,  257,   40,   57,   58,  274,  119,   45,   59,   60,
   59,   59,  257,   46,  257,  270,   40,   76,  111,   40,
  271,   45,  135,   41,   44,   44,   41,   41,  125,   40,
   40,   44,   41,   92,   45,   41,  257,  257,   44,   41,
  123,   41,   44,   44,  262,   42,   43,   44,   45,   41,
   47,  257,  263,   59,  123,   45,   40,   59,   41,  125,
  125,   41,   59,   59,   61,   41,  125,   41,   59,  128,
  263,   41,  125,  125,   14,   56,  105,  123,  137,   91,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  123,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,  270,   -1,
  262,  263,  257,  258,  259,  260,   -1,  257,  270,   -1,
  262,  263,  274,  275,  276,  277,  278,  279,  270,   -1,
  279,  266,  267,  275,  276,  277,  278,  279,   -1,   -1,
  262,  263,   -1,  275,  276,  277,  278,   -1,  270,   -1,
  262,  263,   -1,  275,  276,  277,  278,  279,  270,   -1,
  262,  263,   -1,  275,  276,  277,  278,  279,  270,  274,
  256,  257,   -1,  259,  260,  261,   -1,   -1,  264,  265,
  266,  267,  268,  269,  256,  257,   -1,  259,  260,  261,
   -1,   -1,  264,  265,  266,  267,  268,  269,  256,  257,
   -1,  259,  260,  261,   -1,   -1,  264,  265,  266,  267,
  268,  269,  256,  257,   -1,  259,  260,  261,   -1,   -1,
  264,  265,  266,  267,  268,  269,  257,   -1,  259,  260,
  261,   16,   -1,  264,  265,   -1,  262,  263,  269,   -1,
  262,  263,   -1,   -1,  270,  262,  263,  257,  270,  259,
  260,  262,  263,  270,   39,   -1,   41,   42,   43,  270,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   56,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   64,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   99,   -1,   -1,  102,   -1,   -1,
  105,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IDENTIFICADOR","CADENA","CTE_LONG",
"CTE_DFLOAT","IF","ELSE","ENDIF","PRINT","RETURN","LONG","DFLOAT","VAR","DO",
"UNTIL","CV","SL","LE","ASIGNACION","MAYOR_IGUAL","MENOR_IGUAL","DISTINTO",
"IGUAL","FLECHA","UMINUS",
};
final static String yyrule[] = {
"$accept : programa",
"programa : IDENTIFICADOR '{' lista_sentencias '}'",
"lista_sentencias :",
"lista_sentencias : lista_sentencias sentencia",
"sentencia : declarativa ';'",
"sentencia : ejecutable ';'",
"sentencia : funcion_def",
"sentencia : error ';'",
"declarativa : lista_tipos lista_variables",
"declarativa : VAR asignacion",
"lista_variables : IDENTIFICADOR",
"lista_variables : lista_variables ',' IDENTIFICADOR",
"tipo : LONG",
"tipo : DFLOAT",
"lista_tipos : tipo",
"lista_tipos : lista_tipos ',' tipo",
"funcion_def : lista_tipos IDENTIFICADOR '(' parametros_formales_opt ')' accion_media_funcion '{' cuerpo_funcion '}'",
"accion_media_funcion :",
"parametros_formales_opt :",
"parametros_formales_opt : parametros_formales",
"parametros_formales : parametro_formal",
"parametros_formales : parametros_formales ',' parametro_formal",
"parametro_formal : opt_sem_pasaje tipo IDENTIFICADOR",
"opt_sem_pasaje :",
"opt_sem_pasaje : CV SL",
"opt_sem_pasaje : CV LE",
"cuerpo_funcion : lista_sentencias",
"ejecutable : asignacion",
"ejecutable : asignacion_multiple",
"ejecutable : if_statement",
"ejecutable : do_until_statement",
"ejecutable : print_statement",
"ejecutable : return_statement",
"ejecutable : lambda_inline",
"ejecutable : expresion",
"asignacion : lhs ASIGNACION expresion",
"lhs : IDENTIFICADOR",
"lhs : IDENTIFICADOR '.' IDENTIFICADOR",
"asignacion_multiple : lista_lhs '=' lista_rhs",
"lista_lhs : lhs",
"lista_lhs : lista_lhs ',' lhs",
"lista_rhs : expresion",
"lista_rhs : lista_rhs ',' expresion",
"if_statement : IF '(' condicion ')' bloque_ejecutable else_opt ENDIF",
"else_opt :",
"else_opt : ELSE bloque_ejecutable",
"do_until_statement : DO bloque_ejecutable UNTIL '(' condicion ')'",
"bloque_ejecutable : ejecutable",
"bloque_ejecutable : '{' lista_sentencias '}'",
"condicion : expresion comparador expresion",
"comparador : '>'",
"comparador : '<'",
"comparador : MAYOR_IGUAL",
"comparador : MENOR_IGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"expresion : termino",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : '-' expresion",
"termino : factor",
"termino : termino '*' factor",
"termino : termino '/' factor",
"factor : lhs",
"factor : CTE_LONG",
"factor : CTE_DFLOAT",
"factor : invocacion",
"invocacion : IDENTIFICADOR '(' lista_param_real_opt ')'",
"lista_param_real_opt :",
"lista_param_real_opt : lista_param_real",
"lista_param_real : parametro_real",
"lista_param_real : lista_param_real ',' parametro_real",
"parametro_real : expresion FLECHA IDENTIFICADOR",
"print_statement : PRINT '(' CADENA ')'",
"print_statement : PRINT '(' expresion ')'",
"return_statement : RETURN '(' lista_rhs ')'",
"lambda_inline : '(' tipo IDENTIFICADOR ')' '{' lista_sentencias '}' '(' argumento ')'",
"argumento : IDENTIFICADOR",
"argumento : CTE_LONG",
"argumento : CTE_DFLOAT",
};



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




void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) 
     s = yyname[ch];    
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}






int yyn;       
int yym;       
int yystate;   
String yys;    





int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          
  yystate=0;            
  state_push(yystate);  
  val_push(yylval);     
  while (true) 
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      
        {
        yychar = yylex();  
        if (yydebug) debug(" next yychar:"+yychar);
        
        if (yychar < 0)    
          {
          yychar = 0;      
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }
      yyn = yysindex[yystate];  
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        
        yystate = yytable[yyn];
        state_push(yystate);   
        val_push(yylval);      
        yychar = -1;           
        if (yyerrflag > 0)     
           --yyerrflag;        
        doaction=false;        
        break;   
        }

    yyn = yyrindex[yystate];  
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; 
      break;         
      }
    else 
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) 
        {
        yyerrflag = 3;
        while (true)   
          {
          if (stateptr<0)   
            {
            yyerror("stack underflow. aborting...");  
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   
              {
              yyerror("Stack underflow. aborting...");  
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            
        {
        if (yychar == 0)
          return 1; 
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  
        }
      }
    }
    if (!doaction)   
      continue;      
    yym = yylen[yyn];          
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 
      yyval = val_peek(yym-1); 
    yyval = dup_yyval(yyval); 
    switch(yyn)
      {

case 1:

{
            String nombreProg = val_peek(3).sval;
            NodoBloque bloqueProg = (NodoBloque)val_peek(1).obj;
            yyval = new ParserVal(new NodoPrograma(nombreProg, bloqueProg));
            System.out.println("Programa completo reconocido y AST construido.");
        }
break;
case 2:

{
            yyval = new ParserVal(new NodoBloque());
        }
break;
case 3:

{
            NodoBloque bloque = (NodoBloque)val_peek(1).obj;
            Nodo nuevaSentencia = (Nodo)val_peek(0).obj;
            bloque.agregarSentencia(nuevaSentencia);
            yyval = new ParserVal(bloque);
        }
break;
case 4:

{
            yyval = new ParserVal(val_peek(1).obj);
        }
break;
case 5:

{   
            yyval = new ParserVal(val_peek(1).obj);
        }
break;
case 6:

{
            yyval = new ParserVal(val_peek(0).obj);
        }
break;
case 7:

{ 
            System.err.println("ERROR Sintactico recuperado en línea " + lexer.numero_linea);
            yyval = new ParserVal(null); 
        }
break;
case 8:

{
            ArrayList<String> tipos = (ArrayList<String>)val_peek(1).obj;
            ArrayList<String> variables = (ArrayList<String>)val_peek(0).obj;
            
            
            if (tipos.size() > 1) {
                yyerror("Error Semantico: No se pueden declarar variables con multiples tipos.");
                yyval = new ParserVal(null); 
            } else {
                String tipo_de_datos = tipos.get(0);
                yyval = new ParserVal(new NodoDeclaracion(tipo_de_datos, variables));
            }
        }
break;
case 9:

{
            NodoAsignacion nodoAsig = (NodoAsignacion)val_peek(0).obj;
            nodoAsig.setEsInferencia();
            yyval = new ParserVal(nodoAsig);
        }
break;
case 10:

{
            ArrayList<String> lista = new ArrayList<>();
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 11:

{
            ArrayList<String> lista = (ArrayList<String>)val_peek(2).obj;
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 12:

{ yyval = new ParserVal("long"); }
break;
case 13:

{ yyval = new ParserVal("dfloat"); }
break;
case 14:

{
            ArrayList<String> lista = new ArrayList<>();
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 15:

{
            ArrayList<String> lista = (ArrayList<String>)val_peek(2).obj;
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 16:

{ 
          NodoFuncionDef nodoFunc = (NodoFuncionDef)val_peek(3).obj;
          NodoBloque cuerpoFunc = (NodoBloque)val_peek(1).obj;
          nodoFunc.setCuerpo(cuerpoFunc);
          yyval = new ParserVal(nodoFunc);
          
          String nombreFuncion = nodoFunc.getNombre();
          System.out.println("Linea " + lexer.numero_linea + ": Definicion de funcion '" + nombreFuncion + "' reconocida.");
          funcionActual = null;
      }
break;
case 17:

{ 
          List<String> tiposRetorno = (ArrayList<String>)val_peek(4).obj;
          String nombreFuncion = val_peek(3).sval;
          ArrayList<NodoParametro> params = (ArrayList<NodoParametro>)val_peek(1).obj;

          
          AtributosTokens attrsFuncion = new AtributosTokens(TiposToken.IDENTIFICADOR);
          
          attrsFuncion.setUso("funcion");
          attrsFuncion.setTiposRetorno(tiposRetorno);
          attrsFuncion.setParametros(params);
          
          NodoFuncionDef nodoFunc = new NodoFuncionDef(nombreFuncion, tiposRetorno, params, null, attrsFuncion);
          funcionActual = nodoFunc; 
          yyval = new ParserVal(nodoFunc);
      }
break;
case 18:

{ yyval = new ParserVal(null); }
break;
case 19:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 20:

{
            ArrayList<NodoParametro> lista = new ArrayList<>();
            lista.add((NodoParametro)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 21:

{
            ArrayList<NodoParametro> lista = (ArrayList<NodoParametro>)val_peek(2).obj;
            lista.add((NodoParametro)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 22:

{
            yyval = new ParserVal(new NodoParametro((String)val_peek(0).sval, (String)val_peek(1).sval, (String)val_peek(2).sval));
        }
break;
case 23:

{ yyval = new ParserVal(null); }
break;
case 24:

{ yyval = new ParserVal("cv sl"); }
break;
case 25:

{ yyval = new ParserVal("cv le"); }
break;
case 26:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 27:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 28:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 29:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 30:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 31:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 32:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 33:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 34:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 35:

{
            NodoVariable var = new NodoVariable(val_peek(2).sval);
            Nodo expr = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoAsignacion(var, expr));
        }
break;
case 36:

{ yyval = new ParserVal(val_peek(0).sval); }
break;
case 37:

{ yyval = new ParserVal(val_peek(2).sval + "." + val_peek(0).sval); }
break;
case 38:

{
            ArrayList<NodoVariable> vars = (ArrayList<NodoVariable>)val_peek(2).obj;
            ArrayList<Nodo> exprs = (ArrayList<Nodo>)val_peek(0).obj;
            yyval = new ParserVal(new NodoAsignacionMultiple(vars, exprs));
        }
break;
case 39:

{
            ArrayList<NodoVariable> lista = new ArrayList<>();
            lista.add(new NodoVariable(val_peek(0).sval)); 
            yyval = new ParserVal(lista);
        }
break;
case 40:

{
            ArrayList<NodoVariable> lista = (ArrayList<NodoVariable>)val_peek(2).obj;
            lista.add(new NodoVariable(val_peek(0).sval));
            yyval = new ParserVal(lista);
        }
break;
case 41:

{
            ArrayList<Nodo> listaNodos = new ArrayList<>();
            listaNodos.add((Nodo)val_peek(0).obj);
            yyval = new ParserVal(listaNodos);
        }
break;
case 42:

{
            ArrayList<Nodo> listaNodos = (ArrayList<Nodo>)val_peek(2).obj;
            listaNodos.add((Nodo)val_peek(0).obj);
            yyval = new ParserVal(listaNodos);
        }
break;
case 43:

{
            Nodo nodo_cond = (Nodo)val_peek(4).obj;
            NodoBloque bloque_true = (NodoBloque)val_peek(2).obj;
            NodoBloque bloque_false = (NodoBloque)val_peek(1).obj;
            yyval = new ParserVal(new NodoIf(nodo_cond, bloque_true, bloque_false));
        }
break;
case 44:

{ yyval = new ParserVal(null); }
break;
case 45:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 46:

{
            NodoBloque bloque_do = (NodoBloque)val_peek(4).obj;
            Nodo nodo_cond = (Nodo)val_peek(1).obj;
            yyval = new ParserVal(new NodoDoUntil(bloque_do, nodo_cond));
        }
break;
case 47:

{
            NodoBloque bloque = new NodoBloque();
            bloque.agregarSentencia((Nodo)val_peek(0).obj);
            yyval = new ParserVal(bloque);
        }
break;
case 48:

{
            yyval = new ParserVal(val_peek(1).obj);
        }
break;
case 49:

{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            String op = val_peek(1).sval;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoCondicion(nodo_izq, nodo_der, op));
        }
break;
case 50:

{ yyval = new ParserVal(">"); }
break;
case 51:

{ yyval = new ParserVal("<"); }
break;
case 52:

{ yyval = new ParserVal(">="); }
break;
case 53:

{ yyval = new ParserVal("<="); }
break;
case 54:

{ yyval = new ParserVal("=="); }
break;
case 55:

{ yyval = new ParserVal("!="); }
break;
case 56:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 57:

{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("+", nodo_izq, nodo_der));
        }
break;
case 58:

{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("-", nodo_izq, nodo_der));
        }
break;
case 59:

{
            Nodo nodo_hijo = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("UMINUS", nodo_hijo, null));
        }
break;
case 60:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 61:

{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("*", nodo_izq, nodo_der));
        }
break;
case 62:

{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("/", nodo_izq, nodo_der));
        }
break;
case 63:

{ yyval = new ParserVal(new NodoVariable(val_peek(0).sval)); }
break;
case 64:

{
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoConstante(attrs.getValor(), "long"));
        }
break;
case 65:

{
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoConstante(attrs.getValor(), "dfloat"));
        }
break;
case 66:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 67:

{
            yyval = new ParserVal(new NodoInvocacion(val_peek(3).sval, (ArrayList<NodoParametroReal>)val_peek(1).obj));
        }
break;
case 68:

{ yyval = new ParserVal(null); }
break;
case 69:

{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 70:

{   
            ArrayList<NodoParametroReal> lista = new ArrayList<>();
            lista.add((NodoParametroReal)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 71:

{
            ArrayList<NodoParametroReal> lista = (ArrayList<NodoParametroReal>)val_peek(2).obj;
            lista.add((NodoParametroReal)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 72:

{
            yyval = new ParserVal(new NodoParametroReal((Nodo)val_peek(2).obj, val_peek(0).sval));
        }
break;
case 73:

{
            String lexemaCadena = val_peek(1).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCadena);
            NodoConstante nodoCadena = new NodoConstante(attrs.getValor(), "string");
            yyval = new ParserVal(new NodoPrint(nodoCadena));
        }
break;
case 74:

{
            yyval = new ParserVal(new NodoPrint((Nodo)val_peek(1).obj));
        }
break;
case 75:

{
            ArrayList<Nodo> expresiones = (ArrayList<Nodo>)val_peek(1).obj;
            if (funcionActual == null) {
                yyerror("Sentencia RETURN fuera de una funcion.");
                yyval = new ParserVal(null);
            } else {
                yyval = new ParserVal(new NodoReturn(expresiones, funcionActual));
            }
        }
break;
case 76:

{
            String tipoParamLambda = val_peek(8).sval;
            String nombreParamLambda = val_peek(7).sval;
            NodoBloque cuerpoLambda = (NodoBloque)val_peek(4).obj;
            NodoArgumento argLambda = (NodoArgumento)val_peek(1).obj;
            yyval = new ParserVal(new NodoLambdaInline(tipoParamLambda, nombreParamLambda, cuerpoLambda, argLambda));
            System.out.println("Linea " + lexer.numero_linea + ": Expresión Lambda en línea reconocida.");
        }
break;
case 77:

{
            yyval = new ParserVal(new NodoArgumento(val_peek(0).sval, "id"));
        }
break;
case 78:

{
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoArgumento(attrs.getValor(), "long"));
        }
break;
case 79:

{
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoArgumento(attrs.getValor(), "dfloat"));
        }
break;


    }
    
    if (yydebug) debug("reduce");
    state_drop(yym);             
    yystate = state_peek(0);     
    val_drop(yym);               
    yym = yylhs[yyn];            
    if (yystate == 0 && yym == 0)
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         
      state_push(YYFINAL);       
      val_push(yyval);           
      if (yychar < 0)            
        {
        yychar = yylex();        
        if (yychar<0) yychar=0;  
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          
         break;                 
      }
    else                        
      {                         
      yyn = yygindex[yym];      
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; 
      else
        yystate = yydgoto[yym]; 
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     
      val_push(yyval);         
      }
    }
  return 0;
}






public void run()
{
  yyparse();
}






public Parser()
{
  
}



public Parser(boolean debugMe)
{
  yydebug=debugMe;
}




}


