package ArbolSintactico;

import CompiladoresMain.AnalizadorLexico;
import CompiladoresMain.TablaDeAmbitos;

import java.util.HashMap; 

public abstract class Nodo {
    
    public abstract String chequear(TablaDeAmbitos TdA); 
    public abstract void imprimir(String prefijo);
}