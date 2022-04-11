package modelo.Presentacion;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;
import java.awt.event.WindowFocusListener;

public class VentanaConsumo implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public static JDialog frame;
		private JPanel  panelBtn,panelLst;
		public  JLabel		lbl9;
		public  JLabel		lbl1,lbl2,lbl3,lblT,lblH,lblA;
		public JTextField 			textBus;
		private JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonSalir,buttonPagos;
		JComboBox<String> cbBus;
	    
		private JScrollPane scrollTable;
		private JTextArea textArea = new JTextArea();
		private JTable tableList;
		private DefaultTableModel model;
		
		public Integer totalitems;
		public static String id="",detalle="";
		
		//private String tipo="",fecha="";
		//private float  precio,importe,dsct,total;
		protected static int    cantidad;
		private String consultar="";
		
		float IMPORTES=0,DESCT=0;
		
		private int COD_HOSPEDAJE=0;
		private int COD_DETELLE=0;
		
		private int NRO_HAB=0;
		private String HABI="";
		
		public VentanaConsumo(int cOD_HOSPEDAJE,int cOD_DETALLE, int nRO_HAB, String hABI ) {
			super();
			COD_HOSPEDAJE =cOD_HOSPEDAJE;
			COD_DETELLE =cOD_DETALLE;
			
			NRO_HAB =nRO_HAB;
			HABI =hABI;
			
			frameConsumo();
			crearPanel();
			crearButtons();
			crearTable();
			crearComboBox();
			crearTextFields();
			crearLabels();
			panelLst.setVisible(true); // PANEL LISTA
			llenarcbBuscar();
			
			llenarConsumo();
		}
		public void frameConsumo() {
			frame = new JDialog();
			frame.addWindowFocusListener(new WindowFocusListener() {
				public void windowGainedFocus(WindowEvent arg0) {
					 buttonNuevo.requestFocusInWindow();
				}
				public void windowLostFocus(WindowEvent arg0) {
				}
			});
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowActivated(WindowEvent arg0) {
					llenarConsumo();
				}
			});
			frame.setResizable(false);
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaConsumo.class.getResource("/modelo/Images/consumo.png")));
			frame.setTitle("Tarjeta de consumo"); 
			frame.setBounds(100, 100, 799, 355);
			frame.getContentPane().setLayout(null);
			frame.setLocationRelativeTo(null);
			frame.setModal(true);
		}
		public void crearPanel() {
			panelBtn = new JPanel();
			panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelBtn.setBounds(10, 265, 767, 49);
			frame.getContentPane().add(panelBtn);
			panelBtn.setLayout(null);
			
			panelLst = new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 72, 767, 193); //10, 333, 659, 268
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
			
		}
		void LimpiarTable(){
			try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
		}
		public void llenarConsumo () {
			id="";detalle="";
			IMPORTES=0;DESCT=0;
			int CANT=0;
			conexion = new ConexionDB();
	        try {
	     	   totalitems=0;
	     	   if (textBus.getText().isEmpty()) {
	     		  consultar="Select * from DETALLE_A_CONSUMO,DETALLE_A_HABITACION where  DETALLE_A_CONSUMO.Id_D =DETALLE_A_HABITACION.Id_D and DETALLE_A_CONSUMO.Id_D ='" + this.COD_DETELLE + "'";
	     	   }else{
					if(cbBus.getSelectedItem()=="NRO COMANDA") {
						consultar="Select * from DETALLE_A_CONSUMO,DETALLE_A_HABITACION where  DETALLE_A_CONSUMO.Id_D =DETALLE_A_HABITACION.Id_D and DETALLE_A_CONSUMO.Id_D ='" + this.COD_DETELLE + "'and Id_C like'"+textBus.getText()+"%'";
					}
					if(cbBus.getSelectedItem()=="DESCRIPCION") {
						consultar="Select * from DETALLE_A_CONSUMO,DETALLE_A_HABITACION where  DETALLE_A_CONSUMO.Id_D =DETALLE_A_HABITACION.Id_D and DETALLE_A_CONSUMO.Id_D ='" + this.COD_DETELLE + "'and DescripcionC like'"+textBus.getText()+"%'";
					}
	     	   }
	     	   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
			   model= new DefaultTableModel();
			   model.addColumn("Nro com.");
			   model.addColumn("F.Emisión");
			   model.addColumn("Descripción");
			   model.addColumn("Pre. S/.");
			   model.addColumn("Cant.");
		       model.addColumn("Imp. S/.");
		       model.addColumn("Dsct.S/.");
		       model.addColumn("Tot. S/.");
			       
	 		   String []datos= new String[8];
	 		   tableList.setModel(model);
	     	  
	           while(rs.next()) {
	            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_C"));
	            datos[1]=" "+Menu.formatoFechaString.format(rs.getDate("FechaC")) +"  " + rs.getString("HoraC") ;
	            datos[2]=" "+rs.getString("DescripcionC");
	            datos[3]=" "+Menu.formateadorCurrency.format(rs.getFloat("PrecioC"));
	            datos[4]=" "+rs.getString("CantidadC");
	            datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("ImporteC"));
	            datos[6]=" "+Menu.formateadorCurrency.format(rs.getFloat("DsctC"));
	            datos[7]=" "+Menu.formateadorCurrency.format(rs.getFloat("TotalC"));
	            
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableList.setModel(model);
	            
	            CANT= CANT + rs.getInt("CantidadC");
	            IMPORTES= IMPORTES + rs.getFloat("ImporteC");
	            DESCT= DESCT + rs.getFloat("DsctC");
	            buttonPagos.setEnabled(true);
	            
		        }
	           st.close();
	           
	           // LLENO EL ESPACIO
	    	   if (totalitems>0) {
		           String []d= new String[8];
		 		   tableList.setModel(model);
		            d[1]="==========";
		            d[2]="=======================================";
		            d[7]="======";
		            model.addRow(d);
		            tableList.setModel(model);
		            
		           // LLENO RESULTADOS
		           String []dato= new String[8];
		 		   tableList.setModel(model);
		 		   
		            dato[1]=" " + Menu.date +" "+ Menu.HORA;
		            dato[2]=" RESULTADO TOTAL DE TARJETA ===> ";
		            dato[7]=" " +	Menu.formateadorCurrency.format(IMPORTES - DESCT);
		            model.addRow(dato);
		            tableList.setModel(model);
	 		   }
	    	   // MODELO TABLE
	     	   int CONT=9;
	     	   if (totalitems>0) {
	     		   int l=0;
	 	            l=CONT-totalitems;
	 	            if ( CONT>totalitems) {
	 				    String []registros= new String[l];
	 				    for (int n=2; n<l;n++) {
	 					    model.addRow(registros);
	 					    tableList.setModel(model);
	 				    }
	 	            } 
	 		   }else{
	 			    String []registros= new String[CONT];
	 			    for (int n=0; n<CONT;n++) {
	 				    model.addRow(registros);
	 				    tableList.setModel(model);
	 			    }
	 		   }
	     	   // FIN MODELOTABLE
		           
	           DefaultTableCellRenderer modeloRight = new DefaultTableCellRenderer();
	           modeloRight.setHorizontalAlignment(SwingConstants.RIGHT);
	           
	           DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
	           modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
	           
	           tableList.getColumnModel().getColumn(3).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(4).setCellRenderer(modelocentrar);
	           tableList.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(6).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(7).setCellRenderer(modelocentrar);
	           tableList.getColumnModel().getColumn(7).setCellRenderer(modeloRight);
           
	     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(60);
	     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(90);
	     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(340);
	     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(50);
	     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(40);
	     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(60);
	     	   tableList.getColumnModel().getColumn(6).setPreferredWidth(55);
	     	   tableList.getColumnModel().getColumn(7).setPreferredWidth(60);
	     	  
	           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
			} catch (SQLException e) {
				//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		public void crearLabels() {
			
			lbl9= new JLabel("Filtrar por:");
			lbl9.setBounds(53, 8, 124, 14);
			panelBtn.add(lbl9);
			lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl9.setFont(Menu.fontLabel);
		
			lblT= new JLabel("  ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			lblT.setBackground(new Color(238, 232, 170));
			lblT.setBounds(150, 11, 492, 20);
			frame.getContentPane().add(lblT);
			lblT.setHorizontalAlignment(SwingConstants.LEFT);
			lblT.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			lblT.setOpaque(true);
			
			lblH= new JLabel("  ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			lblH.setBackground(new Color(238, 232, 170));
			lblH.setBounds(150, 31, 623, 20);
			frame.getContentPane().add(lblH);
			lblH.setHorizontalAlignment(SwingConstants.LEFT);
			lblH.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			lblH.setOpaque(true);
			
			lblA= new JLabel("  ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			lblA.setBackground(new Color(238, 232, 170));
			lblA.setBounds(150, 51, 623, 20);
			frame.getContentPane().add(lblA);
			lblA.setHorizontalAlignment(SwingConstants.LEFT);
			lblA.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			lblA.setOpaque(true);
			
			lbl1= new JLabel(" TITULAR                       : ");
			lbl1.setBackground(new Color(238, 232, 170));
			lbl1.setBounds(20, 11, 130, 20);
			frame.getContentPane().add(lbl1);
			lbl1.setHorizontalAlignment(SwingConstants.LEFT);
			lbl1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			lbl1.setOpaque(true);
			
			lbl2= new JLabel(" HABITACIÓN                : ");
			lbl2.setBackground(new Color(238, 232, 170));
			lbl2.setBounds(20, 31, 130, 20);
			frame.getContentPane().add(lbl2);
			lbl2.setHorizontalAlignment(SwingConstants.LEFT);
			lbl2.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			lbl2.setOpaque(true);
			
			lbl3= new JLabel(" ACOMPAÑANTE            : ");
			lbl3.setBackground(new Color(238, 232, 170));
			lbl3.setBounds(20, 51, 130, 20);
			frame.getContentPane().add(lbl3);
			lbl3.setHorizontalAlignment(SwingConstants.LEFT);
			lbl3.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			lbl3.setOpaque(true);
			
		}
		public void crearTextFields(){

			textBus = new  JTextFieldIcon(new JTextField(),"searchYelow.png","Search","searchYelow.png");
			textBus.setColumns(10);
			textBus.setFont(Menu.fontText);
			textBus.setForeground(Menu.textColorForegroundInactivo);
			textBus.setHorizontalAlignment(SwingConstants.LEFT);
			textBus.addActionListener(this);
			textBus.addFocusListener(this);
			textBus.addKeyListener(this);
			textBus.addPropertyChangeListener(this);
			textBus.setBounds(200, 21, 398, 22);
			panelBtn.add(textBus);
			textBus.addFocusListener(this);
		}
		public void crearComboBox() {
			cbBus = new JComboBox<>();
	        cbBus.setBounds(10, 21, 187, 21);
	        cbBus.setFont(Menu.fontText);
	        cbBus.removeAllItems();
	        cbBus.addFocusListener(this);
	        cbBus.addKeyListener(this);
	        panelBtn.add(cbBus);
		}
		public void llenarcbBuscar() {
	        cbBus.removeAllItems();
			String [] lista1 = {"NRO COMANDA","DESCRIPCION"};
			for (String llenar:lista1) {
				cbBus.addItem(llenar);
			}
			cbBus.setSelectedIndex(1);
		}
		public void crearButtons() {
			buttonNuevo= new JButton("");
			buttonNuevo.setToolTipText("Agregar ítem");
			buttonNuevo.addActionListener(this);
			buttonNuevo.setBounds(603, 20, 36, 23);
			buttonNuevo.setIcon(new ImageIcon(VentanaConsumo.class.getResource("/modelo/Images/nuevo.png")));
			panelBtn.add(buttonNuevo);

			buttonEditar= new JButton("");
			buttonEditar.setToolTipText("Modificar ítem");
			buttonEditar.addActionListener(this);
			buttonEditar.setBounds(641, 20, 36, 23);
			buttonEditar.setIcon(new ImageIcon(VentanaConsumo.class.getResource("/modelo/Images/edit.png")));
			panelBtn.add(buttonEditar);
			
			buttonEliminar= new JButton("");
			buttonEliminar.setToolTipText("Eliminar ítem");
			buttonEliminar.addActionListener(this);
			buttonEliminar.setBounds(679, 20, 36, 23);
			buttonEliminar.setIcon(new ImageIcon(VentanaConsumo.class.getResource("/modelo/Images/delete.png")));
			panelBtn.add(buttonEliminar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(717, 20, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaConsumo.class.getResource("/modelo/Images/Exit.png")));
			panelBtn.add(buttonSalir);
			
			buttonPagos= new JButton("Pagos a cuenta");
			buttonPagos.setHorizontalAlignment(SwingConstants.LEFT);
			buttonPagos.setEnabled(false);
			buttonPagos.setBounds(643, 10, 130, 22);
			buttonPagos.setToolTipText("<html>"+
					 "Cuenta de la habitación"+  "<br>"+
					 "por el consumo total ..."+
					 " </h4></html>");
			buttonPagos.addActionListener(this);
			buttonPagos.setIcon(new ImageIcon(VentanaConsumo.class.getResource("/modelo/Images/Dollar_Green.png")));
			frame.getContentPane().add(buttonPagos);
		}
		public void crearTable(){
			tableList = new JTable(); 
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 303, 665, 229);
			tableList.addMouseListener(this);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 17, 747, 167);
	        panelLst.add(scrollTable);
	        
	    	tableList.setShowHorizontalLines(false);
	    	tableList.setShowVerticalLines(true);
	    	tableList.setFillsViewportHeight(true);
	    	tableList.setGridColor(new Color(255, 204, 153));
		}
		public void limpiarTexts() {
			textArea.setText(null);
			textArea.setBackground(Menu.textColorBackgroundInactivo);	
			textArea.setForeground(Menu.textColorForegroundInactivo);
			
			textBus.setText(null);
			textBus.setBackground(Menu.textColorBackgroundInactivo);	
			textBus.setForeground(Menu.textColorForegroundInactivo);
			
			cbBus.removeAllItems();
		}
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
				  	VentanaConsumoAgregar.MOD=0;// NO PERMITE MODIFICAR
					VentanaConsumoAgregar ven= new VentanaConsumoAgregar(this.COD_HOSPEDAJE,this.COD_DETELLE);
				    ven.frame.toFront();
				    ven.frame.setVisible(true);
				    VentanaConsumoAgregar.cbTipo.requestFocus(true);
				    ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos " , TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));
				  }
			  if (evento.getSource() == buttonEditar){// EDITAR
				  if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				  }
				  if (id.length() > 0){
					  modificarConsumo();
				  	}
				  }	
			  if (evento.getSource() == buttonEliminar){// ELIMINAR
				  delete();
				  }	
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }
			  if (evento.getSource() == buttonPagos){	// IR A PAGOS
				  try {
						/*VentanaCuentaHuesped v = new VentanaCuentaHuesped();
						v.lblAlq.setText("HABITACION: "+VentanaControlHotel.NRO +" | " + VentanaControlHotel.HABITACION);
						v.lblAbonado.setText("TITULAR: "+ VentanaControlHotel.HUESPED);
						v.cbBus.setSelectedItem("DESCRIPCION");
			      		v.textBus.setText("CONSUMO");
			      		VentanaCuentaHuesped.frame.setVisible(true);
			      		VentanaCuentaHuesped.frame.setVisible(false);
			      		v.tableList.selectAll();
			      		v.mostrarPagosxHabitacion();*/
					  
					  	conexion = new ConexionDB();
						consultar="Select * from CUENTA_HUESPED where Id_A  ='" + this.COD_HOSPEDAJE  +"'and estadoCta ='" + "A" +"'and DescripcionCta like'" +"CONSUMO"+"%'";
				   	   	Statement st = conexion.gConnection().createStatement();
						ResultSet rs=st.executeQuery(consultar);
						if (rs.next()==true) {
							VentanaCuentaHuesped.id=rs.getString("Id_Cta").trim();
							VentanaCuentaHuesped.descripcion=rs.getString("DescripcionCta").trim();
							
							VentanaCuentaHuespedPagos.MOD=0;// PERMITE REGISTRAR 
							VentanaCuentaHuespedPagos ven= new VentanaCuentaHuespedPagos("HABITACION: "+ this.NRO_HAB +" | " + this.HABI,this.COD_HOSPEDAJE);
						    
						    VentanaCuentaHuespedPagos.cbNH.removeAllItems();
						    VentanaCuentaHuespedPagos.cbNH.addItem(Integer.toString(this.NRO_HAB));
						    VentanaCuentaHuespedPagos.cbNH.setSelectedIndex(0);
						    VentanaCuentaHuespedPagos.cbNH.setEnabled(false);
						    
					    	ven.lbl5.setText("MONTO CONSUMO:");
						    VentanaCuentaHuespedPagos.textFormatAcuenta.requestFocus(true);
						    VentanaCuentaHuespedPagos.textFormatAcuenta.selectAll();
							ven.frame.setTitle("Registro de pagos por habitación - cuenta #: " +  Menu.formatid_7.format(Integer.parseInt(VentanaCuentaHuesped.id)) +"                                    ");
						    ven.frame.toFront();
						    ven.frame.setVisible(true);	
						}else{
							JOptionPane.showMessageDialog(null, "La cta por consumo ya fue cancelada...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
						}
						st.close();
						rs.close();
						conexion.DesconectarDB();
				  	} catch (Exception e) {}
				  }
			}
			void modificarConsumo() {
				int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea modificar el ítem?" + Menu.separador + detalle, Menu.SOFTLE_HOTEL,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				if (respuesta == JOptionPane.YES_OPTION) {
					VentanaConsumoAgregar.MOD=1;// PERMITE MODIFICAR
					VentanaConsumoAgregar ven= new VentanaConsumoAgregar(this.COD_HOSPEDAJE,this.COD_DETELLE);
					VentanaConsumoAgregar.textCod.setText(id);
					ven.llenarParaModificar();
					ven.frame.toFront();
					ven.frame.setVisible(true);
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
			
			// DESCUENTO EL STOCK DEL ARTICULO
			void actualizarStock(){
				conexion = new ConexionDB();
				try{
					Statement s = conexion.gConnection().createStatement();
					ResultSet r = s.executeQuery("Select * from INVENTARIOS where DescripcionInv= '"+ detalle.trim() +"'"); 
					int CAT=0;
					if (r.next()==true) {
						CAT=(r.getInt("StockInv"));
				         String sq="UPDATE INVENTARIOS SET StockInv = ?"
				                 + "WHERE DescripcionInv = '"+ detalle.trim() +"'"; 
						PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
						pst.setDouble(1, CAT + cantidad);
						pst.executeUpdate();
						pst.close();
						//JOptionPane.showMessageDialog(null, "El stock del artículo " + VentanaConsumo.detalle.trim()  + " fue actualizado",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
					s.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error actualizar stock" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}
			// DESCUENTO EL STOCK DEL ARTICULO
			
			//	METODO ELIMINAR
			public void delete() {
				if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				}
				int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem?" + Menu.separador + id +" " + detalle, Menu.SOFTLE_HOTEL,
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					try {
						actualizarStock();
						conexion = new ConexionDB();
						Statement statement =  conexion.gConnection().createStatement();
						String query ="Delete from DETALLE_A_CONSUMO where Id_C ='" + id  + "'";
						statement.execute(query);
						JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						statement.close();
						llenarConsumo();
						VentanaConsumoAgregar v = new VentanaConsumoAgregar(this.COD_HOSPEDAJE,this.COD_DETELLE);
						v.insertCuentaHuesped();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
			public void focusGained(FocusEvent ev) {
				if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundActivo);}
			}
			public void focusLost(FocusEvent ev) {
				if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundInactivo);}
			}
			public void keyReleased(KeyEvent evet) {
				char e=evet.getKeyChar();
					if (evet.getSource() == textBus){
						llenarConsumo();
						int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
						if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
								//buscar();
							}
					}
			}
			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO
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
			id="";detalle="";
			cantidad=0;
			//tipo="";
			//precio=0;importe=0;dsct=0;total=0;
			if (Mouse_evt.getSource().equals(tableList)) {
				try {
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
					if (id==null || id == ""){
						tableList.requestFocus();
						return;
					}
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					detalle=(String)tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
					cantidad=Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),4).toString().trim());
					
					//tipo=(String)tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
					//precio=Float.parseFloat(tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim().replaceAll(",", ""));
					//importe=Float.parseFloat(tableList.getValueAt(tableList.getSelectedRow(),5).toString().trim().replaceAll(",", ""));
					//dsct=Float.parseFloat(tableList.getValueAt(tableList.getSelectedRow(),6).toString().trim().replaceAll(",", ""));
					//total=Float.parseFloat (tableList.getValueAt(tableList.getSelectedRow(),7).toString().trim().replaceAll(",", ""));
					//fecha=tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						modificarConsumo();
					}
				} catch (Exception e) {}
			}
		}

}
