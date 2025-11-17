package ArbolSintactico;
import CompiladoresMain.AnalizadorLexico;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; // <--- NUEVO
import java.util.HashMap; 

public abstract class Nodo {
    
    public abstract String chequear(TablaDeAmbitos TdA); 
    
    public abstract void imprimir(String prefijo);

    /**
     * Genera el código Assembler para este nodo.
     * @param G El generador de código.
     * @return El nombre de la variable (o aux) donde quedó el resultado,
     * o null si es una sentencia que no devuelve valor.
     */
    public abstract String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA); 
}