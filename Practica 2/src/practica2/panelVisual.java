package practica2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class panelVisual extends JFrame
{
    private static final long serialVersionUID = 1L; //No se (tenia que a�adirlo)
    private JButton tablero_boton[][];  //array de botones para representar un rango por pantalla
    private JPanel principalPanel;
    private JTextField rango;  //Campo de texto donde se introduce el rango

    public panelVisual () 
    {
        initPanelPrincipal();
        initTablero();
        initPanelDerecho();
        initPanelInferior();
        initJFrame();
    }

    private void initPanelPrincipal() 
    {
        principalPanel = new JPanel();
        principalPanel.setLayout(new BorderLayout());
        getContentPane().add(principalPanel);
    }
    
    private void initTablero() // Inicializa el tablero de botones
    {
        tablero_boton = new JButton[13][13];
        JPanel panelRangos = new JPanel();
        panelRangos.setLayout(new GridLayout(13, 13, 2, 2));
        principalPanel.add(panelRangos, BorderLayout.CENTER);
        
        char[] tablero = {'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'};
        String o = "o", s = "s";
        
        for(int i = 0; i < 13; i++)
        {
            for(int j = 0; j < 13; j++)
            {
                if (i < j)
                {
                    tablero_boton[i][j] = new JButton(String.valueOf(tablero[i])+String.valueOf(tablero[j])+s);
                    tablero_boton[i][j].setBackground(Constante.RED);
                }
                else if (i == j)
                {
                    tablero_boton[i][j] = new JButton(String.valueOf(tablero[i])+String.valueOf(tablero[i]));
                    tablero_boton[i][j].setBackground(Constante.GREEN);
                }
                else
                {
                   tablero_boton[i][j] = new JButton(String.valueOf(tablero[j])+String.valueOf(tablero[i])+o);
                   tablero_boton[i][j].setBackground(Constante.LIGHT_GRAY);
                }
                
                tablero_boton[i][j].setBorder(null); // para que quepan las letras y no salgan puntitos
                panelRangos.add(tablero_boton[i][j]);
                tablero_boton[i][j].addActionListener(new BotonListener(i, j));
            }
        }
    }

    private void initPanelDerecho() //Inicializa la parte izquierda de la ventana
    { 
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new GridLayout(10, 1));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton seleccionarTodo = new JButton ("All");
        JButton seleccionarDiagonalArriba = new JButton ("Any Suited");
        JButton seleccionarMejores = new JButton("Any Broadway"); 
        JButton seleccionarParejas = new JButton ("Any Pair");
        JButton seleccionarLimpiar = new JButton ("Clear");
        JButton seleccionarRango = new JButton ("Buscar rango");
        rango = new JTextField("");
        JButton combo = new JButton ("Calcular Combos");

        seleccionarTodo.addActionListener((ActionEvent e) -> 
        {
            for (int i = 0; i < 13; ++i)
                for (int j = 0; j < 13; ++j)
                    marcaAmarillo(i, j);
            mostrarRangoMarcado();
        });

        seleccionarDiagonalArriba.addActionListener((ActionEvent e) -> 
        {
            for (int i = 0; i < 13; ++i)
                for (int j = i + 1; j < 13; ++j)
                    marcaAmarillo(i, j);
            mostrarRangoMarcado();
        });

        seleccionarMejores.addActionListener((ActionEvent e) -> 
        {
            for (int i = 0; i < 5; ++i)
                for (int j = 0; j < 5; ++j)
                    marcaAmarillo(i, j);
            mostrarRangoMarcado();
        });
        
        seleccionarParejas.addActionListener((ActionEvent e) -> 
        {
            for (int i = 0; i < 13; ++i)
                marcaAmarillo(i, i);
            mostrarRangoMarcado();
        });
        
        seleccionarLimpiar.addActionListener((ActionEvent e) -> 
        {
            rango.setText("");
            reset();
        });
        
        seleccionarRango.addActionListener((ActionEvent e) -> 
        {
            try
            {
                introducirRangoTablero();
            }
            catch (Exception error)
            {
                reset();
                JOptionPane.showMessageDialog(null, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        combo.addActionListener((ActionEvent e) -> 
        {
            panelCombo aux = new panelCombo(tablero_boton);
        });        

        panelDerecho.add(new JLabel("Selección automática"));
        panelDerecho.add(seleccionarTodo);
        panelDerecho.add(seleccionarDiagonalArriba);
        panelDerecho.add(seleccionarMejores);
        panelDerecho.add(seleccionarParejas);
        panelDerecho.add(seleccionarLimpiar);
        panelDerecho.add(new JLabel("Seleccion:"));
        panelDerecho.add(rango);
        panelDerecho.add(seleccionarRango);
        panelDerecho.add(combo);
        principalPanel.add(panelDerecho, BorderLayout.EAST);
    }
    
    private void initPanelInferior()
    {
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior,BoxLayout.X_AXIS));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel porcentajeMarcador = new JLabel("0");
        JSlider porcentajeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        
        panelInferior.add(porcentajeSlider);
        panelInferior.add(porcentajeMarcador);
        
        porcentajeSlider.addChangeListener((javax.swing.event.ChangeEvent evt) -> 
        {
            porcentajeMarcador.setText(String.valueOf(porcentajeSlider.getValue()));
            rango.setText("");
            reset();
            int n = (int) (1.69 * porcentajeSlider.getValue());
            for (int i = 0; i < n; ++i)
                marcaMagenta(Constante.SKLANSKYCHUBUKOVRANKINGS[2*i], Constante.SKLANSKYCHUBUKOVRANKINGS[2*i+1]);
        });
        
        principalPanel.add(panelInferior, BorderLayout.SOUTH);
    }

    private void initJFrame()
    {
        setTitle("HJA, Representacion de rangos");
        setSize(700, 500);
        setVisible(true);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void marcaAmarillo (int i, int j)
    { 
        tablero_boton[i][j].setBackground(Constante.YELLOW);
    }

    private void marcaMagenta(int i, int j)
    {
        tablero_boton[i][j].setBackground(Constante.MAGENTA);
    }
    
    private void reset () 
    {
        for (int i = 0; i < 13; ++i) 
            for (int j = 0; j < 13; ++j) 
                if (i < j)
                    tablero_boton[i][j].setBackground(Constante.RED);
                else if (i == j)
                    tablero_boton[i][j].setBackground(Constante.GREEN);
                else
                    tablero_boton[i][j].setBackground(Constante.LIGHT_GRAY);
    }

    private class BotonListener implements ActionListener
    {
        private final int i, j;

        public BotonListener(int i, int j) 
        {
            this.i = i;
            this.j = j;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(tablero_boton[i][j].getBackground() != Constante.YELLOW)
                tablero_boton[i][j].setBackground(Constante.YELLOW);
            else 
            {
                if (i < j)
                    tablero_boton[i][j].setBackground(Constante.RED);
                else if (i == j)
                    tablero_boton[i][j].setBackground(Constante.GREEN);
                else
                    tablero_boton[i][j].setBackground(Constante.LIGHT_GRAY);
            }
            
            mostrarRangoMarcado();
        }
    }
    
    // Rangos
    private void introducirRangoTablero () throws Exception
    {
        if(!rango.getText().isEmpty())
        {
            reset();
            String[] introducirRango = rango.getText().split(",");
            String caracterPermitidoCarta = "AKQJT98765432", caracterPermitidoAgregado = "so", error;
            
            for (int i = 0; i < introducirRango.length; ++i)
            {
                error = "Formato no correcto: " + introducirRango[i];
                
                if (introducirRango[i].indexOf('-') != -1)
                {
                    comprobarRangoGuiones(introducirRango[i], caracterPermitidoCarta, caracterPermitidoAgregado, error);
                    
                    String[] cartas = introducirRango[i].split("-");
                    int[] primerRango = {obtenerValorCarta(cartas[0].charAt(0)), obtenerValorCarta(cartas[0].charAt(1))};
                    int[] segundoRango = {obtenerValorCarta(cartas[1].charAt(0)), obtenerValorCarta(cartas[1].charAt(1))};

                    if (introducirRango[i].indexOf('o') != -1)
                        for (int j = segundoRango[1]; j <= primerRango[1]; ++j)
                            marcaAmarillo(12 - j, 12 - primerRango[0]);
                    else if (introducirRango[i].indexOf('s') != -1)
                        for (int j = segundoRango[1]; j <= primerRango[1]; ++j)
                            marcaAmarillo(12 - primerRango[0], 12 - j);
                    else
                        for (int j = segundoRango[0]; j <= primerRango[0]; ++j)
                            marcaAmarillo(12 - j, 12 - j);
                }
                else if(introducirRango[i].indexOf('+') != -1)
                {
                    comprobarRangoResto(introducirRango[i], caracterPermitidoCarta, caracterPermitidoAgregado, error, 3, 4);
                    int[] primerRango = {obtenerValorCarta(introducirRango[i].charAt(0)), obtenerValorCarta(introducirRango[i].charAt(1))};

                    if (introducirRango[i].indexOf('o') != -1)
                        for (int j = primerRango[1]; j < primerRango[0]; ++j)
                            marcaAmarillo(12 - j, 12 - primerRango[0]);
                    else if (introducirRango[i].indexOf('s') != -1)
                        for (int j = primerRango[1]; j < primerRango[0]; ++j)
                            marcaAmarillo(12 - primerRango[0], 12 - j);
                    else
                        for (int j = primerRango[1]; j <= 12; ++j)
                            marcaAmarillo(12 - j, 12 - j);
                }
                else
                {
                    comprobarRangoResto(introducirRango[i], caracterPermitidoCarta, caracterPermitidoAgregado, error, 2 , 3);
                    int[] primerRango = {obtenerValorCarta(introducirRango[i].charAt(0)), obtenerValorCarta(introducirRango[i].charAt(1))};

                    if (introducirRango[i].indexOf('o') != -1)
                        marcaAmarillo(12 - primerRango[1], 12 - primerRango[0]);
                    else if (introducirRango[i].indexOf('s') != -1)
                        marcaAmarillo(12 - primerRango[0], 12 - primerRango[1]);
                    else
                        marcaAmarillo(12 - primerRango[1], 12 - primerRango[1]);
                }
            }
        }
    }
    
    private void comprobarRangoGuiones(String introducirRango, String caracterPermitidoCarta, String caracterPermitidoAgregado, String error) throws Exception
    {
        int length = introducirRango.length();

        if(length != 5 && length != 7)
            throw new Exception(error);

        String[] cartasSeparadas = introducirRango.split("-");

        if (cartasSeparadas.length != 2)
            throw new Exception(error);
        
        for (int j = 0; j < 2; ++j)
            if (caracterPermitidoCarta.indexOf(cartasSeparadas[j].charAt(0)) == -1 || caracterPermitidoCarta.indexOf(cartasSeparadas[j].charAt(1)) == -1)
                throw new Exception(error);        
        
        if (length == 5 && (cartasSeparadas[0].charAt(0) != cartasSeparadas[0].charAt(1) || cartasSeparadas[1].charAt(0) != cartasSeparadas[1].charAt(1)))
            throw new Exception(error);
        
        if (length == 7 && (cartasSeparadas[0].charAt(0) != cartasSeparadas[1].charAt(0) || cartasSeparadas[0].charAt(1) < cartasSeparadas[1].charAt(1)))
            throw new Exception(error);
        
        if (length == 7 && (caracterPermitidoAgregado.indexOf(cartasSeparadas[0].charAt(2)) == -1 || cartasSeparadas[0].charAt(2) != cartasSeparadas[1].charAt(2)))
            throw new Exception(error);
    }
    
    private void comprobarRangoResto(String introducirRango, String caracterPermitidoCarta, String caracterPermitidoAgregado, String error, int diagonalLength, int diagonalNoLength) throws Exception
    {
        int length = introducirRango.length();

        if (length != diagonalLength && length != diagonalNoLength)
            throw new Exception(error);
        
        if ((length == diagonalLength && introducirRango.charAt(0) != introducirRango.charAt(1)) || (length == diagonalNoLength && (obtenerValorCarta(introducirRango.charAt(0)) <= obtenerValorCarta(introducirRango.charAt(1)) || caracterPermitidoAgregado.indexOf(introducirRango.charAt(2)) == -1)))
            throw new Exception(error);

        if (caracterPermitidoCarta.indexOf(introducirRango.charAt(0)) == -1 && caracterPermitidoCarta.indexOf(introducirRango.charAt(1)) == -1)
            throw new Exception(error);     
    }
    
    private int obtenerValorCarta(char valor)
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
    
    private String obtenerCarta (int c)
    {
        String aux;
        switch (c) 
        {
            case 12:
                aux = "A";
                break;
            case 11:
                aux = "K";
                break;
            case 10:
                aux = "Q";
                break;
            case 9:
                aux = "J";
                break;
            case 8:
                aux = "T";
                break;
            default:
                aux = String.valueOf(c + 2);
        }
        
        return aux;
    }

    private void mostrarRangoMarcado()
    {
        String obtenerRango = "";
        int primeraCarta = -1, aux = 0;
        
        for (int i = 0; i < 13; ++i)
        {
            if (tablero_boton[i][i].getBackground() == Constante.YELLOW)
            {
                aux++;
                
                if (primeraCarta == -1)
                    primeraCarta = 12 - i;
                
                if(i == 12)
                {
                    if(aux > 1 && primeraCarta == 12)
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - i) + "+," ;
                    else if(aux > 1 && primeraCarta != -1)
                        obtenerRango += obtenerCarta(primeraCarta) + obtenerCarta(primeraCarta) + "-" + obtenerCarta(12 - i) + obtenerCarta(12 - i) + ",";
                    else if(primeraCarta != -1)
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - i) + ",";
                }
            }
            else
            {
                if(aux > 1 && primeraCarta == 12)
                    obtenerRango += obtenerCarta(12 - i + 1) + obtenerCarta(12 - i + 1) + "+," ;
                else if(aux > 1 && primeraCarta != -1)
                    obtenerRango += obtenerCarta(primeraCarta) + obtenerCarta(primeraCarta) + "-" + obtenerCarta(12 - i + 1) + obtenerCarta(12 - i + 1) + ",";
                else if(aux == 1)
                    obtenerRango += obtenerCarta(12 - i + 1) + obtenerCarta(12 - i + 1) + ",";
                
                primeraCarta = -1;
                aux = 0;
            }
        }
        
        // Suited
        
        for (int i = 0; i < 13; ++i)
        {
            primeraCarta = -1;
            aux = 0;
            for (int j = i + 1; j < 13; ++j)
            {
                if (tablero_boton[i][j].getBackground() == Constante.YELLOW)
                {
                    aux++;
                    if (primeraCarta == -1)
                        primeraCarta = 12 - j;

                    if(j == 12)
                    {
                        if(aux > 1 && primeraCarta == (12 - i - 1))
                            obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - j) + "s+," ;
                        else if(aux > 1)
                            obtenerRango += obtenerCarta(12 - i) + obtenerCarta(primeraCarta) + "s-" + obtenerCarta(12 - i) + obtenerCarta(12 - j) + "s,";
                        else if(aux == 1)
                            obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - j) + "s,";
                    }
                }
                else
                {
                    if(aux > 1 && primeraCarta == (12 - i - 1))
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - j + 1) + "s+," ;
                    else if(aux > 1)
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(primeraCarta) + "s-" + obtenerCarta(12 - i) + obtenerCarta(12 - j + 1) + "s,";
                    else if(aux == 1)
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - j + 1) + "s,";
                    
                    primeraCarta = -1;
                    aux = 0;
                } 
            }
        }
        
        // Ofsuited
        
        for (int i = 0; i < 13; ++i)
        {
            primeraCarta = -1;
            aux = 0;
            for (int j = i + 1; j < 13; ++j)
            {
                if (tablero_boton[j][i].getBackground() == Constante.YELLOW)
                {
                    aux++;
                    if (primeraCarta == -1)
                        primeraCarta = 12 - j;

                    if(j == 12)
                    {
                        if(aux > 1 && primeraCarta == (12 - i - 1))
                            obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - j) + "o+," ;
                        else if(aux > 1)
                            obtenerRango += obtenerCarta(12 - i) + obtenerCarta(primeraCarta) + "o-" + obtenerCarta(12 - i) + obtenerCarta(12 - j) + "o,";
                        else if(aux == 1)
                            obtenerRango += obtenerCarta(12 - i) + obtenerCarta(primeraCarta) + "o,";
                    }
                }
                else
                {
                    if(aux > 1 && primeraCarta == (12 - i - 1))
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(12 - j + 1) + "o+," ;
                    else if(aux > 1)
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(primeraCarta) + "o-" + obtenerCarta(12 - i) + obtenerCarta(12 - j + 1) + "o,";
                    else if(aux == 1)
                        obtenerRango += obtenerCarta(12 - i) + obtenerCarta(primeraCarta) + "o,";
                    
                    primeraCarta = -1;
                    aux = 0;
                } 
            }
        }
        
        if(!obtenerRango.isEmpty())
            rango.setText(obtenerRango.substring(0, obtenerRango.length() - 1));
        else
            rango.setText("");
    }
}
