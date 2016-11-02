package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public final class ClientAgentGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFrame clientFrame;
	private JPanel clientPanel;
	private JTextField manufacturerField, modeloField, entregaField;
	private JLabel manufacturerLabel, modeloLabel, entregaLabel;
	private JButton encontrarProducto;
	private ClientAgent clientAgent;
	
// Constructor: crea la Interfaz grafica
   public ClientAgentGUI(ClientAgent clientAgent) {
      JFrame.setDefaultLookAndFeelDecorated(true);
      this.clientAgent = clientAgent;
      // Crea y ajusta los valores de la ventana.
      clientFrame = new JFrame("---Encuentra el producto más barato---");
      clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      clientFrame.setSize(new Dimension(140, 60));
      clientFrame.setLocation(340,340);
      // Crea y ajusta los valores del panel.
      clientPanel = new JPanel(new GridLayout(4, 2));
      //Añade los widgets.
      addWidgets();
      //Establece el boton default.
      clientFrame.getRootPane().setDefaultButton(encontrarProducto);
      //AAñade el panel a la ventana.
      clientFrame.getContentPane().add(clientPanel, BorderLayout.CENTER);
      //Muestra la ventana
      clientFrame.pack();
      clientFrame.setVisible(true);
   }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	// Añade los widgets al JFrame
	private void addWidgets() {
		// Crea los widgets.
		manufacturerField = new JTextField("Cisco");
		modeloField = new JTextField("123A");
		entregaField = new JTextField("2");
		manufacturerLabel = new JLabel("Manufacturer", SwingConstants.LEFT);
		modeloLabel = new JLabel("Modelo", SwingConstants.LEFT);
		entregaLabel = new JLabel("Tiempo de entrega (dias)", SwingConstants.LEFT);
		encontrarProducto = new JButton("Buscar Producto");
		// Escucha a los eventos del boton Convert button.
		encontrarProducto.addActionListener(this);
		// Añade los widgets al container.
		clientPanel.add(manufacturerLabel);
		clientPanel.add(manufacturerField);
		clientPanel.add(modeloLabel);
		clientPanel.add(modeloField);
		clientPanel.add(entregaLabel);
		clientPanel.add(entregaField);
		clientPanel.add(encontrarProducto);
		manufacturerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		modeloLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		entregaLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	}
	
	// Muestra una ventana con el mensaje recibido
	public void showDialog(String msg) {
		JOptionPane.showMessageDialog(clientFrame, msg);
	}
}
