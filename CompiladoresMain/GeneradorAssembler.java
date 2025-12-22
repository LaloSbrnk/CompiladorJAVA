package CompiladoresMain;

import ArbolSintactico.Nodo;
import ArbolSintactico.NodoFuncionDef;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map; 
import java.util.HashMap; 


public class GeneradorAssembler {

    private StringBuilder header; 
    private StringBuilder dataSection; 
    private StringBuilder codeSection; 
    private StringBuilder footer; 

    private TablaDeAmbitos tablaDeAmbitos;
    private int auxCounter = 0;
    private int labelCounter = 0;
    private int stringCounter = 0;
    
    
    public static final String AUX_LONG = "_aux_long";
    public static final String AUX_DFLOAT = "_aux_dfloat";

    public GeneradorAssembler(TablaDeAmbitos tda) {
        this.tablaDeAmbitos = tda;
        
        this.header = new StringBuilder();
        this.dataSection = new StringBuilder();
        this.codeSection = new StringBuilder();
        this.footer = new StringBuilder();

        this.iniciarEnsamblado();
    }

    
    private void iniciarEnsamblado() {
        
        header.append("option casemap :none\n");
        header.append("include \\masm32\\include\\masm32rt.inc\n");
        header.append("includelib \\masm32\\lib\\masm32.lib\n");
        header.append("printf PROTO C :VARARG\n");
        header.append("\n");


        
        dataSection.append(".DATA\n");
        
        dataSection.append("_new_line_ DB 13, 10, 0\n"); 
        dataSection.append("_format_long DB \"%d\", 13, 10, 0\n"); 
        dataSection.append("_format_dfloat DB \"%.20Lf\", 13, 10, 0\n"); 
        dataSection.append("_format_string DB \"%s\", 13, 10, 0\n");
        
        
        dataSection.append("_MSG_ERROR_DIV_CERO DB \"ERROR EN TIEMPO DE EJECUCION: Division por cero.\", 0\n");
        dataSection.append("_MSG_ERROR_DIV_CERO_FLOAT DB \"ERROR EN TIEMPO DE EJECUCION: Division por cero (flotante).\", 0\n");
        dataSection.append("_MSG_ERROR_OVERFLOW_PROD DB \"ERROR EN TIEMPO DE EJECUCION: Overflow en producto de enteros.\", 0\n");
        dataSection.append("_MSG_ERROR_RECURSION DB \"ERROR EN TIEMPO DE EJECUCION: Recursion no permitida.\", 0\n");
        
        
        dataSection.append(AUX_LONG + " DD ?\n"); 
        dataSection.append(AUX_DFLOAT + " DQ ?\n"); 
    }

    
    public void declararTablaDeSimbolos(HashMap<String, AtributosTokens> tsGlobal) {
        dataSection.append("\n; --- Variables y Constantes del Programa ---\n");
        
        for (Map.Entry<String, AtributosTokens> entry : tsGlobal.entrySet()) {
            String lexema = entry.getKey();
            AtributosTokens attrs = entry.getValue();
            String nombreAsm = getNombreAsm(lexema); 

            if (attrs.getUso() == null) { 
                
                if (attrs.getValor() == null) {
                    continue; 
                }
                
                if (attrs.getIdToken() == TiposToken.CTE_LONG) {
                    
                    continue; 
                } else if (attrs.getIdToken() == TiposToken.CTE_DFLOAT) {
                    
                    dataSection.append(nombreAsm + " DQ " + attrs.getValor().toString() + "\n");
                } else if (attrs.getIdToken() == TiposToken.CADENA) {
                    
                    String cadena = attrs.getValor().toString(); 
                    dataSection.append(nombreAsm + " DB \"" + cadena + "\", 0\n");
                }
            } else if ("variable".equals(attrs.getUso()) || "parametro".equals(attrs.getUso()) || "parametro_lambda".equals(attrs.getUso())) {
                
                if (attrs.getMangledName() == null) continue; 
                nombreAsm = getNombreAsm(attrs.getMangledName()); 
                if ("long".equals(attrs.getTipoDato())) {
                    dataSection.append(nombreAsm + " DD ?\n"); 
                } else if ("dfloat".equals(attrs.getTipoDato())) {
                    dataSection.append(nombreAsm + " DQ ?\n"); 
                }
            } else if ("funcion".equals(attrs.getUso())) {
                
                dataSection.append("_IN_FUNC_" + getNombreAsm(attrs.getMangledName()) + " DB 0\n");
                
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

    
    public void iniciarCodigo() {
        
        
        codeSection.append("\n.CODE\n");
        
        
    }

    
    public void iniciarPuntoEntrada() {
        codeSection.append("START:\n");
        
        codeSection.append("FINIT\n");
    }


    
    public void finalizarPrograma() {
        
        codeSection.append("\n; --- Fin del programa principal ---\n");
        codeSection.append("invoke ExitProcess, 0\n\n");

        
        footer.append("\n; --- Rutinas de Error en Tiempo de Ejecucion ---\n");

        
        footer.append("_ERROR_DIV_CERO:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_DIV_CERO\n"); 
        footer.append("invoke ExitProcess, 1\n");

        
        footer.append("_ERROR_DIV_CERO_FLOAT:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_DIV_CERO_FLOAT\n"); 
        footer.append("invoke ExitProcess, 1\n");

        
        footer.append("_ERROR_OVERFLOW_PROD:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_OVERFLOW_PROD\n"); 
        footer.append("invoke ExitProcess, 1\n");

        
        footer.append("_ERROR_RECURSION:\n");
        footer.append("invoke printf, ADDR _MSG_ERROR_RECURSION\n"); 
        footer.append("invoke ExitProcess, 1\n");

        
        footer.append("\nEND START\n"); 
    }

    

    public void agregarCodigo(String instruccion) {
        codeSection.append(instruccion + "\n");
    }

    
    public String getNuevoLabel() {
        return "_label" + (labelCounter++);
    }

    
    public String getNombreAsm(String nombreMangled) {
        
        if (nombreMangled == null) return "NULL_MANGLED_ERROR";
        
        
        
        if (nombreMangled.startsWith("_aux")) {
            return nombreMangled;
        }
        
        String cleanName = nombreMangled;
        
        
        if (cleanName.startsWith("\"") && cleanName.endsWith("\"")) {
             cleanName = cleanName.substring(1, cleanName.length() - 1);
        }
        
        
        cleanName = cleanName.replaceAll("[^a-zA-Z0-9_]", "_");
        
        
        if (!cleanName.startsWith("_")) {
            cleanName = "_" + cleanName;
        }
        
        return cleanName;
    }

    
    public String getNombreRetorno(String nombreFuncionMangled, int index) {
         return "_RET_" + index + "_" + getNombreAsm(nombreFuncionMangled);
    }
    
    
    public String getFlagRecursion(String nombreFuncionMangled) {
        return "_IN_FUNC_" + getNombreAsm(nombreFuncionMangled);
    }

    
    public void convertirLongADFloat(String operandoLong) {
        agregarCodigo("FILD " + operandoLong); 
    }

    
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
