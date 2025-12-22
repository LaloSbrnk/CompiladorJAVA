package ArbolSintactico;
import CompiladoresMain.*;
import java.util.HashMap; 

public class NodoParametro extends Nodo {
    private String nombre;
    private String tipo;
    private String modoPasaje; 
    private AtributosTokens atributos = null; 
    
    public NodoParametro(String nombre, String tipo, String modoPasaje) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.modoPasaje = modoPasaje;
    }
    
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getModoPasaje() { return modoPasaje; }

    
    public AtributosTokens getAtributos() {
        return this.atributos;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        
        this.atributos = new AtributosTokens(TiposToken.IDENTIFICADOR); 
        this.atributos.setTipoDato(this.tipo);
        this.atributos.setUso("parametro");
        this.atributos.setModoPasaje(this.modoPasaje); 
        
        if (!TdA.agregar(this.nombre, this.atributos)) { 
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

    
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        return null; 
    }
}
