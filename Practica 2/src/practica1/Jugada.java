package Practica1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.TreeMap;

public class Jugada
{
    private final TreeMap<Integer, List<Carta>> cartas;
    private final int[] cartasColor;
    private String rango;
    private int identificadorJugador;
    private int nCartas;
    
    public Jugada ()
    {
        cartas = new TreeMap<>();
        cartasColor = new int[4];
        nCartas = 0;
    }
    
    public Jugada (int identificadorJugador)
    {
        this.identificadorJugador = identificadorJugador;
        cartas = new TreeMap<>();
        cartasColor = new int[4];
        nCartas = 0;
    }
    
    public Jugada (String rango)
    {
        this.rango = rango;
        cartas = new TreeMap<>();
        cartasColor = new int[4];
        nCartas = 0;
        identificadorJugador = 0;
    }
    
    public void agregarCarta(Carta carta)
    {
        List<Carta> aux = cartas.get(carta.getValor());
        
        if(aux == null)
            aux = new ArrayList<>();
        
        aux.add(carta);
        cartas.put(carta.getValor(), aux);
        cartasColor[carta.getPaloPosicion()] += 1;
        nCartas++;
    }
    
    public int getIdentificadorJugador()
    {
        return identificadorJugador;
    }
    
    public String getRango()
    {
        return rango;
    }
    
    private boolean evaluarEscaleraColor(int valorMejor, int colorPalo)
    {
        boolean aux = false;
        int[] colorEscaleraArray = new int[4];

        for(int i = valorMejor - 4; i <= valorMejor; i++)
        {
            if(i < 0)
            {
                for(int j = 0; j < cartas.get(12).size(); j++)
                    colorEscaleraArray[cartas.get(12).get(j).getPaloPosicion()]++;
                i++;
            }

            for(int j = 0; j < cartas.get(i).size(); j++)
            {
                colorEscaleraArray[cartas.get(i).get(j).getPaloPosicion()]++;
                
                if(colorEscaleraArray[cartas.get(i).get(j).getPaloPosicion()] == 5)
                    aux = true;
            }
        }   
        
        return aux;
    }
    
    public Resultado evaluarJugada(int identificadorJugador) // Se evalua con una sola iteracion todo, excepto escalera de color que hay que hacer una iteracion mas.
    {
        Resultado resultadoJugada = new Resultado(identificadorJugador);
        boolean escalera = false, color = false;
        int valor, pareja = 0, trio = 0, poker = 0, colorPalo = 0;
        int valorAnterior = 13, gutshotCalculo = 0, contadorEscalera = 0;
        int auxiliarEscalera = 0, auxiliarPoker = 0, auxiliarTrio = 0;
        Stack<Integer> kickers = new Stack<>(), auxiliarPareja = new Stack<>(), auxiliarColor = new Stack<>();
        
        for(int i = 0; i < 4; i++)
        {
            if (cartasColor[i] >= 5)
            {
                color = true;
                colorPalo = i;
            }
            else if (cartasColor[i] == 4)
                resultadoJugada.setFlush(true);
        }

        Iterator it = cartas.keySet().iterator();
        valor = (Integer) it.next();
        valorAnterior = valor - 1;
        it = cartas.keySet().iterator();
        
        while(it.hasNext())
        {
            valor = (Integer) it.next();
            kickers.push(valor);

            // Calculo de escalera

            if(valor == valorAnterior + 1)
            {
                contadorEscalera++;

                if(contadorEscalera >= 5 || (contadorEscalera == 4 && valor == 3 && cartas.containsKey(12)))
                {
                    escalera = true;
                    auxiliarEscalera = valor;
                }
                else if(contadorEscalera == 4 || (gutshotCalculo + contadorEscalera == 3 && valor < 3 && cartas.containsKey(12)))
                    resultadoJugada.setOpenEnded(true);
                else if((gutshotCalculo != 0 && gutshotCalculo + contadorEscalera == 4) || (gutshotCalculo + contadorEscalera == 3 && valor <= 3 && cartas.containsKey(12)))
                    resultadoJugada.setGutshot(true);
            }
            else
            {
                if(contadorEscalera == 4)
                    resultadoJugada.setOpenEnded(true);
                
                if((gutshotCalculo != 0 && gutshotCalculo + contadorEscalera == 4) || (contadorEscalera >= 3 && valor - valorAnterior <= 2))
                    resultadoJugada.setGutshot(true);
                
                gutshotCalculo = (valor - valorAnterior > 2) ? 0 : contadorEscalera;                
                contadorEscalera = 1;
            }

            // Fin escalera

            // Calculo color

            if(color)
                for(int i = 0; i < cartas.get(valor).size(); i++)
                    if(cartas.get(valor).get(i).getPaloPosicion() == colorPalo)
                        auxiliarColor.add(valor);

            // Fin color

            // Calculo parejas

            switch(cartas.get(valor).size())
            {
                case 4:
                    poker++;
                    auxiliarPoker = valor;
                    break;
                case 3:
                    trio++;
                    auxiliarTrio = valor;
                    break;
                case 2:
                    pareja++;
                    auxiliarPareja.push(valor);
                    break;
            }

            // Fin parejas

            valorAnterior = valor;
        }

        if(escalera && color && evaluarEscaleraColor(auxiliarEscalera, colorPalo))
        {
            resultadoJugada.setResultadoJugada(8);

            for(int i = auxiliarEscalera; i >= (auxiliarEscalera - 4); i--)
            {            
                if(i < 0)
                {
                    for(int j = 0; j < cartas.get(12).size(); j++)
                        if(cartas.get(12).get(j).getPaloPosicion() == colorPalo)
                            resultadoJugada.setCarta(cartas.get(12).get(j));
                }
                else
                {
                    for(int j = 0; j < cartas.get(i).size(); j++)
                        if(cartas.get(i).get(j).getPaloPosicion() == colorPalo)
                            resultadoJugada.setCarta(cartas.get(i).get(j));
                }
            }

        }
        else if (poker == 1)
        {
            resultadoJugada.setResultadoJugada(7);
            
            for(int i = 0; i < 4; i++)
            {                
                resultadoJugada.setCarta(cartas.get(auxiliarPoker).get(i));
            }
            
            if(cartas.size() > 4)
            {
                if(kickers.peek() == auxiliarPoker)
                    kickers.pop();
            
                resultadoJugada.setKicker(cartas.get(kickers.peek()).get(0));
            }
        }
        else if(trio == 1 && pareja > 0)
        {
            resultadoJugada.setResultadoJugada(6);
            for(int i = 0; i < 3; i++)
            {                
                resultadoJugada.setCarta(cartas.get(auxiliarTrio).get(i));
            }
            
            for(int i = 0; i < 2; i++)
            {                
                resultadoJugada.setCarta(cartas.get(auxiliarPareja.peek()).get(i));
            }            
        }
        else if(color)
        {
            resultadoJugada.setResultadoJugada(5);

            for(int i = 0; i < 5; i++)
            {
                for(int j = 0; j < cartas.get(auxiliarColor.peek()).size(); j++)
                    if(cartas.get(auxiliarColor.peek()).get(j).getPaloPosicion() == colorPalo)
                        resultadoJugada.setCarta(cartas.get(auxiliarColor.peek()).get(j));
                
                auxiliarColor.pop();
            }
        }
        else if(escalera)
        {
            resultadoJugada.setResultadoJugada(4);
            
            for(int i = auxiliarEscalera; i >= (auxiliarEscalera - 4); i--)
            {
                if(i < 0)
                    resultadoJugada.setCarta(cartas.get(12).get(0));
                else
                    resultadoJugada.setCarta(cartas.get(i).get(0));
            }
        }
        else if (trio > 0)
        {
            resultadoJugada.setResultadoJugada(3);
            
            for(int i = 0; i < 3; i++)                
                resultadoJugada.setCarta(cartas.get(auxiliarTrio).get(i));
            
            if(cartas.size() > 4)
            {
                for (int i = 0; i < 2; i++)
                {
                    if(kickers.peek() == auxiliarTrio)
                        kickers.pop();

                    resultadoJugada.setKicker(cartas.get(kickers.peek()).get(0));

                    kickers.pop();
                }
            }
        }
        else if (pareja > 1)
        {
            resultadoJugada.setResultadoJugada(2);
            for(int i = 0; i < 2; i++)
            {     
                for(int j = 0; j < 2; j++)
                    resultadoJugada.setCarta(cartas.get(auxiliarPareja.peek()).get(j));

                if(kickers.peek() == cartas.get(auxiliarPareja.peek()).get(0).getValor())
                    kickers.pop();

                auxiliarPareja.pop();
            }
            
            if(cartas.size() > 4)
                resultadoJugada.setKicker(cartas.get(kickers.peek()).get(0));
        }
        else if(pareja == 1)
        {
            resultadoJugada.setResultadoJugada(1);
            
            for(int i = 0; i < 2; i++)
                resultadoJugada.setCarta(cartas.get(auxiliarPareja.peek()).get(i));

            if(cartas.size() > 4)
            {
                for(int i = 0; i < 3; i++)
                {
                    if(Objects.equals(auxiliarPareja.peek(), kickers.peek()))
                        kickers.pop();

                    resultadoJugada.setKicker(cartas.get(kickers.peek()).get(0));

                    kickers.pop();
                }
            }
        }
        else
        {
            resultadoJugada.setCarta(cartas.get(kickers.peek()).get(0));
            kickers.pop();
            
            if(cartas.size() > 4)
            {
                for(int i = 0; i < 4; i++)
                {
                    resultadoJugada.setKicker(cartas.get(kickers.peek()).get(0));
                    kickers.pop();
                }
            }
        }
        
        if(nCartas >= 7)
            resultadoJugada.setActivoDraw(false);
        
        if (cartas.size() > 4)
            resultadoJugada.calculoValorJugada();
        
        return resultadoJugada;
    }
}