package ArbolSintactico;
import CompiladoresMain.*;
import java.util.HashMap;

public class NodoAsignacion extends Nodo {
    private NodoVariable variable;
    private Nodo expresion;
    private boolean esInferencia = false;
    
    public NodoAsignacion(NodoVariable variable, Nodo expresion) {
        this.variable = variable;
        this.expresion = expresion;
    }
    
    public String getNombreVariable() {
        return variable.getNombre();
    }
    
    public String chequearTipoExpresion(TablaDeAmbitos TdA) {
         return expresion.chequear(TdA);
    }
    
    public void setEsInferencia() {
        this.esInferencia = true;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // ... (código de chequeo existente sin cambios) ...
        String tipoExpr = expresion.chequear(TdA);
        if (tipoExpr.equals("error")) return "error";
        String nombreVar = variable.getNombre(); 
        if (esInferencia) {
            AtributosTokens attrs = null;
            if (nombreVar.contains(".")) { 
                attrs = TdA.buscarPrefijado(nombreVar);
            } else {
                attrs = TdA.buscar(nombreVar);
            }
            if (attrs != null && attrs.getUso() != null) {
                System.err.println("ERROR Semantico: Redeclaracion de variable (por inferencia) '" + nombreVar + "'.");
                return "error";
            }
            AtributosTokens attrsNuevos = new AtributosTokens(TiposToken.IDENTIFICADOR);
            attrsNuevos.setTipoDato(tipoExpr);
            attrsNuevos.setUso("variable");
            if (!TdA.agregar(nombreVar, attrsNuevos)) {
                 System.err.println("ERROR Semantico: Redeclaracion de variable (por inferencia) '" + nombreVar + "'.");
                return "error";
            }
            return tipoExpr;
        } else {
            String tipoVar = variable.chequear(TdA);
            if (tipoVar.equals("error")) {
                return "error"; 
            }
            AtributosTokens attrs = null;
            if (nombreVar.contains(".")) {
                String[] partes = nombreVar.split("\\.", 2);
                String rootScope = TdA.getRootScopeName();
                if (rootScope != null && partes[0].equals(rootScope)) {
                    attrs = AnalizadorLexico.tablaSimbolos.get(partes[1] + ":" + partes[0]); 
                } else {
                    attrs = TdA.buscarPrefijado(nombreVar);
                }
            } else {
                attrs = TdA.buscar(nombreVar);
            }
            if (attrs != null && "parametro".equals(attrs.getUso()) && "cv sl".equals(attrs.getModoPasaje())) {
                System.err.println("ERROR Semantico (Tema 24): Se intento asignar un valor al parametro de solo lectura ('cv sl') '" + nombreVar + "'.");
            }
            if (tipoVar.equals("long") && tipoExpr.equals("dfloat")) {
                System.err.println("ERROR Semantico: Asignacion incompatible... (posible perdida de datos) al asignar a '" + nombreVar + "'.");
                return "error";
            }
            return tipoVar;
        }
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
        if (esInferencia) {
            System.out.println(prefijo + "Asignacion (con Inferencia 'var')");
        } else {
            System.out.println(prefijo + "Asignacion (:=)");
        }
        variable.imprimir(prefijo + "  " + "Lado Izquierdo: ");
        expresion.imprimir(prefijo + "  " + "Lado Derecho: ");
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        // 1. Obtener nombre ASM de la variable
        String nombreVarAsm = variable.generarCodigo(G, TdA);
        
        // 2. Generar código de la expresión y obtener dónde está el resultado
        String resExpr = expresion.generarCodigo(G, TdA);
        
        // 3. Obtener tipos para manejar conversión (Tema 30)
        String tipoVar = variable.chequear(TdA);
        String tipoExpr = expresion.chequear(TdA);

        if ("error".equals(tipoVar) || "error".equals(tipoExpr)) {
            return null; // No generar código si hay error semántico
        }

        // 4. Generar código de asignación
        if (tipoVar.equals("long")) {
            // Asignación a Long (Solo puede venir de Long)
            G.agregarCodigo("MOV EAX, " + resExpr); // [cite: 2016]
            G.agregarCodigo("MOV " + nombreVarAsm + ", EAX");
        } else if (tipoVar.equals("dfloat")) {
            // Asignación a DFloat
            if (tipoExpr.equals("long")) {
                // Conversión Implícita (Tema 30) [cite: 1803]
                G.convertirLongADFloat(resExpr); // FILD [cite: 2913]
            } else {
                // dfloat -> dfloat
                G.agregarCodigo("FLD " + resExpr); // [cite: 2909]
            }
            G.agregarCodigo("FSTP " + nombreVarAsm); // [cite: 2906]
        }
        
        return null;
    }
}