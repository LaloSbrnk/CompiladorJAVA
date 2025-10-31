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
        
        String tipoExpr = expresion.chequear(TdA);
        if (tipoExpr.equals("error")) return "error";
        
        String nombreVar = variable.getNombre(); // Ej: "X" o "TESTSEMANTICO.VARGLOBAL%D"

        if (esInferencia) {
            // --- Caso Inferencia (VAR X := ...) ---
            // (La lógica de inferencia... debe buscar si ya existe)
            AtributosTokens attrs = null;
            if (nombreVar.contains(".")) { 
                 // La inferencia no deberia aplicar a prefijos, pero chequeamos por si acaso
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
            System.out.println("DEBUG: Declarada variable (por inferencia) '" + attrsNuevos.getMangledName() + "' con tipo '" + tipoExpr + "'");
            return tipoExpr;

        } else {
            // --- Caso Asignacion Normal (X := ...) ---
            
            // 1. Chequear existencia y obtener tipo (esto usa la lógica de NodoVariable)
            String tipoVar = variable.chequear(TdA);
            if (tipoVar.equals("error")) {
                return "error"; // Error ya reportado por NodoVariable
            }

            // 2. Volver a buscar los atributos para chequear 'cv sl'
            // (Esta es la parte que faltaba)
            AtributosTokens attrs = null;
            if (nombreVar.contains(".")) {
                String[] partes = nombreVar.split("\\.", 2);
                String rootScope = TdA.getRootScopeName();
                if (rootScope != null && partes[0].equals(rootScope)) {
                    // Es un prefijo de Root Scope (ej: "TESTSEMANTICO.VARGLOBAL%D")
                    // Busca "VARGLOBAL%D:TESTSEMANTICO"
                    attrs = AnalizadorLexico.tablaSimbolos.get(partes[1] + ":" + partes[0]); 
                } else {
                    // Es un prefijo de Funcion (ej: "F1.X")
                    attrs = TdA.buscarPrefijado(nombreVar);
                }
            } else {
                // Es una variable simple (ej: "A")
                attrs = TdA.buscar(nombreVar);
            }

            // 3. Chequear 'cv sl' (Tema 24)
            if (attrs != null && "parametro".equals(attrs.getUso()) && "cv sl".equals(attrs.getModoPasaje())) {
                System.err.println("ERROR Semantico (Tema 24): Se intento asignar un valor al parametro de solo lectura ('cv sl') '" + nombreVar + "'.");
                // No retornamos error para seguir chequeando
            }
            
            // 4. Chequear tipos (Tema 30)
            if (tipoVar.equals("long") && tipoExpr.equals("dfloat")) {
                System.err.println("ERROR Semantico: Asignacion incompatible... (posible perdida de datos) al asignar a '" + nombreVar + "'.");
                return "error";
            }
            return tipoVar;
        }
    }
}