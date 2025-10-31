package CompiladoresMain;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import ArbolSintactico.NodoParametro;

public class AtributosTokens {
    private int cantidad;
    private int idToken;
    private String uso = null;
    private String tipoDato = null;
    private Object valor = null;
    private String nombre_var = null;
    private List<String> tiposRetorno = null; // Para funciones (Tema 20)
    private ArrayList<NodoParametro> parametros = null; // Para funciones
    private HashMap<String, AtributosTokens> ambitoLocal = null;

    public AtributosTokens(int idToken) {
        this.cantidad = 0;
        this.idToken = idToken;
    }
    
    public AtributosTokens(int cantidad, int idToken) {
        this.cantidad = cantidad;
        this.idToken = idToken;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void incrementarCantidad() {
        this.cantidad++;
    }

    public void decrementarCantidad() {this.cantidad--; }

    public void setNombre_var(String nombre_var) {
        this.nombre_var = nombre_var;
    }

    public int getIdToken() {
        return idToken;
    }

    public void setIdToken(int idToken) {
        this.idToken = idToken;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public List<String> getTiposRetorno() {
        return tiposRetorno;
    }

    public void setTiposRetorno(List<String> tiposRetorno) {
        this.tiposRetorno = tiposRetorno;
    }

    public ArrayList<NodoParametro> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<NodoParametro> parametros) {
        this.parametros = parametros;
    }

    public HashMap<String, AtributosTokens> getAmbitoLocal() {
        return ambitoLocal;
    }

    public void setAmbitoLocal(HashMap<String, AtributosTokens> ambitoLocal) {
        this.ambitoLocal = ambitoLocal;
    }
    
    @Override
    public String toString() {
        String impresion = "";
        if (uso != null) {
            impresion += "Uso: " + uso;
        }
        if (tipoDato != null) {
            impresion += ", Tipo de Dato: " + tipoDato;
        }
        if (valor != null) {
            impresion += ", Valor: " + valor.toString();
        }
        return impresion;
    }
}
