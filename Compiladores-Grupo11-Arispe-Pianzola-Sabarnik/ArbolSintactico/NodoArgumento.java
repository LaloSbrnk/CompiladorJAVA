package ArbolSintactico;

import CompiladoresMain.*;
import java.util.HashMap;

public class NodoArgumento extends Nodo {
    private Object valor; // Puede ser String (nombre ID) o Numero (valor CTE)
    private String tipoArgumento; // "id", "long", "dfloat"

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
        // Chequeamos el argumento en si mismo
        if (tipoArgumento.equals("id")) {
            // Si es un ID, buscamos su tipo
            AtributosTokens attrs = TdA.buscar((String)valor);
            if (attrs == null || attrs.getUso() == null) {
                System.err.println("ERROR Semantico: Variable '" + valor + "' usada como argumento lambda no declarada.");
                return "error";
            }
            return attrs.getTipoDato();
        } else {
            // Si es CTE_LONG o CTE_DFLOAT, el tipo ya lo sabemos
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
}