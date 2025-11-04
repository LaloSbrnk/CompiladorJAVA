//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package CompiladoresMain;



//#line 2 "gramatica.y"

//#line 19 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
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
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
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
//#### end semantic value section ####
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
    0,    1,    1,    2,    2,    2,    2,    3,    3,    7,
    7,    6,    6,    5,    9,    9,   10,   10,   12,   12,
   13,   14,   14,   14,   11,    4,    4,    4,    4,    4,
    4,    4,    4,    8,   22,   22,   15,   23,   23,   24,
   24,   16,   27,   27,   17,   26,   26,   25,   28,   28,
   28,   28,   28,   28,   21,   21,   21,   21,   29,   29,
   29,   30,   30,   30,   30,   31,   32,   32,   33,   33,
   34,   18,   18,   19,   20,   35,   35,   35,
};
final static short yylen[] = {                            2,
    4,    0,    2,    2,    2,    1,    2,    2,    2,    1,
    3,    1,    1,    8,    1,    3,    0,    1,    1,    3,
    3,    0,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    1,    3,    3,    1,    3,    1,
    3,    7,    0,    2,    6,    1,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    2,    1,    3,
    3,    1,    1,    1,    1,    4,    0,    1,    1,    3,
    3,    4,    4,    4,   10,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,   63,   64,    0,    0,
    0,   12,   13,    0,    0,    0,    1,    0,    3,    0,
    0,    6,    0,   26,    0,   27,   28,   29,   30,   31,
   32,    0,    0,    0,    0,   59,   65,    7,    0,    0,
    0,    0,    0,    0,    9,    0,    2,   46,    0,   58,
   62,    0,    4,    5,   10,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   69,   36,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   16,    0,    0,    0,   39,    0,   60,   61,    0,
   66,    0,   51,   52,   54,   53,   49,   50,    0,    0,
   72,   73,    0,   74,   47,    0,    0,   11,    0,    0,
    0,   19,    0,   71,   70,    0,    0,    0,    0,    2,
   23,   24,    0,    0,    0,    0,    0,   45,    0,    2,
   20,   21,   44,   42,    0,    0,    0,    0,   14,   76,
   77,   78,    0,   75,
};
final static short yydgoto[] = {                          2,
    4,   19,   20,   21,   22,   23,   56,   24,   25,  110,
  137,  111,  112,  113,   26,   27,   28,   29,   30,   31,
   32,   51,   34,   76,   72,   49,  127,   99,   35,   36,
   37,   67,   68,   69,  143,
};
final static short yysindex[] = {                      -219,
  -78,    0,    0,   25,   -6,   16,    0,    0,   15,   20,
   43,    0,    0, -165,   80,  101,    0, -241,    0,   36,
   37,    0, -160,    0,  -29,    0,    0,    0,    0,    0,
    0,   21, -175,   -7,   10,    0,    0,    0,  101, -155,
  101,  -34,  101,   57,    0, -175,    0,    0, -166,    0,
    0, -152,    0,    0,    0,   62,   68, -241, -185, -185,
  101, -165,  101, -185, -185,  -38,   70,   69,    0,    0,
  -21,   73,   74,   28,   21,   41,   39,   76,   78, -140,
 -150,    0,   10,   10,   21,    0,   79,    0,    0, -135,
    0,  101,    0,    0,    0,    0,    0,    0,  101,   80,
    0,    0,  101,    0,    0,  101,    8,    0, -196,   83,
   84,    0, -241,    0,    0,   21, -128,   21,   99,    0,
    0,    0,   12, -150, -115,   80, -120,    0,   53,    0,
    0,    0,    0,    0,  105,   67,   22, -179,    0,    0,
    0,    0,  108,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  107,    0,    0,    0,    0,    0,    0,    0,
    0,  -51,   94,    0,  -31,    0,    0,    0,  111,    0,
    0,    0,    0,    6,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   95,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  115,    0,    0,
    0,    0,    0,    0,   85,    0,    0,    0,    0,    0,
  -24,    0,  -11,   -1,    9,    0,  100,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  116,    0,    0,    0,    0,  119, -101,   89,    0,    0,
    0,    0,    0, -180,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   38,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -20,    0,    0,    1,    0,    5,    0,  151,    0,    0,
    0,    0,   42,    0,    0,    0,    0,    0,    0,    0,
  326,   32,    0,  104,   63,  -91,    0,    0,   29,   26,
    0,    0,    0,   81,    0,
};
final static int YYTABLESIZE=432;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         35,
   35,   35,   35,   35,   59,   35,   60,   33,  117,   55,
   16,   55,   55,   55,   58,   48,   17,   35,   35,   35,
   35,   59,   52,   60,   12,   13,   77,   55,   55,   56,
   55,   56,   56,   56,  133,   33,   62,    1,   98,   57,
   97,   57,   57,   57,    3,   46,   33,   56,   56,   35,
   56,   64,   38,   63,   41,   39,   65,   57,   57,   42,
   57,   40,   82,   59,   18,   60,   35,   34,  102,   16,
   59,    6,   60,    7,    8,  121,  122,  140,   18,  141,
  142,  104,   43,   16,  103,   22,   22,   83,   84,   88,
   89,   44,   18,   86,   53,   54,   55,   16,   61,  129,
   48,   70,   40,   78,   79,   80,   18,   81,   33,  136,
   91,   16,   92,  100,  101,  106,  108,  125,  107,   18,
  109,  114,  103,  123,   16,   40,   48,  124,   40,   41,
  120,   33,   41,  126,  130,   62,   62,   38,   62,  128,
   62,  132,  134,   40,  138,   16,  139,   41,  144,   17,
   15,   67,   62,    8,   38,   68,   18,   33,   37,   48,
   33,   43,   25,  105,   45,  131,   87,   33,  119,    0,
    0,    0,  115,    0,    0,    0,    0,  135,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
   33,   33,    0,    0,    0,    0,    0,    0,   33,    0,
   35,   35,    6,   73,    7,    8,    0,   57,   35,    0,
   55,   55,   35,   35,   35,   35,   35,   35,   55,    0,
   90,   22,   22,   55,   55,   55,   55,   55,    0,    0,
   56,   56,    0,   93,   94,   95,   96,    0,   56,    0,
   57,   57,    0,   56,   56,   56,   56,   56,   57,    0,
   34,   34,    0,   57,   57,   57,   57,   57,   34,   35,
    5,    6,    0,    7,    8,    9,    0,    0,   10,   11,
   12,   13,   14,   15,    5,    6,    0,    7,    8,    9,
    0,    0,   10,   11,   12,   13,   14,   15,    5,    6,
    0,    7,    8,    9,    0,    0,   10,   11,   12,   13,
   14,   15,    5,    6,    0,    7,    8,    9,    0,    0,
   10,   11,   12,   13,   14,   15,    6,    0,    7,    8,
    9,   50,    0,   10,   11,    0,   40,   40,   15,    0,
   41,   41,    0,    0,   40,   62,   62,    6,   41,    7,
    8,   37,   37,   62,   66,    0,   71,   74,   75,   37,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   85,    0,   75,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   66,    0,    0,
    0,    0,    0,    0,  116,    0,    0,    0,  118,    0,
    0,   71,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   43,   47,   45,   59,  100,   41,
   45,   43,   44,   45,   44,   15,   41,   59,   60,   61,
   62,   43,   18,   45,  266,  267,   47,   59,   60,   41,
   62,   43,   44,   45,  126,    4,   44,  257,   60,   41,
   62,   43,   44,   45,  123,   14,   15,   59,   60,   44,
   62,   42,   59,   61,   40,   40,   47,   59,   60,   40,
   62,   46,   58,   43,   40,   45,   61,   59,   41,   45,
   43,  257,   45,  259,  260,  272,  273,  257,   40,  259,
  260,   41,   40,   45,   44,  266,  267,   59,   60,   64,
   65,  257,   40,   62,   59,   59,  257,   45,  274,  120,
  100,  257,   46,  270,  257,   44,   40,   40,   77,  130,
   41,   45,   44,   41,   41,   40,  257,  113,   41,   40,
  271,  257,   44,   41,   45,   41,  126,   44,   44,   41,
  123,  100,   44,  262,  123,   42,   43,   44,   45,   41,
   47,  257,  263,   59,   40,   45,  125,   59,   41,  125,
   44,   41,   59,   59,   61,   41,   41,  126,   59,   41,
  129,  263,  125,  125,   14,  124,   63,  136,  106,   -1,
   -1,   -1,   92,   -1,   -1,   -1,   -1,  125,   -1,   -1,
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
   -1,   -1,   -1,   -1,   -1,   -1,   61,   -1,   63,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   92,   -1,   -1,
   -1,   -1,   -1,   -1,   99,   -1,   -1,   -1,  103,   -1,
   -1,  106,
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
"declarativa : tipo lista_variables",
"declarativa : VAR asignacion",
"lista_variables : IDENTIFICADOR",
"lista_variables : lista_variables ',' IDENTIFICADOR",
"tipo : LONG",
"tipo : DFLOAT",
"funcion_def : lista_tipos IDENTIFICADOR '(' parametros_formales_opt ')' '{' cuerpo_funcion '}'",
"lista_tipos : tipo",
"lista_tipos : lista_tipos ',' tipo",
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

//#line 242 "gramatica.y"

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
//#line 439 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
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
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 27 "gramatica.y"
{ System.out.println("Programa completo reconocido."); }
break;
case 7:
//#line 40 "gramatica.y"
{ System.err.println("ERROR Sintáctico recuperado en línea " + lexer.numero_linea); }
break;
case 8:
//#line 49 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Declaración de variables con tipo explícito."); }
break;
case 9:
//#line 51 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Declaración de variable por inferencia."); }
break;
case 14:
//#line 67 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Definición de función '" + val_peek(6).sval + "' reconocida."); }
break;
case 34:
//#line 116 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Asignación simple (:=) reconocida."); }
break;
case 37:
//#line 128 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Asignación múltiple (=) reconocida."); }
break;
case 42:
//#line 146 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Sentencia IF reconocida."); }
break;
case 45:
//#line 157 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Sentencia DO-UNTIL reconocida."); }
break;
case 72:
//#line 220 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Sentencia PRINT con cadena reconocida."); }
break;
case 73:
//#line 222 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Sentencia PRINT con expresión reconocida."); }
break;
case 74:
//#line 228 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Sentencia RETURN reconocida."); }
break;
case 75:
//#line 234 "gramatica.y"
{ System.out.println("Línea " + lexer.numero_linea + ": Expresión Lambda en línea reconocida."); }
break;
//#line 640 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
