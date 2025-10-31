package CompiladoresMain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList; // Asegurarse que este import esté

import ArbolSintactico.Nodo;

public class ProbadorSimple {

    public static void main(String[] args) {
        
        // Usamos el caso de prueba V6 que sabemos que funciona
        Map<String, String> casosDePrueba = new LinkedHashMap<>();
        casosDePrueba.put(
            "2. Prueba de Errores Semanticos (Sintaxis OK)",
                "TESTSEMANTICO {\n" +
                "\n" +
                "    long VARGLOBAL%L;\n" +
                "    dfloat VARGLOBAL%D;\n" +
                "    var VARGLOBAL%INF := -100L;\n" +
                "\n" +
                "    long, dfloat FUNCION%TEST(cv sl long P%SL, cv le dfloat P%LE) {\n" +
                "        P%LE := P%LE + P%SL;\n" +
                "        TESTSEMANTICO.VARGLOBAL%D := P%LE;\n" +
                "        return (VARGLOBAL%INF, P%LE);\n" +
                "    }\n" +
                "\n" +
                "    ## --- ERRORES SEMANTICOS A DETECTAR --- ##\n" +
                "\n" +
                "    ## 1. Tema 10: Redeclaracion\n##" +
                "    long VARGLOBAL%L;\n" + 
                "\n" +
                "    ## 2. Tema 30: Asignacion incompatible (perdida)\n##" +
                "    VARGLOBAL%L = VARGLOBAL%D;\n" + // <--- ¡OJO! Este fallará en sintaxis
                "\n" +
                "    ## 3. Tema 16: Discrepancia en asignacion multiple\n##" +
                "    long A, B;\n" +
                "    A, B = VARGLOBAL%L;\n" + 
                "\n" +
                "    ## 4. Tema 20/30: Discrepancia en retorno multiple\n##" +
                "    long R1, R2;\n" +
                "    R1, R2 = FUNCION%TEST(1L -> P%SL, 1.0 -> P%LE);\n" + 
                "\n" +
                "    ## 5. Tema 24: Asignacion a 'cv sl'\n##" +
                "    long, long FUNCION%SL(cv sl long P) {\n" +
                "        P := 1L;\n" + 
                "        return(P, P);\n" + 
                "    }\n" +
                "\n" +
                "    ## 6. Variable no declarada\n##" +
                "    A := VARIABLE%NO%DECLARADA;\n" +
                "\n" +
                "    ## 7. Tema 20: Cantidad de retornos incorrecta\n##" +
                "    long R3;\n" +
                "    R3 = FUNCION%TEST(1L -> P%SL, 1.0 -> P%LE);\n" +
                "\n" +
                "}"
        );


        // --- EL MOTOR DE PRUEBAS ---
        for (Map.Entry<String, String> testCase : casosDePrueba.entrySet()) {
            System.out.println("=======================================================");
            System.out.println("--- Salida del Compilador para: " + testCase.getKey() + " ---");
            System.out.println("=======================================================");

            Path tempFile = null;
            AnalizadorLexico lex = null;
            Nodo arbol = null;
            int erroresSintacticos = 0;
            // Capturamos los errores semánticos para el reporte unificado
            ArrayList<String> erroresSemanticos = new ArrayList<>(); 

            try {
                tempFile = Files.createTempFile("test_", ".txt");
                Files.writeString(tempFile, testCase.getValue());
                
                lex = new AnalizadorLexico(tempFile.toString());
                Parser parser = new Parser(lex);
                
                parser.yyparse(); 
                arbol = (Nodo) parser.yyval.obj; 
                erroresSintacticos = parser.yynerrs;

                if (arbol != null && erroresSintacticos == 0) {
                    TablaDeAmbitos tablaDeAmbitos = new TablaDeAmbitos(AnalizadorLexico.tablaSimbolos);
                    
                    // --- RE-DIRECCION DE ERRORES SEMANTICOS ---
                    // Capturamos System.err para el reporte unificado
                    java.io.ByteArrayOutputStream errContent = new java.io.ByteArrayOutputStream();
                    System.setErr(new java.io.PrintStream(errContent));
                    
                    arbol.chequear(tablaDeAmbitos); // Ejecuta el chequeo
                    
                    System.setErr(System.out); // Restaura System.err
                    String[] errores = errContent.toString().split("\n");
                    for (String e : errores) {
                        if (!e.isEmpty()) erroresSemanticos.add(e);
                    }
                    // --- FIN RE-DIRECCION ---

                }


            } catch (Exception e) {
                System.err.println("Error critico durante la compilacion: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // --- SALIDA FINAL REQUERIDA POR TP3 ---

                // Requisito 1: Código Intermedio (Árbol Sintáctico)
                System.out.println("\n--- 1. Código Intermedio (Árbol Sintáctico) ---");
                if (arbol != null) {
                    arbol.imprimir(""); // Llamada al nuevo método
                } else {
                    System.out.println("Arbol no generado (debido a errores sintacticos fatales).");
                }

                // Requisito 3: Errores (Consolidados)
                System.out.println("\n--- 3. Reporte de Errores y Warnings ---");
                if (lex != null) {
                    // Imprime errores léxicos y warnings
                    for (String msg : AnalizadorLexico.errores_y_warnings) {
                        System.out.println(msg);
                    }
                }
                if (erroresSintacticos > 0) {
                    System.out.println("Se reportaron " + erroresSintacticos + " errores SINTACTICOS (ver traza anterior si la hubo).");
                }
                // Imprime errores semánticos
                for (String errSem : erroresSemanticos) {
                    System.out.println(errSem);
                }
                if (erroresSintacticos == 0 && erroresSemanticos.isEmpty() && AnalizadorLexico.errores_y_warnings.isEmpty()) {
                    System.out.println("Compilacion finalizada sin errores.");
                }


                // Requisito 4: Tabla de Símbolos
                if (lex != null) {
                    lex.printTablaSimbolos();
                }

                // Borrado de archivo temporal
                if (tempFile != null) {
                    try {
                        Files.deleteIfExists(tempFile);
                    } catch (IOException e) { /* ignorar */ }
                }
            }
            System.out.println("=======================================================");
        }
    }
}