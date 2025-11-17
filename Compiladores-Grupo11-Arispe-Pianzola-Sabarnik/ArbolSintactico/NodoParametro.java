package ArbolSintactico;
import CompiladoresMain.*;
import java.util.HashMap; 

public class NodoParametro extends Nodo {
    private String nombre;
    private String tipo;
    private String modoPasaje; 
    private AtributosTokens atributos = null; // <--- CORRECCIÓN (Cachear atributos)
    
    public NodoParametro(String nombre, String tipo, String modoPasaje) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.modoPasaje = modoPasaje;
    }
    
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getModoPasaje() { return modoPasaje; }

    // <--- CORRECCIÓN (Getter para los atributos) ---
    public AtributosTokens getAtributos() {
        return this.atributos;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // <--- CORRECCIÓN (Usar el campo de la clase 'this.atributos') ---
        this.atributos = new AtributosTokens(TiposToken.IDENTIFICADOR); 
        this.atributos.setTipoDato(this.tipo);
        this.atributos.setUso("parametro");
        this.atributos.setModoPasaje(this.modoPasaje); 
        
        if (!TdA.agregar(this.nombre, this.atributos)) { // Usar this.atributos
            System.err.println("ERROR Semantico: Redeclaracion de parametro '" + this.nombre + "'.");
            return "error";
        } else {
            // (No hacer nada)
        }
        return this.tipo; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        String modo = (modoPasaje != null) ? " (" + modoPasaje + ")" : "";
        System.out.println(prefijo + "Parametro: " + nombre + " (Tipo: " + tipo + modo + ")");
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        // No genera código ejecutable por sí mismo
        return null; 
    }
}