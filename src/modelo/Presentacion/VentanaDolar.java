
package modelo.Presentacion;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import modelo.Datos.ConexionDB;

	public class VentanaDolar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public JDialog frame;
		public JPanel  panelDto;
		private JLabel				lbl3,lbl4;
		private JButton  			buttonGrabar,buttonSalir;
		protected static String dateEmision;
		protected static JTextField 			textDolarC,textDolarV;
		
		// constructor
		public VentanaDolar() {
			frameDolar();
			crearPanel();
			crearButtons();
			crearTextFields();
			crearLabels();
			buscarIGV();
		}
		void buscarIGV() {
			conexion = new ConexionDB();
			try {
				Statement statement =conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select* from VARIABLES");
				while (resultSet.next()==true) {
					textDolarC.setText(resultSet.getString("DOLARC"));
					textDolarV.setText(resultSet.getString("DOLARV"));
				}
			} catch (Exception e) {}
		}
		public void frameDolar() {
			frame = new JDialog();
			frame.setTitle("COTIZACI\u00D3N DOLAR $:                              ");
			frame.setBounds(100, 100, 331, 135);
			frame.getContentPane().setLayout(null);
			frame.setModal(true);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
		}
		public void crearPanel() {
			panelDto= new JPanel();
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Tipo de cambio:", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelDto.setBounds(10, 11, 299, 69);
			frame.getContentPane().add(panelDto);
			panelDto.setLayout(null);
		}
		public void crearLabels() {
			
			lbl3= new JLabel("Venta:");
			lbl3.setBounds(120, 24, 81, 14);
			panelDto.add(lbl3);
			lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl3.setFont(Menu.fontLabel);
		
			lbl4= new JLabel("Compra:");
			lbl4.setBounds(26, 24, 86, 14);
			panelDto.add(lbl4);
			lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl4.setFont(Menu.fontLabel);
		}
		public void crearTextFields(){
			
			textDolarC= new JTextField();
			textDolarC.setColumns(10);
			textDolarC.setFont(Menu.fontText);
			textDolarC.setForeground(Menu.textColorForegroundInactivo);
			textDolarC.setHorizontalAlignment(SwingConstants.RIGHT);
			textDolarC.addActionListener(this);
			textDolarC.addFocusListener(this);
			textDolarC.addKeyListener(this);
			textDolarC.setBounds(26, 37, 86, 22);
			panelDto.add(textDolarC);

			textDolarV= new JTextField();
			textDolarV.setColumns(10);
			textDolarV.setFont(Menu.fontText);
			textDolarV.setForeground(Menu.textColorForegroundInactivo);
			textDolarV.setHorizontalAlignment(SwingConstants.RIGHT);
			textDolarV.addActionListener(this);
			textDolarV.addFocusListener(this);
			textDolarV.addKeyListener(this);
			textDolarV.setBounds(115, 37, 86, 22);
			panelDto.add(textDolarV);
		}
		public void crearButtons() {
			
			buttonGrabar= new JButton("");
			buttonGrabar.setToolTipText("Actualizar datos");
			buttonGrabar.addActionListener(this);
			buttonGrabar.setBounds(208, 36, 36, 23);
			buttonGrabar.setIcon(new ImageIcon(VentanaDolar.class.getResource("/modelo/Images/Refresh.png")));
			panelDto.add(buttonGrabar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(248, 36, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaDolar.class.getResource("/modelo/Images/Exit.png")));
			panelDto.add(buttonSalir);
		}
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonGrabar){// GRABAR
				  insertarUpdate();
				  }
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }
			}
			public void insertarUpdate() {
				if (textDolarC.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textDolarC.requestFocus();
					return;
				}
				if (textDolarV.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textDolarV.requestFocus();
					return;
				}
				conexion = new ConexionDB();
				try {
			         String sql="UPDATE VARIABLES SET DOLARC = ?,"
			                 + "DOLARV = ?";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setString(1,textDolarC.getText().trim());
					ps.setString(2,textDolarV.getText().trim());
					
					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			
			public void focusGained(FocusEvent ev) {
				if (ev.getSource() == textDolarC){textDolarC.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textDolarC){textDolarC.setForeground(Menu.textColorForegroundActivo);}
				
				if (ev.getSource() == textDolarV){textDolarV.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textDolarV){textDolarV.setForeground(Menu.textColorForegroundActivo);}
			}
			public void focusLost(FocusEvent ev) {
				if (ev.getSource() == textDolarC){textDolarC.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textDolarC){textDolarC.setForeground(Menu.textColorForegroundInactivo);}

				if (ev.getSource() == textDolarV){textDolarV.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textDolarV){textDolarV.setForeground(Menu.textColorForegroundInactivo);}
			}
			
			public void keyReleased(KeyEvent evet) {
				char e=evet.getKeyChar();
				if (evet.getSource() == textDolarC){
					if (textDolarC.getText().toLowerCase().isEmpty()|| textDolarC.getText().toLowerCase().length()>8){
						textDolarC.requestFocus();
						textDolarC.selectAll();
						textDolarC.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDolarC.getText().toLowerCase().length()==8){
							textDolarV.requestFocus();
							textDolarV.selectAll();
						}
				}	
				if (evet.getSource() == textDolarV){
					if (textDolarV.getText().toLowerCase().isEmpty()|| textDolarV.getText().toLowerCase().length()>8){
						textDolarV.requestFocus();
						textDolarV.selectAll();
						textDolarV.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){
							buttonGrabar.doClick();
						}
				}
			}

			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO
				char e=evet.getKeyChar();
				if (evet.getSource() == textDolarC){
					if ((e<'0'||e>'9')&&(e<'.'||e>'.'))evet.consume();
				}
				if (evet.getSource() == textDolarV){
					if ((e<'0'||e>'9')&&(e<'.'||e>'.'))evet.consume();
				}
			}
			
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			//Object source = e.getSource();
		}
		
		@Override
		public void keyPressed(KeyEvent evet) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent Mouse_evt) {
			// TODO Auto-generated method stub

		}
		}
