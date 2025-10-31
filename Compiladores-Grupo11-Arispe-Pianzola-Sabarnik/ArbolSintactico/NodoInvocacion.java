package ArbolSintactico;

import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;

public class NodoInvocacion extends Nodo {
    private String nombreFuncion;
    private ArrayList<NodoParametroReal> parametrosReales;

    public NodoInvocacion(String nombreFuncion, ArrayList<NodoParametroReal> parametrosReales) {
        this.nombreFuncion = nombreFuncion;
        this.parametrosReales = parametrosReales;
    }
    
    public String getNombre() { return nombreFuncion; }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // Buscar la funcion en la Tabla de Ambitos
        AtributosTokens attrsFuncion = TdA.buscar(nombreFuncion);

        // Chequear si existe y si es una funcion
        if (attrsFuncion == null || !attrsFuncion.getUso().equals("funcion")) {
            System.err.println("ERROR Semantico: Se intento invocar a '" + nombreFuncion + "' que no es una funcion o no esta declarada.");
            return "error";
        }

        // Obtener los parametros FORMALES esperados
        ArrayList<NodoParametro> paramsFormales = attrsFuncion.getParametros();

        int cantFormales = (paramsFormales == null) ? 0 : paramsFormales.size();
        int cantReales = (parametrosReales == null) ? 0 : parametrosReales.size();

        if (cantFormales != cantReales) {
            System.err.println("ERROR Semantico: La funcion '" + nombreFuncion + "' esperaba " + cantFormales +
                               " parametros, pero se recibieron " + cantReales + ".");
            return "error"; // No podemos seguir si las cantidades no coinciden
        }

        // Chequear tipos y nombres (par por par)
        if (parametrosReales != null) { // Solo si hay parametros
            // Para chequear que los nombres ->ID sean correctos y no repetidos
            HashMap<String, NodoParametro> mapFormales = new HashMap<>();
            for (NodoParametro formal : paramsFormales) {
                mapFormales.put(formal.getNombre(), formal);
            }

            for (NodoParametroReal real : parametrosReales) {
                String nombreFormalBuscado = real.getNombreFormal();
                NodoParametro formalCorrespondiente = mapFormales.get(nombreFormalBuscado);

                // Chequeo A: El nombre formal ('->ID') existe?
                if (formalCorrespondiente == null) {
                    System.err.println("ERROR Semantico: La funcion '" + nombreFuncion + "' no tiene un parametro llamado '" + nombreFormalBuscado + "'.");
                    return "error";
                }

                // Chequeo B: Los tipos son compatibles?
                String tipoReal = real.chequear(TdA); // Chequea la expresion
                String tipoFormal = formalCorrespondiente.getTipo();

                if (tipoReal.equals("error")) return "error"; // Error ya reportado

                // Permitimos pasar 'long' donde se espera 'dfloat'
                if (tipoFormal.equals("dfloat") && tipoReal.equals("long")) {
                } else if (!tipoFormal.equals(tipoReal)) {
                    System.err.println("ERROR Semantico: Incompatibilidad de tipos al invocar '" + nombreFuncion +
                                       "'. Se esperaba '" + tipoFormal + "' para el parametro '" + nombreFormalBuscado +
                                       "' pero se recibio '" + tipoReal + "'.");
                    return "error";
                }
            }
        }

        // Determinar el tipo de retorno
        List<String> tiposRetorno = attrsFuncion.getTiposRetorno();
        if (tiposRetorno == null || tiposRetorno.isEmpty()) {
             System.err.println("ERROR Interno: Funcion '" + nombreFuncion + "' sin tipos de retorno definidos.");
             return "error";
        } else if (tiposRetorno.size() == 1) {
             // Si solo hay un tipo de retorno, la invocacion tiene ESE tipo
             return tiposRetorno.get(0);
        } else {
             // Tema 20: Si hay multiples retornos, la invocacion NO puede usarse
             // directamente en una expresion simple. Marcamos con un tipo especial.
             // La regla 'asignacion_multiple' debera manejar esto.
             return "multiple"; 
        }
    }
}