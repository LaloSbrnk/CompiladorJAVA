package CompiladoresMain;

import java.util.HashMap;
import java.util.Stack;

public class TablaDeAmbitos {
    
    // Una Pila de HashMaps. Cada HashMap es un ámbito.
    private Stack<HashMap<String, AtributosTokens>> pila;
    
    // Guardamos una referencia a la TS original (global)
    private HashMap<String, AtributosTokens> tablaGlobal;

    public TablaDeAmbitos(HashMap<String, AtributosTokens> tablaSimbolosGlobal) {
        this.pila = new Stack<>();
        this.tablaGlobal = tablaSimbolosGlobal;
    }

    /**
     * Inicia el chequeo semántico abriendo el Ámbito Global.
     * Carga la TS precargada (operadores, palabras, etc.) en este ámbito.
     */
    public void abrirAmbitoGlobal() {
        this.pila.push(this.tablaGlobal);
    }

    /**
     * Abre un nuevo ámbito (ej. al entrar a una función o un bloque)
     * Apila un HashMap vacío.
     */
    public void abrirAmbito() {
        this.pila.push(new HashMap<String, AtributosTokens>());
    }

    /**
     * Cierra el ámbito actual (ej. al salir de una función o bloque)
     * Desapila el HashMap superior.
     */
    public void cerrarAmbito() {
        if (!pila.isEmpty()) {
            this.pila.pop();
        }
    }

    /**
     * Agrega un símbolo (variable, parámetro) AL ÁMBITO ACTUAL (el de arriba).
     * Primero chequea si ya existe en este ámbito para detectar redeclaraciones.
     */
    public boolean agregar(String nombre, AtributosTokens atributos) {
        if (pila.isEmpty()) {
            return false; // No debería pasar
        }
        
        // Chequea si ya existe EN EL AMBITO ACTUAL
        if (this.pila.peek().containsKey(nombre)) {
            //Ya existe en este mabito.
            return false; 
        }
        
        // No existe, lo agregamos al ambito actual (el 'peek')
        this.pila.peek().put(nombre, atributos);
        return true;
    }

    /**
     * Busca un símbolo.
     * Esta es la lógica clave de "scopes":
     * Busca de adentro hacia afuera (del tope de la pila al fondo).
     */
    public AtributosTokens buscar(String nombre) {
        if (pila.isEmpty()) {
            return null;
        }

        // Itera la pila desde el tope (actual) hacia el fondo (global)
        for (int i = pila.size() - 1; i >= 0; i--) {
            HashMap<String, AtributosTokens> ambito = pila.get(i);
            if (ambito.containsKey(nombre)) {
                return ambito.get(nombre); 
            }
        }

        return null; // No se encontró en ningún ámbito
    }

    
    // Devuelve el HashMap del ambito actual (tope de la pila).
     
    public HashMap<String, AtributosTokens> getAmbitoActual() {
        if (pila.isEmpty()) {
            return null; 
        }
        return pila.peek();
    }
}