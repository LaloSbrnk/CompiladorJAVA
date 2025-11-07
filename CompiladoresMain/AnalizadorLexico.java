package CompiladoresMain;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import AccionesSemanticas.*;
import conjuntoSimbolos.*;
public class AnalizadorLexico {
    private static final int ESTADO_ERROR = -1;
    private static final int ESTADO_FINAL = -2;
    private int[][] matrizTransicion;
    private AccionSemantica[][] matrizAcciones;
    private ArrayList<ConjuntoSimbolos> mapeoColumnas = new ArrayList<>();
    public static HashMap<String, AtributosTokens> tablaSimbolos = new HashMap<>();
    public static ArrayList<String> errores_y_warnings = new ArrayList<>();
    private BufferedReader lector;
    private String lineaActual;
    public static int numero_linea = 1;
    public static int indice_caracter_leer = 0;
    public static int cant_constantes = 0;
    public static int estado_actual = 0;
    public ParserVal yylval;
    public AnalizadorLexico(String rutaArchivo) throws IOException {
        this.lector = new BufferedReader(new FileReader(rutaArchivo));
        this.lineaActual = lector.readLine();
        if (this.lineaActual != null) {
            this.lineaActual += "\n"; 
        }
        iniciarColumnas();
        cargarMatrices();
    }
    private void iniciarColumnas(){
    mapeoColumnas.add(new ConjuntoD());                
    mapeoColumnas.add(new ConjuntoL());                
    mapeoColumnas.add(new ConjuntoMayus());            
    mapeoColumnas.add(new ConjuntoMinus());            
    mapeoColumnas.add(new ConjuntoDigito());           
    mapeoColumnas.add(new ConjuntoPorcentaje());       
    mapeoColumnas.add(new ConjuntoSignos());           
    mapeoColumnas.add(new ConjuntoPunto());            
    mapeoColumnas.add(new ConjuntoMas());              
    mapeoColumnas.add(new ConjuntoMayor());            
    mapeoColumnas.add(new ConjuntoMenor());            
    mapeoColumnas.add(new ConjuntoIgual());            
    mapeoColumnas.add(new ConjuntoExclamasion());      
    mapeoColumnas.add(new ConjuntoDosPuntos());        
    mapeoColumnas.add(new ConjuntoMenos());            
    mapeoColumnas.add(new ConjuntoComillaDoble());     
    mapeoColumnas.add(new ConjuntoNumeral());          
    mapeoColumnas.add(new ConjuntoSaltoLinea());       
    mapeoColumnas.add(new ConjuntoBlanco());           
    mapeoColumnas.add(new ConjuntoTabulado());         
}
    private void cargarMatrices(){
        matrizTransicion = new int[][]{
            {1, 1, 1, 6, 7, ESTADO_ERROR, ESTADO_FINAL, 8, ESTADO_FINAL, 3, 3, 2, ESTADO_ERROR, 4, 5, 13, 14, 0, 0, 0, ESTADO_ERROR},
             {1, 1, 1, ESTADO_FINAL, 1, 1, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_FINAL, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
            {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, 6, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {ESTADO_ERROR, ESTADO_FINAL, ESTADO_ERROR, ESTADO_ERROR, 7, ESTADO_ERROR, ESTADO_ERROR, 9, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
            {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, 9, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {10, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, 9, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 11, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 11, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
            {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 12, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
            {ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, 12, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL, ESTADO_FINAL},
            {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, ESTADO_FINAL, 13, ESTADO_ERROR, 13, 13, 13},
            {ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, 15, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR, ESTADO_ERROR},
            {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 16, 15, 15, 15, 15},
            {15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0, 15, 15, 15, 15}
        };
        matrizAcciones = new AccionSemantica[][]{
          {new AS1(), new AS1(), new AS1(), new AS1(), new AS1(), new ASE(), new AS2(), new AS1(), new AS2(), new AS1(), new AS1(), new AS1(), new ASE(), new AS1(), new AS1(), null, null, new AS11(), null, null, new ASE()},
         {new AS3(), new AS3(), new AS3(), new AS4(), new AS3(), new AS3(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4(), new AS4()},
         {new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS6(), new AS6(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5()},
         {new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS6(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5()},
         {new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new AS6(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
         {new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS6(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5()},
         {new AS7(), new AS7(), new AS7(), new AS3(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7(), new AS7()},
         {new ASE(), new AS8(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
         {new AS5(), new AS5(), new AS5(), new AS5(), new AS3(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5(), new AS5()},
         {new AS3(), new AS9(), new AS9(), new AS9(), new AS3(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9()},
         {new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
         {new ASE(), new ASE(), new ASE(), new ASE(), new AS3(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE()},
         {new AS9(), new AS9(), new AS9(), new AS9(), new AS3(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9(), new AS9()},
         {new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS3(), new AS10(), new AS3(), new ASE(), new AS3(), new AS3(), new AS3()},
         {new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), new ASE(), null, new ASE(), new ASE(), new ASE(), new ASE()},
         {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new AS11(), null, null, null},
         {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
    };
    } 
    public static void printReporteLexico() {
        System.out.println("\n--- 3. Reporte de Errores y Warnings (Lexicos) ---");
        ArrayList<String> warnings = new ArrayList<>();
        ArrayList<String> errores = new ArrayList<>();
        for (String msg : errores_y_warnings) {
            if (msg.contains("WARNING")) {
                warnings.add(msg);
            } else if (msg.contains("ERROR")) {
                errores.add(msg);
            }
        }
        if (warnings.isEmpty() && errores.isEmpty()) {
            System.out.println("Sin errores lexicos ni warnings.");
            return;
        }
        for (String w : warnings) {
            System.out.println(w);
        }
        for (String e : errores) {
            System.out.println(e);
        }
    }
    public void printTablaSimbolos(){
        System.out.println("\n--- 4. Contenido de la Tabla de Símbolos ---");
        for(Map.Entry<String, AtributosTokens> entry: tablaSimbolos.entrySet()){
            String lexema = entry.getKey();
            AtributosTokens atributo = entry.getValue();
            if (atributo.getUso() != null || atributo.getIdToken() >= 257) {
                 System.out.println("\"" + lexema + "\", " + atributo);
            }
        }
    }
// ... (dentro de la clase AnalizadorLexico) ...

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

                int estado_previo = estado_actual;
                estado_actual = proximo_estado;

                //ejecuto accion
                if (accionSemantica != null) {
                    if (accionSemantica instanceof ASE) {
                        AnalizadorLexico.estado_actual = estado_previo;
                    }
                    accionSemantica.ejecutar(newToken, caracter);
                }

                if (estado_actual == ESTADO_FINAL){
                    
                    int tokenId = newToken.getId();
                    
                    // --- INICIO DE LA MODIFICACIÓN (Cumplir consigna yylval) ---
                    
                    // Por defecto, yylval está vacío.
                    this.yylval = new ParserVal(); 

                    switch (tokenId) {
                        case Parser.IDENTIFICADOR:
                        case Parser.CADENA:
                        case Parser.CTE_LONG:
                        case Parser.CTE_DFLOAT:
                            
                            // Para todos estos, la "referencia" que pasamos al parser
                            // es su lexema (la clave de la TS o el nombre simple del ID).
                            this.yylval.sval = newToken.getLexema();
                            break;

                        // Para todos los demás (IF, ELSE, '+', ':=', etc.)
                        // no hacemos nada. yylval queda vacío, el parser
                        // solo necesita el tokenId, que se retorna abajo.
                    }
                    // --- FIN DE LA MODIFICACIÓN ---

                    return tokenId; // Retornamos el ID del token
                
                } else if (estado_actual == ESTADO_ERROR){
                    if (newToken.getId() != -1) { 
                        this.yylval = new ParserVal();
                        return newToken.getId();
                    }
                    estado_actual = 0;
                    newToken = new Token();
                }
            } //fin while(true)
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
// ... (resto de AnalizadorLexico.java) ...
public static void main(String[] args) {
    if (args.length == 0) {
        System.out.println("Error: Debes pasar la ruta del archivo a compilar como argumento.");
        return;
    }
    try {
        AnalizadorLexico lex = new AnalizadorLexico(args[0]);
        int tokenId;
        System.out.println("---Lista de Tokens---");
        while ((tokenId = lex.yylex()) != -1) { 
            ParserVal t = lex.yylval; 
            if (t != null) { 
                System.out.println(t.toString());
            }
        }
        lex.printTablaSimbolos(); 
    } catch (IOException e) {
        System.err.println("Error al leer el archivo: " + e.getMessage());
    }
}
}