package CompiladoresMain;
public class Token {
    private int id;
    private String lexema;
    public Token(){
        this.id = -1;
        this.lexema = "";
    }
    public Token(int id, String lexema){
        this.id = id;
        this.lexema = lexema;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getLexema(){
        return lexema;
    }
    public void setLexema(String lexema){
        this.lexema = lexema;
    }
    @Override
    public String toString() {
        return lexema + " [" + id + "]";
    }
}
