package CompiladoresMain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProbadorSimple {
    public static void main(String[] args) {
        // --- BATERÍA DE CASOS DE PRUEBA PARA EL TP2 ---
        Map<String, String> casosDePrueba = new LinkedHashMap<>();

        // --- CASOS VÁLIDOS ---
        casosDePrueba.put(
            "1. Prueba de Inferencia 'var'",
                "MIPROGRAMA {\n" +     
                "    var X := 1L;\n" +   
                "}"              
        );

        casosDePrueba.put("2. Asignaciones (Simple y Múltiple - Tema 16)",
            "PROGASIG {\n" +
            "    long X, Y;\n" +
            "    X := 100L;\n" +
            "    Y = X - 20L;\n" +
            "}"
        );

       casosDePrueba.put("3. Estructuras de Control (IF y DO-UNTIL - Tema 13)",
            "PROGRAMACONTROL {\n" +
            "    long CONTADOR;\n" +
            "    if (CONTADOR > 0L) {\n" +
            "        print(\"Contador positivo\");\n" +
            "    } else {\n" +
            "        print(\"Contador no positivo\");\n" +
            "    } endif;\n" +
            "    do {\n" +
            "        CONTADOR := CONTADOR - 1L;\n" +
            "    } until (CONTADOR == 0L);\n" +
            "}"
        );
     
        casosDePrueba.put("4. Funciones (Temas 20, 23, 24)",
            "PROGFUNCIONES {\n" +
            "    long, dfloat FUNCION(cv sl long P1, cv le dfloat P2) {\n" +
            "        P2 := P1 * 1.5;\n" +
            "        return (P1, P2);\n" +
            "    }\n" +
            "    long X;\n" +
            "    dfloat Y;\n" +
            "    MODULO.Z := 1.0;\n" + // Tema 23: Prefijado
            "    X, Y = FUNCION(10L -> P1, 3.14 -> P2);\n" +
            "}"
        );
 
 
        casosDePrueba.put("5. Lambda en Línea (Tema 27)",
            "PROGLAMDA {\n" +
            "    (long Z) { print(\"lambda ejecutada\"); } (123L);\n" +
            "}"
        );


        // --- CASOS CON ERRORES SINTÁCTICOS ---
        
        casosDePrueba.put("ERROR 1: Sentencia sin punto y coma",
            "PROGERROR1 {\n" +
            "    long X\n" + // <-- Falta ;
            "    print(\"esta linea deberia ser analizada\");\n" +
            "}"
        );

        casosDePrueba.put("ERROR 2: IF con condición mal formada",
            "PROGERROR2 {\n" +
            "    if X > 0L { print(\"mal\"); } endif;\n" + //Faltan ( ) en la condición
            "}"
        );

        casosDePrueba.put("ERROR 3: Asignación simple con =",
            "PROGERROR3 {\n" +
            "    long X;\n" +
            "    X = 10L; \n" + //  Debería ser :=
            "}"
        );
 
        // --- EL MOTOR DE PRUEBAS ---
        for (Map.Entry<String, String> testCase : casosDePrueba.entrySet()) {
            System.out.println("\n=======================================================");
            System.out.println("--- Ejecutando Test: " + testCase.getKey() + " ---");
            System.out.println("=======================================================");

            Path tempFile = null;
            try {
                tempFile = Files.createTempFile("test_", ".txt");
                Files.writeString(tempFile, testCase.getValue());
                
                AnalizadorLexico lex = new AnalizadorLexico(tempFile.toString());
                Parser parser = new Parser(lex);
                parser.yyparse(); // Se llama al método del parser

            } catch (IOException e) {
                System.err.println("Error de I/O durante la prueba: " + e.getMessage());
            } finally {
                // Asegurarse de borrar el archivo temporal al final
                if (tempFile != null) {
                    try {
                        Files.deleteIfExists(tempFile);
                    } catch (IOException e) {
                        // CORRECCIÓN: Añade una acción aquí, como imprimir el error.
                        System.err.println("Advertencia: No se pudo borrar el archivo temporal: " + tempFile.toString());
                    }
                }
            }
        }
    }
}