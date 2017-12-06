package Practica1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Practica1 
{
    public static void leerArchivo(String entrada, ArrayList<Mesa> mesas, int apartado)
    {      
        Mesa mesa;
        Carta aux;
        Jugada jugador;        
        FileReader f = null;
        BufferedReader b = null;
        String entradaDeDatos;

        try 
        {
            f = new FileReader (entrada);
            b = new BufferedReader(f);

            while((entradaDeDatos = b.readLine())!=null)
            {
                mesa = new Mesa(entradaDeDatos);
                
                if (apartado == 1)
                {
                    jugador = new Jugada();
                    
                    for(int i = 0; i < 5; i++)
                    {
                        aux = new Carta(entradaDeDatos.charAt(i*2), entradaDeDatos.charAt(i*2+1));
                        jugador.agregarCarta(aux);
                    }
                    
                    mesa.setJugador(jugador);
                }
                else if (apartado == 2)
                {
                    jugador = new Jugada();
                    String[] entradaDatos;
                    entradaDatos = entradaDeDatos.split(";");
                    
                    for(int i = 0; i < 2; i++)
                    {
                        aux = new Carta(entradaDatos[0].charAt(i*2), entradaDatos[0].charAt(i*2+1));
                        jugador.agregarCarta(aux);
                    }
                    
                    for(int i = 0; i < Integer.parseInt(entradaDatos[1]); i++)
                    {
                        aux = new Carta(entradaDatos[2].charAt(i*2), entradaDatos[2].charAt(i*2+1));
                        jugador.agregarCarta(aux);
                    }
                    
                    mesa.setJugador(jugador);
                }
                else if (apartado == 3)
                {
                    int posicion, size;
                    String[] entradaDatos;
                    entradaDatos = entradaDeDatos.split(";");
                    size = Integer.parseInt(entradaDatos[0]);
                    
                    for(int i = 1; i <= size; i++)
                    {
                        jugador = new Jugada(i);
                        aux = new Carta(entradaDatos[i].charAt(2), entradaDatos[1].charAt(3));
                        jugador.agregarCarta(aux);
                        aux = new Carta(entradaDatos[i].charAt(4), entradaDatos[1].charAt(5));
                        jugador.agregarCarta(aux);
                        mesa.setJugador(jugador);
                    }
                    
                    posicion = Integer.parseInt(entradaDatos[0]) + 1;
                    
                    for(int i = 0; i < 5; i++)
                    {
                        aux = new Carta(entradaDatos[posicion].charAt(i*2), entradaDatos[posicion].charAt(i*2+1));
                        mesa.setCarta(aux);
                    }
                }
                    
                mesas.add(mesa);
            }  
        }
        catch(IOException e)
        {
           System.out.println("Error leer archivo, 1. " + e);
        }
        finally
        {
            try
            {                    
                if(f != null)  
                    f.close();   
                if(b != null)
                    b.close();
            }
            catch (IOException e2)
            {
                System.out.println("Error leer archivo, 2. " + e2);
            }
        }
    }
    
    public static void leerOmaha(String entrada, ArrayList<Omaha> omaha)
    {
        Omaha o;
        Carta aux;      
        FileReader f = null;
        BufferedReader b = null;
        String entradaDeDatos;

        try 
        {
            f = new FileReader (entrada);
            b = new BufferedReader(f);

            while((entradaDeDatos = b.readLine())!=null)
            {
                o = new Omaha(entradaDeDatos);
                String[] entradaDatos = entradaDeDatos.split(";");
                
                
                for(int i = 0; i < 4; i++)
                {
                    aux = new Carta(entradaDatos[0].charAt(i*2), entradaDatos[0].charAt(i*2+1));
                    o.setCartaMano(aux); 
                }
                
                for(int i = 0; i < Integer.parseInt(entradaDatos[1]); i++)
                {
                    aux = new Carta(entradaDatos[2].charAt(i*2), entradaDatos[2].charAt(i*2+1));
                    o.setCartaMesa(aux);
                }
                    
                omaha.add(o);
            }  
        }
        catch(IOException e)
        {
           System.out.println("Error leer archivo, 1. " + e);
        }
        finally
        {
            try
            {                    
                if(f != null)  
                    f.close();   
                if(b != null)
                    b.close();
            }
            catch (IOException e2)
            {
                System.out.println("Error leer archivo, 2. " + e2);
            }
        }
    }    
    
    public static void escribirFichero(String nombreArchivo, String escribirFichero)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(nombreArchivo);
            pw = new PrintWriter(fichero);
            pw.print(escribirFichero);
        } 
        catch (IOException e) 
        {
            System.out.println("Error escribir archivo, 1. " + e);
        } 
        finally 
        {
            try 
            {
                if (null != fichero)
                   fichero.close();
                if(pw != null)
                    pw.close();
            }
            catch (IOException e2) 
            {
                System.out.println("Error escribir archivo, 2. " + e2);
            }
        }        
    }
    
    public static void realizarCalculo(String[] args, int apartado) throws IOException
    {
        ArrayList<Mesa> juegos = new ArrayList<>();
        leerArchivo(args[1], juegos, apartado);
        String escribirFichero = "";
        
        for(int i = 0; i < juegos.size(); i++)
        {
            juegos.get(i).evaluarJugadas();
            
            if(apartado == 3)
                juegos.get(i).ordenarResultados();

            escribirFichero += juegos.get(i).mostrar(apartado);
        }
        
        escribirFichero(args[2], escribirFichero);
    }
    
    public static void calculoOmaha(String[] args) 
    {
        ArrayList<Omaha> omaha = new ArrayList<>();
        leerOmaha(args[1], omaha);
        String escribirFichero = "";
        
        for(int i = 0; i < omaha.size(); i++)
        {
            omaha.get(i).nJugadas();
            escribirFichero += omaha.get(i);
        }
        
        escribirFichero(args[2], escribirFichero);
    }


    
    public static void main(String[] args) throws IOException 
    {
        switch(args[0])
        {
            case "1":
            case "2":
            case "3":
                realizarCalculo(args, Integer.parseInt(args[0]));
                break;
            case "4":
                calculoOmaha(args);
                break;
            default:
                System.out.println("No tenemos esa opcion implementada, pruebe con otra.");
        }
    }
}