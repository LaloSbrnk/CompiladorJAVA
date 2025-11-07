package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; // <--- NUEVO

public class NodoDoUntil extends Nodo {
    private NodoBloque bloque;
    private Nodo condicion;
    
    public NodoDoUntil(NodoBloque bloque, Nodo condicion) {
        this.bloque = bloque;
        this.condicion = condicion;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // ... (código de chequeo existente sin cambios) ...
        bloque.chequear(TdA); 
        condicion.chequear(TdA);
        return "void"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
        System.out.println(prefijo + "Sentencia DO-UNTIL");
        System.out.println(prefijo + "  " + "Cuerpo DO:");
        bloque.imprimir(prefijo + "    ");
        System.out.println(prefijo + "  " + "Condicion UNTIL:");
        condicion.imprimir(prefijo + "    ");
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        // do { ... } until (cond);
        // Se ejecuta hasta que la condición sea VERDADERA.
        // Se repite si la condición es FALSA.
        
        // 1. Etiqueta de inicio del bucle
        String labelStart = G.getNuevoLabel();
        G.agregarCodigo(labelStart + ":");
        
        // 2. Generar código del bloque
        bloque.generarCodigo(G, TdA);
        
        // 3. Generar código de la condición
        String jumpInstruction = condicion.generarCodigo(G, TdA);
        
        // 4. Saltar al inicio si la condición es FALSA
        G.agregarCodigo(jumpInstruction + " " + labelStart);
        
        // (La filmina de WHILE [cite: 879] muestra un JMP a la etiqueta inicial,
        // que es lo que hacemos si la condición de *salida* no se cumple)
        
        return null;
    }
}