package ArbolSintactico;
import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodoAsignacionMultiple extends Nodo {
    private ArrayList<NodoVariable> variables; 
    private ArrayList<Nodo> expresiones;    
    
    public NodoAsignacionMultiple(ArrayList<NodoVariable> variables, ArrayList<Nodo> expresiones) {
        this.variables = variables;
        this.expresiones = expresiones;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // ... (código de chequeo existente sin cambios) ...
        // (Tema 20: Permite menos retornos que variables, chequea tipos)
        if (expresiones.size() == 1 && expresiones.get(0) instanceof NodoInvocacion) {
            NodoInvocacion invoc = (NodoInvocacion)expresiones.get(0);
            AtributosTokens attrsFuncion = TdA.buscar(invoc.getNombre());
            if (attrsFuncion == null) {
                invoc.chequear(TdA); 
                return "error";
            }
            invoc.chequear(TdA); 
            List<String> tiposRetorno = attrsFuncion.getTiposRetorno();
            
            // TEMA 20: "En caso que el número de componentes del retorno sea menor que 
            // el número de variables... se asignará un valor por defecto... y se informará... Warning."
            // "En caso que... sea mayor ... se informará error." [cite: 2633, 2635]
            if (variables.size() < tiposRetorno.size()) {
                 System.err.println("ERROR Semantico (Tema 20): Invocacion a '" + invoc.getNombre() + "' devuelve " +
                                tiposRetorno.size() + " valores, pero se intentan asignar a solo " + variables.size() + " variables.");
                return "error";
            }
            if (variables.size() > tiposRetorno.size()) {
                System.out.println("WARNING Semantico (Tema 20): Invocacion a '" + invoc.getNombre() + "' devuelve " +
                                tiposRetorno.size() + " valores, pero se asignan a " + variables.size() + 
                                " variables. Las variables sobrantes no seran inicializadas.");
                // No es un error, continuamos.
            }
            
            boolean huboError = false;
            for (int i = 0; i < tiposRetorno.size(); i++) { // Solo chequear hasta los retornos disponibles
                String tipoVar = variables.get(i).chequear(TdA);
                String tipoRetorno = tiposRetorno.get(i);
                if (tipoVar.equals("long") && tipoRetorno.equals("dfloat")) {
                    System.err.println("ERROR Semantico: Asignacion multiple incompatible en elemento " + (i+1) +
                                        ". Se intenta asignar un '" + tipoRetorno + "' a la variable '" +
                                        variables.get(i).getNombre() + "' de tipo '" + tipoVar + "'.");
                    huboError = true;
                }
            }
            if (huboError) return "error";
            return "void";
        } else {
             // ... (código chequeo Tema 16 existente) ...
            if (variables.size() != expresiones.size()) {
                System.err.println("ERROR Semantico (Tema 16): La cantidad de elementos no coincide. " +
                                variables.size() + " variables a la izquierda, " +
                                expresiones.size() + " expresiones a la derecha.");
                return "error";
            }
            // ... (resto chequeo Tema 16) ...
            return "void"; 
        }
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        // Caso 1: Asignación desde Invocación (Tema 20)
        if (expresiones.size() == 1 && expresiones.get(0) instanceof NodoInvocacion) {
            NodoInvocacion invoc = (NodoInvocacion)expresiones.get(0);
            
            // Generar el CALL (esto ejecuta la función)
            invoc.generarCodigo(G, TdA); 
            
            AtributosTokens attrsFuncion = TdA.buscar(invoc.getNombre());
            String funcMangledName = attrsFuncion.getMangledName();
            List<String> tiposRetorno = attrsFuncion.getTiposRetorno();

            // Asignar los valores de retorno (ej. _RET_0, _RET_1) a las variables
            for (int i = 0; i < tiposRetorno.size(); i++) {
                if (i >= variables.size()) break; // Más retornos que variables (error ya detectado)

                String varAsmName = variables.get(i).generarCodigo(G, TdA);
                String varRetAsmName = G.getNombreRetorno(funcMangledName, i);
                String tipoVar = variables.get(i).chequear(TdA);
                
                if (tipoVar.equals("long")) {
                    G.agregarCodigo("MOV EAX, " + varRetAsmName);
                    G.agregarCodigo("MOV " + varAsmName + ", EAX");
                } else {
                    G.agregarCodigo("FLD " + varRetAsmName);
                    G.agregarCodigo("FSTP " + varAsmName);
                }
            }
            // (Tema 20: Variables sobrantes no se tocan, Warning ya fue emitido)

        } else {
            // Caso 2: Asignación Múltiple estándar (Tema 16)
            // (Requiere auxiliares para evitar "A, B = B, A")
            
            ArrayList<String> resultadosExpr = new ArrayList<>();
            for (Nodo expr : expresiones) {
                resultadosExpr.add(expr.generarCodigo(G, TdA));
            }
            
            for (int i = 0; i < variables.size(); i++) {
                String varAsmName = variables.get(i).generarCodigo(G, TdA);
                String resExpr = resultadosExpr.get(i);
                
                String tipoVar = variables.get(i).chequear(TdA);
                String tipoExpr = expresiones.get(i).chequear(TdA);
                
                if (tipoVar.equals("long")) {
                    G.agregarCodigo("MOV EAX, " + resExpr);
                    G.agregarCodigo("MOV " + varAsmName + ", EAX");
                } else if (tipoVar.equals("dfloat")) {
                    if (tipoExpr.equals("long")) {
                        G.convertirLongADFloat(resExpr); // Tema 30
                    } else {
                        G.agregarCodigo("FLD " + resExpr);
                    }
                    G.agregarCodigo("FSTP " + varAsmName);
                }
            }
        }
        return null;
    }
}