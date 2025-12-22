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
        
        if (expresiones.size() == 1 && expresiones.get(0) instanceof NodoInvocacion) {
            NodoInvocacion invoc = (NodoInvocacion)expresiones.get(0);
            AtributosTokens attrsFuncion = TdA.buscar(invoc.getNombre());
            if (attrsFuncion == null) {
                invoc.chequear(TdA); 
                return "error";
            }
            invoc.chequear(TdA); 
            List<String> tiposRetorno = attrsFuncion.getTiposRetorno();
            
            
            
            if (variables.size() < tiposRetorno.size()) {
                 System.err.println("ERROR Semantico (Tema 20): Invocacion a '" + invoc.getNombre() + "' devuelve " +
                                tiposRetorno.size() + " valores, pero se intentan asignar a solo " + variables.size() + " variables.");
                return "error";
            }
            if (variables.size() > tiposRetorno.size()) {
                System.out.println("WARNING Semantico (Tema 20): Invocacion a '" + invoc.getNombre() + "' devuelve " +
                                tiposRetorno.size() + " valores, pero se asignan a " + variables.size() + 
                                " variables. Las variables sobrantes seran inicializadas en 0.");
                
            }
            
            boolean huboError = false;
            for (int i = 0; i < tiposRetorno.size(); i++) { 
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
             
            if (variables.size() != expresiones.size()) {
                System.err.println("ERROR Semantico (Tema 16): La cantidad de elementos no coincide. " +
                                variables.size() + " variables a la izquierda, " +
                                expresiones.size() + " expresiones a la derecha.");
                return "error";
            }
            
            boolean huboError = false;
            for(int i=0; i<variables.size(); i++) {
                String tVar = variables.get(i).chequear(TdA);
                String tExpr = expresiones.get(i).chequear(TdA);
                if(tVar.equals("long") && tExpr.equals("dfloat")) {
                     System.err.println("ERROR Semantico: Asignacion multiple incompatible en elemento " + (i+1));
                     huboError = true;
                }
            }
            if(huboError) return "error";
            return "void"; 
        }
    }
    
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Asignacion Multiple");
    }

    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        
        if (expresiones.size() == 1 && expresiones.get(0) instanceof NodoInvocacion) {
            NodoInvocacion invoc = (NodoInvocacion)expresiones.get(0);
            
            
            invoc.generarCodigo(G, TdA); 
            
            AtributosTokens attrsFuncion = TdA.buscar(invoc.getNombre());
            String funcMangledName = attrsFuncion.getMangledName();
            List<String> tiposRetorno = attrsFuncion.getTiposRetorno();

            
            for (int i = 0; i < tiposRetorno.size(); i++) {
                if (i >= variables.size()) break; 

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
            
            
            for (int i = tiposRetorno.size(); i < variables.size(); i++) {
                String varAsmName = variables.get(i).generarCodigo(G, TdA);
                String tipoVar = variables.get(i).chequear(TdA);

                if (tipoVar.equals("long")) {
                    
                    G.agregarCodigo("MOV DWORD PTR " + varAsmName + ", 0");
                } else {
                    
                    G.agregarCodigo("FLDZ"); 
                    G.agregarCodigo("FSTP " + varAsmName); 
                }
            }

        } else {
            
            
            
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
                        G.convertirLongADFloat(resExpr); 
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
