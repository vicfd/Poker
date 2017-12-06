package practica3;
import Practica1.Carta;
import Practica1.Jugada;
import Practica1.Resultado;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;

public class panelPrincipal extends JFrame
{
    private JPanel principalPanel; // Panel principal donde se realizan las operaciones
    private JButton tablero[][], limpiarSeleccion, valorAleatorio, fold; // tablero de las cartas, boton para limpiar una selecciom, valor aleatorio de cartas para los jugadore y fold para echar las cartas
    private int seleccion; // Valor para saber jugador actual o board
    private int[][] tablero_posicion; // tablero para saber que carta tiene seleccionado cada uno
    private int[] nPlayer; // cartas que tiene cada jugador seleccionadas
    private JSlider porcentajeSlider; // slide para el numero aleatorio del board
    private boolean[] foldPlayer; // bool donde estan los jugadores que se han ido
    
    public panelPrincipal()
    {
        initVariables();
        initPanelPrincipal();
        initPanelCentral();
        initPanelSuperior();
        initJFrame();
    }    
    
    // Iniciamos variables tipo tablero, fold, numero de cartas, etc...
    
    private void initVariables()
    {
        seleccion = 0;
        tablero_posicion = new int[14][4];
        foldPlayer = new boolean[6];
        nPlayer = new int[7];
        
        for (int i = 0; i < 13; ++i)
            for (int j = 0; j < 4; ++j)
                tablero_posicion[i][j] = -1;
        
        for (int i = 0; i < 7; ++i)
        {
            if (i < 6)
                foldPlayer[i] = false;
            nPlayer[i] = 0;
        }
    }
    
    // inicio del panel principal
    
    private void initPanelPrincipal()
    {
        principalPanel = new JPanel();
        principalPanel.setLayout(new BorderLayout());
        getContentPane().add(principalPanel); 
    }
    
    // inicio del panel del tablero de las cartas
    
    private void initPanelCentral() // Inicializa el tablero de botones
    {
        tablero = new JButton[13][4];
        JPanel panelRangos = new JPanel();
        panelRangos.setLayout(new GridLayout(13, 4, 2, 2));
        panelRangos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        principalPanel.add(panelRangos, BorderLayout.CENTER);
        
        char[] tableroLetra = {'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'};
        char[] tableroPalo = {'h', 'c', 'd', 's'};
        
        for(int i = 0; i < 13; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                tablero[i][j] = new JButton(String.valueOf(tableroLetra[i])+String.valueOf(tableroPalo[j]));
                colorBoton(i, j);
                tablero[i][j].setBorder(null); // para que quepan las letras y no salgan puntitos
                panelRangos.add(tablero[i][j]);
                tablero[i][j].addActionListener(new panelPrincipal.BotonListener(i, j));
            }
        }
    }
    
    // inicio del panel de los botones de accion, botones aleatorios, eliminar selecciones, calcular el equity, etc...
    
    private void initPanelSuperior()
    {
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayout(7, 2));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton calcularEquity = new JButton("Calcular");
        JButton limpiarTotal = new JButton("Limpiar");
        valorAleatorio = new JButton("Aleatorio");
        limpiarSeleccion = new JButton("Limpiar Board");
        fold = new JButton("Board");
        JButton jugadores[] = new JButton[7];
        fold.setEnabled(false);
        porcentajeSlider = new JSlider(JSlider.HORIZONTAL, 0, 2, 0);
        
        porcentajeSlider.setMinimum(3);
        porcentajeSlider.setMaximum(5);
        porcentajeSlider.setMajorTickSpacing(1);
        porcentajeSlider.setMinorTickSpacing(1);
        porcentajeSlider.setPaintTicks(true);
        porcentajeSlider.setPaintLabels(true);
        
        calcularEquity.addActionListener((ActionEvent e) -> 
        {
            int error = -1, players = 0;
            
            for (int i = 0; i < nPlayer.length && error == -1; ++i)
            {
                if ((i == 0 && (nPlayer[i] > 0 && nPlayer[i] < 3)) || (i != 0 && nPlayer[i] == 1))
                    error = i;
                
                if (i > 0 && !foldPlayer[i - 1] && nPlayer[i] == 2)
                    players++;
            }
            
            if (error != -1 || players < 2)
            {
                String mensaje;
                
                if (players < 2)
                    mensaje = "Error: debe haber minimo dos jugadores";
                else if (error == 0)
                    mensaje = "Error board, debes seleccionar 3-4-5 o ninguna carta";
                else
                    mensaje = "Error jugador " + error + " debe seleccionar 2 cartas o ninguna";
                
                JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                ArrayList<Carta> cartasBoard = new ArrayList();
                TreeMap<Integer, List<Carta>> cartasJugadores = new TreeMap<>();
                TreeMap<Integer, Equity> equityJugadores = new TreeMap<>();
                int nCartas = 52;
                
                for (int i = 0; i < 13; ++i)
                    for (int j = 0; j < 4; ++j)
                    {
                        if (tablero_posicion[i][j] != -1)
                        {
                            Carta c = new Carta(tablero[i][j].getText().charAt(0), tablero[i][j].getText().charAt(1));
                            
                            if (tablero_posicion[i][j] == 0)
                                cartasBoard.add(c);
                            else if (!foldPlayer[tablero_posicion[i][j] - 1])
                            {
                                List<Carta> aux = cartasJugadores.get(tablero_posicion[i][j]);

                                if (aux == null)
                                {
                                    Equity jugadorEquity = new Equity();
                                    aux = new ArrayList<>();
                                    nCartas -= 2;
                                    equityJugadores.put(tablero_posicion[i][j], jugadorEquity);
                                }

                                aux.add(c);
                                cartasJugadores.put(tablero_posicion[i][j], aux);
                            }
                        }
                    }
 
                char[] cartaValor = {'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'};
                char[] cartaPalo = {'h', 'c', 'd', 's'};
                num nCombinaciones = new num();
                nCartas -= cartasBoard.size();     
                
                calcularManos(cartasBoard, 0, nCombinaciones, cartaValor, cartaPalo, cartasJugadores, equityJugadores, nCartas);
                
                DecimalFormat df = new DecimalFormat("#.###");
                String salida = "";
                
                for (int i = 1; i <= 6; ++i)
                    if (equityJugadores.containsKey(i))
                        salida += "Jugador " + i + ": " + df.format(equityJugadores.get(i).getPorcentaje(nCombinaciones.getNum())) + "%\n";
            
                JFrame aux = new JFrame("Equity");
                JScrollPane scroll = new JScrollPane(new JTextArea(salida));
                scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                aux.setSize(300, 400);
                aux.setVisible(true);
                aux.add(scroll);
            }
        });
        
        limpiarTotal.addActionListener((ActionEvent e) -> 
        {
            for (int i = 0; i < 13; ++i)
                for (int j = 0; j < 4; ++j)
                    tablero_posicion[i][j] = -1;

            for (int i = 0; i < 7; ++i)
                nPlayer[i] = 0;

            for(int i = 0; i < 13; i++)
                for(int j = 0; j < 4; j++)
                    colorBoton(i, j);
            
            for (int i = 0; i < 6; ++i)
                foldPlayer[i] = false;
            
            valorAleatorio.setEnabled(true);
            fold.setEnabled(false);
        });
        
        valorAleatorio.addActionListener((ActionEvent e) -> 
        {
            limpiarSeleccion();

            int n = 2;

            if (seleccion == 0)
                n = porcentajeSlider.getValue();
            else
                fold.setEnabled(true);

            while (nPlayer[seleccion] < n)
            {
                int i = (int) (Math.random() * 14), j = (int) (Math.random() * 4);

                if (tablero_posicion[i][j] == -1)
                {
                    tablero[i][j].setBackground(Constante.PLAYER[seleccion]);
                    tablero_posicion[i][j] = seleccion;
                    nPlayer[seleccion]++;
                }
            }
        }); 
        
        limpiarSeleccion.addActionListener((ActionEvent e) -> 
        {
            limpiarSeleccion();
        });
        
        fold.addActionListener((ActionEvent e) -> 
        {
            if (foldPlayer[seleccion - 1])
            {
                valorAleatorio.setEnabled(true);
                fold.setText("Fold Jugador " + seleccion);
                foldPlayer[seleccion - 1] = false;
            }
            else
            {
                valorAleatorio.setEnabled(false);
                fold.setText("Unfold Jugador " + seleccion);
                foldPlayer[seleccion - 1] = true;
            }
        });
        
        for (int i = 0; i < jugadores.length; ++i)
        {
            if (i == 0)
            {
                jugadores[i] = new JButton("Board");
                jugadores[i].addActionListener(new BotonListenerJugador(i));
                panelInferior.add(jugadores[0]);
                panelInferior.add(porcentajeSlider);
            }
            else
            {
                jugadores[i] = new JButton("Jugador " + i);
                jugadores[i].addActionListener(new BotonListenerJugador(i));
                panelInferior.add(jugadores[i]);
            }
            
            jugadores[i].setBackground(Constante.PLAYER[i]);
        }
        
        panelInferior.add(calcularEquity);
        panelInferior.add(limpiarTotal);
        panelInferior.add(valorAleatorio);
        panelInferior.add(limpiarSeleccion);
        panelInferior.add(fold);
        principalPanel.add(panelInferior, BorderLayout.NORTH);
    }
    
    // Inicio del jframe para mostrar todo los paneles
    
    private void initJFrame()
    {
        setTitle("HJA, Combos");
        setSize(400, 600);
        setVisible(true);
        setResizable(false);
    }
    
    // Vuelta atras para calcular todas las combinaciones posibles
    
    private void calcularManos (ArrayList <Carta> board, int cartaInicio, num nCombinaciones, char[] cartaValor, char[] cartaPalo, TreeMap<Integer, List<Carta>> cartasJugadores, TreeMap<Integer, Equity> equityJugadores, int nCartas) 
    {            
        if (board.size() == 5)
        {
            nCombinaciones.sumNum();
            calcularJugadas(board, cartasJugadores, equityJugadores, nCartas);
        }
        else
        {
            for (int i = cartaInicio; i < 52; ++i) 
            {
                board.add(new Carta (cartaValor[i % 13], cartaPalo[i / 13]));
                if (tablero_posicion[12 - board.get(board.size() - 1).getValor()][board.get(board.size() - 1).getPaloPosicion()] == -1) 
                    calcularManos(board, i + 1, nCombinaciones, cartaValor, cartaPalo, cartasJugadores, equityJugadores, nCartas);
                board.remove(board.size() - 1);
            }
        }
    }
    
    // Calculo de quien gana cada una de las jugadas
    
    private void calcularJugadas(ArrayList <Carta> board, TreeMap<Integer, List<Carta>> cartasJugadores, TreeMap<Integer, Equity> equityJugadores, int nCartas)
    {
        ArrayList<Jugada> jugadasJugadores = new ArrayList();
        ArrayList<Resultado> resultadoJugadores = new ArrayList();
        
        for (int i = 1; i <= 6; ++i)
        {
            if (cartasJugadores.containsKey(i))
            {
                Jugada aux = new Jugada(i);
                
                for (int j = 0; j < board.size(); ++j)
                    aux.agregarCarta(board.get(j));
                
                aux.agregarCarta(cartasJugadores.get(i).get(0));
                aux.agregarCarta(cartasJugadores.get(i).get(1));
                
                jugadasJugadores.add(aux);
            }
        }
        
        for (int i = 0; i < jugadasJugadores.size(); ++i)
            resultadoJugadores.add(jugadasJugadores.get(i).evaluarJugada(jugadasJugadores.get(i).getIdentificadorJugador()));
        
        Collections.sort(resultadoJugadores);
        
        if (resultadoJugadores.get(resultadoJugadores.size() - 1).getValorJugada() > resultadoJugadores.get(resultadoJugadores.size() - 2).getValorJugada())
            equityJugadores.get(resultadoJugadores.get(resultadoJugadores.size() - 1).getIdentificadorJugador()).addWin();
        else
        {
            int tie = 0, valorJugada = resultadoJugadores.get(resultadoJugadores.size() - 1).getValorJugada();
            
            for (int i = 0; i < resultadoJugadores.size(); ++i)
                if (resultadoJugadores.get(i).getValorJugada() == valorJugada)
                    tie++;
            
            for (int i = 0; i < resultadoJugadores.size(); ++i)
                if (resultadoJugadores.get(i).getValorJugada() == valorJugada)
                    equityJugadores.get(resultadoJugadores.get(i).getIdentificadorJugador()).addTie((float) 1 / tie);
        }
    }
    
    // Limpia el jugador actual de la seleccion de cartas
    
    private void limpiarSeleccion()
    {
        if (nPlayer[seleccion] > 0)
        {
            nPlayer[seleccion] = 0;

            for(int i = 0; i < 13; i++)
                for(int j = 0; j < 4; j++)
                    if (tablero_posicion[i][j] == seleccion)
                    {
                        colorBoton(i, j);
                        tablero_posicion[i][j] = -1;
                    }
            
            if (seleccion > 0)
            {
                foldPlayer[seleccion - 1] = false;
                valorAleatorio.setEnabled(true);
                fold.setEnabled(false);
                fold.setText("Fold jugador " + seleccion);
            }
        }
    }
    
    // Funcion para colorear cada palo de la botonera de cartas
    
    private void colorBoton(int i, int j)
    {
        switch(j)
        {
            case 0:
                tablero[i][j].setBackground(Constante.RED);
                break;
            case 1:
                tablero[i][j].setBackground(Constante.GREEN);
                break;
            case 2:
                tablero[i][j].setBackground(Constante.LIGHT_BLUE);
                break;
            case 3:
                tablero[i][j].setBackground(Constante.GRAY);
        }
    }
    
    // Action listener de cada jugador
    
    public class BotonListenerJugador implements ActionListener
    {
        private final int jugador;

        public BotonListenerJugador(int i) 
        {
            jugador = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            seleccion = jugador;
            if (jugador == 0)
            {
                limpiarSeleccion.setText("Limpiar Board");
                fold.setText("Board");
                fold.setEnabled(false);
                valorAleatorio.setEnabled(true);
            }
            else
            {
                limpiarSeleccion.setText("Limpiar Jugador " + jugador); 
                
                if (foldPlayer[jugador - 1])
                {
                    fold.setText("Unfold Jugador " + jugador);
                    valorAleatorio.setEnabled(false);
                }
                else
                {
                    fold.setText("Fold Jugador " + jugador);
                    valorAleatorio.setEnabled(true);
                }
                
                if (nPlayer[jugador] == 2)
                    fold.setEnabled(true);
                else
                    fold.setEnabled(false);
            }
        }
    }    
    
    // Action listener de cada una de las cartas de la botonera
    
    public class BotonListener implements ActionListener
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
            if (seleccion == 0 || !foldPlayer[seleccion - 1])
            {
                if( tablero_posicion[i][j] == -1)
                {
                    if ((seleccion == 0 && nPlayer[seleccion] < 5) || (seleccion != 0 && nPlayer[seleccion] < 2))
                    {
                        tablero[i][j].setBackground(Constante.PLAYER[seleccion]);
                        tablero_posicion[i][j] = seleccion;
                        nPlayer[seleccion]++;

                        if (seleccion != 0 && nPlayer[seleccion] == 2)
                            fold.setEnabled(true);
                    }
                }
                else 
                {
                    nPlayer[tablero_posicion[i][j]]--;
                    tablero_posicion[i][j] = -1;
                    colorBoton(i, j);
                    fold.setEnabled(false);
                }
            }
        }
    }    
}