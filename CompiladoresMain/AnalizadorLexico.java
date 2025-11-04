package CompiladoresMain;

import java.io.BufferedReader;
import java.io.FileReader;
// para probar string reader
import java.io.StringReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// --- Import de Archivos, Acciones, Conjuntos, etc... ---
import AccionesSemanticas.*;
import conjuntoSimbolos.*;

public class AnalizadorLexico {

    // --- Constantes de estado ---
    private static final int ESTADO_ERROR = -1;
    private static final int ESTADO_FINAL = -2;

    // --- Componentes del Autómata ---
    private int[][] matrizTransicion;
    private AccionSemantica[][] matrizAcciones;
    private ArrayList<ConjuntoSimbolos> mapeoColumnas = new ArrayList<>();

    // --- Estructuras de Datos ---
    public static HashMap<String, AtributosTokens> tablaSimbolos = new HashMap<>();
    public static ArrayList<String> errores_y_warnings = new ArrayList<>();

    // --- Variables de Estado de Lectura ---
    private BufferedReader lector;
    private String lineaActual;
    public static int numero_linea = 1;
    public static int indice_caracter_leer = 0;
    public static int cant_constantes = 0;
    public static int estado_actual = 0;

    //TP2
    public ParserVal yylval;

    // --- Constructor ---
    public AnalizadorLexico(String rutaArchivo) throws IOException {
        this.lector = new BufferedReader(new FileReader(rutaArchivo));
        this.lineaActual = lector.readLine();
        if (this.lineaActual != null) {
            this.lineaActual += "\n"; // salto de linea para marcar el final
        }
        iniciarColumnas();
        cargarMatrices();
        cargarTablaSimbolos();
    }

    private void iniciarColumnas(){
    mapeoColumnas.add(new ConjuntoD());                // Columna 0: "D"
    mapeoColumnas.add(new ConjuntoL());                // Columna 1: "L"
    mapeoColumnas.add(new ConjuntoMayus());            // Columna 2: L (Mayúsculas)
    mapeoColumnas.add(new ConjuntoMinus());            // Columna 3: l (Minúsculas)
    mapeoColumnas.add(new ConjuntoDigito());           // Columna 4: d
    mapeoColumnas.add(new ConjuntoPorcentaje());       // Columna 5: %
    mapeoColumnas.add(new ConjuntoSignos());           // Columna 6: _  
    mapeoColumnas.add(new ConjuntoPunto());            // Columna 7: .
    mapeoColumnas.add(new ConjuntoMas());              // Columna 8: +
    mapeoColumnas.add(new ConjuntoMayor());            // Columna 10: >
    mapeoColumnas.add(new ConjuntoMenor());            // Columna 11: <
    mapeoColumnas.add(new ConjuntoIgual());            // Columna 13: =
    mapeoColumnas.add(new ConjuntoExclamasion());      // Columna 12: !
    mapeoColumnas.add(new ConjuntoDosPuntos());        // Columna 14: :
    mapeoColumnas.add(new ConjuntoMenos());            // Columna 15: ;
    mapeoColumnas.add(new ConjuntoComillaDoble());     // Columna 17: "
    mapeoColumnas.add(new ConjuntoNumeral());          // Columna 16: #
    mapeoColumnas.add(new ConjuntoSaltoLinea());       // Columna 20: \n
    mapeoColumnas.add(new ConjuntoBlanco());           // Columna 18: espacio
    mapeoColumnas.add(new ConjuntoTabulado());         // Columna 19: \t
}

    private void cargarMatrices(){
        matrizTransicion = new int[][]{
        /* 0  */    {1, 1, 1, 6, 7, ESTADO_ERROR, ESTADO_FINAL, 8, ESTADO_FINAL, 3, 3, 2, ESTADO_ERROR, 4, 5, 13, 14, 0, 0, 0, ESTADO_ERROR},
        /* 1  */    {1, 1, 1, ESTADO_FINAL, 1, 1, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
        /* 2  */    {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
        /* 3  */    {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
        /* 4  */    {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_FINAL, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
        /* 5  */    {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
        /* 6  */    {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, 6, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
        /* 7  */    {ESTADO_ERROR, ESTADO_FINAL, ESTADO_ERROR, ESTADO_ERROR, 7, ESTADO_ERROR, ESTADO_ERROR, 9, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
        /* 8  */    {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 9, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
        /* 9  */    {10, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, 9, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
        /* 10  */    {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 11, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 11, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
        /* 11 */    {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 12, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
        /* 12 */    {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, 12, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
        /* 13 */    {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, ESTADO_FINAL, 13, ESTADO_ERROR, 13, 13, 13},
        /* 14 */    {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 15, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
        /* 15 */    {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 16, 15, 15, 15, 15},
        /* 16 */    {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0, 15, 15, 15, 15}
        };
        matrizAcciones = new AccionSemantica[][]{
        /* 0  */ {new AS1(), new AS1(), new AS1(), new AS1(), new AS1(), new ASE(), new AS2(), new AS1(), new AS2(), new AS1(), new AS1(), new AS1(), new ASE(), new AS1(), new AS1(), null, null, null, null, null, new ASE()},
        /* 1  */ {new AS3(), new AS3(), new AS3(), new AS4(), new AS3(), new AS3(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4()},
        /* 2  */ {new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS6(), new AS6(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5()},
        /* 3  */ {new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS6(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5()},
        /* 4  */ {new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new AS6(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
        /* 5  */ {new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS6(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5()},
        /* 6  */ {new AS7(), new AS7(), new AS7(), new AS3(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7()},
        /* 7  */ {new ASE(), new AS8(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
        /* 8  */ {new ASE(), new ASE(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
        /* 9  */ {new AS3(), new AS9(), new AS9(), new AS9(), new AS3(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9()},
        /* 10 */ {new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
        /* 11 */ {new ASE(), new ASE(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
        /* 12 */ {new AS9(), new AS9(), new AS9(), new AS9(), new AS3(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9()},
        /* 13 */ {new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS10(), new AS3(), new ASE(), new AS3(), new AS3(), new AS3()},
        /* 14 */ {new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), null, new ASE(), new ASE(), new ASE(), new ASE()},
        /* 15 */ {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 16 */ {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
    };

    } 
    public void printWarnings(){
        System.out.println();
        System.out.println("---Warnings---");
        for(String warnings: errores_y_warnings)
            if(warnings.contains("WARNING"))
                System.out.println(warnings);
    }

    public void printErrors(){
    System.out.println();
    System.out.println("---Errores---");
    for(String errores: errores_y_warnings)
        if(errores.contains("ERROR"))
            System.out.println(errores);
}

    public void printTablaSimbolos(){
        System.out.println();
        System.out.println("---Tabla de Simbolos---");
        for(Map.Entry<String, AtributosTokens> entry: tablaSimbolos.entrySet()){
            String lexema = entry.getKey();
            AtributosTokens atributo = entry.getValue();
            System.out.println("\"" + lexema + "\", " + atributo);
        }
    }

    // --- Métodos Principales ---

    public int yylex() {
        try{
            int estado_actual = 0;
            Token newToken = new Token(); //Crear un nuevo Token vacio

            //Entrar en un bucle que no termine hasta que se retorne un token
            while (true) {
                if (lineaActual  == null) return 0; //manejo fin de linea y fin de archivo
                if (indice_caracter_leer >= lineaActual.length()){
                    lineaActual = lector.readLine();
                    if (lineaActual != null){
                        lineaActual += "\n";
                        numero_linea ++;
                        indice_caracter_leer = 0;
                    } else return 0; //fin del archivo
                }

                //leo caracter y obtengo linea
                char caracter = lineaActual.charAt(indice_caracter_leer);
                indice_caracter_leer ++;
                boolean col_correcta = false;
                int j = 0;
                while (!col_correcta && j < mapeoColumnas.size()){
                    if (mapeoColumnas.get(j).contieneSimbolo(caracter))
                        col_correcta = true;
                    else
                        j++;
                }
                if (!col_correcta) {
                    j = 20; // El índice de la columna "Otro"
                }


                //consulto matrices
                AccionSemantica accionSemantica = matrizAcciones[estado_actual][j];
                int proximo_estado = matrizTransicion[estado_actual][j];

                //ejecuto accion
                if (accionSemantica != null)
                    accionSemantica.ejecutar(newToken, caracter);

                if (proximo_estado == ESTADO_FINAL){


                    int tokenId = newToken.getId();

                    
                    // Creamos un nuevo ParserVal para yylval. Por defecto está vacío.
                    this.yylval = new ParserVal();

                    // Dependiendo del tipo de token, ponemos el valor correcto en el campo correcto.
                    switch (tokenId) {
                        case Parser.IDENTIFICADOR:
                        case Parser.CADENA:
                            System.out.println("DEBUG -> Asignando a yylval.sval: '" + newToken.getLexema() + "'"); // <-- DEBUG CLAVE
                            // Para ID y CADENA, el parser espera un String en .sval
                            this.yylval.sval = newToken.getLexema();
                            break;

                        case Parser.CTE_LONG:
                            // Para CTE_LONG, el parser espera un int en .ival
                            AtributosTokens attrLong = tablaSimbolos.get(newToken.getLexema());
                            if (attrLong != null && attrLong.getValor() != null) {
                                // Hacemos la conversión de Long a int
                                this.yylval.ival = ((Long) attrLong.getValor()).intValue();
                            }
                            break;

                        case Parser.CTE_DFLOAT:
                            // Para CTE_DFLOAT, el parser espera un double en .dval
                            AtributosTokens attrDfloat = tablaSimbolos.get(newToken.getLexema());
                            if (attrDfloat != null && attrDfloat.getValor() != null) {
                                this.yylval.dval = (Double) attrDfloat.getValor();
                            }
                            break;
                        
                        // Para otros tokens (palabras reservadas, símbolos), no se necesita
                        // pasar un valor, por lo que yylval puede ir vacío.
                    }

                    System.out.println("DEBUG -> Token Reconocido: " + tokenId);
                    return tokenId; // Retornamos el ID del token
                } else if (proximo_estado == ESTADO_ERROR){
                    estado_actual = 0;
                    newToken = new Token();
                } else {
                    estado_actual = proximo_estado;
                }
            } //fin while(true)
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void cargarTablaSimbolos() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("Inicializacion/tablaSimbolos.txt"));
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] entrada = linea.split(" ");
            AtributosTokens at = new AtributosTokens(Integer.parseInt(entrada[1]));
            tablaSimbolos.put(entrada[0], at);
        }
    }

    
// --- Programa Principal para Probar ---// 

public static void main(String[] args) {
    if (args.length == 0) {
        System.out.println("Error: Debes pasar la ruta del archivo a compilar como argumento.");
        return;
    }

    try {
        AnalizadorLexico lex = new AnalizadorLexico(args[0]);
        int tokenId;

        System.out.println("---Lista de Tokens---");
        
        // El bucle ahora guarda el ID y comprueba que no sea el de fin de archivo (-1)
        while ((tokenId = lex.yylex()) != -1) { 
            // Obtenemos el token completo desde la variable pública yylval
            ParserVal t = lex.yylval; 
            if (t != null) { // Verificamos que no sea nulo (por si acaso)
                System.out.println(t.toString());
            }
        }

        lex.printTablaSimbolos();
        lex.printErrors();
        lex.printWarnings(); 

    } catch (IOException e) {
        System.err.println("Error al leer el archivo: " + e.getMessage());
    }
}
}