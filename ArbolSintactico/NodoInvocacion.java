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
        AtributosTokens attrsFuncion = TdA.buscar(nombreFuncion);
        if (attrsFuncion == null || !attrsFuncion.getUso().equals("funcion")) {
            System.err.println("ERROR Semantico: Se intento invocar a '" + nombreFuncion + "' que no es una funcion o no esta declarada.");
            return "error";
        }
        ArrayList<NodoParametro> paramsFormales = attrsFuncion.getParametros();
        int cantFormales = (paramsFormales == null) ? 0 : paramsFormales.size();
        int cantReales = (parametrosReales == null) ? 0 : parametrosReales.size();
        if (cantFormales != cantReales) {
            System.err.println("ERROR Semantico: La funcion '" + nombreFuncion + "' esperaba " + cantFormales +
                               " parametros, pero se recibieron " + cantReales + ".");
            return "error"; 
        }
        if (parametrosReales != null) { 
            HashMap<String, NodoParametro> mapFormales = new HashMap<>();
            for (NodoParametro formal : paramsFormales) {
                mapFormales.put(formal.getNombre(), formal);
            }
            for (NodoParametroReal real : parametrosReales) {
                String nombreFormalBuscado = real.getNombreFormal();
                NodoParametro formalCorrespondiente = mapFormales.get(nombreFormalBuscado);
                if (formalCorrespondiente == null) {
                    System.err.println("ERROR Semantico: La funcion '" + nombreFuncion + "' no tiene un parametro llamado '" + nombreFormalBuscado + "'.");
                    return "error";
                }
                String tipoReal = real.chequear(TdA); 
                String tipoFormal = formalCorrespondiente.getTipo();
                if (tipoReal.equals("error")) return "error"; 
                if (tipoFormal.equals("dfloat") && tipoReal.equals("long")) {
                } else if (!tipoFormal.equals(tipoReal)) {
                    System.err.println("ERROR Semantico: Incompatibilidad de tipos al invocar '" + nombreFuncion +
                                       "'. Se esperaba '" + tipoFormal + "' para el parametro '" + nombreFormalBuscado +
                                       "' pero se recibio '" + tipoReal + "'.");
                    return "error";
                }
            }
        }
        List<String> tiposRetorno = attrsFuncion.getTiposRetorno();
        if (tiposRetorno == null || tiposRetorno.isEmpty()) {
             System.err.println("ERROR Interno: Funcion '" + nombreFuncion + "' sin tipos de retorno definidos.");
             return "error";
        } else if (tiposRetorno.size() == 1) {
             return tiposRetorno.get(0);
        } else {
             return "multiple"; 
        }
    }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Invocacion a: " + nombreFuncion);
        if (parametrosReales != null && !parametrosReales.isEmpty()) {
             System.out.println(prefijo + "  " + "Parametros Reales:");
            for (NodoParametroReal pr : parametrosReales) {
                pr.imprimir(prefijo + "    ");
            }
        }
    }
}