package CompiladoresMain;

import ArbolSintactico.Nodo;
import ArbolSintactico.NodoFuncionDef;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map; 
import java.util.HashMap; 

/**
 * Clase encargada de gestionar la generación del código Assembler
 * de 32 bits (MASM32).
 * Utiliza la técnica de "Variables Auxiliares".
 */
public class GeneradorAssembler {

    private StringBuilder header; // Includes y encabezado
    private StringBuilder dataSection; // .DATA
    private StringBuilder codeSection; // .CODE
    private StringBuilder footer; // Rutinas de error y final del programa

    private TablaDeAmbitos tablaDeAmbitos;
    private int auxCounter = 0;
    private int labelCounter = 0;
    private int stringCounter = 0;
    
    // --- INICIO DE LA CORRECCIÓN 1 ---
    // Nombres de variables auxiliares para operaciones
    // (No deben empezar con '@' para evitar conflicto con @data)
    public static final String AUX_LONG = "_aux_long";
    public static final String AUX_DFLOAT = "_aux_dfloat";
    // --- FIN DE LA CORRECCIÓN 1 ---

    public GeneradorAssembler(TablaDeAmbitos tda) {
        this.tablaDeAmbitos = tda;
        
        this.header = new StringBuilder();
        this.dataSection = new StringBuilder();
        this.codeSection = new StringBuilder();
        this.footer = new StringBuilder();

        this.iniciarEnsamblado();
    }

    /**
     * Prepara las secciones .DATA y .CODE, e incluye las librerías.
     */
    private void iniciarEnsamblado() {
        // --- Encabezado ---
        // (masm32rt.inc provee .386 y .model)
        header.append("option casemap :none\n");
        header.append("include \\masm32\\include\\masm32rt.inc\n");
        header.append("includelib \\masm32\\lib\\masm32.lib\n");
        header.append("printf PROTO C :VARARG\n");
        header.append("\n");


        // --- Sección .DATA ---
        dataSection.append(".DATA\n");
        // Datos estándar (formatos, strings de error)
        dataSection.append("_new_line_ DB 13, 10, 0\n"); // CRLF
        dataSection.append("_format_long DB \"%d\", 13, 10, 0\n"); // Formato para print de long
        dataSection.append("_format_dfloat DB \"%.20Lf\", 13, 10, 0\n"); // Formato para print de dfloat
        dataSection.append("_format_string DB \"%s\", 13, 10, 0\n");
        
        // Mensajes de error en tiempo de ejecución
        dataSection.append("_MSG_ERROR_DIV_CERO DB \"ERROR EN TIEMPO DE EJECUCION: Division por cero.\", 0\n");
        dataSection.append("_MSG_ERROR_DIV_CERO_FLOAT DB \"ERROR EN TIEMPO DE EJECUCION: Division por cero (flotante).\", 0\n");
        dataSection.append("_MSG_ERROR_OVERFLOW_PROD DB \"ERROR EN TIEMPO DE EJECUCION: Overflow en producto de enteros.\", 0\n");
        dataSection.append("_MSG_ERROR_RECURSION DB \"ERROR EN TIEMPO DE EJECUCION: Recursion no permitida.\", 0\n");
        
        // Variables auxiliares para operaciones
        dataSection.append(AUX_LONG + " DD ?\n"); // Ahora usará _aux_long
        dataSection.append(AUX_DFLOAT + " DQ ?\n"); // Ahora usará _aux_dfloat
    }

    /**
     * Recorre la Tabla de Símbolos completa y declara todas las variables,
     * constantes y strings en la sección .DATA.
     */
    public void declararTablaDeSimbolos(HashMap<String, AtributosTokens> tsGlobal) {
        dataSection.append("\n; --- Variables y Constantes del Programa ---\n");
        
        for (Map.Entry<String, AtributosTokens> entry : tsGlobal.entrySet()) {
            String lexema = entry.getKey();
            AtributosTokens attrs = entry.getValue();
            String nombreAsm = getNombreAsm(lexema); 

            if (attrs.getUso() == null) { 
                // Es una constante o cadena literal
                if (attrs.getValor() == null) {
                    continue; 
                }
                
                if (attrs.getIdToken() == TiposToken.CTE_LONG) {
                    // NO declaramos constantes LONG en .DATA
                    continue; 
                } else if (attrs.getIdToken() == TiposToken.CTE_DFLOAT) {
                    // SÍ declaramos DFLOAT
                    dataSection.append(nombreAsm + " DQ " + attrs.getValor().toString() + "\n");
                } else if (attrs.getIdToken() == TiposToken.CADENA) {
                    // SÍ declaramos Cadenas
                    String cadena = attrs.getValor().toString(); 
                    dataSection.append(nombreAsm + " DB \"" + cadena + "\", 0\n");
                }
            } else if ("variable".equals(attrs.getUso()) || "parametro".equals(attrs.getUso()) || "parametro_lambda".equals(attrs.getUso())) {
                // Es una variable (global o local) o un parámetro
                if (attrs.getMangledName() == null) continue; 
                nombreAsm = getNombreAsm(attrs.getMangledName()); // Usar nombre mangled
                if ("long".equals(attrs.getTipoDato())) {
                    dataSection.append(nombreAsm + " DD ?\n"); // 32 bits
                } else if ("dfloat".equals(attrs.getTipoDato())) {
                    dataSection.append(nombreAsm + " DQ ?\n"); // 64 bits
                }
            } else if ("funcion".equals(attrs.getUso())) {
                // (h) Flag para chequeo de recursión
                dataSection.append("_IN_FUNC_" + getNombreAsm(attrs.getMangledName()) + " DB 0\n");
                // Declarar variables de retorno (Tema 20)
                int i = 0;
                if (attrs.getTiposRetorno() != null) {
                    for (String tipoRetorno : attrs.getTiposRetorno()) {
                        String nombreRet = "_RET_" + (i++) + "_" + getNombreAsm(attrs.getMangledName());
                        if ("long".equals(tipoRetorno)) {
                            dataSection.append(nombreRet + " DD ?\n");
                        } else if ("dfloat".equals(tipoRetorno)) {
                            dataSection.append(nombreRet + " DQ ?\n");
                        }
                    }
                }
            }
        }
        dataSection.append("; --- Fin Variables y Constantes ---\n\n");
    }

    /**
     * Inicia la sección de código principal.
     */
    public void iniciarCodigo() {
        codeSection.append("\n.CODE\n"); //
        codeSection.append("START:\n"); //
        
        // --- INICIO DE LA CORRECCIÓN 2 ---
        // (Estas líneas son para 16-bit y causan el conflicto A2004)
        // codeSection.append("mov eax, @data\n");
        // codeSection.append("mov ds, eax\n");
        // --- FIN DE LA CORRECCIÓN 2 ---
        
        // Inicializar el coprocesador 80x87
        codeSection.append("FINIT\n");
    }

    /**
     * Agrega las rutinas de error y finaliza el programa.
     */
    public void finalizarPrograma() {
        // Salida normal
        codeSection.append("\n; --- Fin del programa principal ---\n");
        codeSection.append("invoke ExitProcess, 0\n\n");

        // --- Rutinas de Error (Footer) ---
        footer.append("\n; --- Rutinas de Error en Tiempo de Ejecucion ---\n");

        // (a) División por cero (Entero)
        footer.append("_ERROR_DIV_CERO:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_DIV_CERO\n"); 
        footer.append("invoke ExitProcess, 1\n");

        // (a) División por cero (Flotante)
        footer.append("_ERROR_DIV_CERO_FLOAT:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_DIV_CERO_FLOAT\n"); 
        footer.append("invoke ExitProcess, 1\n");

        // (d) Overflow en producto (Entero)
        footer.append("_ERROR_OVERFLOW_PROD:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_OVERFLOW_PROD\n"); 
        footer.append("invoke ExitProcess, 1\n");

        // (h) Recursión
        footer.append("_ERROR_RECURSION:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_RECURSION\n"); 
        footer.append("invoke ExitProcess, 1\n");

        // --- Fin del archivo ---
        footer.append("\nEND START\n"); //
    }

    // --- Helpers para Nodos ---

    public void agregarCodigo(String instruccion) {
        codeSection.append(instruccion + "\n");
    }

    /**
     * Devuelve un nombre de etiqueta único (ej. "_label1").
     */
    public String getNuevoLabel() {
        return "_label" + (labelCounter++);
    }

    /**
     * Convierte un nombre mangled de la TS (ej. "VAR%X:PROG")
     * en un nombre válido para Assembler (ej. "_VAR%X_PROG").
     */
    public String getNombreAsm(String nombreMangled) {
        if (nombreMangled == null) return "NULL_MANGLED";
        
        // Si ya es un auxiliar (empieza con _), no agregar otro '_'.
        if (nombreMangled.startsWith("_aux")) {
            return nombreMangled;
        }
        
        String cleanName = nombreMangled;
        
        // 1. Quitar comillas de las cadenas
        if (cleanName.startsWith("\"") && cleanName.endsWith("\"")) {
             cleanName = cleanName.substring(1, cleanName.length() - 1);
        }
        
        // 2. Reemplazar todos los caracteres no válidos (espacio, %, :, .) por _
        cleanName = cleanName.replaceAll("[^a-zA-Z0-9_]", "_");
        
        // 3. Asegurarse de que comience con '_'
        if (!cleanName.startsWith("_")) {
            cleanName = "_" + cleanName;
        }
        
        return cleanName;
    }

    /**
     * Devuelve el nombre de la variable de retorno para una función.
     */
    public String getNombreRetorno(String nombreFuncionMangled, int index) {
         return "_RET_" + index + "_" + getNombreAsm(nombreFuncionMangled);
    }
    
    /**
     * Devuelve el flag de recursión para una función.
     */
    public String getFlagRecursion(String nombreFuncionMangled) {
        return "_IN_FUNC_" + getNombreAsm(nombreFuncionMangled);
    }

    /**
     * Genera el código para una conversión implícita (Tema 30).
     * Carga el operando (long) en el coprocesador.
     */
    public void convertirLongADFloat(String operandoLong) {
        agregarCodigo("FILD " + operandoLong); // Cargar Entero
    }

    /**
     * Escribe el código ensamblado completo en un archivo.
     */
    public void escribirArchivo(String rutaArchivo) {
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            fw.write(header.toString());
            fw.write(dataSection.toString());
            fw.write(codeSection.toString());
            fw.write(footer.toString());
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo Assembler: " + e.getMessage());
        }
    }
}