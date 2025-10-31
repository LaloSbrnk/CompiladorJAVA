package ArbolSintactico;
import java.util.HashMap;

import CompiladoresMain.TablaDeAmbitos;

public class NodoPrint extends Nodo {
    private Nodo expresion;

    public NodoPrint(Nodo expresion) {
        this.expresion = expresion;
    }

    @Override
    public String chequear(TablaDeAmbitos TdA) {
        // Chequeamos la expresi
        String tipoExpr = expresion.chequear(TdA);
        
        if (tipoExpr.equals("error")) {
            return "error";
        }
        
        return "void"; 
    }
}