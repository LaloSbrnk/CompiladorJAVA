package ArbolSintactico;
import CompiladoresMain.*; 
import java.util.HashMap;

public class NodoVariable extends Nodo {
    private String nombre;

    public NodoVariable(String nombre) {
        this.nombre = nombre;
    }

@Override
    public String chequear(TablaDeAmbitos TdA) {
        
        AtributosTokens attrs = null;

        if (this.nombre.contains(".")) {
            // --- Caso 1: Variable con prefijo (Tema 23) ---
            String[] partes = this.nombre.split("\\.", 2); 
            String nombreModulo = partes[0]; 
            String nombreVarLocal = partes[1];
            String rootScope = TdA.getRootScopeName();

            if (rootScope != null && nombreModulo.equals(rootScope)) {
                // --- Caso 1a: Prefijo es el Root Scope (ej: "MAIN.A") ---
                // Construimos el nombre mangled global: "A:MAIN"
                String mangledName = nombreVarLocal + ":" + rootScope;
                
                // Buscamos directamente en la tabla de símbolos global
                attrs = AnalizadorLexico.tablaSimbolos.get(mangledName);
                
                if (attrs == null || attrs.getUso() == null) {
                     System.err.println("ERROR Semantico: Variable '" + nombreVarLocal + "' no declarada en el ambito global '" + rootScope + "'.");
                    return "error";
                }

            } else {
                // --- Caso 1b: Prefijo es una funcion (ej: "F1.X") ---
                // Usamos la lógica de búsqueda prefijada estándar
                attrs = TdA.buscarPrefijado(this.nombre);
                
                if (attrs == null) {
                    // El error ya fue reportado por buscarPrefijado
                    return "error";
                }
            }

        } else {
            // --- Caso 2: Variable sin prefijo (ej: "A" dentro de la función) ---
            // Busca "nombre" caminando la pila de ámbitos (de adentro hacia afuera).
            attrs = TdA.buscar(this.nombre);

            if (attrs == null || attrs.getUso() == null) {
                System.err.println("ERROR Semantico: Variable '" + this.nombre + "' no declarada en este ambito o ambitos superiores.");
                return "error";
            }
        }
            
        // Devolvemos el tipo de la variable encontrada
        return attrs.getTipoDato();
    }

    public String getNombre(){
        return this.nombre;
    }
}