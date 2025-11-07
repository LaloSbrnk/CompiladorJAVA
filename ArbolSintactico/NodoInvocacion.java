package ArbolSintactico;
import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;

public class NodoInvocacion extends Nodo {
    private String nombreFuncion;
    private ArrayList<NodoParametroReal> parametrosReales;
    private AtributosTokens attrsFuncion = null; // Cachear
    
    public NodoInvocacion(String nombreFuncion, ArrayList<NodoParametroReal> parametrosReales) {
        this.nombreFuncion = nombreFuncion;
        this.parametrosReales = parametrosReales;
    }
    
    public String getNombre() { return nombreFuncion; }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        if (this.attrsFuncion != null) {
            // <--- CORRECCIÓN (Lógica de cacheo mejorada) ---
             List<String> tiposRetorno = this.attrsFuncion.getTiposRetorno();
             if (tiposRetorno == null || tiposRetorno.isEmpty()) return "error";
             if (tiposRetorno.size() == 1) return tiposRetorno.get(0);
             else return "multiple";
        }
        
        AtributosTokens attrsFuncion = TdA.buscar(nombreFuncion);
        
        if (attrsFuncion == null || !attrsFuncion.getUso().equals("funcion")) {
            System.err.println("ERROR Semantico: Se intento invocar a '" + nombreFuncion + "' que no es una funcion o no esta declarada.");
            return "error";
        }
        ArrayList<NodoParametro> paramsFormales = attrsFuncion.getParametros();
        int cantFormales = (paramsFormales == null) ? 0 : paramsFormales.size();
        int cantReales = (parametrosReales == null) ? 0 : parametrosReales.size();
        if (cantFormales != cantReales) {
            System.err.println("ERROR Semantico: La funcion '" + nombreFuncion + "' esperaba " + cantFormales +
                               " parametros, pero se recibieron " + cantReales + ".");
            return "error"; 
        }
        if (parametrosReales != null) { 
            HashMap<String, NodoParametro> mapFormales = new HashMap<>();
            // <--- CORRECCIÓN (Añadir chequeo de nulidad) ---
            if (paramsFormales != null) { 
                for (NodoParametro formal : paramsFormales) {
                    mapFormales.put(formal.getNombre(), formal);
                }
            }
            for (NodoParametroReal real : parametrosReales) {
                String nombreFormalBuscado = real.getNombreFormal();
                NodoParametro formalCorrespondiente = mapFormales.get(nombreFormalBuscado);
                if (formalCorrespondiente == null) {
                    System.err.println("ERROR Semantico: La funcion '" + nombreFuncion + "' no tiene un parametro llamado '" + nombreFormalBuscado + "'.");
                    return "error";
                }
                String tipoReal = real.chequear(TdA); 
                String tipoFormal = formalCorrespondiente.getTipo();
                if (tipoReal.equals("error")) return "error"; 
                if (tipoFormal.equals("dfloat") && tipoReal.equals("long")) {
                } else if (!tipoFormal.equals(tipoReal)) {
                    System.err.println("ERROR Semantico: Incompatibilidad de tipos al invocar '" + nombreFuncion +
                                       "'. Se esperaba '" + tipoFormal + "' para el parametro '" + nombreFormalBuscado +
                                       "' pero se recibio '" + tipoReal + "'.");
                    return "error";
                }
            }
        }
        // --- Fin chequeo ---
        
        this.attrsFuncion = attrsFuncion; // Cachear
        List<String> tiposRetorno = attrsFuncion.getTiposRetorno();
        
        if (tiposRetorno == null || tiposRetorno.isEmpty()) {
             System.err.println("ERROR Interno: Funcion '" + nombreFuncion + "' sin tipos de retorno definidos.");
             return "error";
        } else if (tiposRetorno.size() == 1) {
             return tiposRetorno.get(0);
        } else {
             return "multiple"; // Para NodoAsignacionMultiple
        }
    }
    
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Invocacion a: " + nombreFuncion);
        if (parametrosReales != null && !parametrosReales.isEmpty()) {
             System.out.println(prefijo + "  " + "Parametros Reales:");
            for (NodoParametroReal pr : parametrosReales) {
                pr.imprimir(prefijo + "    ");
            }
        }
    }

    // --- METODO CORREGIDO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        // 1. Asegurarse de tener los atributos
        if (this.attrsFuncion == null) {
            this.chequear(TdA);
        }
        if (this.attrsFuncion == null) return null; // Falló el chequeo
        
        String funcMangledName = this.attrsFuncion.getMangledName();
        String funcAsmName = G.getNombreAsm(funcMangledName);
        String flagRecursion = G.getFlagRecursion(funcMangledName);

        // 2. (h) Chequeo de Recursión
        G.agregarCodigo("; Chequeo de recursion (h)");
        G.agregarCodigo("CMP " + flagRecursion + ", 1");
        G.agregarCodigo("JE _ERROR_RECURSION");
        G.agregarCodigo("MOV " + flagRecursion + ", 1");
        
        // 3. Pasaje de Parámetros (Tema 24: Copia-Valor)
        // (Simulamos pasaje por copia a las variables de ámbito de la función)
        HashMap<String, NodoParametro> mapFormales = new HashMap<>();
        // <--- CORRECCIÓN (Añadir chequeo de nulidad) ---
        if (attrsFuncion.getParametros() != null) { 
            for (NodoParametro formal : attrsFuncion.getParametros()) {
                mapFormales.put(formal.getNombre(), formal);
            }
        }
        
        if (parametrosReales != null) {
            for (NodoParametroReal real : parametrosReales) {
                String nombreFormal = real.getNombreFormal();
                NodoParametro formal = mapFormales.get(nombreFormal);
                
                // --- INICIO DE LA CORRECCIÓN (Error 3) ---
                String paramAsmName = G.getNombreAsm(formal.getAtributos().getMangledName());
                // --- FIN DE LA CORRECCIÓN ---

                String resExpr = real.getExpresion().generarCodigo(G, TdA);
                String tipoReal = real.getExpresion().chequear(TdA);
                String tipoFormal = formal.getTipo();

                // Asignación (con conversión implícita si es necesario)
                if (tipoFormal.equals("long")) {
                    G.agregarCodigo("MOV EAX, " + resExpr);
                    G.agregarCodigo("MOV " + paramAsmName + ", EAX");
                } else if (tipoFormal.equals("dfloat")) {
                    if (tipoReal.equals("long")) {
                        G.convertirLongADFloat(resExpr); // Tema 30
                    } else {
                        G.agregarCodigo("FLD " + resExpr);
                    }
                    G.agregarCodigo("FSTP " + paramAsmName);
                }
            }
        }

        // 4. Llamada a la función
        G.agregarCodigo("CALL " + funcAsmName);

        // 5. Resetear flag de recursión
        G.agregarCodigo("MOV " + flagRecursion + ", 0");
        
        // 6. Manejar valor de retorno (para expresiones)
        List<String> tiposRetorno = attrsFuncion.getTiposRetorno();
        if (tiposRetorno.size() == 1) {
            // Si es una expresión, devuelve el primer (y único) valor de retorno
            String tipoRet = tiposRetorno.get(0);
            String varRetorno = G.getNombreRetorno(funcMangledName, 0);
            String aux = (tipoRet.equals("long")) ? GeneradorAssembler.AUX_LONG : GeneradorAssembler.AUX_DFLOAT;
            aux = G.getNombreAsm(aux);

            if (tipoRet.equals("long")) {
                G.agregarCodigo("MOV EAX, " + varRetorno);
                G.agregarCodigo("MOV " + aux + ", EAX");
            } else {
                G.agregarCodigo("FLD " + varRetorno);
                G.agregarCodigo("FSTP " + aux);
            }
            return aux;
        }

        // Si es "multiple" o "void", no devuelve nada (manejado por AsignacionMultiple)
        return null;
    }
}