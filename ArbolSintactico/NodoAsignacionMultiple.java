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
            if (variables.size() != tiposRetorno.size()) {
                System.err.println("ERROR Semantico (Tema 16/20): Invocacion a '" + invoc.getNombre() + "' devuelve " +
                                tiposRetorno.size() + " valores, pero se asignan a " + variables.size() + " variables.");
                return "error";
            }
            boolean huboError = false;
            for (int i = 0; i < variables.size(); i++) {
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
        for (int i = 0; i < variables.size(); i++) {
            NodoVariable varNode = variables.get(i);
            Nodo exprNode = expresiones.get(i);
            String tipoVar = varNode.chequear(TdA); 
            String tipoExpr = exprNode.chequear(TdA); 
            if (tipoVar.equals("error") || tipoExpr.equals("error")) {
                huboError = true; 
                continue; 
            }
            if (tipoVar.equals("long") && tipoExpr.equals("dfloat")) {
                System.err.println("ERROR Semantico: Asignacion incompatible en elemento " + (i+1) +
                                   " de la asignacion multiple. Posible perdida de datos al asignar '" + tipoExpr +
                                   "' a la variable '" + varNode.getNombre() + "' de tipo '" + tipoVar + "'.");
                huboError = true;
            }
        }
        if (huboError) {
            return "error";
        }
        return "void"; 
    }
}
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Asignacion Multiple (=)");
        System.out.println(prefijo + "  " + "Lado Izquierdo:");
        for (NodoVariable v : variables) {
            v.imprimir(prefijo + "    ");
        }
        System.out.println(prefijo + "  " + "Lado Derecho:");
        for (Nodo e : expresiones) {
            e.imprimir(prefijo + "    ");
        }
    }
}