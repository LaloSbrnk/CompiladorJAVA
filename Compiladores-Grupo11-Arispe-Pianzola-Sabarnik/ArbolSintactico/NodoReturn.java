package ArbolSintactico;

import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 

public class NodoReturn extends Nodo {
    private ArrayList<Nodo> expresionesRetorno; // Lista de Nodos (arboles) devueltos
    private NodoFuncionDef funcionContenedora; // Referencia a la funcion actual

    public NodoReturn(ArrayList<Nodo> expresiones, NodoFuncionDef funcion) {
        this.expresionesRetorno = expresiones;
        this.funcionContenedora = funcion;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // Obtenemos los tipos esperados de la funcion
        List<String> tiposEsperados = funcionContenedora.getTiposRetorno();
        
        // Obtenemos los tipos REALES devueltos (chequeando cada expresion)
        List<String> tiposReales = new ArrayList<>();
        if (expresionesRetorno != null) {
            for (Nodo expr : expresionesRetorno) {
                tiposReales.add(expr.chequear(TdA));
            }
        }

        // Chequeo 1: Cantidad de valores (Tema 20)
        if (tiposEsperados.size() != tiposReales.size()) {
            System.err.println("ERROR Semantico: La funcion '" + funcionContenedora.getNombre() + // Necesitas un getNombre() en NodoFuncionDef
                               "' esperaba " + tiposEsperados.size() + " valores de retorno, pero se retornaron " + tiposReales.size() + ".");
            return "error"; 
        }

        // Chequeo 2: Tipos (par por par)
        boolean errorTipos = false;
        for (int i = 0; i < tiposEsperados.size(); i++) {
            String esperado = tiposEsperados.get(i);
            String real = tiposReales.get(i);

            if (real.equals("error")) {
                errorTipos = true; 
                continue;
            }

            // no se puede retornar dfloat si se espera long
            if (esperado.equals("long") && real.equals("dfloat")) {
                System.err.println("ERROR Semantico: Tipo de retorno incompatible en la posicion " + (i+1) +
                                   ". Se esperaba '" + esperado + "' pero se retorno '" + real + "' (posible perdida de datos).");
                errorTipos = true;
            }
            // (Si se espera dfloat y se retorna long, esta OK)
            // (Si son iguales, esta OK)
        }

        if (errorTipos) {
            return "error";
        }

        System.out.println("DEBUG: Chequeo de RETURN para funcion '" + funcionContenedora.getNombre() + "' OK.");
        return "void"; 
    }
}