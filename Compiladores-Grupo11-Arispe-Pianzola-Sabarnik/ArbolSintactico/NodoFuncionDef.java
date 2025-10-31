package ArbolSintactico;

import CompiladoresMain.*;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List; 

public class NodoFuncionDef extends Nodo {
    private String nombre;
    private List<String> tiposRetorno; // Para Tema 20
    private ArrayList<NodoParametro> parametros; 
    private NodoBloque cuerpo;

    // Guardaremos una referencia a los atributos de la función en la TS
    private AtributosTokens atributosFuncion;

    public NodoFuncionDef(String nombre, List<String> tiposRetorno, ArrayList<NodoParametro> parametros, NodoBloque cuerpo, AtributosTokens attrs) {
        this.nombre = nombre;
        this.tiposRetorno = tiposRetorno;
        this.parametros = parametros;
        this.cuerpo = cuerpo;
        this.atributosFuncion = attrs; // Guardamos la referencia
    }

    public List<String> getTiposRetorno() {
        return tiposRetorno;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setCuerpo(NodoBloque cuerpo) {
        this.cuerpo = cuerpo;
    }
    
@Override
    public String chequear(TablaDeAmbitos TdA) {
        System.out.println("DEBUG: Chequeando funcion '" + this.nombre + "'");

        // 1. La función en sí misma se declara en el ámbito *actual* (padre)
        String mangledName = this.nombre + TdA.getMangledScope(); // Ej: "F1:MAIN"
        this.atributosFuncion.setMangledName(mangledName);
        
        // (El parser ya puso 'atributosFuncion' en la tabla global con la clave simple,
        // ahora lo actualizamos con la clave mangled y los atributos correctos)
        AnalizadorLexico.tablaSimbolos.put(mangledName, this.atributosFuncion);


        // --- MANEJO DE AMBITO ---
        // 2. Abrimos un nuevo ambito para la funcion
        TdA.abrirAmbito(this.nombre); // Pila -> [..., "F1"]
        
        // 3. Agregamos los parametros al nuevo ambito (ej: "P1:MAIN:F1")
        if (parametros != null) {
            for (NodoParametro p : parametros) {
                p.chequear(TdA); 
            }
        }

        // 4. Chequeamos el cuerpo de la funcion DENTRO del nuevo ambito
        cuerpo.chequear(TdA);

        // 5. Cerramos el ámbito
        TdA.cerrarAmbito(); // Pila -> [...]
        // --- FIN MANEJO DE AMBITO ---

        System.out.println("DEBUG: Fin chequeo funcion '" + this.nombre + "'");
        return "void";
    }
}