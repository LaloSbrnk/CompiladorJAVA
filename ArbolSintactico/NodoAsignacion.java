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
        
        if (esInferencia) {
            System.out.println(prefijo + "Asignacion (con Inferencia 'var')");
        } else {
            System.out.println(prefijo + "Asignacion (:=)");
        }
        variable.imprimir(prefijo + "  " + "Lado Izquierdo: ");
        expresion.imprimir(prefijo + "  " + "Lado Derecho: ");
    }

    
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        
        String nombreVarAsm = variable.generarCodigo(G, TdA);
        
        
        String resExpr = expresion.generarCodigo(G, TdA);
        
        
        String tipoVar = variable.chequear(TdA);
        String tipoExpr = expresion.chequear(TdA);

        if ("error".equals(tipoVar) || "error".equals(tipoExpr)) {
            return null; 
        }

        
        
        if (tipoVar.equals("long") && tipoExpr.equals("dfloat")) {
            return null;
        }
        

        
        if (tipoVar.equals("long")) {
            
            G.agregarCodigo("MOV EAX, " + resExpr); 
            G.agregarCodigo("MOV " + nombreVarAsm + ", EAX");
        } else if (tipoVar.equals("dfloat")) {
            
            if (tipoExpr.equals("long")) {
                
                G.convertirLongADFloat(resExpr); 
            } else {
                
                G.agregarCodigo("FLD " + resExpr); 
            }
            G.agregarCodigo("FSTP " + nombreVarAsm); 
        }
        
        return null;
    }
}
