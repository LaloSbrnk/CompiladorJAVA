package CompiladoresMain;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import ArbolSintactico.Nodo;

public class ProbadorSimple {

    public static void main(String[] args) {

        // --- BATERÍA DE CASOS DE PRUEBA PARA EL TP2 ---
        Map<String, String> casosDePrueba = new LinkedHashMap<>();
/* 
        // --- CASOS VÁLIDOS ---
         casosDePrueba.put(
            "1. Prueba de Inferencia 'var'",
                "MIPROGRAMA {\n" +      // <-- Nombre del programa y llave de apertura
                "    var X := 1L;\n" +   // <-- Tu sentencia, terminada con ;
                "}"                      // <-- Llave de cierre
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
        */
        
        
        
        
        
        
/* 

casosDePrueba.put(
            "1. Prueba Integral (Todos los Temas V4 - Corregida)",
                "TESTCOMPLETO {\n" +
                "\n" +
                "    ## --- TEMAS 3, 6, 10: Declaracion y Inferencia Opcional --- ##\n" +
                "    long VARGLOBAL%L;\n" +
                "    dfloat VARGLOBAL%D;\n" +
                "    var VARGLOBAL%INF := -100L;\n" +
                "\n" +
                "    ## --- TEMAS 20, 24: Funciones con Retorno Múltiple y Pasaje CV --- ##\n" +
                "    long, dfloat FUNCION%TEST(cv sl long P%SL, cv le dfloat P%LE) {\n" +
                "        \n" +
                "        ## --- TEMA 30: Conversion Implicita (dfloat + long) --- ##\n" +
                "        P%LE := P%LE + P%SL;\n" +
                "\n" +
                "        ## --- TEMA 23: Prefijado Opcional (Raiz) --- ##\n" +
                "        TESTCOMPLETO.VARGLOBAL%D := P%LE; ## Asigna al global ##\n" +
                "\n" +
                "        return (VARGLOBAL%INF, P%LE); ## Retorno Múltiple ##\n" +
                "    }\n" +
                "\n" +
                "    ## --- TEMA 13: Bucle Do-Until --- ##\n" +
                "    do {\n" +
                "        VARGLOBAL%INF := VARGLOBAL%INF + 1L;\n" +
                "    } until (VARGLOBAL%INF >= 0L);\n" +
                "\n" +
                "    ## --- TEMA 7: Cadenas y Print --- ##\n" +
                "    print(\"Bucle 'do-until' finalizado.\");\n" +
                "\n" +
                "    ## --- TEMA 16 y 20: Asignacion Múltiple con Invocación --- ##\n" +
                "    VARGLOBAL%L, VARGLOBAL%D = FUNCION%TEST(50L -> P%SL, 1.1D+1 -> P%LE);\n" +
                "\n" +
                "    ## --- TEMA 27: Lambda en Línea --- ##\n" +
                "    (long ARG%L) {\n" +
                "        print(ARG%L);\n" +
                "    } (VARGLOBAL%L);\n" +
                "\n" +
                "    ## --- TEMA 33: Comentario Multilínea (CORREGIDO) --- ##\n" +
                "    \n" +
                "    ##   INICIO DE SECCION DE ERRORES DETECTABLES\n" +
                "       Esta seccion ahora esta DENTRO de un solo comentario.\n" +
                "    ##\n" +
                "\n" +
                "    ## --- ERRORES SEMANTICOS (Deben ser reportados) --- ##\n" +
                "    \n" +
                "    ## Tema 10: Redeclaracion\n ##"  +
                "    long VARGLOBAL%L;\n" + // ERROR: Redeclaracion de 'VARGLOBAL%L'
                "\n" +
                "    ## Tema 30: Conversion Implicita (Pérdida de precisión)\n ##" +
                "    VARGLOBAL%L = VARGLOBAL%D;\n" + // ERROR: Asignacion incompatible (long = dfloat)
                "\n" +
                "    ## Tema 16: Discrepancia de cantidad en asignacion multiple\n ##" +
                "    long A, B;\n" +
                "    A, B = VARGLOBAL%L;\n" + // ERROR: 2 variables, 1 expresion
                "\n" +
                "    ## Tema 20 y 30: Discrepancia de tipos en retorno multiple\n##" +
                "    long R1, R2;\n" +
                "    R1, R2 = FUNCION%TEST(1L -> P%SL, 1.0 -> P%LE);\n" + // ERROR: R2 (long) no puede recibir el 2do retorno (dfloat)
                "\n" +
                "    ## --- CORRECCION DE SINTAXIS AQUI --- ##\n" +
                "    long, long FUNCION%SL(cv sl long P) {\n" + // CAMBIADO: 'int' por 'long'
                "        P := 1L;\n" + // AHORA SÍ veremos este ERROR SEMÁNTICO (Tema 24)
                "        return(P); \n" + // AHORA SÍ veremos este ERROR SEMÁNTICO (Tema 20: 1 vs 2 retornos)
                "    }\n" +
                "\n" +
                "    ## Variable no declarada\n##" +
                "    A := VARIABLE%NO%DECLARADA;\n" + // ERROR: 'VARIABLE%NO%DECLARADA' no existe
                "\n" +
                "    ## --- ERRORES LEXICOS (Deben ser reportados) --- ##\n" +
                "\n" +
                "    long ESTOESUNIDMASLARGODE20CARACTERES;\n" + // WARNING: ID truncado a 'ESTOESUNIDMASLARGODE'
                "    long val = 1L;\n" + // ERROR: 'val' (ID minúscula) no permitido
                "    dfloat D%OOR := 9.9D+999;\n" + // ERROR: Constante dfloat fuera de rango
                "    print(\"Cadena con \n salto de linea\");\n" + // ERROR: Salto de linea en cadena (Tema 7)
                "\n" +
                "    ## --- ERRORES SINTACTICOS (Recuperables) --- ##\n" +
                "\n" +
                "    long E%SINT%1;\n" + // <-- CORREGIDO: Añadido ';'
                "    do { A := 1L; } until (A == 1L);\n" + // <-- CORREGIDO: Añadido 'until'
                "\n" +
                "}"
        );     */
        
        






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
                "    VARGLOBAL%L = VARGLOBAL%D;\n" + 
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
                "        return(P, P);\n" + // Corregido para que coincida el N° de retornos
                "    }\n" +
                "\n" +
                "    ## 6. Variable no declarada\n##" +
                "    A := VARIABLE%NO%DECLARADA;\n" +
                "\n" +
                "    ## 7. Tema 20: Cantidad de retornos incorrecta\n##" +
                "    long R3;\n" +
                "    R3 = FUNCION%TEST(1L -> P%SL, 1.0 -> P%LE);\n" + // Asigna 2 retornos a 1 variable
                "\n" +
                "}"
        );



        
        
        
        
        
        /* 
        casosDePrueba.put(
        "6. Prueba de Name Mangling (Shadowing y Prefijo)",
            "MAIN {\n" +
            "    dfloat A;\n" +      // Declara A:MAIN
            "    A := 100.0;\n" +   // Asigna a A:MAIN
            "\n" +
            "    long, long FUNCIONAA(cv sl dfloat X) {\n" + // Declara FUNCIONAA:MAIN y X:MAIN:FUNCIONAA
            "        long A;\n" +     // Declara A:MAIN:FUNCIONAA (shadowing)
            "        A := 1L;\n" +     // Asigna a A:MAIN:FUNCIONAA
            "\n" +
            "        ## Ahora asignamos a la 'A' de afuera usando prefijo ##\n" +
            "        MAIN.A := 2.0;\n" + // Asigna a A:MAIN (Tema 23)
            "\n" +
            "        return (A, 0L);\n" + // Retorna A:MAIN:FUNCIONAA (valor 1)
            "    }\n" +
            "\n" +
            "    long R1, R2;\n" +
            "    R1, R2 = FUNCIONAA(A -> X);\n" + // Pasa A:MAIN (valor 100.0)
            "\n" +
            "    ## Si 'name mangling' funcionó: ##\n" +
            "    ## R1 debe ser 1 (el 'A' de adentro). ##\n" +
            "    ## A (MAIN.A) debe ser 2.0 (modificado por prefijo). ##\n" +
            "    print(\"Fin de prueba\");\n" +
            "}"
    );*/
  /*  
        // --- CASOS CON ERRORES SINTÁCTICOS ---
        
        casosDePrueba.put("ERROR 1: Sentencia sin punto y coma",
            "PROGERROR1 {\n" +
            "    long X\n" + // <-- Falta ;
            "    print(\"esta linea deberia ser analizada\");\n" +
            "}"
        );
 
        casosDePrueba.put("ERROR 2: IF con condición mal formada",
            "PROGERROR2 {\n" +
            "    if X > 0L { print(\"mal\"); } endif;\n" + // <-- Faltan ( ) en la condición
            "}"
        );

        casosDePrueba.put("ERROR 3: Asignación simple con =",
            "PROGERROR3 {\n" +
            "    long X;\n" +
            "    X = 10L; \n" + // <-- Debería ser :=
            "}"
        );
 */
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
                
                // 1. Ejecutar el Parseo (Construye el AST)
                parser.yyparse(); 

                // 2. Obtener el AST (la raiz es el valor semantico final)
                Nodo arbol = (Nodo) parser.yyval.obj; 

                // 3. --- INICIO DEL CHEQUEO SEMANTICO ---
                if (arbol != null && parser.yynerrs == 0) { // Solo si no hubo errores sintacticos
                    System.out.println("\n--- Iniciando Chequeo Semantico ---");

                    // 4. Crear la Tabla de Ambitos
                    TablaDeAmbitos tablaDeAmbitos = new TablaDeAmbitos(AnalizadorLexico.tablaSimbolos);

                    // 5. --- CORRECCIÓN ---
                    // La llamada a tablaDeAmbitos.abrirAmbitoGlobal() SE ELIMINA.
                    
                    // 6. ¡Llamar al 'chequear' de la raiz del arbol!
                    arbol.chequear(tablaDeAmbitos);

                    System.out.println("--- Chequeo Semantico Finalizado ---");

                } else {
                    System.out.println("\n--- Chequeo Semantico Omitido (Errores Sintacticos o AST nulo) ---");
                }
                // --- FIN DEL CHEQUEO SEMANTICO ---

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