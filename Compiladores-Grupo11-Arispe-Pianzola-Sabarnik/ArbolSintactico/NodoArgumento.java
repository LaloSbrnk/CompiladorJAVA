package ArbolSintactico;
import CompiladoresMain.*;
import java.util.HashMap;
public class NodoArgumento extends Nodo {
    private Object valor; 
    private String tipoArgumento; 
    public NodoArgumento(Object valor, String tipo) {
        this.valor = valor;
        this.tipoArgumento = tipo;
    }
    public Object getValor() {
        return valor;
    }
    public String getTipoArgumento() {
        return tipoArgumento;
    }
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        if (tipoArgumento.equals("id")) {
            AtributosTokens attrs = TdA.buscar((String)valor);
            if (attrs == null || attrs.getUso() == null) {
                System.err.println("ERROR Semantico: Variable '" + valor + "' usada como argumento lambda no declarada.");
                return "error";
            }
            return attrs.getTipoDato();
        } else {
            return tipoArgumento;
        }
    }
    @Override
    public void imprimir(String prefijo) {
        if ("id".equals(tipoArgumento)) {
            System.out.println(prefijo + "Argumento (Variable: " + valor.toString() + ")");
        } else {
            System.out.println(prefijo + "Argumento (Constante " + tipoArgumento + ": " + valor.toString() + ")");
        }
    }
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        // El argumento es una expresión (constante o variable)
        if ("id".equals(tipoArgumento)) {
            // Es una variable, necesitamos buscarla
            AtributosTokens attrs = TdA.buscar((String)valor);
            if (attrs != null) {
                return G.getNombreAsm(attrs.getMangledName());
            }
            return G.getNombreAsm((String)valor); // Fallback
        } else {
            // Es una constante
            NodoConstante cte = new NodoConstante(this.valor, this.tipoArgumento);
            return cte.generarCodigo(G, TdA);
        }
    }
}