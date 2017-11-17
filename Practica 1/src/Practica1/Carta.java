package Practica1;

public class Carta
{
    private final char valor;
    private final char palo;
    
    public Carta(char valor, char palo)
    {
        this.valor = valor;
        this.palo = palo;       
    }
    
    public int getValor()
    {
        int aux;
        switch(valor)
        {
            case 'T':
                aux = 8;
                break;
            case 'J':
                aux = 9;
                break;
            case 'Q':
                aux = 10;
                break;
            case 'K':
                aux = 11;
                break;
            case 'A':
                aux = 12;
                break;   
            default:
                aux = Character.getNumericValue(valor) - 2;
        }
        return aux;
    }
    
    public int getPaloPosicion()
    {
        int aux = 0;
        switch (palo) 
        {
            case 'h':
                aux = 0;
                break;
            case 'd':
                aux = 1;
                break;
            case 'c':
                aux = 2;
                break;
            case 's':
                aux = 3;
                break;
        }
        return aux;        
    }
    
    @Override
    public String toString()
    {
        return String.valueOf(valor) + String.valueOf(palo);
    }
}