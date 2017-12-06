package Practica1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Resultado implements Comparable<Resultado>
{
    private int identificadorJugador, resultadoJugada, valorJugada;
    private boolean activoDraw, gutshot, openEnded, flush;
    private ArrayList<Carta> cartas, kickers;
    private String rango;
    
    public Resultado(int identificadorJugador)
    {
        this.identificadorJugador = identificadorJugador;
        resultadoJugada = 0;
        valorJugada = 0;
        cartas = new ArrayList<>();
        kickers = new ArrayList<>();
        activoDraw = true;
        gutshot = false;
        openEnded = false;
        flush = false;
    }
    
    public void setResultadoJugada(int resultadoJugada)
    {
        this.resultadoJugada = resultadoJugada;
    }
    
    public void setKicker(Carta kicker)
    {
        this.kickers.add(kicker);
    }
    
    public void setActivoDraw(boolean activoDraw)
    {
        this.activoDraw = activoDraw;
    }

    public void setGutshot(boolean gutshot)
    {
        this.gutshot = gutshot;
    }
    
    public void setOpenEnded(boolean openEnded)
    {
        this.openEnded = openEnded;
    }
    
    public void setFlush(boolean flush)
    {
        this.flush = flush;
    }
    
    public void setRango(String rango)
    {
        this.rango = rango;
    }
    
    public void calculoValorJugada()
    {
        ArrayList<Carta> calcularCartas = new ArrayList();
        
        for (int i = 0; i < cartas.size(); ++i)
            calcularCartas.add(cartas.get(i));
        
        for (int i = 0; i < kickers.size(); ++i)
            calcularCartas.add(kickers.get(i));
        
        Collections.sort(calcularCartas);
        
        int aux = 100000;
        valorJugada += (resultadoJugada * aux);
        
        for (int i = 0; i < calcularCartas.size(); i++)
        {
            aux /= 10;
            valorJugada += calcularCartas.get(i).getValor() * aux;
        }
    }
    
    public void setCarta(Carta aux)
    {
        this.cartas.add(aux);
    }
    
    public int getResultadoJugada()
    {
        return resultadoJugada;
    }
    
    public int getValorJugada()
    {
        return valorJugada;
    }
    
    public int getIdentificadorJugador()
    {
        return identificadorJugador;
    }
    
    public String getRango()
    {
        return rango;
    }
    
    public Carta getCarta(int carta)
    {
        return cartas.get(carta);
    }
    
    private String getValores()
    {
        String aux = "";
        Iterator it = cartas.iterator();
        Carta a;
        
        while (it.hasNext())
        {
            a = (Carta) it.next();
            aux += a;
        }
        
        return aux;
    }
    
    private String getKicker()
    {
        String aux = "";
        Iterator it = kickers.iterator();
        Carta a;
        
        while (it.hasNext())
        {
            a = (Carta) it.next();
            aux += a;
        }
        
        return aux;
    }
    
    private String getTextoValor(int index)
    {
        String aux = "";
        switch(cartas.get(index).getValor())
        {
            case 0:
                aux = "deuces";
                break;
            case 1:
                aux = "treys";
                break;
            case 2:
                aux = "four";
                break;
            case 3:
                aux = "five";
                break;
            case 4:
                aux = "six";
                break;
            case 5:
                aux = "seven";
                break;
            case 6:
                aux = "eight";
                break;
            case 7:
                aux = "nine";
                break;
            case 8:
                aux = "ten";
                break;
            case 9:
                aux = "jack";
                break;
            case 10:
                aux = "queen";
                break;
            case 11:
                aux = "king";
                break;
            case 12:
                aux = "aces";
                break;
        }
        
        return aux;
    }
    
    private String getParseoApartado1y2()
    {
        String aux = "";
        
        switch(resultadoJugada)
        {
            case 0:
                aux += "High card with " + getTextoValor(0);
                break;
            case 1:
                aux += "Pair with " + getTextoValor(0);
                break;
            case 2:
                aux += "Two-pair with " + getTextoValor(0) + " and " + getTextoValor(2);
                break;
            case 3:
                aux += "Three-of-a-kind with " + getTextoValor(0);
                break;
            case 4:
                aux += "Straight to " + getTextoValor(0);
                break;
            case 5:
                aux += "Flush with " + getTextoValor(0);
                break;
            case 6:
                aux += "Full house with " + getTextoValor(0) + " and " + getTextoValor(3);
                break;
            case 7:
                aux += "Four-of-a-kind with " + getTextoValor(0);
                break;
            case 8:
                aux += "Straight flush to " + getTextoValor(0);
                break;   
        }
        
        return aux;
    }
    
    public String mostrarApartado1()
    {
        String aux = " - Best hand: " + getParseoApartado1y2() + " (" + getValores() + ")\r\n";

        if(gutshot && resultadoJugada != 4 && resultadoJugada != 8)
            aux += " - Draw: Straight Gutshot\r\n";
        
        if(openEnded && resultadoJugada != 4 && resultadoJugada != 8)
            aux += " - Draw: Straight OpenEnded\r\n";        

        if(flush)
            aux += " - Draw: Flush\r\n";
        
        return aux;
    }
    
    public String mostrarApartado2()
    {
        String aux = " - Best hand: " + getParseoApartado1y2() + " (" + getValores() + getKicker() + ")\r\n";

        if(activoDraw)
        {
            if(gutshot && resultadoJugada != 4 && resultadoJugada != 8)
                aux += " - Draw: Straight Gutshot\r\n";
            
            if(openEnded && resultadoJugada != 4 && resultadoJugada != 8)
                aux += " - Draw: Straight OpenEnded\r\n";

            if(flush)
                aux += " - Draw: Flush\r\n";
        }
        
        return aux;
    }
    
    public String mostrarApartado3()
    {
        String aux = "";
        
        switch(resultadoJugada)
        {
            case 0:
                aux = "(High card)";
                break;
            case 1:
                aux = "(Pair of " + getTextoValor(0) + ")";
                break;
            case 2:
                aux = "(Two-pair of " + getTextoValor(0) + " and " + getTextoValor(2) + ")";
                break;
            case 3:
                aux = "(Three-of-a-kind of " + getTextoValor(0) + ")";
                break;
            case 4:
                aux = "(Straight to " + getTextoValor(0) + ")";
                break;
            case 5:
                aux = "(Flush of " + getTextoValor(0) + ")";
                break;
            case 6:
                aux = "(Full house of " + getTextoValor(0) + " and " + getTextoValor(3) + ")";
                break;
            case 7:
                aux = "(Four-of-a-kind of " + getTextoValor(0) + ")";
                break;
            case 8:
                aux = "(Straight flush to " + getTextoValor(0) + ")";
                break;   
        }
        
        aux = getValores() + getKicker() + " " + aux;
        
        return aux;
    }

    @Override
    public int compareTo(Resultado o) 
    {
        int aux;
        
        if(valorJugada > o.getValorJugada())
            aux = 1;
        else if (valorJugada == o.getValorJugada())
            aux = 0;
        else
            aux = -1;
        
        return aux;
    }
}
