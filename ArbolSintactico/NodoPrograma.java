package ArbolSintactico;
import CompiladoresMain.*; 
import java.util.ArrayList; // <--- NUEVO
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
        // TdA.cerrarAmbito(); // No cerramos el global
        return "void"; 
    }
    
    @Override
    public void imprimir(String prefijo) {
        System.out.println(prefijo + "Raiz (Programa: " + nombrePrograma + ")");
        bloquePrincipal.imprimir(prefijo + "  ");
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        
        // 1. Declarar todas las variables y constantes en .DATA
        G.declararTablaDeSimbolos(AnalizadorLexico.tablaSimbolos);
        
        // 2. Separar funciones de sentencias main
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

        // 3. Generar el código de las funciones (PROCs)
        for (NodoFuncionDef f : funciones) {
            f.generarCodigo(G, TdA);
        }

        // 4. Iniciar el código principal (START:)
        G.iniciarCodigo();

        // 5. Generar el código de las sentencias main
        for (Nodo s : sentenciasMain) {
            s.generarCodigo(G, TdA);
        }

        // 6. Finalizar el programa (rutinas de error, END START)
        G.finalizarPrograma();
        
        return null;
    }
}