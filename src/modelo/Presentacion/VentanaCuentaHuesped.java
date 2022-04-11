package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaCuentaHuesped implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public static JDialog frame;
	private JPanel  panelBtn,panelLst;
	public  JLabel		lbl9,lblAlq,lblAbonado;
	public JTextField 			textBus;
	protected JButton  			buttonSalir;
	JComboBox<String> cbBus,cbEstado;
    
	private JScrollPane scrollTable;
	protected JTable tableList;
	private DefaultTableModel model;
	
	public Integer totalitems;
	
	public static String id="",descripcion="";
	private String consultar="";

	private int COD_HOSPEDAJE=0;
	public VentanaCuentaHuesped(int cOD_HOSPEDAJE) {
		super();
		COD_HOSPEDAJE =cOD_HOSPEDAJE;
		frameCuentaHuesped();
		crearPanel();
		crearButtons();
		crearTable();
		crearComboBox();
		crearTextFields();
		crearLabels();
		llenarcbEstado();
		llenarcbBuscar();
		llenarcbFamilia();
		
		llenarTable();
	}
	public void frameCuentaHuesped() {
		frame = new JDialog();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				llenarTable();
			}
		});
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaCuentaHuesped.class.getResource("/modelo/Images/Dollar_Green.png")));
		//frame.setTitle("Inspección de cuentas por hospedaje y consumos                                                                           "); 
		frame.setTitle("Control de Ctas Maestras por alojamiento y consumo |**| Nro Reg: "+ Menu.formatid_9.format(COD_HOSPEDAJE) + " |**|                           ");
		frame.setBounds(100, 100, 701, 304);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setModal(true);
	}
	public void crearPanel() {
		panelBtn = new JPanel();
		panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelBtn.setBounds(10, 211, 668, 49);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelLst = new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 11, 668, 202); //10, 333, 659, 268
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	public void llenarTable () {
		id="";descripcion="";
		
		float MONTO=0,SALDO=0;
		conexion = new ConexionDB();
        try {
     	   totalitems=0;
     	  if (cbEstado.getSelectedItem()=="%TODOS") {
	     	   if (textBus.getText().isEmpty()) {
	     		  consultar="Select * from CUENTA_HUESPED where Id_A ='" + this.COD_HOSPEDAJE  +"'";
	     	   }else{
					if(cbBus.getSelectedItem()=="NRO #") {
						consultar="Select * from CUENTA_HUESPED where Id_A ='" +  this.COD_HOSPEDAJE +"'and Id_Cta='" +textBus.getText()+"'";
					}
					if(cbBus.getSelectedItem()=="DESCRIPCION") {
						consultar="Select * from CUENTA_HUESPED where Id_A ='" +  this.COD_HOSPEDAJE  +"'and DescripcionCta like'" +textBus.getText()+"%'";
					}
	     	   }
     	  }
     	  if (cbEstado.getSelectedItem()!="%TODOS") {
	     	   if (textBus.getText().isEmpty()) {
	     		  consultar="Select * from CUENTA_HUESPED where Id_A ='" +  this.COD_HOSPEDAJE +"'and estadoCta ='" + cbEstado.getSelectedItem()  +"'";
	     	   }else{
					if(cbBus.getSelectedItem()=="NRO #") {
						consultar="Select * from CUENTA_HUESPED where Id_A  ='" +  this.COD_HOSPEDAJE  +"'and estadoCta ='" + cbEstado.getSelectedItem() +"'and Id_Cta='" +textBus.getText()+"'";
					}
					if(cbBus.getSelectedItem()=="DESCRIPCION") {
						consultar="Select * from CUENTA_HUESPED where Id_A  ='" +  this.COD_HOSPEDAJE  +"'and estadoCta ='" + cbEstado.getSelectedItem() +"'and DescripcionCta like'" +textBus.getText()+"%'";
					}
	     	   }
     	  }
     	  
     	   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
 		   
		   model= new DefaultTableModel();
		   model.addColumn("Nro Cta");
		   model.addColumn("Id_alquiler");
		   model.addColumn("Fecha - hora");
		   model.addColumn("Descripción");
	       model.addColumn("Monto S/.");
	       model.addColumn("Saldo S/.");
	       model.addColumn("estado");
	       
 		   String []datos= new String[7];
 		   tableList.setModel(model);
     	  
           while(rs.next()) {
            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_Cta"));
            datos[1]=" "+rs.getString("Id_A");
            datos[2]=" "+Menu.formatoFechaString.format(rs.getDate("FechaCta"))+"  " +rs.getString("HoraCta");
            datos[3]=" "+rs.getString("DescripcionCta");
            datos[4]=" "+Menu.formateadorCurrency.format(rs.getFloat("MontoCta"));
            datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("SaldoCta"));
            datos[6]=" "+rs.getString("EstadoCta");
            
            totalitems=totalitems+1;
            model.addRow(datos);
            tableList.setModel(model);
            
            MONTO= MONTO + rs.getFloat("MontoCta");
            SALDO= SALDO + rs.getFloat("SaldoCta");
           }
           
           st.close();
           
           // LLENO EL ESPACIO
    	   if (totalitems>0) {
	           String []d= new String[8];
	 		   tableList.setModel(model);
	   
	            d[3]=" ==================================";
	            d[4]="=========";
	            d[5]="=======";
	            model.addRow(d);
	            tableList.setModel(model);
	            
	           // LLENO RESULTADOS
	           String []dato= new String[7];
	 		   tableList.setModel(model);
	   
	            dato[3]=" TOTAL ===>";
	            dato[4]=" " + 	Menu.formateadorCurrency.format(MONTO);
	            dato[5]=" " + 	Menu.formateadorCurrency.format(SALDO);
	            model.addRow(dato);
	            tableList.setModel(model);
 		   }
           // MODELO TABLE
    	   int CONT=6;
    	   if (totalitems>0) {
    		   int c=0;
	            c=CONT-totalitems;
	            if ( CONT>totalitems) {
				    String []registros= new String[c];
				    for (int n=0; n<c;n++) {
					    model.addRow(registros);
					    tableList.setModel(model);
				    }
	            }   
		   }else{
			   CONT=CONT+2;
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
       
       tableList.getColumnModel().getColumn(4).setCellRenderer(modeloRight);
       tableList.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
       tableList.getColumnModel().getColumn(6).setCellRenderer(modelocentrar);
           
       tableList.getColumnModel().getColumn(1).setMinWidth(0);
       tableList.getColumnModel().getColumn(1).setMaxWidth(0);
       
 	   tableList.getColumnModel().getColumn(0).setPreferredWidth(65);
 	   tableList.getColumnModel().getColumn(1).setPreferredWidth(40);
 	   tableList.getColumnModel().getColumn(2).setPreferredWidth(170);
 	   tableList.getColumnModel().getColumn(3).setPreferredWidth(350);
 	   tableList.getColumnModel().getColumn(4).setPreferredWidth(90);
 	   tableList.getColumnModel().getColumn(5).setPreferredWidth(80);
 	   tableList.getColumnModel().getColumn(6).setPreferredWidth(20);
     	  
           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista total: ítems "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void crearLabels() {
		lbl9= new JLabel("Filtrar por:");
		lbl9.setBounds(103, 7, 145, 14);
		panelBtn.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);
        
        lblAlq = new JLabel(":::");
        lblAlq.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAlq.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblAlq.setBounds(297, 20, 355, 14);
        panelLst.add(lblAlq);
        
        lblAbonado = new JLabel(":::");
        lblAbonado.setHorizontalAlignment(SwingConstants.LEFT);
        lblAbonado.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblAbonado.setBounds(10, 20, 288, 14);
        panelLst.add(lblAbonado);
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
		textBus.setBounds(252, 21, 368, 21);
		panelBtn.add(textBus);
		textBus.addFocusListener(this);
	}
	public void crearComboBox() {
		cbEstado= new JComboBox<>();
		cbEstado.setBounds(10, 21, 73, 21);
		cbEstado.setFont(Menu.fontText);
		cbEstado.removeAllItems();
		cbEstado.addActionListener(this);
		cbEstado.addFocusListener(this);
		cbEstado.addKeyListener(this);
        panelBtn.add(cbEstado);
	        
		cbBus = new JComboBox<>();
        cbBus.setBounds(87, 21, 161, 21);
        cbBus.setFont(Menu.fontText);
        cbBus.removeAllItems();
        cbBus.addFocusListener(this);
        cbBus.addKeyListener(this);
        panelBtn.add(cbBus);
        
	}
	public void llenarcbEstado() {
        cbEstado.removeAllItems();
		String [] lista1 = {"%TODOS","A","X"};
		for (String llenar:lista1) {
			cbEstado.addItem(llenar);
		}
		cbEstado.setSelectedIndex(0);
	}
	public void llenarcbBuscar() {
        cbBus.removeAllItems();
		String [] lista1 = {"NRO #","DESCRIPCION"};
		for (String llenar:lista1) {
			cbBus.addItem(llenar);
		}
		cbBus.setSelectedIndex(0);
	}
	public void llenarcbFamilia() {
	}
	public void crearButtons() {
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(620, 20, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaCuentaHuesped.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
	}
	public void crearTable(){
		tableList = new JTable();
		tableList.setForeground(new Color(0, 0, 0));
		tableList.setFillsViewportHeight(true);
		tableList.setBackground(SystemColor.inactiveCaptionBorder);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		tableList.addMouseListener(this);
		tableList.addKeyListener(this);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 36, 642, 157);
        panelLst.add(scrollTable);

        tableList.setGridColor(new Color(238, 232, 170));
	}
	public void limpiarTexts() {
		textBus.setText(null);
		textBus.setBackground(Menu.textColorBackgroundInactivo);	
		textBus.setForeground(Menu.textColorForegroundInactivo);
		
		cbBus.setSelectedIndex(0);
	}
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonSalir){// SALIR
			  frame.dispose();
			  }
		  if (evento.getSource() == cbEstado){
			  llenarTable();
			  }
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
					llenarTable();
					int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (e==KeyEvent.VK_ENTER ){
						tableList.selectAll();
						id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
						if (id==null || id == ""){
							tableList.requestFocus();
							return;
						}
						mostrarPagos();
					}
				}
		}
		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
		}
		
	/*public void mostrarPagosxHabitacion(){
		id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
		descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
		if (tableList.getValueAt(tableList.getSelectedRow(),6).toString().trim().equals("A")) {
			VentanaCuentaHuespedPagos.MOD=0;// PERMITE REGISTRAR 
			VentanaCuentaHuespedPagos ven= new VentanaCuentaHuespedPagos(lblAlq.getText());
			ven.frame.setTitle("Pagos a cuenta #: " + id);
		    ven.frame.toFront();
		    ven.frame.setVisible(true);
		    //ven.NRO_HAB_CONSUMO=Integer.parseInt(VentanaControlHotel.NRO);
		    
		    VentanaCuentaHuespedPagos.cbNH.removeAllItems();
		    VentanaCuentaHuespedPagos.cbNH.addItem(VentanaControlHotel.NRO);
		    VentanaCuentaHuespedPagos.cbNH.setSelectedIndex(0);
		    VentanaCuentaHuespedPagos.cbNH.setEnabled(false);
		    
		    //ven.llenarTable(ven.NRO_HAB_CONSUMO);
		    if (tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim().equals("CONSUMO")) {
		    	ven.lbl5.setText("MONTO CONSUMO:");
		    }else{
		    	ven.lbl5.setText("MONTO ALOJAMIENTO:");
		    }
		    //VentanaCuentaHuespedPagos.textFormatMontoFactura.setText((String)Menu.formateadorCurrency.format(MontoxHab));
		    VentanaCuentaHuespedPagos.textFormatAcuenta.requestFocus(true);
		    VentanaCuentaHuespedPagos.textFormatAcuenta.selectAll();
		}else{
			JOptionPane.showMessageDialog(null, "La cuenta Nro: " +id+" X "+ descripcion +"\nya fue cancelada...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
	    }
	}*/
	public void mostrarPagos(){
		id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
		descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
		if (tableList.getValueAt(tableList.getSelectedRow(),6).toString().trim().equals("A")) {
			VentanaCuentaHuespedPagos.MOD=0;// PERMITE REGISTRAR 
			VentanaCuentaHuespedPagos ven= new VentanaCuentaHuespedPagos(lblAbonado.getText(),this.COD_HOSPEDAJE);
		    //VentanaCuentaHuespedPagos.cbNH.setSelectedIndex(0);
		    if (tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim().equals("CONSUMO")) {
		    	ven.lbl5.setText("MONTO CONSUMO:");
		    }else{
		    	ven.lbl5.setText("MONTO ALOJAMIENTO:");
		    }
		    VentanaCuentaHuespedPagos.textFormatMontoFactura.setText((String) tableList.getValueAt(tableList.getSelectedRow(),4).toString().trim());
		    VentanaCuentaHuespedPagos.textFormatAcuenta.requestFocus(true);
		    VentanaCuentaHuespedPagos.textFormatAcuenta.selectAll();
			ven.frame.setTitle("Registro de pagos - cuenta #: " + id +"                                                               ");
		    ven.frame.toFront();
		    ven.frame.setVisible(true);
		}else{
			JOptionPane.showMessageDialog(null, "La cuenta Nro: " +id+" X "+ descripcion +"\nya fue cancelada...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
	    }
	}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		//Object source = e.getSource();
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		//char e=evet.getKeyChar();
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
				id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
				if (id==null || id == ""){
						tableList.requestFocus();
						return;
					}
					if (Mouse_evt.getClickCount() == 2) {
						mostrarPagos();
					}
				}
			} catch (Exception e) {}
		}
}
