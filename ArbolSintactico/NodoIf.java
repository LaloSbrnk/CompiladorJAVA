package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; // <--- NUEVO

public class NodoIf extends Nodo {
    private Nodo condicion;
    private NodoBloque bloqueTrue;
    private NodoBloque bloqueFalse; 
    
    public NodoIf(Nodo condicion, NodoBloque bloqueTrue, NodoBloque bloqueFalse) {
        this.condicion = condicion;
        this.bloqueTrue = bloqueTrue;
        this.bloqueFalse = bloqueFalse;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // ... (código de chequeo existente sin cambios) ...
        condicion.chequear(TdA);
        bloqueTrue.chequear(TdA); 
        if (bloqueFalse != null) {
            bloqueFalse.chequear(TdA); 
        }
        return "void"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
        System.out.println(prefijo + "Sentencia IF");
        System.out.println(prefijo + "  " + "Condicion:");
        condicion.imprimir(prefijo + "    ");
        System.out.println(prefijo + "  " + "Bloque THEN:");
        bloqueTrue.imprimir(prefijo + "    ");
        if (bloqueFalse != null) {
            System.out.println(prefijo + "  " + "Bloque ELSE:");
            bloqueFalse.imprimir(prefijo + "    ");
        }
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        // (Basado en la filmina [cite: 30, 75])
        
        String labelElse = G.getNuevoLabel();
        
        // 1. Generar código de la condición
        String jumpInstruction = condicion.generarCodigo(G, TdA);
        
        // 2. Salto al ELSE si la condición es FALSA
        G.agregarCodigo(jumpInstruction + " " + labelElse); // [cite: 40, 303]
        
        // 3. Generar código del bloque THEN
        bloqueTrue.generarCodigo(G, TdA);
        
        if (bloqueFalse != null) {
            // Si hay ELSE, saltamos al final
            String labelEndIf = G.getNuevoLabel();
            G.agregarCodigo("JMP " + labelEndIf); // [cite: 44, 318]
            
            // 4. Marca del ELSE
            G.agregarCodigo(labelElse + ":"); // [cite: 44]
            
            // 5. Generar código del bloque ELSE
            bloqueFalse.generarCodigo(G, TdA);
            
            // 6. Marca del ENDIF
            G.agregarCodigo(labelEndIf + ":"); // [cite: 48]
        } else {
            // 4. Si no hay ELSE, la etiqueta es el final
            G.agregarCodigo(labelElse + ":");
        }
        
        return null;
    }
}