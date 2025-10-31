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

        // --- MANEJO DE AMBITO ---
        // Abrimos un nuevo ambito para la funcion
        TdA.abrirAmbito();
        
        HashMap<String, AtributosTokens> ambitoFuncion = TdA.getAmbitoActual();
        if (this.atributosFuncion != null) { // Si la funcion se registro bien
            this.atributosFuncion.setAmbitoLocal(ambitoFuncion);
        }

        //Agregamos los parametros al nuevo ambito 
        if (parametros != null) {
            for (NodoParametro p : parametros) {
                p.chequear(TdA); 
            }
        }

        // Chequeamos el cuerpo de la funcion DENTRO del nuevo ambito
        //    (Las NodoDeclaracion dentro del cuerpo tambien usaran TdA.agregar())
        cuerpo.chequear(TdA);

        TdA.cerrarAmbito();
        // --- FIN MANEJO DE AMBITO ---

        System.out.println("DEBUG: Fin chequeo funcion '" + this.nombre + "'");
        return "void";
    }
}