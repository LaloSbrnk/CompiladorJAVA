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






//#line 2 "gramatica.y"
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
    import java.io.FileWriter; /* <--- NUEVO*/
    import java.io.BufferedWriter; /* <--- NUEVO*/
//#line 32 "Parser.java"




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
    0,    1,    1,    2,    2,    2,    2,    3,    3,   31,
   31,   34,   34,   25,   32,   27,   27,   28,   28,   29,
   29,   30,   36,   36,   36,   26,    4,    4,    4,    4,
    4,    4,    4,    4,    5,   33,   33,    6,    7,    7,
    8,    8,    9,   10,   10,   11,   12,   12,   13,   35,
   35,   35,   35,   35,   35,   14,   14,   14,   14,   15,
   15,   15,   16,   16,   16,   16,   17,   18,   18,   19,
   19,   20,   21,   21,   22,   23,   24,   24,   24,
};
final static short yylen[] = {                            2,
    4,    0,    2,    2,    2,    1,    2,    2,    2,    1,
    3,    1,    1,    9,    0,    1,    3,    0,    1,    1,
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
   31,   32,   33,    6,    0,    0,    0,    7,    0,    0,
    0,    0,    0,    0,    9,    0,    2,   47,    0,   59,
   63,    0,    4,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   10,    0,    0,    0,    0,   70,   37,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   40,
    0,    0,    0,   61,   62,    0,   17,    0,    0,    0,
   67,    0,    0,   52,   53,   55,   54,   50,   51,    0,
   73,   74,    0,   75,   48,    0,    0,    0,    0,    0,
   20,    0,   11,   72,   71,    0,    0,    0,    0,    2,
   24,   25,   15,    0,    0,    0,    0,   46,    0,    0,
   21,   22,   45,   43,    0,    2,    0,    0,    0,   77,
   78,   79,    0,   14,   76,
};
final static short yydgoto[] = {                          2,
    4,   19,   20,   21,   22,   23,   24,   75,   25,  127,
   26,   49,   71,   27,   28,   29,   30,   67,   68,   69,
   31,   32,   33,  143,   34,  139,   35,  109,  110,  111,
   65,  130,   51,   37,  100,  112,
};
final static short yysindex[] = {                      -221,
  -78,    0,    0,   25,   24,   14,    0,    0,   42,   49,
   52,    0,    0, -162,   80,  101,    0, -241,    0,   37,
   38,    0,    0,    2,    0,    0,   12,    5,    0,    0,
    0,    0,    0,    0,  -29, -175, -156,    0,  101, -155,
  101,  -34,  101,   58,    0, -175,    0,    0, -165,    0,
    0, -151,    0,    0, -162,  101, -188, -188, -188, -188,
   69, -241,  101,    0,   66,  -38,   70,   71,    0,    0,
   72,  -21,   73,   21,   33,   12,   39,   77,   78,    0,
   74,    5,    5,    0,    0, -150,    0,   12, -135, -134,
    0,  101,   80,    0,    0,    0,    0,    0,    0,  101,
    0,    0,  101,    0,    0,  101,    8, -192,   87,   88,
    0, -241,    0,    0,    0, -128,   12,   12,   99,    0,
    0,    0,    0, -150, -122,   80, -121,    0,   53,   20,
    0,    0,    0,    0,  105,    0, -184,   67,   22,    0,
    0,    0,  110,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -51,  -31,    0,    0,
    0,    0,    0,    0,    0,   94,  112,    0,  113,    0,
    0,    0,    0,    6,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   98,    0,    0,  117,    0,    0,
    0,    0,    0,    0,    0,   85,    0,    0,    0,    0,
    9,  -11,   -1,    0,    0,  -24,    0,  100,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  119,
    0,    0,    0,    0,    0, -101,  122,   89,    0,    0,
    0,    0,    0, -181,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   40,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -12,    0,    0,    1,  152,    0,    0,  111,    0,    0,
    0,  -70,   62,  326,   30,   31,    0,    0,    0,   79,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   45,
    0,    0,   23,   -9,    0,    0,
};
final static int YYTABLESIZE=432;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         36,
   36,   36,   36,   36,   57,   36,   58,   34,   52,   56,
   16,   56,   56,   56,   62,   48,   18,   36,   36,   36,
   36,   57,  116,   58,   12,   13,   36,   56,   56,   57,
   56,   57,   57,   57,   77,    1,   46,   36,   99,   58,
   98,   58,   58,   58,    3,   55,   59,   57,   57,   36,
   57,   60,   87,   39,   57,  133,   58,   58,   58,   40,
   58,  102,   56,   57,   18,   58,   36,   38,    6,   16,
    7,    8,  140,  104,  141,  142,  103,   80,   18,  121,
  122,   41,   38,   16,   23,   23,   82,   83,   42,   84,
   85,   43,   18,   48,   44,   53,   54,   16,   63,   36,
   64,   70,  125,   40,   78,   79,   18,  129,   86,   89,
   91,   16,   93,  101,   92,   36,  106,  103,  107,   18,
  108,  113,  114,  138,   16,   41,   48,  123,   41,   42,
  120,  124,   42,  126,  132,   63,   63,   39,   63,  128,
   63,  134,  136,   41,  137,   16,  144,   42,   36,   17,
  145,   36,   63,   68,   39,   16,    8,   69,   35,   19,
   36,   44,   49,  105,   26,   45,   81,  119,  131,    0,
  115,    0,    0,    0,    0,    0,    0,  135,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
   34,   34,    0,    0,    0,    0,    0,    0,   34,    0,
   36,   36,    6,   73,    7,    8,    0,   61,   36,    0,
   56,   56,   36,   36,   36,   36,   36,   36,   56,    0,
   90,   23,   23,   56,   56,   56,   56,   56,    0,    0,
   57,   57,    0,   94,   95,   96,   97,    0,   57,    0,
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
    8,   35,   35,   63,   66,    0,   72,   74,   76,   35,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   76,    0,    0,    0,    0,    0,    0,   88,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   66,    0,    0,
    0,    0,    0,    0,    0,  117,    0,    0,  118,    0,
    0,   72,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   43,   47,   45,   59,   18,   41,
   45,   43,   44,   45,   44,   15,   41,   59,   60,   61,
   62,   43,   93,   45,  266,  267,    4,   59,   60,   41,
   62,   43,   44,   45,   47,  257,   14,   15,   60,   41,
   62,   43,   44,   45,  123,   44,   42,   59,   60,   44,
   62,   47,   62,   40,   43,  126,   45,   59,   60,   46,
   62,   41,   61,   43,   40,   45,   61,   59,  257,   45,
  259,  260,  257,   41,  259,  260,   44,   55,   40,  272,
  273,   40,   59,   45,  266,  267,   57,   58,   40,   59,
   60,   40,   40,   93,  257,   59,   59,   45,  274,   77,
  257,  257,  112,   46,  270,  257,   40,  120,   40,   44,
   41,   45,   41,   41,   44,   93,   40,   44,   41,   40,
  271,  257,  257,  136,   45,   41,  126,   41,   44,   41,
  123,   44,   44,  262,  257,   42,   43,   44,   45,   41,
   47,  263,  123,   59,   40,   45,  125,   59,  126,  125,
   41,  129,   59,   41,   61,   44,   59,   41,   59,   41,
  138,  263,   41,  125,  125,   14,   56,  106,  124,   -1,
   92,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,
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
   -1,   56,   -1,   -1,   -1,   -1,   -1,   -1,   63,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   92,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  100,   -1,   -1,  103,   -1,
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
"funcion_def : lista_tipos IDENTIFICADOR '(' parametros_formales_opt ')' accion_media_funcion '{' cuerpo_funcion '}'",
"accion_media_funcion :",
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

//#line 603 "gramatica.y"

private NodoFuncionDef funcionActual = null; //variable para saber en que funcion estamos
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

// --- METODO MAIN ACTUALIZADO ---
public static void main(String[] args) {
    if (args.length == 0) {
        System.err.println("Error: Debes pasar la ruta del archivo a compilar como argumento.");
        return;
    }
    
    String rutaFuente = args[0];
    String rutaAsm = rutaFuente.replaceAll("\\..*$", ".asm"); // Cambia extensión a .asm

    try {
        AnalizadorLexico lexer = new AnalizadorLexico(rutaFuente);
        Parser parser = new Parser(lexer);

        parser.yyparse(); 
        
        Nodo arbol = (Nodo) parser.yyval.obj;
        TablaDeAmbitos tablaDeAmbitos = new TablaDeAmbitos(AnalizadorLexico.tablaSimbolos);
        
        // --- 1. Chequeo Semantico ---
        if (arbol != null && parser.yynerrs == 0) { 
            System.out.println("\n--- Iniciando Chequeo Semantico ---");
            arbol.chequear(tablaDeAmbitos);
            System.out.println("--- Chequeo Semantico Finalizado ---");
        } else {
            System.out.println("\n--- Chequeo Semantico Omitido (Errores Sintacticos o AST nulo) ---");
        }

        // --- 2. Generación de Código (NUEVO TP4) ---
        // (Solo si no hubo errores sintácticos)
        if (arbol != null && parser.yynerrs == 0) {
            System.out.println("\n--- Iniciando Generacion de Codigo Assembler ---");
            
            // Crear el generador
            GeneradorAssembler generador = new GeneradorAssembler(tablaDeAmbitos);
            
            // Recorrer el AST para generar el código
            arbol.generarCodigo(generador, tablaDeAmbitos);
            
            // Escribir el archivo .asm
            generador.escribirArchivo(rutaAsm);
            System.out.println("--- Generacion de Codigo Finalizada ---");
            System.out.println("Archivo Assembler generado en: " + rutaAsm);
        } else {
             System.out.println("\n--- Generacion de Codigo Omitida ---");
        }

        // --- 3. Salidas Requeridas (TP4 [cite: 1808-1811]) ---
        
        // Salida 1: Errores (Léxicos, Sintácticos, Semánticos)
        // (System.err ya imprimió los sintácticos y semánticos)
        AnalizadorLexico.printReporteLexico(); 

        // Salida 2: Representación Intermedia (Árbol Sintáctico)
        System.out.println("\n--- 2. Representacion Intermedia (Arbol Sintactico) ---");
        if (arbol != null) {
            arbol.imprimir("");
        } else {
            System.out.println("Arbol no generado.");
        }

        // Salida 3: Contenido de la Tabla de Símbolos
        lexer.printTablaSimbolos();
        
        // Salida 4: Archivo .asm (ya generado)

    } catch (IOException e) {
        System.err.println("Error de I/O al leer el archivo: " + e.getMessage());
    } catch (Exception e) { 
        System.err.println("Error general durante la compilacion: " + e.getMessage());
        e.printStackTrace(); 
    }
}
//#line 509 "Parser.java"
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
//#line 57 "gramatica.y"
{
            String nombreProg = val_peek(3).sval;
            NodoBloque bloqueProg = (NodoBloque)val_peek(1).obj;
            yyval = new ParserVal(new NodoPrograma(nombreProg, bloqueProg));
            System.out.println("Programa completo reconocido y AST construido.");
        }
break;
case 2:
//#line 67 "gramatica.y"
{
            yyval = new ParserVal(new NodoBloque());
        }
break;
case 3:
//#line 71 "gramatica.y"
{
            NodoBloque bloque = (NodoBloque)val_peek(1).obj;
            Nodo nuevaSentencia = (Nodo)val_peek(0).obj;
            bloque.agregarSentencia(nuevaSentencia);
            yyval = new ParserVal(bloque);
        }
break;
case 4:
//#line 81 "gramatica.y"
{
            yyval = new ParserVal(val_peek(1).obj);
        }
break;
case 5:
//#line 85 "gramatica.y"
{   
            yyval = new ParserVal(val_peek(1).obj);
        }
break;
case 6:
//#line 89 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj); /* Pasa el NodoFuncionDef*/
        }
break;
case 7:
//#line 94 "gramatica.y"
{ 
            System.err.println("ERROR Sintactico recuperado en línea " + lexer.numero_linea);
            yyval = new ParserVal(null); 
        }
break;
case 8:
//#line 106 "gramatica.y"
{
            String tipo_de_datos = val_peek(1).sval;
            ArrayList<String> variables = (ArrayList<String>)val_peek(0).obj;
            yyval = new ParserVal(new NodoDeclaracion(tipo_de_datos, variables));
        }
break;
case 9:
//#line 112 "gramatica.y"
{
            /* Marcamos el nodo como inferencia*/
            NodoAsignacion nodoAsig = (NodoAsignacion)val_peek(0).obj;
            nodoAsig.setEsInferencia(); /* <-- AÑADIR ESTO*/
            yyval = new ParserVal(nodoAsig);
        }
break;
case 10:
//#line 122 "gramatica.y"
{
            ArrayList<String> lista = new ArrayList<>();
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 11:
//#line 128 "gramatica.y"
{
            ArrayList<String> lista = (ArrayList<String>)val_peek(2).obj;
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 12:
//#line 137 "gramatica.y"
{
            yyval = new ParserVal("long");
        }
break;
case 13:
//#line 141 "gramatica.y"
{
            yyval = new ParserVal("dfloat");
        }
break;
case 14:
//#line 149 "gramatica.y"
{ 
          /* $3.obj es el 'nodoFunc' incompleto de la accion anterior*/
          NodoFuncionDef nodoFunc = (NodoFuncionDef)val_peek(3).obj;
          /* $1.obj es el NodoBloque de 'cuerpo_funcion'*/
          NodoBloque cuerpoFunc = (NodoBloque)val_peek(1).obj;
          
          /* Completamos el nodo con el cuerpo*/
          nodoFunc.setCuerpo(cuerpoFunc);

          /* Devolvemos el nodo completo*/
          yyval = new ParserVal(nodoFunc);
          
          String nombreFuncion = nodoFunc.getNombre();
          System.out.println("Linea " + lexer.numero_linea + ": Definicion de funcion '" + nombreFuncion + "' reconocida.");
          
          /* Reseteamos funcionActual al salir*/
          funcionActual = null;
      }
break;
case 15:
//#line 171 "gramatica.y"
{ 
          List<String> tiposRetorno = (ArrayList<String>)val_peek(4).obj;
          String nombreFuncion = val_peek(3).sval;
          ArrayList<NodoParametro> params = (ArrayList<NodoParametro>)val_peek(1).obj;

          /* --- INICIO DE LA LÓGICA CORREGIDA ---*/
          /* Simplemente CREAMOS los atributos, pero NO los guardamos en la tabla*/
          /* para evitar la duplicación de símbolos.*/
          
          AtributosTokens attrsFuncion = new AtributosTokens(TiposToken.IDENTIFICADOR);
          
          /* (El chequeo de redefinición lo hará NodoFuncionDef.chequear() */
          /*  después, con el ámbito correcto).*/
          
          /* --- FIN DE LA LÓGICA CORREGIDA ---*/

          attrsFuncion.setUso("funcion");
          attrsFuncion.setTiposRetorno(tiposRetorno);
          attrsFuncion.setParametros(params);
          
          /* Creamos el nodo INCOMPLETO (sin cuerpo)*/
          NodoFuncionDef nodoFunc = new NodoFuncionDef(nombreFuncion, tiposRetorno, params, null, attrsFuncion);
          
          /* Seteamos ANTES de parsear el cuerpo!*/
          funcionActual = nodoFunc; 
          
          /* Pasamos el nodo incompleto a la siguiente parte de la regla*/
          yyval = new ParserVal(nodoFunc);
      }
break;
case 16:
//#line 204 "gramatica.y"
{
            ArrayList<String> lista = new ArrayList<>();
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 17:
//#line 210 "gramatica.y"
{
            ArrayList<String> lista = (ArrayList<String>)val_peek(2).obj;
            lista.add(val_peek(0).sval);
            yyval = new ParserVal(lista);
        }
break;
case 18:
//#line 219 "gramatica.y"
{
            yyval = new ParserVal(null);
        }
break;
case 19:
//#line 223 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj);
        }
break;
case 20:
//#line 230 "gramatica.y"
{
            ArrayList<NodoParametro> lista = new ArrayList<>();
            lista.add((NodoParametro)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 21:
//#line 236 "gramatica.y"
{
            ArrayList<NodoParametro> lista = (ArrayList<NodoParametro>)val_peek(2).obj;
            lista.add((NodoParametro)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 22:
//#line 246 "gramatica.y"
{
            /* $3=ID(nombre), $2=tipo, $1=modoPasaje*/
            yyval = new ParserVal(new NodoParametro((String)val_peek(0).sval, (String)val_peek(1).sval, (String)val_peek(2).sval));
        }
break;
case 23:
//#line 254 "gramatica.y"
{ yyval = new ParserVal(null); }
break;
case 24:
//#line 256 "gramatica.y"
{ yyval = new ParserVal("cv sl"); }
break;
case 25:
//#line 258 "gramatica.y"
{ yyval = new ParserVal("cv le"); }
break;
case 26:
//#line 263 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj); 
        }
break;
case 27:
//#line 272 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 28:
//#line 274 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 29:
//#line 276 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 30:
//#line 278 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 31:
//#line 280 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 32:
//#line 282 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 33:
//#line 284 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 34:
//#line 286 "gramatica.y"
{ yyval = new ParserVal(val_peek(0).obj); }
break;
case 35:
//#line 291 "gramatica.y"
{
            NodoVariable var = new NodoVariable(val_peek(2).sval);
            Nodo expr = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoAsignacion(var, expr));
        }
break;
case 36:
//#line 301 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).sval);
        }
break;
case 37:
//#line 305 "gramatica.y"
{
            yyval = new ParserVal(val_peek(2).sval + "." + val_peek(0).sval);
        }
break;
case 38:
//#line 313 "gramatica.y"
{
            ArrayList<NodoVariable> vars = (ArrayList<NodoVariable>)val_peek(2).obj;
            ArrayList<Nodo> exprs = (ArrayList<Nodo>)val_peek(0).obj;
            yyval = new ParserVal(new NodoAsignacionMultiple(vars, exprs));
        }
break;
case 39:
//#line 322 "gramatica.y"
{
            ArrayList<NodoVariable> lista = new ArrayList<>();
            lista.add(new NodoVariable(val_peek(0).sval)); 
            yyval = new ParserVal(lista);
        }
break;
case 40:
//#line 328 "gramatica.y"
{
            ArrayList<NodoVariable> lista = (ArrayList<NodoVariable>)val_peek(2).obj;
            lista.add(new NodoVariable(val_peek(0).sval));
            yyval = new ParserVal(lista);
        }
break;
case 41:
//#line 337 "gramatica.y"
{
            ArrayList<Nodo> listaNodos = new ArrayList<>();
            listaNodos.add((Nodo)val_peek(0).obj);
            yyval = new ParserVal(listaNodos);
        }
break;
case 42:
//#line 343 "gramatica.y"
{
            ArrayList<Nodo> listaNodos = (ArrayList<Nodo>)val_peek(2).obj;
            listaNodos.add((Nodo)val_peek(0).obj);
            yyval = new ParserVal(listaNodos);
        }
break;
case 43:
//#line 354 "gramatica.y"
{
            Nodo nodo_cond = (Nodo)val_peek(4).obj;
            NodoBloque bloque_true = (NodoBloque)val_peek(2).obj;
            NodoBloque bloque_false = (NodoBloque)val_peek(1).obj;
            yyval = new ParserVal(new NodoIf(nodo_cond, bloque_true, bloque_false));
        }
break;
case 44:
//#line 364 "gramatica.y"
{
            yyval = new ParserVal(null);
        }
break;
case 45:
//#line 368 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj);
        }
break;
case 46:
//#line 376 "gramatica.y"
{
            NodoBloque bloque_do = (NodoBloque)val_peek(4).obj;
            Nodo nodo_cond = (Nodo)val_peek(1).obj;
            yyval = new ParserVal(new NodoDoUntil(bloque_do, nodo_cond));
        }
break;
case 47:
//#line 385 "gramatica.y"
{
            NodoBloque bloque = new NodoBloque();
            bloque.agregarSentencia((Nodo)val_peek(0).obj);
            yyval = new ParserVal(bloque);
        }
break;
case 48:
//#line 391 "gramatica.y"
{
            yyval = new ParserVal(val_peek(1).obj);
        }
break;
case 49:
//#line 401 "gramatica.y"
{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            String op = val_peek(1).sval;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoCondicion(nodo_izq, nodo_der, op));
        }
break;
case 50:
//#line 410 "gramatica.y"
{ yyval = new ParserVal(">"); }
break;
case 51:
//#line 411 "gramatica.y"
{ yyval = new ParserVal("<"); }
break;
case 52:
//#line 412 "gramatica.y"
{ yyval = new ParserVal(">="); }
break;
case 53:
//#line 413 "gramatica.y"
{ yyval = new ParserVal("<="); }
break;
case 54:
//#line 414 "gramatica.y"
{ yyval = new ParserVal("=="); }
break;
case 55:
//#line 415 "gramatica.y"
{ yyval = new ParserVal("!="); }
break;
case 56:
//#line 420 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj);
        }
break;
case 57:
//#line 424 "gramatica.y"
{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("+", nodo_izq, nodo_der));
        }
break;
case 58:
//#line 430 "gramatica.y"
{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("-", nodo_izq, nodo_der));
        }
break;
case 59:
//#line 437 "gramatica.y"
{
            Nodo nodo_hijo = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("UMINUS", nodo_hijo, null));
        }
break;
case 60:
//#line 445 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj);
        }
break;
case 61:
//#line 449 "gramatica.y"
{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("*", nodo_izq, nodo_der));
        }
break;
case 62:
//#line 455 "gramatica.y"
{
            Nodo nodo_izq = (Nodo)val_peek(2).obj;
            Nodo nodo_der = (Nodo)val_peek(0).obj;
            yyval = new ParserVal(new NodoOperacion("/", nodo_izq, nodo_der));
        }
break;
case 63:
//#line 464 "gramatica.y"
{
            /* El lexer nos pasa el nombre simple (ej. "X") vía sval*/
            yyval = new ParserVal(new NodoVariable(val_peek(0).sval));
        }
break;
case 64:
//#line 469 "gramatica.y"
{
            /* MODIFICADO:*/
            /* 1. Obtener la referencia (lexema, ej. "100L") que pasó el léxico*/
            String lexemaCte = val_peek(0).sval;
            
            /* 2. Usar la referencia para buscar la entrada completa en la TS*/
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            
            /* 3. Construir el nodo usando el valor real de los atributos*/
            yyval = new ParserVal(new NodoConstante(attrs.getValor(), "long"));
        }
break;
case 65:
//#line 481 "gramatica.y"
{
            /* MODIFICADO:*/
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoConstante(attrs.getValor(), "dfloat"));
        }
break;
case 66:
//#line 488 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj);
        }
break;
case 67:
//#line 498 "gramatica.y"
{
            yyval = new ParserVal(new NodoInvocacion(val_peek(3).sval, (ArrayList<NodoParametroReal>)val_peek(1).obj));
        }
break;
case 68:
//#line 505 "gramatica.y"
{
            yyval = new ParserVal(null);
        }
break;
case 69:
//#line 509 "gramatica.y"
{
            yyval = new ParserVal(val_peek(0).obj);
        }
break;
case 70:
//#line 516 "gramatica.y"
{   
            ArrayList<NodoParametroReal> lista = new ArrayList<>();
            lista.add((NodoParametroReal)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 71:
//#line 522 "gramatica.y"
{
            ArrayList<NodoParametroReal> lista = (ArrayList<NodoParametroReal>)val_peek(2).obj;
            lista.add((NodoParametroReal)val_peek(0).obj);
            yyval = new ParserVal(lista);
        }
break;
case 72:
//#line 531 "gramatica.y"
{
            yyval = new ParserVal(new NodoParametroReal((Nodo)val_peek(2).obj, val_peek(0).sval));
        }
break;
case 73:
//#line 538 "gramatica.y"
{
            /* MODIFICADO: El lexer nos pasa la referencia (el lexema)*/
            String lexemaCadena = val_peek(1).sval;
            /* La buscamos en la TS para obtener los atributos*/
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCadena);
            /* Creamos el nodo. Usamos el valor real de la TS.*/
            NodoConstante nodoCadena = new NodoConstante(attrs.getValor(), "string");
            yyval = new ParserVal(new NodoPrint(nodoCadena));
        }
break;
case 74:
//#line 548 "gramatica.y"
{
            yyval = new ParserVal(new NodoPrint((Nodo)val_peek(1).obj));
        }
break;
case 75:
//#line 556 "gramatica.y"
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
//#line 570 "gramatica.y"
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
//#line 582 "gramatica.y"
{
            /* $1.sval es el nombre/referencia del ID*/
            yyval = new ParserVal(new NodoArgumento(val_peek(0).sval, "id"));
        }
break;
case 78:
//#line 587 "gramatica.y"
{
            /* MODIFICADO: $1.sval es la referencia (lexema)*/
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoArgumento(attrs.getValor(), "long"));
        }
break;
case 79:
//#line 594 "gramatica.y"
{
            /* MODIFICADO: $1.sval es la referencia (lexema)*/
            String lexemaCte = val_peek(0).sval;
            AtributosTokens attrs = AnalizadorLexico.tablaSimbolos.get(lexemaCte);
            yyval = new ParserVal(new NodoArgumento(attrs.getValor(), "dfloat"));
        }
break;
//#line 1235 "Parser.java"
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
