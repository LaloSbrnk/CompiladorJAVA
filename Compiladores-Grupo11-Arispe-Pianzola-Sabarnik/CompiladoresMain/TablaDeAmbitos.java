package CompiladoresMain;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
public class TablaDeAmbitos {
    private Stack<String> pilaAmbitos;
    private HashMap<String, AtributosTokens> tablaDeSimbolos;
    public TablaDeAmbitos(HashMap<String, AtributosTokens> tablaSimbolosGlobal) {
        this.pilaAmbitos = new Stack<>();
        this.tablaDeSimbolos = tablaSimbolosGlobal; 
    }
    public void abrirAmbito(String nombreAmbito) {
        this.pilaAmbitos.push(nombreAmbito); 
    }
    public void cerrarAmbito() {
        if (!pilaAmbitos.isEmpty()) {
            this.pilaAmbitos.pop();
        }
    }
    public String getMangledScope() {
        if (pilaAmbitos.isEmpty()) {
            return ""; 
        }
        StringBuilder mangled = new StringBuilder();
        for (String scope : pilaAmbitos) {
            mangled.append(":").append(scope);
        }
        return mangled.toString(); 
    }
    public boolean agregar(String nombreSimple, AtributosTokens atributos) {
        String mangledName = nombreSimple + this.getMangledScope();
        if (this.tablaDeSimbolos.containsKey(mangledName)) {
            return false; 
        }
        this.tablaDeSimbolos.put(mangledName, atributos);
        atributos.setMangledName(mangledName); 
        return true;
    }
    public AtributosTokens buscar(String nombreSimple) {
        if (pilaAmbitos.isEmpty()) {
            return null;
        }
        List<String> sufijos = new ArrayList<>();
        StringBuilder sufijoActual = new StringBuilder();
        for(String scope : pilaAmbitos) {
            sufijoActual.append(":").append(scope);
            sufijos.add(sufijoActual.toString());
        }
        Collections.reverse(sufijos); 
        for (String sufijo : sufijos) {
            String mangledName = nombreSimple + sufijo;
            if (this.tablaDeSimbolos.containsKey(mangledName)) {
                return this.tablaDeSimbolos.get(mangledName); 
            }
        }
        return null; 
    }
    public AtributosTokens buscarPrefijado(String nombrePrefijado) {
        String[] partes = nombrePrefijado.split("\\.", 2); 
        String nombreModulo = partes[0]; 
        String nombreVarLocal = partes[1]; 
        AtributosTokens attrsModulo = this.buscar(nombreModulo);
        if (attrsModulo == null || !attrsModulo.getUso().equals("funcion") || attrsModulo.getMangledName() == null) {
            System.err.println("ERROR Semantico: El prefijo '" + nombreModulo + "' no corresponde a una funcion declarada o accesible.");
            return null;
        }
        String mangledScopeDeLaFuncion = attrsModulo.getMangledName().substring(nombreModulo.length()) + ":" + nombreModulo; 
        String mangledVarName = nombreVarLocal + mangledScopeDeLaFuncion; 
        AtributosTokens attrsVar = this.tablaDeSimbolos.get(mangledVarName);
        if (attrsVar == null || attrsVar.getUso() == null) {
             System.err.println("ERROR Semantico: La funcion '" + nombreModulo + "' no contiene una variable o parametro llamado '" + nombreVarLocal + "'.");
            return null;
        }
        return attrsVar;
    }
    public String getRootScopeName() {
        if (pilaAmbitos.isEmpty()) {
            return null;
        }
        return pilaAmbitos.get(0); 
    }
}