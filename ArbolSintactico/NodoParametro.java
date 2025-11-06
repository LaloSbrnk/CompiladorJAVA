package ArbolSintactico;
import CompiladoresMain.*;
import java.util.HashMap; 
public class NodoParametro extends Nodo {
    private String nombre;
    private String tipo;
    private String modoPasaje; 
    public NodoParametro(String nombre, String tipo, String modoPasaje) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.modoPasaje = modoPasaje;
    }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getModoPasaje() { return modoPasaje; }
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        AtributosTokens attrs = new AtributosTokens(TiposToken.IDENTIFICADOR); 
        attrs.setTipoDato(this.tipo);
        attrs.setUso("parametro");
        attrs.setModoPasaje(this.modoPasaje); 
        if (!TdA.agregar(this.nombre, attrs)) {
            System.err.println("ERROR Semantico: Redeclaracion de parametro '" + this.nombre + "'.");
            return "error";
        } else {
        }
        return this.tipo; 
    }
    @Override
    public void imprimir(String prefijo) {
        String modo = (modoPasaje != null) ? " (" + modoPasaje + ")" : "";
        System.out.println(prefijo + "Parametro: " + nombre + " (Tipo: " + tipo + modo + ")");
    }
}