package ArbolSintactico;
import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List; 

public class NodoFuncionDef extends Nodo {
    private String nombre;
    private List<String> tiposRetorno; 
    private ArrayList<NodoParametro> parametros; 
    private NodoBloque cuerpo;
    private AtributosTokens atributosFuncion;
    
    public NodoFuncionDef(String nombre, List<String> tiposRetorno, ArrayList<NodoParametro> parametros, NodoBloque cuerpo, AtributosTokens attrs) {
        this.nombre = nombre;
        this.tiposRetorno = tiposRetorno;
        this.parametros = parametros;
        this.cuerpo = cuerpo;
        this.atributosFuncion = attrs; 
    }
    
    public List<String> getTiposRetorno() {
        return tiposRetorno;
    }
    
    public String getNombre(){
        return this.nombre;
    }

    public AtributosTokens getAtributos() { // <--- NUEVO
        return this.atributosFuncion;
    }
    
    public void setCuerpo(NodoBloque cuerpo) {
        this.cuerpo = cuerpo;
    }
    
    @Override
    public String chequear(TablaDeAmbitos TdA) {
        String mangledName = this.nombre + TdA.getMangledScope(); 
        this.atributosFuncion.setMangledName(mangledName);
        AnalizadorLexico.tablaSimbolos.put(mangledName, this.atributosFuncion);
        
        TdA.abrirAmbito(this.nombre); 
        if (parametros != null) {
            for (NodoParametro p : parametros) {
                p.chequear(TdA); 
            }
        }
        cuerpo.chequear(TdA);
        TdA.cerrarAmbito(); 
        return "void";
    }
    
    @Override
    public void imprimir(String prefijo) {
        // ... (código de imprimir existente sin cambios) ...
        String retornos = tiposRetorno.toString();
        System.out.println(prefijo + "Definicion Funcion: " + nombre + " (Retorna: " + retornos + ")");
        if (parametros != null && !parametros.isEmpty()) {
            System.out.println(prefijo + "  " + "Parametros Formales:");
            for (NodoParametro p : parametros) {
                p.imprimir(prefijo + "    ");
            }
        }
        System.out.println(prefijo + "  " + "Cuerpo:");
        cuerpo.imprimir(prefijo + "    ");
    }

    // --- NUEVO METODO ---
    @Override
    public String generarCodigo(GeneradorAssembler G, TablaDeAmbitos TdA) {
        String nombreProc = G.getNombreAsm(atributosFuncion.getMangledName());
        
        G.agregarCodigo("\n; --- Definicion de Funcion: " + this.nombre + " ---");
        G.agregarCodigo(nombreProc + " PROC");
        
        // Stack setup (para printf) [cite: 2399-2403]
        G.agregarCodigo("push ebp");
        G.agregarCodigo("mov ebp, esp");
        G.agregarCodigo("push edi");
        G.agregarCodigo("push esi");

        // Abrir ámbito para la generación del cuerpo
        TdA.abrirAmbito(this.nombre);
        cuerpo.generarCodigo(G, TdA);
        TdA.cerrarAmbito();

        // Stack teardown (antes de RET) [cite: 2404-2407]
        G.agregarCodigo(nombreProc + "_exit:"); // Etiqueta para el RET
        G.agregarCodigo("pop esi");
        G.agregarCodigo("pop edi");
        G.agregarCodigo("mov esp, ebp");
        G.agregarCodigo("pop ebp");
        G.agregarCodigo("RET");
        
        G.agregarCodigo(nombreProc + " ENDP");
        G.agregarCodigo("; --- Fin de Funcion: " + this.nombre + " ---\n");
        
        return null;
    }
}