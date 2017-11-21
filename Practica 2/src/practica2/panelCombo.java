package practica2;

import Practica1.Carta;
import Practica1.Jugada;
import Practica1.Resultado;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class panelCombo extends JFrame
{
    private JPanel principalPanel;
    private JButton[][] tablero_board, tablero_rango;
    private int nSeleccionado;
    
    public panelCombo(JButton[][] tablero_rango)
    {
        this.tablero_rango = tablero_rango;
        nSeleccionado = 0;
        initPanelPrincipal();
        initPanelCentral();
        initPanelInferior();
        initJFrame();
    }    
    
    private void initPanelPrincipal()
    {
        principalPanel = new JPanel();
        principalPanel.setLayout(new BorderLayout());
        getContentPane().add(principalPanel); 
    }
    
    private void initPanelCentral() // Inicializa el tablero de botones
    {
        tablero_board = new JButton[13][4];
        JPanel panelRangos = new JPanel();
        panelRangos.setLayout(new GridLayout(13, 4, 2, 2));
        principalPanel.add(panelRangos, BorderLayout.CENTER);
        
        char[] tablero = {'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'};
        char[] palo = {'h', 'c', 'd', 's'};
        
        for(int i = 0; i < 13; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                tablero_board[i][j] = new JButton(String.valueOf(tablero[i])+String.valueOf(palo[j]));
                
                switch(j)
                {
                    case 0:
                        tablero_board[i][j].setBackground(Constante.RED);
                        break;
                    case 1:
                        tablero_board[i][j].setBackground(Constante.GREEN);
                        break;
                    case 2:
                        tablero_board[i][j].setBackground(Constante.LIGHT_BLUE);
                        break;
                    case 3:
                        tablero_board[i][j].setBackground(Constante.GRAY);
                }
                
                tablero_board[i][j].setBorder(null); // para que quepan las letras y no salgan puntitos
                panelRangos.add(tablero_board[i][j]);
                tablero_board[i][j].addActionListener(new panelCombo.BotonListener(i, j));
            }
        }
    }
    
    private void initPanelInferior()
    {
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayout(1, 2));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton calcularCombo = new JButton("Calcular");
        JButton limpiarSeleccion = new JButton("Limpiar");
        
        calcularCombo.addActionListener((ActionEvent e) -> 
        {
            calcularCombo();
        });
        
        limpiarSeleccion.addActionListener((ActionEvent e) -> 
        {
            reset();
        });
        
        panelInferior.add(calcularCombo);
        panelInferior.add(limpiarSeleccion);
        principalPanel.add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void initJFrame()
    {
        setTitle("HJA, Combos");
        setSize(300, 400);
        setVisible(true);
        setMinimumSize(new Dimension(300, 400));
    }
    
    private void calcularCombo()
    {
        if (nSeleccionado > 2)
        {
            String seleccionadosRango = rangosSeleccionados();
            
            if (!seleccionadosRango.isEmpty())
            {
                char[] palo = {'h', 'c', 'd', 's'};
                String[] rango = seleccionadosRango.split(",");
                ArrayList<Jugada> combinaciones = new ArrayList();
                ArrayList<Resultado> resultadoCombinaciones = new ArrayList();
                ArrayList<Carta> board = obtenerBoard();
                ArrayList<Integer> nResultadosCantidad = new ArrayList();
                ArrayList<String> nResultadosRangos = new ArrayList();
                
                for (int i = 0; i < 14; ++i)
                {
                    nResultadosCantidad.add(0);
                    nResultadosRangos.add("");
                }
                
                for (int i = 0; i < rango.length; ++i)
                {
                    if (rango[i].contains("s"))
                    {
                        for (int j = 0; j < 4; ++j)
                        {
                            Carta primeraCarta = new Carta(rango[i].charAt(0), palo[j]);
                            Carta segundaCarta = new Carta(rango[i].charAt(1), palo[j]);

                            if (!existeCartaBoard(primeraCarta, board) && !existeCartaBoard(segundaCarta, board))
                            {
                                Jugada aux = new Jugada(String.valueOf(rango[i].charAt(0)) + String.valueOf(palo[j]) + String.valueOf(rango[i].charAt(1)) + String.valueOf(palo[j]));

                                for (int s = 0; s < board.size(); ++s)
                                    aux.agregarCarta(board.get(s));

                                aux.agregarCarta(primeraCarta);
                                aux.agregarCarta(segundaCarta);
                                combinaciones.add(aux);
                            }
                        }
                    }
                    else if (rango[i].contains("o"))
                    {
                        for (int j = 0; j < 4; ++j)
                        {
                            Carta base = new Carta(rango[i].charAt(0), palo[j]);
                            if (!existeCartaBoard(base, board))
                            {
                                for (int k = 0; k < 4; ++k)
                                {
                                    if (j != k)
                                    {
                                        Carta agregada = new Carta(rango[i].charAt(1), palo[k]);
                                        if (!existeCartaBoard(agregada, board))
                                        {
                                            Jugada aux = new Jugada(String.valueOf(rango[i].charAt(0)) + String.valueOf(palo[j]) + String.valueOf(rango[i].charAt(1)) + String.valueOf(palo[k]));

                                            for (int s = 0; s < board.size(); ++s)
                                                aux.agregarCarta(board.get(s));

                                            aux.agregarCarta(base);
                                            aux.agregarCarta(agregada);
                                            combinaciones.add(aux);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        for (int j = 0; j < 3; ++j)
                        {
                            Carta base = new Carta(rango[i].charAt(0), palo[j]);
                            if (!existeCartaBoard(base, board))
                            {
                                for (int k = j + 1; k < 4; ++k)
                                {
                                    Carta agregada = new Carta(rango[i].charAt(1), palo[k]);
                                    if (!existeCartaBoard(agregada, board))
                                    {
                                        Jugada aux = new Jugada(String.valueOf(rango[i].charAt(0)) + String.valueOf(palo[j]) + String.valueOf(rango[i].charAt(1)) + String.valueOf(palo[k]));

                                        for (int s = 0; s < board.size(); ++s)
                                            aux.agregarCarta(board.get(s));

                                        aux.agregarCarta(base);
                                        aux.agregarCarta(agregada);
                                        combinaciones.add(aux);
                                    }
                                }
                            }
                        }
                    }
                }
                
                for (int i = 0; i < combinaciones.size(); ++i)
                {
                    resultadoCombinaciones.add(combinaciones.get(i).evaluarJugada(0));
                    resultadoCombinaciones.get(i).setRango(combinaciones.get(i).getRango());
                }

                obtenerNPosibilidades(resultadoCombinaciones, board, nResultadosCantidad, nResultadosRangos);
                crearSalida(resultadoCombinaciones, nResultadosCantidad, nResultadosRangos);
            }
            else
                mostrarAlerta("Debes seleccionar algún rango");
        }
        else
            mostrarAlerta("Debes seleccionar más de tres cartas en el board");
    }
    
    private ArrayList<Carta> obtenerBoard()
    {
        ArrayList<Carta> board = new ArrayList<>();
        for (int i = 0; i < tablero_board.length; ++i)
            for (int j = 0; j < tablero_board[0].length; ++j)
                if (tablero_board[i][j].getBackground() == Constante.YELLOW)
                    board.add(new Carta(tablero_board[i][j].getText().charAt(0), tablero_board[i][j].getText().charAt(1)));
        return board;
    }
    
    private boolean existeCartaBoard(Carta buscar, ArrayList<Carta> board)
    {
        boolean aux = false;
        for (int i = 0; i < board.size() && !aux; ++i)
            if (buscar.equal(board.get(i)))
                aux = true;
        return aux;
    }
    
    private void obtenerNPosibilidades(ArrayList<Resultado> resultadoCombinaciones, ArrayList<Carta> board, ArrayList<Integer> nResultadosCantidad, ArrayList<String> nResultadosRangos)
    {
        Carta mayorCarta = board.get(0), segundaMayorCarta = board.get(1);
        Jugada boardJugada = new Jugada();
        
        for(int i = 0; i < board.size(); ++i)
            boardJugada.agregarCarta(board.get(i));
        
        Resultado resultadoMesa = boardJugada.evaluarJugada(0);
        
        for (int i = 0; i < resultadoCombinaciones.size(); ++i)
        {
            if((resultadoCombinaciones.get(i).getResultadoJugada() == 8
                    || resultadoCombinaciones.get(i).getResultadoJugada() == 7
                    || resultadoCombinaciones.get(i).getResultadoJugada() == 6
                    || resultadoCombinaciones.get(i).getResultadoJugada() == 5
                    || resultadoCombinaciones.get(i).getResultadoJugada() == 4
                    || resultadoCombinaciones.get(i).getResultadoJugada() == 3) && resultadoCombinaciones.get(i).getValorJugada() <= resultadoMesa.getValorJugada())
                resultadoCombinaciones.get(i).setResultadoJugada(0);
            else if (resultadoMesa.getResultadoJugada() == 1 && resultadoCombinaciones.get(i).getResultadoJugada() == 2)
                resultadoCombinaciones.get(i).setResultadoJugada(1);
            else if (resultadoMesa.getResultadoJugada() == 1 && resultadoCombinaciones.get(i).getResultadoJugada() == 1)
                resultadoCombinaciones.get(i).setResultadoJugada(0);
            
            if(resultadoCombinaciones.get(i).getResultadoJugada() == 6 && resultadoMesa.getValorJugada() >= resultadoCombinaciones.get(i).getValorJugada())
                resultadoCombinaciones.get(i).setResultadoJugada(0);
            else if (resultadoCombinaciones.get(i).getResultadoJugada() == 6 && resultadoMesa.getResultadoJugada() == resultadoCombinaciones.get(i).getResultadoJugada() && resultadoMesa.getValorJugada() >= resultadoCombinaciones.get(i).getResultadoJugada())
                resultadoCombinaciones.get(i).setResultadoJugada(0);
            
            switch (resultadoCombinaciones.get(i).getResultadoJugada())
            {
                case 0:
                    if(!resultadoCombinaciones.get(i).getRango().contains("A"))
                    {
                        nResultadosCantidad.set(0, nResultadosCantidad.get(0) + 1);
                        nResultadosRangos.set(0, nResultadosRangos.get(0) + resultadoCombinaciones.get(i).getRango() + ",");
                    }
                    else
                    {
                        nResultadosCantidad.set(1, nResultadosCantidad.get(1) + 1);
                        nResultadosRangos.set(1, nResultadosRangos.get(1) + resultadoCombinaciones.get(i).getRango() + ",");
                    }
                    break;
                case 1:
                    Carta primeraCarta = new Carta(resultadoCombinaciones.get(i).getRango().charAt(0), resultadoCombinaciones.get(i).getRango().charAt(1));
                    Carta segundaCarta = new Carta(resultadoCombinaciones.get(i).getRango().charAt(2), resultadoCombinaciones.get(i).getRango().charAt(3));
                    
                    if (primeraCarta.getValor() == segundaCarta.getValor() && primeraCarta.getValor() > mayorCarta.getValor()) // over pair
                    {
                        nResultadosCantidad.set(6, nResultadosCantidad.get(6) + 1);
                        nResultadosRangos.set(6, nResultadosRangos.get(6) + resultadoCombinaciones.get(i).getRango() + ",");
                    }
                    else if (primeraCarta.getValor() == mayorCarta.getValor() || segundaCarta.getValor() == mayorCarta.getValor()) // top pair
                    {
                        nResultadosCantidad.set(5, nResultadosCantidad.get(5) + 1);
                        nResultadosRangos.set(5, nResultadosRangos.get(5) + resultadoCombinaciones.get(i).getRango() + ",");
                    }
                    else if (primeraCarta.getValor() == segundaCarta.getValor() && primeraCarta.getValor() < mayorCarta.getValor() && primeraCarta.getValor() > segundaMayorCarta.getValor())// pp below tp
                    {
                        nResultadosCantidad.set(4, nResultadosCantidad.get(4) + 1);
                        nResultadosRangos.set(4, nResultadosRangos.get(4) + resultadoCombinaciones.get(i).getRango() + ",");
                    }
                    else if (primeraCarta.getValor()  == segundaMayorCarta.getValor() || segundaCarta.getValor()  == segundaMayorCarta.getValor()) // middle pair
                    {
                        nResultadosCantidad.set(3, nResultadosCantidad.get(3) + 1);
                        nResultadosRangos.set(3, nResultadosRangos.get(3) + resultadoCombinaciones.get(i).getRango() + ","); 
                    }
                    else // weak pair
                    {
                        nResultadosCantidad.set(2, nResultadosCantidad.get(2) + 1);
                        nResultadosRangos.set(2, nResultadosRangos.get(2) + resultadoCombinaciones.get(i).getRango() + ","); 
                    }
                    break;
                case 2:
                    nResultadosCantidad.set(7, nResultadosCantidad.get(7) + 1);
                    nResultadosRangos.set(7, nResultadosRangos.get(7) + resultadoCombinaciones.get(i).getRango() + ",");
                    break;
                case 3:
                    nResultadosCantidad.set(8, nResultadosCantidad.get(8) + 1);
                    nResultadosRangos.set(8, nResultadosRangos.get(8) + resultadoCombinaciones.get(i).getRango() + ",");
                    break;
                case 4:
                    nResultadosCantidad.set(9, nResultadosCantidad.get(9) + 1);
                    nResultadosRangos.set(9, nResultadosRangos.get(9) + resultadoCombinaciones.get(i).getRango() + ",");
                    break;
                case 5:
                    nResultadosCantidad.set(10, nResultadosCantidad.get(10) + 1);
                    nResultadosRangos.set(10, nResultadosRangos.get(10) + resultadoCombinaciones.get(i).getRango() + ",");
                    break;
                case 6:
                    nResultadosCantidad.set(11, nResultadosCantidad.get(11) + 1);
                    nResultadosRangos.set(11, nResultadosRangos.get(11) + resultadoCombinaciones.get(i).getRango() + ",");
                    break;
                case 7:
                    nResultadosCantidad.set(12, nResultadosCantidad.get(12) + 1);
                    nResultadosRangos.set(12, nResultadosRangos.get(12) + resultadoCombinaciones.get(i).getRango() + ",");
                    break;
                case 8:
                    nResultadosCantidad.set(13, nResultadosCantidad.get(13) + 1);
                    nResultadosRangos.set(13, nResultadosRangos.get(13) + resultadoCombinaciones.get(i).getRango() + ",");
                    break;
            }
        }
    }
    
    private void crearSalida(ArrayList<Resultado> resultadoCombinaciones, ArrayList<Integer> nResultadosCantidad, ArrayList<String> nResultadosRangos)
    {
        String salida = "Combinaciones: " + resultadoCombinaciones.size() + "\n";
        
        for (int i = 13; i >= 0; --i)
        {
            if(nResultadosCantidad.get(i) == 0)
                salida += "0% - (0) " + comboTexto(i) + "\n";
            else
                salida += (nResultadosCantidad.get(i) * 100 / resultadoCombinaciones.size()) + "% - (" + nResultadosCantidad.get(i) + ") " + comboTexto(i) + nResultadosRangos.get(i).substring(0, nResultadosRangos.get(i).length() - 1) + "\n";
        }
            
        JFrame aux = new JFrame();
        JScrollPane scroll = new JScrollPane(new JTextArea(salida));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        aux.setSize(300, 400);
        aux.setVisible(true);
        aux.add(scroll);
    }
    
    private String comboTexto(int tipo)
    {
        String salida = null;
        switch (tipo)
        {
            case 0:
                salida = "no made hand: ";
                break;
            case 1:
                salida = "ace high: ";
                break;
            case 2:
                salida = "weak pair: ";
                break;
            case 3:
                salida = "middle pair: ";
                break;
            case 4:
                salida = "pp below tp: ";
                break;
            case 5:
                salida = "top pair: ";
                break;
            case 6:
                salida = "over pair: ";
                break;
            case 7:
                salida = "two pair: ";
                break;
            case 8:
                salida = "3 of a kind: ";
                break;
            case 9:
                salida = "straight: ";
                break;
            case 10:
                salida = "flush: ";
                break;
            case 11:
                salida = "full house: ";
                break;
            case 12:
                salida = "quads: ";
                break;
            case 13:
                salida = "str,flush: ";
                break;
        }
        return salida;
    }
    
    private void mostrarAlerta(String mensaje)
    {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void reset()
    {
        nSeleccionado = 0;
        
        for(int i = 0; i < 13; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                switch(j)
                {
                    case 0:
                        tablero_board[i][j].setBackground(Constante.RED);
                        break;
                    case 1:
                        tablero_board[i][j].setBackground(Constante.GREEN);
                        break;
                    case 2:
                        tablero_board[i][j].setBackground(Constante.LIGHT_BLUE);
                        break;
                    case 3:
                        tablero_board[i][j].setBackground(Constante.GRAY);
                }
            }
        }
    }
    
    private String rangosSeleccionados()
    {
        String aux = "";

        for (int i = 0; i < 13; ++i)
            for (int j = 0; j < 13; ++j)
                if (tablero_rango[i][j].getBackground() == Constante.YELLOW || tablero_rango[i][j].getBackground() == Constante.MAGENTA)
                    aux += tablero_rango[i][j].getText() + ",";

        if(!aux.isEmpty())
            aux = aux.substring(0, aux.length() - 1);
        
        return aux;
    }
    
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
            if(tablero_board[i][j].getBackground() != Constante.YELLOW)
            {
                if (nSeleccionado < 5)
                {
                    nSeleccionado++;
                    tablero_board[i][j].setBackground(Constante.YELLOW);
                }
            }
            else 
            {
                nSeleccionado--;
                switch(j)
                {
                    case 0:
                        tablero_board[i][j].setBackground(Constante.RED);
                        break;
                    case 1:
                        tablero_board[i][j].setBackground(Constante.GREEN);
                        break;
                    case 2:
                        tablero_board[i][j].setBackground(Constante.LIGHT_BLUE);
                        break;
                    case 3:
                        tablero_board[i][j].setBackground(Constante.GRAY);
                }
            }
        }
    }    
}