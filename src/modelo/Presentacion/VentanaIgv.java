
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

	public class VentanaIgv implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public JDialog frame;
		public JPanel  panelDto;
		private JLabel				lbl3,lbl4;
		private JButton  			buttonGrabar,buttonSalir;
		protected static String dateEmision;
		protected static JTextField 			textIgv,textRenta;
		
		// constructor
		public VentanaIgv() {
			frameIgv();
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
				if (resultSet.next()==true) {
					textIgv.setText(Menu.formateadorCurrency.format(resultSet.getFloat("IGV")));
					textRenta.setText(Menu.formateadorCurrency.format(resultSet.getFloat("RENTA")));
				}
			} catch (Exception e) {}
		}
		public void frameIgv() {
			frame = new JDialog();
			frame.setTitle("IGV / RENTA:                                          ");
			frame.setBounds(100, 100, 331, 135);
			frame.getContentPane().setLayout(null);
			frame.setModal(true);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
		}
		public void crearPanel() {
			panelDto= new JPanel();
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Impuestos:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
			panelDto.setBounds(10, 11, 299, 69);
			frame.getContentPane().add(panelDto);
			panelDto.setLayout(null);
		}
		public void crearLabels() {
			
			lbl3= new JLabel("RENTA%:");
			lbl3.setBounds(153, 24, 48, 14);
			panelDto.add(lbl3);
			lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl3.setFont(Menu.fontLabel);
		
			lbl4= new JLabel("IGV %:");
			lbl4.setBounds(54, 24, 58, 14);
			panelDto.add(lbl4);
			lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl4.setFont(Menu.fontLabel);
		}
		public void crearTextFields(){
			
			textIgv= new JTextField();
			textIgv.setColumns(10);
			textIgv.setFont(Menu.fontText);
			textIgv.setForeground(Menu.textColorForegroundInactivo);
			textIgv.setHorizontalAlignment(SwingConstants.CENTER);
			textIgv.addActionListener(this);
			textIgv.addFocusListener(this);
			textIgv.addKeyListener(this);
			textIgv.setBounds(26, 37, 86, 22);
			panelDto.add(textIgv);

			textRenta= new JTextField();
			textRenta.setColumns(10);
			textRenta.setFont(Menu.fontText);
			textRenta.setForeground(Menu.textColorForegroundInactivo);
			textRenta.setHorizontalAlignment(SwingConstants.CENTER);
			textRenta.addActionListener(this);
			textRenta.addFocusListener(this);
			textRenta.addKeyListener(this);
			textRenta.setBounds(115, 37, 86, 22);
			panelDto.add(textRenta);
		}
		public void crearButtons() {
			
			buttonGrabar= new JButton("");
			buttonGrabar.setToolTipText("Actualizar datos");
			buttonGrabar.addActionListener(this);
			buttonGrabar.setBounds(208, 36, 36, 23);
			buttonGrabar.setIcon(new ImageIcon(VentanaIgv.class.getResource("/modelo/Images/Refresh.png")));
			panelDto.add(buttonGrabar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(248, 36, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaIgv.class.getResource("/modelo/Images/Exit.png")));
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
				if (textIgv.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textIgv.requestFocus();
					return;
				}
				if (textRenta.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textRenta.requestFocus();
					return;
				}
				conexion = new ConexionDB();
				try {
			         String sql="UPDATE VARIABLES SET IGV = ?,"
			                 + "RENTA = ?";
			                 //+ "WHERE IGV = '"+Integer.parseInt(textIgv.getText())+"'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setFloat(1, Float.parseFloat(textIgv.getText().replaceAll(",", "")));
					ps.setFloat(2, Float.parseFloat(textRenta.getText().replaceAll(",", "")));
					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					Menu.llenarDollarIgv();
					frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			
			public void focusGained(FocusEvent ev) {
				if (ev.getSource() == textIgv){textIgv.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textIgv){textIgv.setForeground(Menu.textColorForegroundActivo);}
				
				if (ev.getSource() == textRenta){textRenta.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textRenta){textRenta.setForeground(Menu.textColorForegroundActivo);}
			}
			public void focusLost(FocusEvent ev) {
				if (ev.getSource() == textIgv){textIgv.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textIgv){textIgv.setForeground(Menu.textColorForegroundInactivo);}

				if (ev.getSource() == textRenta){textRenta.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textRenta){textRenta.setForeground(Menu.textColorForegroundInactivo);}
			}
			
			public void keyReleased(KeyEvent evet) {
				char e=evet.getKeyChar();
				if (evet.getSource() == textIgv){
					if (textIgv.getText().toLowerCase().isEmpty()|| textIgv.getText().toLowerCase().length()>6){
						textIgv.requestFocus();
						textIgv.selectAll();
						textIgv.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textIgv.getText().toLowerCase().length()==6){
							textRenta.requestFocus();
							textRenta.selectAll();
						}
				}	
				if (evet.getSource() == textRenta){
					if (textRenta.getText().toLowerCase().isEmpty()|| textRenta.getText().toLowerCase().length()>6){
						textRenta.requestFocus();
						textRenta.selectAll();
						textRenta.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){
							buttonGrabar.doClick();
						}
				}
			}

			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO
				char e=evet.getKeyChar();
				if (evet.getSource() == textIgv){
					if ((e<'0'||e>'9')&&(e<'.'||e>'.'))evet.consume();
				}
				if (evet.getSource() == textRenta){
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
