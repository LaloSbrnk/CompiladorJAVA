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
        
        // --- NUEVA LOGICA (TEMA 16 + TEMA 20) ---
        if (expresiones.size() == 1 && expresiones.get(0) instanceof NodoInvocacion) {
            // Caso especial: X, Y = FUNCION(...)
            NodoInvocacion invoc = (NodoInvocacion)expresiones.get(0);
            
            // Obtenemos los atributos de la funcion (necesitamos getNombre() en NodoInvocacion)
            AtributosTokens attrsFuncion = TdA.buscar(invoc.getNombre());
            if (attrsFuncion == null) {
                invoc.chequear(TdA); // Dejamos que 'chequear' reporte el error "no es funcion"
                return "error";
            }
            
            // Chequeamos los parametros de la invocacion ANTES de chequear retornos
            invoc.chequear(TdA); 
            
            List<String> tiposRetorno = attrsFuncion.getTiposRetorno();
            
            // Chequeo de Cantidad (Tema 16/20)
            if (variables.size() != tiposRetorno.size()) {
                System.err.println("ERROR Semantico (Tema 16/20): Invocacion a '" + invoc.getNombre() + "' devuelve " +
                                tiposRetorno.size() + " valores, pero se asignan a " + variables.size() + " variables.");
                return "error";
            }
            
            // Chequeo de Tipos (par por par)
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
            
            System.out.println("DEBUG: Asignacion Multiple (con invocacion) chequeada OK.");
            return "void";
            
        } else {
            // --- LOGICA ORIGINAL (Asignacion estandar X, Y = A, B) ---
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

            String tipoVar = varNode.chequear(TdA); // Chequea existencia y obtiene tipo
            String tipoExpr = exprNode.chequear(TdA); // Chequea sub-arbol y obtiene tipo

            if (tipoVar.equals("error") || tipoExpr.equals("error")) {
                huboError = true; 
                continue; 
            }

            // Reutilizamos el chequeo de NodoAsignacion (long = dfloat --> Error)
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

        System.out.println("DEBUG: Asignacion Multiple (Tema 16) chequeada OK.");
        return "void"; 
    }
}
}