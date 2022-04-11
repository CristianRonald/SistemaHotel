

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
import javax.swing.JComboBox;
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

	public class VentanaDocumentoSeries implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public JDialog frame;
		public JPanel  panelDto,panelLst;
		private JLabel				lbl1,lbl2,lbl3,lbl4;
		private JButton  			buttonNuevo,buttonGrabar,buttonSalir,buttonEliminar;
		protected static String dateEmision;
		protected static JTextField 			textCod,textSerie,textNumero;
		protected static JComboBox<String> 		cbDocumento;
		
		public static int MOD;
		
		private JScrollPane scrollTable;
		private JTable tableList;
		private DefaultTableModel model;
		
		NumberFormat formatPrecio;
		private String consultar="";
		private int id=0;private String descripcion="",ID_DOCUMENTO="";
		
		// constructor
		public VentanaDocumentoSeries() {
			frameSeries();
			crearPanel();
			crearButtons();
			crearTextFields();
			crearLabels();
			crearTable();
			llenarcbDocumento();
			llenarNuevo();
			llenarTable();
			MOD=0;
		}
		public void frameSeries() {
			frame = new JDialog();
			frame.setTitle("Series correlativas:                                                       ");
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaDocumentoSeries.class.getResource("/modelo/Images/Diagram.png")));
			frame.setBounds(100, 100, 412, 355);
			frame.getContentPane().setLayout(null);
			frame.setModal(true);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
		}
		public void crearPanel() {
			panelDto= new JPanel();
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
			panelDto.setBounds(10, 11, 391, 140);
			frame.getContentPane().add(panelDto);
			panelDto.setLayout(null);
			
			panelLst= new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 152, 391, 166);
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
		}
		
		public void llenarNuevo() {
			conexion = new ConexionDB();
			try {
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select Id_S from SERIES order by Id_S desc limit 0,1");
				if (resultSet.next()== true) {
					int id=(Integer.parseInt(resultSet.getString("Id_S"))+1);
					textCod.setText(Menu.formatid_4.format(id));
				}else {
					textCod.setText(Menu.formatid_4.format(0001));
				}
				MOD=0;//PERMITE GRABAR NUEVO
				statement.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}

		public void crearLabels() {
			lbl1= new JLabel("Código");
			lbl1.setBounds(21, 28, 73, 14);
			panelDto.add(lbl1);
			lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl1.setFont(Menu.fontLabel);
			
			lbl2= new JLabel("Documento:");
			lbl2.setBounds(12, 54, 82, 14);
			panelDto.add(lbl2);
			lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl2.setFont(Menu.fontLabel);
			
			lbl3= new JLabel("Correlativo:");
			lbl3.setBounds(21, 105, 73, 14);
			panelDto.add(lbl3);
			lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl3.setFont(Menu.fontLabel);
		
			lbl4= new JLabel("Serie:");
			lbl4.setBounds(12, 76, 82, 14);
			panelDto.add(lbl4);
			lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl4.setFont(Menu.fontLabel);
		}
		public void crearTextFields(){
			textCod = new JTextField();
			textCod.setEditable(false);
			textCod.setColumns(10);
			textCod.setFont(Menu.fontText);
			textCod.setForeground(Menu.textColorForegroundInactivo);
			textCod.setHorizontalAlignment(SwingConstants.CENTER);
			textCod.addActionListener(this);
			textCod.addFocusListener(this);
			textCod.addKeyListener(this);
			textCod.setBounds(104, 25, 75, 22);
			panelDto.add(textCod);
			
			cbDocumento = new JComboBox<>();
			cbDocumento.setBounds(104, 51, 262, 21);
			cbDocumento.setFont(Menu.fontText);
			cbDocumento.addActionListener(this);
			cbDocumento.addFocusListener(this);
			cbDocumento.addKeyListener(this);
			panelDto.add(cbDocumento);
			
			textSerie= new JTextField();
			textSerie.setColumns(10);
			textSerie.setFont(Menu.fontText);
			textSerie.setForeground(Menu.textColorForegroundInactivo);
			textSerie.setHorizontalAlignment(SwingConstants.LEFT);
			textSerie.addActionListener(this);
			textSerie.addFocusListener(this);
			textSerie.addKeyListener(this);
			textSerie.setBounds(104, 76, 75, 22);
			panelDto.add(textSerie);

			textNumero= new JTextField();
			textNumero.setColumns(10);
			textNumero.setFont(Menu.fontText);
			textNumero.setForeground(Menu.textColorForegroundInactivo);
			textNumero.setHorizontalAlignment(SwingConstants.LEFT);
			textNumero.addActionListener(this);
			textNumero.addFocusListener(this);
			textNumero.addKeyListener(this);
			textNumero.setBounds(104, 101, 108, 22);
			panelDto.add(textNumero);
		}
		public void crearButtons() {
			buttonNuevo= new JButton("");
			buttonNuevo.setToolTipText("Nuevo ítem");
			buttonNuevo.addActionListener(this);
			buttonNuevo.setBounds(215, 100, 36, 23);
			buttonNuevo.setIcon(new ImageIcon(VentanaDocumentoSeries.class.getResource("/modelo/Images/nuevo.png")));
			panelDto.add(buttonNuevo);
			
			buttonGrabar= new JButton("");
			buttonGrabar.setToolTipText("Grabar ítem");
			buttonGrabar.addActionListener(this);
			buttonGrabar.setBounds(254, 100, 36, 23);
			buttonGrabar.setIcon(new ImageIcon(VentanaDocumentoSeries.class.getResource("/modelo/Images/save.png")));
			panelDto.add(buttonGrabar);
			
			buttonEliminar= new JButton("");
			buttonEliminar.setToolTipText("Eliminar ítem");
			buttonEliminar.addActionListener(this);
			buttonEliminar.setBounds(292, 100, 36, 23);
			buttonEliminar.setIcon(new ImageIcon(VentanaDocumentoSeries.class.getResource("/modelo/Images/delete.png")));
			panelDto.add(buttonEliminar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Regresar");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(330, 100, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaDocumentoSeries.class.getResource("/modelo/Images/Exit.png")));
			panelDto.add(buttonSalir);
		}
		public void crearTable() {
			tableList = new JTable();
			tableList.addMouseListener(this);
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 20, 527, 146);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 20, 371, 135);
	        panelLst.add(scrollTable);
	        tableList.setGridColor(new Color(238, 232, 170));
		}
		
		public static void llenarcbDocumento() { 
			conexion = new ConexionDB();
			cbDocumento.removeAllItems();
			try {
				Statement statement = conexion.gConnection().createStatement(); 
				ResultSet reSet = statement.executeQuery("Select * from DOCUMENTO");
				while (reSet.next()==true) {
					cbDocumento.addItem(reSet.getString("TipoDoc"));
				}
				cbDocumento.setSelectedIndex(-1);
			} catch (Exception e) {}
		}
		
		protected void llenarTable() {
			conexion = new ConexionDB();
			LimpiarTable();
	        try {
		     	//if (textBuscar.getText().isEmpty()) {
		     		consultar="Select * from DOCUMENTO as D,SERIES as S where S.Id_Doc=D.Id_Doc";
		     	 //}else{
					//consultar="Select * from SERIES where Id_S like '" +textBuscar.getText()+"%'";
		     	 //}
	     	   int totalitems=0;
	     	   model= new DefaultTableModel();
			   model.addColumn("Código");
			   model.addColumn("Id Docu");
			   model.addColumn("Documento");
			   model.addColumn("Serie");
			   model.addColumn("Correlativo");
			   
	 		   String []datos= new String[5];
	 		   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	 		   tableList.setModel(model);
	 		   
	           tableList.getColumnModel().getColumn(0).setMinWidth(0);
	           tableList.getColumnModel().getColumn(0).setMaxWidth(0);
	           tableList.getColumnModel().getColumn(1).setMinWidth(0);
	           tableList.getColumnModel().getColumn(1).setMaxWidth(0);
	           
	    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(30);
	    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(80);
	    	   tableList.getColumnModel().getColumn(2).setPreferredWidth(200);
	    	   tableList.getColumnModel().getColumn(3).setPreferredWidth(40);
	    	   tableList.getColumnModel().getColumn(4).setPreferredWidth(80);
	    	   
	    	   while(rs.next()) {
			    	datos[0]=Menu.formatid_4.format(rs.getInt("Id_S"));
			    	datos[1]=rs.getString("Id_Doc");
			    	datos[2]=rs.getString("TipoDoc");
			    	datos[3]=Menu.formatid_4.format(rs.getInt("Serie"));
			    	datos[4]=Menu.formatid_9.format(rs.getInt("NumeroSerie"));
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
			textSerie.setText(null);
			textNumero.setText(null);
			
			textSerie.setBackground(Menu.textColorBackgroundInactivo);	
			textSerie.setForeground(Menu.textColorForegroundInactivo);
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
			textSerie.requestFocus();
		}
		
		public void activarTexts(boolean b) {
			textCod.setEnabled(b);
			textSerie.setEnabled(b);
			textNumero.setEnabled(b);
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
			if (id==0){
				JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			}
			try {
				conexion = new ConexionDB();
				String query ="Select * from DOCUMENTO as D,ALQUILER as A where A.Id_Doc=D.Id_Doc and D.Id_Doc='" + ID_DOCUMENTO  + "'";
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
			
			
			int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar la serie?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (respuesta == JOptionPane.YES_OPTION) {
				try {
					conexion = new ConexionDB();
					String query ="Delete from SERIES where Id_S='" + id  + "'";
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
			  if (evento.getSource() == cbDocumento){// CBDOCUMENTO
				  conexion = new ConexionDB();
					try {
						Statement statement = conexion.gConnection().createStatement();
						ResultSet reSet = statement.executeQuery("Select * from DOCUMENTO where TipoDoc='" + cbDocumento.getSelectedItem() + "'");
						ID_DOCUMENTO="";
						if (reSet.next()==true) {
							ID_DOCUMENTO = reSet.getString("Id_Doc");
						}
					} catch (Exception e) {}
				  }
			  if (evento.getSource() == buttonNuevo){// NUEVO
					limpiarTexts();
					llenarNuevo();
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
				if (cbDocumento.getSelectedItem()==null || cbDocumento.getSelectedItem()==""){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbDocumento.requestFocus();
					return;
				}
				if (textSerie.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textSerie.requestFocus();
					return;
				}
				if (textNumero.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textNumero.requestFocus();
					return;
				}
				conexion = new ConexionDB();
				if (MOD==0) {// REGISTRAR
					try {
						String sql ="INSERT INTO  SERIES (Id_S,Id_Doc,Serie,NumeroSerie) VALUES (?,?,?,?)";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setInt(1, Integer.parseInt(textCod.getText()));
						ps.setString(2,ID_DOCUMENTO);//(String)cbDocumento.getSelectedItem());
						ps.setString(3,textSerie.getText().trim());
						ps.setString(4,textNumero.getText().trim());
						ps.execute();
						ps.close();
						JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						limpiarTexts();
						llenarNuevo();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
				}
				if (MOD==1) { // MODIFICAR 
					try {
				         String sql="UPDATE SERIES SET Id_S = ?,"
				        		 + "Id_Doc = ?,"
				                 + "Serie = ?,"
				                 + "NumeroSerie = ?"
				                 + "WHERE Id_S = '"+Integer.parseInt(textCod.getText())+"'"; 
						
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setInt(1, Integer.parseInt(textCod.getText()));
						ps.setString(2,ID_DOCUMENTO);//(String)cbDocumento.getSelectedItem());
						ps.setString(3,textSerie.getText().trim());
						ps.setString(4,textNumero.getText().trim());
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
				if (ev.getSource() == cbDocumento){cbDocumento.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == cbDocumento){cbDocumento.setForeground(Menu.textColorForegroundActivo);}
				if (ev.getSource() == textSerie){textSerie.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textSerie){textSerie.setForeground(Menu.textColorForegroundActivo);}
				
				if (ev.getSource() == textNumero){textNumero.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textNumero){textNumero.setForeground(Menu.textColorForegroundActivo);}
			}
			public void focusLost(FocusEvent ev) {
				if (ev.getSource() == cbDocumento){cbDocumento.setBackground(new Color(240,240,240));}
				if (ev.getSource() == cbDocumento){cbDocumento.setForeground(Menu.textColorForegroundInactivo);}
				if (ev.getSource() == textSerie){textSerie.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textSerie){textSerie.setForeground(Menu.textColorForegroundInactivo);}

				if (ev.getSource() == textNumero){textNumero.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textNumero){textNumero.setForeground(Menu.textColorForegroundInactivo);}
			}
			
			public void keyReleased(KeyEvent evet) {
				char e=evet.getKeyChar();
				if (evet.getSource() == cbDocumento){
					if (e==KeyEvent.VK_ENTER){
						if (cbDocumento.getSelectedIndex()!=-1){
							textSerie.requestFocus();
						}else{
							cbDocumento.requestFocus();
						}
					}	
				}
				if (evet.getSource() == textSerie){
					if (textSerie.getText().toLowerCase().isEmpty()|| textSerie.getText().toLowerCase().length()>6){
						textSerie.requestFocus();
						textSerie.selectAll();
						textSerie.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textSerie.getText().toLowerCase().length()==6){
							textNumero.requestFocus();
							textNumero.selectAll();
						}
				}	
				if (evet.getSource() == textNumero){
					if (textNumero.getText().toLowerCase().isEmpty()|| textNumero.getText().toLowerCase().length()>9){
						textNumero.requestFocus();
						textNumero.selectAll();
						textNumero.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){
							buttonGrabar.doClick();
						}
				}	
			}

			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO
				char e=evet.getKeyChar();
				if (evet.getSource() == textSerie){
					if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
				}
				if (evet.getSource() == textNumero){
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
			id=0;descripcion="";
			try {
				if (Mouse_evt.getSource().equals(tableList)) {
					id=Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),0).toString());
					ID_DOCUMENTO=tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					if (id==0){
						tableList.requestFocus();
						return;
					}
					descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						textCod.setText(Menu.formatid_4.format(id));
						cbDocumento.setSelectedItem(descripcion);
						textSerie.setText(tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim());
						textNumero.setText(tableList.getValueAt(tableList.getSelectedRow(),4).toString().trim());
						panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos - Modificando", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(165, 42, 42)));
						MOD=1; // PERMITE MODIFICAR
					}
				}
			} catch (Exception e) {}
		}
}	