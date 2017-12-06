package practica3;

public class Equity 
{
    // Clase que devuelve el equity de cada jugador dependiendo del numero de combinaciones totales
    
    private float win, tie;
    
    public Equity()
    {
        win = 0;
        tie = 0;
    }
    
    public void addWin()
    {
        this.win += 1;
    }
    
    public void addTie(float tie)
    {
        this.tie += tie;
    }
    
    public float getPorcentaje(int total)
    {
        return ((win + tie) * 100) / total;
    }
}
