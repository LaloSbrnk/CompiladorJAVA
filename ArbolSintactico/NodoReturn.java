package ArbolSintactico;
import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
public class NodoReturn extends Nodo {
    private ArrayList<Nodo> expresionesRetorno; 
    private NodoFuncionDef funcionContenedora; 
    public NodoReturn(ArrayList<Nodo> expresiones, NodoFuncionDef funcion) {
        this.expresionesRetorno = expresiones;
        this.funcionContenedora = funcion;
    }
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        List<String> tiposEsperados = funcionContenedora.getTiposRetorno();
        List<String> tiposReales = new ArrayList<>();
        if (expresionesRetorno != null) {
            for (Nodo expr : expresionesRetorno) {
                tiposReales.add(expr.chequear(TdA));
            }
        }
        if (tiposEsperados.size() != tiposReales.size()) {
            System.err.println("ERROR Semantico: La funcion '" + funcionContenedora.getNombre() + 
                               "' esperaba " + tiposEsperados.size() + " valores de retorno, pero se retornaron " + tiposReales.size() + ".");
            return "error"; 
        }
        boolean errorTipos = false;
        for (int i = 0; i < tiposEsperados.size(); i++) {
            String esperado = tiposEsperados.get(i);
            String real = tiposReales.get(i);
            if (real.equals("error")) {
                errorTipos = true; 
                continue;
            }
            if (esperado.equals("long") && real.equals("dfloat")) {
                System.err.println("ERROR Semantico: Tipo de retorno incompatible en la posicion " + (i+1) +
                                   ". Se esperaba '" + esperado + "' pero se retorno '" + real + "' (posible perdida de datos).");
                errorTipos = true;
            }
        }
        if (errorTipos) {
            return "error";
        }
        return "void"; 
    }
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Sentencia RETURN");
        for (Nodo e : expresionesRetorno) {
            e.imprimir(prefijo + "  " + "Valor: ");
        }
    }
}