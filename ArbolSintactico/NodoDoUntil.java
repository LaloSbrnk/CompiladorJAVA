package ArbolSintactico;
import java.util.HashMap;
import CompiladoresMain.TablaDeAmbitos;
import CompiladoresMain.GeneradorAssembler; 

public class NodoDoUntil extends Nodo {
    private NodoBloque bloque;
    private Nodo condicion;
    
    public NodoDoUntil(NodoBloque bloque, Nodo condicion) {
        this.bloque = bloque;
        this.condicion = condicion;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        
        bloque.chequear(TdA); 
        condicion.chequear(TdA);
        return "void"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        
        System.out.println(prefijo + "Sentencia DO-UNTIL");
        System.out.println(prefijo + "  " + "Cuerpo DO:");
        bloque.imprimir(prefijo + "    ");
        System.out.println(prefijo + "  " + "Condicion UNTIL:");
        condicion.imprimir(prefijo + "    ");
    }

    
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        
        
        
        
        String labelStart = G.getNuevoLabel();
        G.agregarCodigo(labelStart + ":");
        
        
        bloque.generarCodigo(G, TdA);
        
        
        String jumpInstruction = condicion.generarCodigo(G, TdA);
        
        
        G.agregarCodigo(jumpInstruction + " " + labelStart);
        
        
        
        
        return null;
    }
}
