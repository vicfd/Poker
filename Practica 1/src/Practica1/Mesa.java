package Practica1;

import java.util.ArrayList;
import java.util.Collections;

public class Mesa
{
    private String entradaDeDatos;
    private ArrayList<Jugada> jugador;
    private ArrayList<Resultado> resultado;
    
    public Mesa(String entradaDeDatos)
    {
        this.entradaDeDatos = entradaDeDatos;
        this.jugador = new ArrayList<>();
        this.resultado = new ArrayList<>();
    }
    
    public void setJugador(Jugada jugador)
    {
        this.jugador.add(jugador);
    }
    
    public void setCarta(Carta aux)
    {
        for (int i = 0; i < jugador.size(); i++)
            jugador.get(i).agregarCarta(aux);
    }
    
    public String getEntradaDeDatos()
    {
        return entradaDeDatos;
    }
    
    public void evaluarJugadas()
    {
        for(int i = 0; i < jugador.size(); i++)
            resultado.add(jugador.get(i).evaluarJugada(i + 1));
    }
    
    public void ordenarResultados()
    {
        Collections.sort(resultado);
    }
    
    public String mostrar(int apartado)
    {
        String aux = entradaDeDatos + "\r\n";
        
        for(int i = resultado.size() - 1; i >= 0 ; i--)
        {
            switch (apartado)
            {
                case 1:
                    aux += resultado.get(i).mostrarApartado1() + "\r\n";
                    break;
                case 2:
                    aux += resultado.get(i).mostrarApartado2() + "\r\n";
                    break;
                case 3:
                    aux += "J" + resultado.get(i).getIdentificadorJugador() + ": " + resultado.get(i).mostrarApartado3() + "\r\n";
                    break;
            }
        }
        
        if(apartado == 3)
            aux += "\r\n";
        
        return aux;
    }    
}
