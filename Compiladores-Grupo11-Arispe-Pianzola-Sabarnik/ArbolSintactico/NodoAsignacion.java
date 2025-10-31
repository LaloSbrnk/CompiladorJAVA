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
        AtributosTokens attrs = TdA.buscar(variable.getNombre());
        if (attrs == null) {
            System.err.println("ERROR Semantico: Variable '" + variable.getNombre() + "' no declarada.");
            return "error";
        }

        String tipoExpr = expresion.chequear(TdA);
        if (tipoExpr.equals("error")) return "error";

        // *** LOGICA CORREGIDA ***
        if (esInferencia) {
            // Solo si venimos de 'VAR', chequeamos redeclaracion y asignamos tipo
            if (attrs.getUso() != null) {
                System.err.println("ERROR Semantico: Redeclaracion de variable (por inferencia) '" + variable.getNombre() + "'.");
                return "error";
            }
            attrs.setTipoDato(tipoExpr);
            attrs.setUso("variable");
            System.out.println("DEBUG: Declarada variable (por inferencia) '" + variable.getNombre() + "' con tipo '" + tipoExpr + "'");
            return tipoExpr;
        } else {
            // Es una asignacion normal, chequeamos que YA este declarada
            if (attrs.getUso() == null) {
                System.err.println("ERROR Semantico: Variable '" + variable.getNombre() + "' no declarada (en asignacion).");
                return "error";
            }
            
            // Chequeo de tipos de asignacion normal
            String tipoVar = attrs.getTipoDato();
            if (tipoVar.equals("long") && tipoExpr.equals("dfloat")) {
                System.err.println("ERROR Semantico: Asignacion incompatible... (posible perdida de datos)");
                return "error";
            }
            return tipoVar;
        }
    }
}