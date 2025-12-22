package ArbolSintactico;
import CompiladoresMain.*; 
import java.util.ArrayList; 
import java.util.HashMap;

public class NodoPrograma extends Nodo {
    private String nombrePrograma; 
    private NodoBloque bloquePrincipal;
    
    public NodoPrograma(String nombre, NodoBloque bloque) {
        this.nombrePrograma = nombre;
        this.bloquePrincipal = bloque;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        TdA.abrirAmbito(this.nombrePrograma); 
        bloquePrincipal.chequear(TdA);
        
        return "void"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Raiz (Programa: " + nombrePrograma + ")");
        bloquePrincipal.imprimir(prefijo + "  ");
    }

    
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        
        G.declararTablaDeSimbolos(AnalizadorLexico.tablaSimbolos);
        
        
        ArrayList<Nodo> sentenciasMain = new ArrayList<>();
        ArrayList<NodoFuncionDef> funciones = new ArrayList<>();
        
        if (this.bloquePrincipal != null) {
            for (Nodo s : bloquePrincipal.getSentencias()) {
                if (s instanceof NodoFuncionDef) {
                    funciones.add((NodoFuncionDef) s);
                } else {
                    sentenciasMain.add(s);
                }
            }
        }

        
        G.iniciarCodigo();

        
        for (NodoFuncionDef f : funciones) {
            f.generarCodigo(G, TdA);
        }

        
        G.iniciarPuntoEntrada(); 

        
        for (Nodo s : sentenciasMain) {
            s.generarCodigo(G, TdA);
        }

        
        G.finalizarPrograma();
        
        return null;
    }
}
