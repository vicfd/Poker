package Practica1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Omaha 
{
    class nMesa
    {
        int n;
        
        public nMesa()
        {
            n = 0;
        }
        
        public void aumentaN()
        {
            n++;
        }
        
        public int getN()
        {
            return n;
        }
    }
    
    String entradaDeDatos;
    ArrayList<Carta> mano;
    ArrayList<Carta> mesa;
    ArrayList<Jugada> jugador;
    ArrayList<Resultado> resultado;
    
    public Omaha(String entradaDeDatos)
    {
        this.entradaDeDatos = entradaDeDatos;
        mano = new ArrayList<>();
        mesa = new ArrayList<>();
        jugador = new ArrayList<>();
        resultado = new ArrayList<>();
    }
    
    public void setCartaMano(Carta carta)
    {
        mano.add(carta);
    }
    
    public void setCartaMesa(Carta carta)
    {
        mesa.add(carta);
    }
    
    public void nJugadas()
    {
        int nJugadas = 0;
        
        switch(mesa.size())
        {
            case 3:
                nJugadas = 1;
                break;
            case 4:
                nJugadas = 4;
                break;
            case 5:
                nJugadas = 10;
                break;
        }
        
        nMesa x = new nMesa();
        Stack<Integer> a = new Stack<>();
        vueltaAtrasMano(a, 4, 2, nJugadas);
        vueltaAtrasMesa(a, mesa.size(), 3, nJugadas, x);       
        
        for (int i = 0; i < jugador.size(); i++)
            resultado.add(jugador.get(i).evaluarJugada(0));
        
        Collections.sort(resultado);
    }
    
    private void vueltaAtrasMano(Stack<Integer> a, int n, int max, int nJugadas) 
    {
        if (n == 0) 
        {
            if(max == 0)
            {
                Jugada j;
                int contadorMano = 0, contadorJugada = 0;
                ArrayList<Carta> c = new ArrayList<>();
                
                while(contadorJugada < 2)
                {
                    if(a.get(contadorMano) != 0)
                    {
                        c.add(mano.get(contadorMano));
                        contadorJugada++;
                    }
                    
                    contadorMano++;
                }
                
                for(int i = 0; i < nJugadas; i++)
                {
                    j = new Jugada();
                    
                    for(int k = 0; k < 2; k++)
                        j.agregarCarta(c.get(k));
                    
                    jugador.add(j);
                }
            }
        } 
        else 
        {
            for (int i = 0; i < 2; i++) 
            {
                if(i == 1)
                    max--;
                
                a.push(i);
                vueltaAtrasMano(a, n - 1, max, nJugadas);
                a.pop();
                
                if(i == 1)
                    max++;
            }
        }
    } 
    
    private void vueltaAtrasMesa(Stack<Integer> a, int n, int max, int nJugadas, nMesa x) 
    {
        if (n == 0) 
        {
            if(max == 0)
            {
                ArrayList<Carta> c = new ArrayList<>();
                int contadorMano = 0, contadorJugada = 0;
                
                while(contadorJugada < 3)
                {
                    if(a.get(contadorMano) != 0)
                    {
                        c.add(mesa.get(contadorMano));
                        contadorJugada++;
                    }
                    
                    contadorMano++;
                }
                
                for (int i = 0; i < 6; i++)
                    for(int j = 0; j < 3; j++)
                        jugador.get(i*nJugadas+x.getN()).agregarCarta(c.get(j));
                
                x.aumentaN();
            }  
        } 
        else 
        {
            for (int i = 0; i < 2; i++) 
            {
                if(i == 1)
                    max--;
                
                a.push(i);
                vueltaAtrasMesa(a, n - 1, max, nJugadas, x);
                a.pop();
                
                if(i == 1)
                    max++;
            }
        }
    }
    
    @Override
    public String toString()
    {
        return entradaDeDatos + "\r\n" + resultado.get(resultado.size()-1).mostrarApartado2();
    }
}
