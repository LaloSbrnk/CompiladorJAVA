package CompiladoresMain;

public class AtributosTokens {
    private int cantidad;
    private int idToken;
    private String uso = null;
    private String tipoDato = null;
    private Object valor = null;
    private String nombre_var = null;

    public AtributosTokens(int idToken) {
        this.cantidad = 0;
        this.idToken = idToken;
    }
    
    public AtributosTokens(int cantidad, int idToken) {
        this.cantidad = cantidad;
        this.idToken = idToken;
    }

//manejo de cantidad por las dudas
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
//manejo de cantidad por las dudas

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
