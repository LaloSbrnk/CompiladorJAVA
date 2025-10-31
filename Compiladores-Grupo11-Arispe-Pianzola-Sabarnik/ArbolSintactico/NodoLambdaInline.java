package ArbolSintactico;

import CompiladoresMain.*;
import java.util.HashMap;

public class NodoLambdaInline extends Nodo {
    private String nombreParam;
    private String tipoParam;
    private NodoBloque cuerpo;
    private NodoArgumento argumento;

    public NodoLambdaInline(String tipoParam, String nombreParam, NodoBloque cuerpo, NodoArgumento argumento) {
        this.tipoParam = tipoParam;
        this.nombreParam = nombreParam;
        this.cuerpo = cuerpo;
        this.argumento = argumento;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        System.out.println("DEBUG: Chequeando Lambda Inline");

        // Chequeamos el tipo del argumento que se le PASA
        String tipoArgumentoReal = argumento.chequear(TdA);
        if (tipoArgumentoReal.equals("error")) {
            return "error"; 
        }

        // Chequeamos compatibilidad entre argumento real y parametro formal
        //    (Permitimos long -> dfloat, pero no al reves)
        if (tipoParam.equals("long") && tipoArgumentoReal.equals("dfloat")) {
             System.err.println("ERROR Semantico: Lambda esperaba argumento tipo '" + tipoParam +
                                "' pero recibio '" + tipoArgumentoReal + "' (posible perdida de datos).");
             return "error";
        }
        // Si tipos son distintos pero es dfloat=long, OK. Si son iguales, OK.
        // Cualquier otra diferencia es error.
        if (!tipoParam.equals(tipoArgumentoReal) && !(tipoParam.equals("dfloat") && tipoArgumentoReal.equals("long"))) {
             System.err.println("ERROR Semantico: Lambda esperaba argumento tipo '" + tipoParam +
                                "' pero recibio '" + tipoArgumentoReal + "'.");
             return "error";
        }


        // --- SIMULACION DE EJECUCION CON AMBITO ---
        // Abrimos un ambito TEMPORAL para la lambda
        TdA.abrirAmbito();

        // Declaramos el parametro DENTRO de ese ambito temporal
        AtributosTokens attrsParam = new AtributosTokens(TiposToken.IDENTIFICADOR);
        attrsParam.setTipoDato(this.tipoParam);
        attrsParam.setUso("parametro_lambda");
        if (!TdA.agregar(this.nombreParam, attrsParam)) {
            // Esto no deberia pasar en un ambito nuevo, pero por si acaso
            System.err.println("ERROR Interno: No se pudo agregar el parametro lambda '" + this.nombreParam + "'.");
            TdA.cerrarAmbito(); 
            return "error";
        } else {
             System.out.println("DEBUG: Lambda: Registrado parametro '" + this.nombreParam + "' tipo '" + this.tipoParam + "' en ambito temporal.");
        }


        cuerpo.chequear(TdA);

        TdA.cerrarAmbito();
        // --- FIN SIMULACION ---

        System.out.println("DEBUG: Fin chequeo Lambda Inline.");
        return "void"; 
    }
}