package CompiladoresMain;

import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Tabla de Ámbitos (Implementación con Name Mangling).
 * Esta clase ya NO almacena los símbolos. Solo gestiona la pila
 * de nombres de ámbitos para construir los nombres "mangled".
 * La tabla de símbolos real y única es AnalizadorLexico.tablaSimbolos.
 */
public class TablaDeAmbitos {
    
    // La pila no guarda HashMaps, sino los NOMBRES de los ámbitos.
    private Stack<String> pilaAmbitos;
    
    // Referencia a la ÚNICA tabla de símbolos (la global).
    private HashMap<String, AtributosTokens> tablaDeSimbolos;

    public TablaDeAmbitos(HashMap<String, AtributosTokens> tablaSimbolosGlobal) {
        this.pilaAmbitos = new Stack<>();
        // Asigna la tabla global estática a esta instancia.
        this.tablaDeSimbolos = tablaSimbolosGlobal; 
    }

    /**
     * Abre un nuevo ámbito (ej. al entrar a una función o programa)
     * Apila el nombre del nuevo ámbito.
     */
    public void abrirAmbito(String nombreAmbito) {
        this.pilaAmbitos.push(nombreAmbito); // Ej: "MAIN", luego "F1"
    }

    /**
     * Cierra el ámbito actual (ej. al salir de una función o bloque)
     * Desapila el nombre del ámbito.
     */
    public void cerrarAmbito() {
        if (!pilaAmbitos.isEmpty()) {
            this.pilaAmbitos.pop();
        }
    }

    /**
     * Construye el sufijo de "name mangling" actual.
     * Ej: Si la pila es ["MAIN", "F1"], devuelve ":MAIN:F1"
     */
    public String getMangledScope() {
        if (pilaAmbitos.isEmpty()) {
            return ""; 
        }
        
        StringBuilder mangled = new StringBuilder();
        for (String scope : pilaAmbitos) {
            mangled.append(":").append(scope);
        }
        return mangled.toString(); // Ej: ":MAIN:F1"
    }

    /**
     * Agrega un símbolo (variable, parámetro) AL ÁMBITO ACTUAL.
     * Construye el nombre "mangled" y lo usa como clave en la tabla general.
     */
    public boolean agregar(String nombreSimple, AtributosTokens atributos) {
        String mangledName = nombreSimple + this.getMangledScope();
        
        // Chequea si ya existe ESE nombre EN ESE ambito
        if (this.tablaDeSimbolos.containsKey(mangledName)) {
            // Ya existe en este ambito.
            return false; 
        }
        
        // No existe, lo agregamos a la tabla general con su nombre completo
        this.tablaDeSimbolos.put(mangledName, atributos);
        atributos.setMangledName(mangledName); // Guardamos el nombre mangled para referencia
        return true;
    }

    /**
     * Busca un símbolo (sin prefijo) caminando la pila de ámbitos. [cite: 1175]
     * Busca de adentro hacia afuera (del tope de la pila al fondo).
     */
    public AtributosTokens buscar(String nombreSimple) {
        if (pilaAmbitos.isEmpty()) {
            return null;
        }

        // 1. Construir la lista de sufijos a probar, de más específico a más general
        // Ej: si la pila es ["MAIN", "F1", "AAA"],
        // probaremos ":MAIN:F1:AAA", luego ":MAIN:F1", luego ":MAIN"
        List<String> sufijos = new ArrayList<>();
        StringBuilder sufijoActual = new StringBuilder();
        for(String scope : pilaAmbitos) {
            sufijoActual.append(":").append(scope);
            sufijos.add(sufijoActual.toString());
        }
        // Invertimos para que la búsqueda sea de adentro hacia afuera
        Collections.reverse(sufijos); // Ahora es [":MAIN:F1:AAA", ":MAIN:F1", ":MAIN"]

        // 2. Probar cada sufijo
        for (String sufijo : sufijos) {
            String mangledName = nombreSimple + sufijo;
            if (this.tablaDeSimbolos.containsKey(mangledName)) {
                return this.tablaDeSimbolos.get(mangledName); // ¡Encontrado!
            }
        }

        return null; // No se encontró en ningún ámbito
    }

    /**
     * Busca un símbolo prefijado (Tema 23). [cite: 1176]
     * Ej: "F1.X"
     * Esto ignora el scope actual y busca en el scope del prefijo.
     */


    public AtributosTokens buscarPrefijado(String nombrePrefijado) {
        String[] partes = nombrePrefijado.split("\\.", 2); 
        String nombreModulo = partes[0]; 
        String nombreVarLocal = partes[1]; 

        // 1. Buscar el Módulo/Función (F1) en el scope actual (caminando la pila)
        AtributosTokens attrsModulo = this.buscar(nombreModulo);

        if (attrsModulo == null || !attrsModulo.getUso().equals("funcion") || attrsModulo.getMangledName() == null) {
            System.err.println("ERROR Semantico: El prefijo '" + nombreModulo + "' no corresponde a una funcion declarada o accesible.");
            return null;
        }

        // 2. Construir el nombre "mangled" de la variable que buscamos
        // El nombre mangled de la función es (ej: "F1:MAIN")
        // El scope *dentro* de la función es (ej: ":MAIN:F1")
        // Obtenemos el scope de la función a partir de su propio nombre mangled
        String mangledScopeDeLaFuncion = attrsModulo.getMangledName().substring(nombreModulo.length()) + ":" + nombreModulo; // Ej: ":MAIN:F1"
        String mangledVarName = nombreVarLocal + mangledScopeDeLaFuncion; // Ej: "X:MAIN:F1"
        
        AtributosTokens attrsVar = this.tablaDeSimbolos.get(mangledVarName);

        if (attrsVar == null || attrsVar.getUso() == null) {
             System.err.println("ERROR Semantico: La funcion '" + nombreModulo + "' no contiene una variable o parametro llamado '" + nombreVarLocal + "'.");
            return null;
        }
        
        return attrsVar;
    }
    /**
     * Obtiene el nombre del ámbito raíz (el fondo de la pila).
     */
    public String getRootScopeName() {
        if (pilaAmbitos.isEmpty()) {
            return null;
        }
        return pilaAmbitos.get(0); // Devuelve el primer elemento (ej: "MAIN")
    }
}