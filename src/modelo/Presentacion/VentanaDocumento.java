

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
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;
import java.awt.Toolkit;

	public class VentanaDocumento implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public JDialog frame;
		public JPanel  panelDto,panelLst;
		private JLabel				lbl1,lbl2,lbl4;
		private JButton  			buttonNuevo,buttonGrabar,buttonSalir,buttonEliminar;
		protected static String dateEmision;
		protected static JTextField 			textCod,textDocumento,textSunat;
		
		static int CONTAR_VENTANA_CATEGORIA=0;
		public static int MOD;
		
		private JScrollPane scrollTable;
		private JTable tableList;
		private DefaultTableModel model;
		
		NumberFormat formatPrecio;
		private String consultar="";
		private String id="",descripcion="";
		
		// constructor
		public VentanaDocumento() {
			frameDocumento();
			crearPanel();
			crearButtons();
			crearTextFields();
			crearLabels();
			crearTable();
			llenarTable();
			MOD=0;
		}
		public void frameDocumento() {
			frame = new JDialog();
			frame.setTitle("Documentos de pago:                                                          ");
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaDocumento.class.getResource("/modelo/Images/Script.png")));
			frame.setBounds(100, 100, 443, 340);
			frame.getContentPane().setLayout(null);
			frame.setModal(true);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
		}
		public void crearPanel() {
			panelDto= new JPanel();
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
			panelDto.setBounds(10, 11, 408, 112);
			frame.getContentPane().add(panelDto);
			panelDto.setLayout(null);
			
			panelLst= new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 124, 408, 166);
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
		}
		public void crearLabels() {
			lbl1= new JLabel("Código");
			lbl1.setBounds(21, 28, 75, 14);
			panelDto.add(lbl1);
			lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl1.setFont(Menu.fontLabel);
			
			lbl2= new JLabel("Documento:");
			lbl2.setBounds(12, 54, 84, 14);
			panelDto.add(lbl2);
			lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl2.setFont(Menu.fontLabel);
		
			lbl4= new JLabel("Sunat:");
			lbl4.setBounds(12, 76, 84, 14);
			panelDto.add(lbl4);
			lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl4.setFont(Menu.fontLabel);
		}
		public void crearTextFields(){
			textCod = new JTextField();
			textCod.setColumns(10);
			textCod.setFont(Menu.fontText);
			textCod.setForeground(Menu.textColorForegroundInactivo);
			textCod.setHorizontalAlignment(SwingConstants.CENTER);
			textCod.addActionListener(this);
			textCod.addFocusListener(this);
			textCod.addKeyListener(this);
			textCod.setBounds(104, 25, 75, 22);
			panelDto.add(textCod);
			
			textDocumento= new JTextField();
			textDocumento.setColumns(10);
			textDocumento.setFont(Menu.fontText);
			textDocumento.setForeground(Menu.textColorForegroundInactivo);
			textDocumento.setHorizontalAlignment(SwingConstants.LEFT);
			textDocumento.addActionListener(this);
			textDocumento.addFocusListener(this);
			textDocumento.addKeyListener(this);
			textDocumento.setBounds(104, 50, 266, 22);
			panelDto.add(textDocumento);

			textSunat= new JTextField();
			textSunat.setColumns(10);
			textSunat.setFont(Menu.fontText);
			textSunat.setForeground(Menu.textColorForegroundInactivo);
			textSunat.setHorizontalAlignment(SwingConstants.LEFT);
			textSunat.addActionListener(this);
			textSunat.addFocusListener(this);
			textSunat.addKeyListener(this);
			textSunat.setBounds(104, 76, 114, 22);
			panelDto.add(textSunat);
		}
		public void crearButtons() {
			buttonNuevo= new JButton("");
			buttonNuevo.setToolTipText("Nuevo ítem");
			buttonNuevo.addActionListener(this);
			buttonNuevo.setBounds(219, 75, 36, 23);
			buttonNuevo.setIcon(new ImageIcon(VentanaDocumento.class.getResource("/modelo/Images/nuevo.png")));
			panelDto.add(buttonNuevo);
			
			buttonGrabar= new JButton("");
			buttonGrabar.setToolTipText("Grabar ítem");
			buttonGrabar.addActionListener(this);
			buttonGrabar.setBounds(258, 75, 36, 23);
			buttonGrabar.setIcon(new ImageIcon(VentanaDocumento.class.getResource("/modelo/Images/save.png")));
			panelDto.add(buttonGrabar);
			
			buttonEliminar= new JButton("");
			buttonEliminar.setToolTipText("Eliminar ítem");
			buttonEliminar.addActionListener(this);
			buttonEliminar.setBounds(296, 75, 36, 23);
			buttonEliminar.setIcon(new ImageIcon(VentanaDocumento.class.getResource("/modelo/Images/delete.png")));
			panelDto.add(buttonEliminar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Regresar");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(334, 75, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaDocumento.class.getResource("/modelo/Images/Exit.png")));
			panelDto.add(buttonSalir);
		}
		public void crearTable() {
			tableList = new JTable();
			tableList.addMouseListener(this);
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 20, 527, 146);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 20, 381, 135);
	        panelLst.add(scrollTable);
	        tableList.setGridColor(new Color(238, 232, 170));
		}
		
		protected void llenarTable() {
			conexion = new ConexionDB();
			LimpiarTable();
	        try {
		     	//if (textSunat.getText().isEmpty()) {
		     		consultar="Select * from DOCUMENTO";
		     	 //}else{
					//consultar="Select * from DOCUMENTO where TipoDoc like '" +textSunat.getText()+"%'";
		     	 //}
	     	   int totalitems=0;
	     	   model= new DefaultTableModel();
			   model.addColumn("Código");
			   model.addColumn("Documento");
			   model.addColumn("Sunat");
			   
	 		   String []datos= new String[3];
	 		   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	 		   tableList.setModel(model);
	 		   
	    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(50);
	    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(250);
	    	   tableList.getColumnModel().getColumn(2).setPreferredWidth(30);
	    	   while(rs.next()) {
			    	datos[0]=rs.getString("Id_Doc");
			    	datos[1]=rs.getString("TipoDoc");
			    	datos[2]=rs.getString("Id_Sunat");
			    	
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
		        }
	           rs.close();
	           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
			} catch (SQLException e) {
				//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		void LimpiarTable(){
			try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
		}
		
		public void limpiarTexts() {
			textCod.setText(null);
			textDocumento.setText(null);
			textSunat.setText(null);
			textDocumento.setBackground(Menu.textColorBackgroundInactivo);	
			textDocumento.setForeground(Menu.textColorForegroundInactivo);
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
			textDocumento.requestFocus();
		}
		
		public void activarTexts(boolean b) {
			textCod.setEnabled(b);
			textDocumento.setEnabled(b);
			textSunat.setEnabled(b);
		}
		public void activarButton(boolean c) {
			 if (c == true){
				buttonGrabar.setEnabled(false);// GRABAR
				buttonSalir.setEnabled(true);	// SALIR

				panelDto.setVisible(true);// PANEL DATOS
			 }
			 if (c == false){
				buttonGrabar.setEnabled(true);// GRABAR
				buttonSalir.setEnabled(false);	// SALIR
				
				panelDto.setVisible(true);// PANEL DATOS
			 }
		}
		
		//	METODO ELIMINAR
		public void delete() {
			if (id==""){
				JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			}
			try {
				conexion = new ConexionDB();
				String query ="Select * from DOCUMENTO as D,ALQUILER as A where A.Id_Doc=D.Id_Doc and D.Id_Doc='" + id  + "'";
				Statement statement =  conexion.gConnection().createStatement();
				ResultSet rs=statement.executeQuery(query);
				if (rs.next()==true) {
					JOptionPane.showMessageDialog(null, "No se permite eliminar el ítem " + id +" "+ descripcion   +" "+ Menu.separador + "porque existen transacciones registrados con este documento ...!" ,Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					statement.close();
					return;
				}	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el documento?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (respuesta == JOptionPane.YES_OPTION) {
				try {
					conexion = new ConexionDB();
					String query ="Delete from DOCUMENTO where Id_Doc='" + id  + "'";
					Statement statement =  conexion.gConnection().createStatement();
					statement.execute(query);
					JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					statement.close();
					llenarTable();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else if (respuesta == JOptionPane.NO_OPTION) {}
		}
		
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
					limpiarTexts();
					textCod.requestFocus();
					MOD=0;
				  }
			  if (evento.getSource() == buttonGrabar){// GRABAR
				  insertarUpdate();
				  }
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }
			  if (evento.getSource() == buttonEliminar){// ELIMINAR
				  delete();
				  }
			}
			public void insertarUpdate() {
				if (textCod.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textCod.requestFocus();
					return;
				}
				if (textDocumento.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textDocumento.requestFocus();
					return;
				}

				conexion = new ConexionDB();
				if (MOD==0) {// REGISTRAR
					try {
						String sql ="INSERT INTO  DOCUMENTO (Id_Doc,TipoDoc,Id_Sunat) VALUES (?,?,?)";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setString(1, textCod.getText().trim());
						ps.setString(2,textDocumento.getText().trim());
						ps.setString(3,textSunat.getText().trim());
						ps.execute();
						ps.close();
						JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						limpiarTexts();
						textCod.requestFocus();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
				}
				if (MOD==1) { // MODIFICAR 
					try {
				         String sql="UPDATE DOCUMENTO SET Id_Doc = ?,"
				                 + "TipoDoc = ?,"
				                 + "Id_Sunat = ?"
				                 + "WHERE Id_Doc = '"+textCod.getText()+"'"; 
						
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setString(1, textCod.getText().trim());
						ps.setString(2,textDocumento.getText().trim());
						ps.setString(3,textSunat.getText().trim());
						
						ps.executeUpdate();
						ps.close();
						JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						limpiarTexts();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
				}
				llenarTable();
			}
			
			public void focusGained(FocusEvent ev) {
				if (ev.getSource() == textDocumento){textDocumento.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textDocumento){textDocumento.setForeground(Menu.textColorForegroundActivo);}
				
				if (ev.getSource() == textSunat){textSunat.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textSunat){textSunat.setForeground(Menu.textColorForegroundActivo);}
			}
			public void focusLost(FocusEvent ev) {
				if (ev.getSource() == textDocumento){textDocumento.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textDocumento){textDocumento.setForeground(Menu.textColorForegroundInactivo);}

				if (ev.getSource() == textSunat){textSunat.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textSunat){textSunat.setForeground(Menu.textColorForegroundInactivo);}
			}
			
			public void keyReleased(KeyEvent evet) {
				char e=evet.getKeyChar();
				if (evet.getSource() == textCod){
					int pos = textCod.getCaretPosition();textCod.setText(textCod.getText().toUpperCase());textCod.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textCod.getText().toLowerCase().isEmpty()|| textCod.getText().toLowerCase().length()>9){
						textCod.requestFocus();
						textCod.selectAll();
						textCod.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textCod.getText().toLowerCase().length()==9){
							textDocumento.requestFocus();
							textDocumento.selectAll();
						}
				}
				if (evet.getSource() == textDocumento){
					int pos = textDocumento.getCaretPosition();textDocumento.setText(textDocumento.getText().toUpperCase());textDocumento.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textDocumento.getText().toLowerCase().isEmpty()|| textDocumento.getText().toLowerCase().length()>39){
						textDocumento.requestFocus();
						textDocumento.selectAll();
						textDocumento.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDocumento.getText().toLowerCase().length()==39){
							textSunat.requestFocus();
							textSunat.selectAll();
						}
				}	
				if (evet.getSource() == textSunat){
					int pos = textSunat.getCaretPosition();textSunat.setText(textSunat.getText().toUpperCase());textSunat.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textSunat.getText().toLowerCase().isEmpty()|| textSunat.getText().toLowerCase().length()>8){
						textSunat.requestFocus();
						textSunat.selectAll();
						textSunat.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){
							buttonGrabar.doClick();
						}
				}	
			}

			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO
				char e=evet.getKeyChar();
				if (evet.getSource() == textCod){ 
					//if(!Character.isLetter(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
				}
				if (evet.getSource() == textDocumento){ 
					if(!Character.isLetter(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
				}
				if (evet.getSource() == textSunat){ 
					if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
				}
			}
			
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			formatPrecio =NumberFormat.getNumberInstance();
			formatPrecio.setMaximumFractionDigits(3);
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
			id="";descripcion="";
			try {
				if (Mouse_evt.getSource().equals(tableList)) {
					id=tableList.getValueAt(tableList.getSelectedRow(),0).toString();
					if (id==""){
						tableList.requestFocus();
						return;
					}
					descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						textCod.setText(id);
						textDocumento.setText(descripcion);
						textSunat.setText(tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim());
						panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos - Modificando", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(165, 42, 42)));
						MOD=1; // PERMITE MODIFICAR
					}
				}
			} catch (Exception e) {}
		}
}