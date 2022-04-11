
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;

public class VentanaCuentaHuespedPagos implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public JDialog frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl8,lbl9,lbl10,lbl12;
	protected JLabel			lbl5,lbl6;
	private JButton  			buttonNuevo,buttonEliminar,buttonEditar,buttonGrabar,buttonPrint,buttonSalir;
	protected static JDateChooser dateChooserFecha;
	public String dateEmision;
	protected JTextField 			textCod;
	protected static JComboBox<String> 		cbResponsable,cbNH,cbMPago;
	protected static JFormattedTextField 	textFormatMontoFactura,textFormatAcuenta,textFormatSaldo,textFormatImporte;
	
	public static int MOD;
	String cadenaDescripcion="";
	
	NumberFormat formatDsct;

	public Integer totalitems;
	private String consultar="";
	public static String id="",descripcion="";
	
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	private int COD_HOSPEDAJE=0;
	public VentanaCuentaHuespedPagos(String ABONADO,int cOD_HOSPEDAJE) {
		super();
		COD_HOSPEDAJE =cOD_HOSPEDAJE;
		frameCuentaHuespedPagos();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		crearTable();
		llenarcombox();
		//limpiarTexts();
		
		llenarHab();
		llenarNuevo();
		llenarCbResponsable();

		lbl8.setText(ABONADO);
		
		dateChooserFecha.setDate(Menu.fecha);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dateEmision = form.format(dateChooserFecha.getDate());
		
	}
	public void frameCuentaHuespedPagos() {
		frame = new JDialog();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaCuentaHuespedPagos.class.getResource("/modelo/Images/Dollar_Green.png")));
		frame.setTitle("Registro de pagos a cuenta "); 
		frame.setBounds(100, 100, 573, 342);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setModal(true);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(5, 5, 543, 296);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		dateChooserFecha = new JDateChooser();
		dateChooserFecha.setToolTipText("Fecha de alta");
		dateChooserFecha.setDateFormatString("dd-MMM-yyyy");
		dateChooserFecha.setBorder(new LineBorder(new Color(70, 130, 180), 1, true));
		dateChooserFecha.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooserFecha.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooserFecha.getDateEditor()).setEditable(false);
		dateChooserFecha.setBounds(130, 217, 95, 22);
		panelDto.add(dateChooserFecha);
		dateChooserFecha.addPropertyChangeListener(this);
	}
	public void crearLabels() {
		lbl1= new JLabel("Nro Pago:");
		lbl1.setBounds(22, 221, 49, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.LEFT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Resp. Pago:");
		lbl2.setBounds(10, 244, 72, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.LEFT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("M.Pago  :");
		lbl3.setBounds(22, 267, 60, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.LEFT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl5= new JLabel("Monto facturado:");
		lbl5.setBounds(319, 20, 122, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("::::::::");
		lbl8.setBounds(22, 20, 297, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.LEFT);
		lbl8.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lbl9 = new JLabel("A cuenta S/.:");
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl9.setBounds(297, 247, 69, 14);
		panelDto.add(lbl9);
		
		lbl10 = new JLabel("Saldo S/.:");
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl10.setBounds(244, 270, 122, 14);
		panelDto.add(lbl10);
		
		lbl12 = new JLabel("Total importes S/.:");
		lbl12.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl12.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl12.setBounds(274, 222, 95, 14);
		panelDto.add(lbl12);
	
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
		textCod.setBounds(73, 217, 52, 22);
		panelDto.add(textCod);

		textFormatMontoFactura = new JFormattedTextField();
		textFormatMontoFactura.setEditable(false);
		textFormatMontoFactura.setBackground(new Color(240, 240, 240));
		textFormatMontoFactura.setText("0.00");
		textFormatMontoFactura.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatMontoFactura.setBounds(444, 14, 83, 21);
		textFormatMontoFactura.addFocusListener(this);
		textFormatMontoFactura.addKeyListener(this);
		panelDto.add(textFormatMontoFactura);
		textFormatMontoFactura.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(0, 0, 255)));
		
		textFormatAcuenta = new JFormattedTextField();
		textFormatAcuenta.setBackground(new Color(255, 255, 255));
		textFormatAcuenta.setText("0.00");
		textFormatAcuenta.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatAcuenta.setBounds(372, 240, 76, 21);
		textFormatAcuenta.addActionListener(this);
		textFormatAcuenta.addPropertyChangeListener(this);
		textFormatAcuenta.addFocusListener(this);
		textFormatAcuenta.addKeyListener(this);
		panelDto.add(textFormatAcuenta);
		textFormatAcuenta.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.BLUE));
		
		textFormatSaldo = new JFormattedTextField();
		textFormatSaldo.setEditable(false);
		textFormatSaldo.setBackground(new Color(230, 230, 250));
		textFormatSaldo.setText("0.00");
		textFormatSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatSaldo.setBounds(372, 263, 76, 21);
		textFormatSaldo.addFocusListener(this);
		textFormatSaldo.addKeyListener(this);
		panelDto.add(textFormatSaldo);
		textFormatSaldo.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(165, 42, 42)));
		
		textFormatImporte = new JFormattedTextField();
		textFormatImporte.setEditable(false);
		textFormatImporte.setBackground(new Color(230, 230, 250));
		textFormatImporte.setText("0");
		textFormatImporte.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatImporte.setBounds(372, 217, 76, 21);
		textFormatImporte.addActionListener(this);
		textFormatImporte.addPropertyChangeListener(this);
		textFormatImporte.addKeyListener(this);
		textFormatImporte.addFocusListener(this);
		panelDto.add(textFormatImporte);
		textFormatImporte.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.BLUE));
	}
	
	public void crearComboBox() { 
		cbResponsable = new JComboBox<>();
		cbResponsable.setBounds(73, 241, 201, 21);
		cbResponsable.setFont(Menu.fontText);
		cbResponsable.addActionListener(this);
		cbResponsable.addFocusListener(this);
		cbResponsable.addKeyListener(this);
		panelDto.add(cbResponsable);
		
		cbNH = new JComboBox<>();
		cbNH.setBounds(214, 264, 60, 21);
		cbNH.setFont(Menu.fontText);
		cbNH.addActionListener(this);
		cbNH.addFocusListener(this);
		cbNH.addKeyListener(this);
		panelDto.add(cbNH);
		
		cbMPago = new JComboBox<>();
		cbMPago.setBounds(73, 264, 139, 21);
		cbMPago.setFont(Menu.fontText);
		cbMPago.addActionListener(this);
		cbMPago.addFocusListener(this);
		cbMPago.addKeyListener(this);
		panelDto.add(cbMPago);
	}
	
	public void llenarNuevo() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_DetCta from CUENTA_HUESPED_PAGOS order by Id_DetCta desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_DetCta"))+1);
				textCod.setText(Menu.formatid_7.format(id));
			}else {
				textCod.setText(Menu.formatid_7.format(0000001));
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public static void llenarcombox() {
		cbMPago.removeAllItems();
		cbMPago.addItem("EFECTIVO");
		cbMPago.addItem("TARJETA DE CREDITO");
		cbMPago.addItem("TARJETA DE DEBITO");
		cbMPago.addItem("CHEQUE");
		cbMPago.addItem("DEPOSITO - BANCO");
		cbMPago.setSelectedIndex(-1);
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Agregar ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(455, 216, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaCuentaHuespedPagos.class.getResource("/modelo/Images/nuevo.png")));
		panelDto.add(buttonNuevo);
		
		buttonEditar= new JButton("");
		buttonEditar.setEnabled(false);
		buttonEditar.setToolTipText("Modificar ítem");
		buttonEditar.addActionListener(this);
		buttonEditar.setBounds(455, 239, 36, 23);
		buttonEditar.setIcon(new ImageIcon(VentanaCuentaHuespedPagos.class.getResource("/modelo/Images/edit.png")));
		panelDto.add(buttonEditar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(455, 262, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaCuentaHuespedPagos.class.getResource("/modelo/Images/delete.png")));
		panelDto.add(buttonEliminar);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(491, 216, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaCuentaHuespedPagos.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonPrint= new JButton("");
		buttonPrint.setEnabled(false);
		buttonPrint.setToolTipText("Imprimir...");
		buttonPrint.addActionListener(this);
		buttonPrint.setBounds(491, 239, 36, 23);
		buttonPrint.setIcon(new ImageIcon(VentanaCuentaHuespedPagos.class.getResource("/modelo/Images/print.png")));
		panelDto.add(buttonPrint);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Cerrar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(491, 262, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaCuentaHuespedPagos.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);
	}
	
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textFormatAcuenta.setText(null);
		textFormatAcuenta.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatAcuenta.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatSaldo.setText(null);
		textFormatSaldo.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatSaldo.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatImporte.setText(null);
		textFormatImporte.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatImporte.setForeground(Menu.textColorForegroundInactivo);
		
		cbResponsable.setSelectedIndex(-1);

        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	public void crearTable(){
		tableList = new JTable();
		tableList.setShowHorizontalLines(false);
		tableList.setForeground(Color.BLACK);
		tableList.setFillsViewportHeight(true);
		tableList.setBackground(new Color(240, 248, 255));
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		tableList.addMouseListener(this);
		tableList.addKeyListener(this);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(22, 37, 505, 168);
        panelDto.add(scrollTable);

        tableList.setGridColor(new Color(238, 232, 170));
	}
	public void llenarHab() {
		// ********************************************************** CAMBIO LA HBAITACION DE PAGOS DE HUESPED
		conexion = new ConexionDB(); 
		try {
			int item=0;
			Statement st =  conexion.gConnection().createStatement();
			ResultSet set = st.executeQuery("Select* from DETALLE_A_HABITACION where Id_A='" + this.COD_HOSPEDAJE + "'"); 
			cbNH.addItem("%TODOS");
			while (set.next()==true) {
				cbNH.addItem(set.getString("Numeroh"));
				item=item+1;
			}
			// SOLO CUANDO ES REGISTRO INDIVIDUAL HABITACION Y ALOJAMIENTO
			if(item==1) {
				cbNH.setSelectedIndex(1);
				cbNH.setEnabled(false);
			}
			// SOLO CUANDO ES REGISTRO INDIVIDUAL HABITACION Y ALOJAMIENTO
			st.close();
		} catch (Exception e) {}
		// ********************************************************** CAMBIO LA HBAITACION DE PAGOS DE HUESPED
	}
	public void llenarTable (String NRO) {
		id="";descripcion="";
		float IMPORTE=0,MONTO=0;
		conexion = new ConexionDB();
        try {
     	   totalitems=0;
     	   String consulta_consumo="";
     	   if (cbNH.getSelectedItem()=="%TODOS") {
	     	   consultar="Select * from CUENTA_HUESPED as C,CUENTA_HUESPED_PAGOS as D where D.Id_Cta=C.Id_Cta and D.Id_Cta ='" + Integer.parseInt(VentanaCuentaHuesped.id) +"'";
	     	   consulta_consumo="Select * from ALQUILER, DETALLE_A_CONSUMO,DETALLE_A_HABITACION where DETALLE_A_HABITACION.Id_A= ALQUILER.Id_A and DETALLE_A_CONSUMO.Id_D =DETALLE_A_HABITACION.Id_D and DETALLE_A_HABITACION.Id_A ='" + this.COD_HOSPEDAJE + "'";
     	   } 
     	   if (cbNH.getSelectedItem()!="%TODOS") {
	     	   consultar="Select * from CUENTA_HUESPED as C,CUENTA_HUESPED_PAGOS as D where D.Id_Cta=C.Id_Cta and D.Id_Cta ='" + Integer.parseInt(VentanaCuentaHuesped.id) +"'and D.NroHabCta ='" + NRO +"'";
	     	   consulta_consumo="Select * from ALQUILER, DETALLE_A_CONSUMO,DETALLE_A_HABITACION where DETALLE_A_HABITACION.Id_A= ALQUILER.Id_A and DETALLE_A_CONSUMO.Id_D =DETALLE_A_HABITACION.Id_D and DETALLE_A_HABITACION.Id_A ='" + this.COD_HOSPEDAJE + "'and DETALLE_A_HABITACION.NumeroH ='" + NRO + "'";
     	   } 
     	   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
 		   
		   model= new DefaultTableModel();
		   model.addColumn("Nro Pago");
		   model.addColumn("Nro");
		   model.addColumn("Fecha");
	       model.addColumn("M.Pago");
	       model.addColumn("Responsable de pago");
	       model.addColumn("Importe S/.");
	       
 		   String []datos= new String[9];
 		   tableList.setModel(model);
     	  
           while(rs.next()) {
            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_DetCta"));
            datos[1]=" "+rs.getString("NroHabCta");
            datos[2]=" "+Menu.formatoFechaString.format(rs.getDate("FechaDetCta"))+"  " +rs.getString("HoraDetCta");
            datos[3]=" "+rs.getString("MPagoDetCta");
            datos[4]=" "+rs.getString("ResDetCta");
            datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("AcuentaDetCta"));
            
            totalitems=totalitems+1;
            model.addRow(datos);
            tableList.setModel(model);
            
            IMPORTE = IMPORTE + rs.getFloat("AcuentaDetCta");
            MONTO =  rs.getFloat("MontoCta");
	        }
           
           st.close();
           textFormatImporte.setText((String)Menu.formateadorCurrency.format(IMPORTE));
     	   textFormatSaldo.setText((String)Menu.formateadorCurrency.format(MONTO-IMPORTE));
			// ::::::::::::::::::::::::::::::::::::::::::::::::::
           	if (VentanaCuentaHuesped.descripcion.equals("CONSUMO")){
				Statement s = conexion.gConnection().createStatement();
				ResultSet r=s.executeQuery(consulta_consumo);
				float b=0;
				while(r.next()) {
					b= b + r.getFloat("TotalC");
				}
				textFormatMontoFactura.setText((String)Menu.formateadorCurrency.format(b));
		        textFormatImporte.setText((String)Menu.formateadorCurrency.format(IMPORTE));
		    	textFormatSaldo.setText((String)Menu.formateadorCurrency.format(b-IMPORTE));
           	}else{
           		//cbNH.removeAllItems();
           		//cbNH.addItem("%TODOS");
           	}
	     	// :::::::::::::::::::::::::::::::::::::::::::::::::::   
	     	
           // MODELO TABLE
    	   int CONT=9;
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
           
           tableList.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
           
     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(30);
     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(80);
     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(95);
     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(170);
     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(70);
           panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista total de ítems: "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	void llenarCbResponsable(){
		try {
			String sl="";
			cbResponsable.removeAllItems();
			Statement s =  conexion.gConnection().createStatement();
			
			// LENO EL HUESPE TITULAR
			sl="Select * from CLIENTES as C, ALQUILER as A where A.Id_Cli=C.Id_Cli and A.Id_A ='" +  COD_HOSPEDAJE  +"'";
 		   	ResultSet re=s.executeQuery(sl);
			if (re.next()==true) {
				cbResponsable.addItem(re.getString("NombreCli"));
			}
			re.close();
			
			// LLENO LOS ACOMPAÑANTES SEGUN FORMA DE REGISTRO IN DIVIDUAL O GRUPAL (%TODOS)
			if (cbNH.getSelectedItem()=="%TODOS") {
					sl="Select * from  ALQUILER AS A, DETALLE_A_HABITACION AS DH where DH.Id_A=A.Id_A and DH.Id_A ='" + COD_HOSPEDAJE +"'";
	     	   	} 
     	   if (cbNH.getSelectedItem()!="%TODOS") {
	     	   		sl="Select * from  ALQUILER AS A, DETALLE_A_HABITACION AS DH where DH.Id_A=A.Id_A and DH.Id_A ='" + COD_HOSPEDAJE +"'and DH.NumeroH ='" + cbNH.getSelectedItem() +"'";
     	   		} 

 		   	ResultSet r=s.executeQuery(sl);
			while (r.next()==true) {
				cbResponsable.addItem(r.getString("AcompananteH"));
			}
			r.close();
			
			s.close();
		} catch (Exception e) {}
	}
	
	public void delete() {
		if (id==null || id == ""){
			JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			tableList.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			int resp = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resp == JOptionPane.YES_OPTION) {
				try {
					// CONSULTO SALDO PARA GRABAR
					Statement state =  conexion.gConnection().createStatement();
					ResultSet resultSet = state.executeQuery("Select* from CUENTA_HUESPED where Id_Cta='" + Integer.parseInt(VentanaCuentaHuesped.id) + "'"); 
					float saldo_todo=0;
					if (resultSet.next()) {
						saldo_todo= resultSet.getFloat("SaldoCta");
					}
					state.close();
					// END CONSULTO SALDO PARA GRABA
					
					// BUSCO EL IMPORTE PARA AUMENTAR EL SALDO 
				   float SALDO_IMP=0;
		     	   Statement st = conexion.gConnection().createStatement();
		 		  
		 		   String C="";
		     	   if (cbNH.getSelectedItem()=="%TODOS") {
		     		   C="select* from CUENTA_HUESPED_PAGOS where Id_DetCta='" + id  + "'";
		     	   }
		     	   if (cbNH.getSelectedItem()!="%TODOS") {
		     		   C="select* from CUENTA_HUESPED_PAGOS where Id_DetCta='" + id  + "'and NroHabCta ='" + cbNH.getSelectedItem() +"'";
		     	   }
		     	   ResultSet rs=st.executeQuery(C);
		           if(rs.next()==true) {
		        	   SALDO_IMP = rs.getFloat("AcuentaDetCta");
		           }
		           SALDO_IMP=SALDO_IMP + saldo_todo;
		           st.close();
		           // BUSCO EL IMPORTE PARA AUMENTAR EL SALDO 

		           Statement statement =  conexion.gConnection().createStatement();
					String query ="Delete from CUENTA_HUESPED_PAGOS where Id_DetCta='" + id  + "'";
					statement.execute(query);
					JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					statement.close();
					
					// GRABO EL SALDO Y SU ESTADO ***********************************************************
					String sqll="UPDATE CUENTA_HUESPED SET Id_Cta = ?,"
			        		 + "SaldoCta =?,"
			                 + "EstadoCta =?"
			                 + "WHERE Id_Cta = '"+ Integer.parseInt(VentanaCuentaHuesped.id) + "'"; 
			         
					PreparedStatement p = conexion.gConnection().prepareStatement(sqll);
					p.setInt(1,Integer.parseInt(VentanaCuentaHuesped.id));
					p.setFloat(2, SALDO_IMP);
					p.setString(3, "A"); // ACTIVO O CON DEUDA
					p.executeUpdate();
					p.close();
					//END GRABO EL SALDO Y SU ESTADO ***********************************************************
					
					llenarTable((String)cbNH.getSelectedItem());
				} catch (Exception e) {}
			}else if (resp == JOptionPane.NO_OPTION) {}
			
			} catch (Exception e) {}
			
	}
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textFormatAcuenta.setEnabled(b);
		textFormatSaldo.setEnabled(b);
		textFormatImporte.setEnabled(b);
		cbResponsable.setEnabled(b);
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
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonNuevo){// NUEVO
			  llenarTable((String)cbNH.getSelectedItem());
			  llenarNuevo();
			  cbResponsable.requestFocus();
			  }
		  if (evento.getSource() == buttonGrabar){// GRABAR
			  insertarUpdate();
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  this.frame.dispose();
		  	  }
		  if (evento.getSource() == buttonEliminar){// ELIMINAR
			  delete();
		  	  }
		  if (evento.getSource() == buttonEditar){// MODIFICAR
			  
		  	  }
		  if (evento.getSource() == cbMPago){
			  }
		  if (evento.getSource() == cbResponsable){
			  }
		  if (evento.getSource() == cbNH){
			  llenarTable((String)cbNH.getSelectedItem());
			  llenarCbResponsable();
		  	  }		  
		}
	
		public void insertarUpdate() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				return;
			}
			if (dateChooserFecha.getDate()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				dateChooserFecha.requestFocus();
				return;
			}
			if (cbResponsable.getSelectedItem()==null || cbResponsable.getSelectedItem()==""){
				JOptionPane.showMessageDialog(null, "Falta seleccionar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbResponsable.requestFocus();
				return;
			}
			if (cbMPago.getSelectedItem()==null || cbMPago.getSelectedItem()==""){
				JOptionPane.showMessageDialog(null, "Falta seleccionar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbMPago.requestFocus();
				return;
			}
			if (cbNH.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Selecione la habitacion",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbNH.requestFocus();
				return;
			}
			if (textFormatAcuenta.getText().trim().isEmpty()||Float.parseFloat(textFormatAcuenta.getText().trim().replaceAll(",", ""))==0){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatAcuenta.requestFocus();
				textFormatAcuenta.selectAll();
				return;
			}
		try {
			float A= Float.parseFloat(textFormatAcuenta.getText().replaceAll(",", ""));
			if (A==0 || A==0.00 || A==0){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatAcuenta.requestFocus();
				return;
			}
	
			float TotalImpor =Float.parseFloat(textFormatAcuenta.getText().replaceAll(",", "")) + Float.parseFloat(textFormatImporte.getText().replaceAll(",", ""));
			if (TotalImpor>(Float.parseFloat(textFormatMontoFactura.getText().replaceAll(",", "")))){
				JOptionPane.showMessageDialog(null, "El monto ingresado supera el monto dado",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatAcuenta.requestFocus();
				textFormatAcuenta.selectAll();
				return;
			}
			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
					// CONSULTO SALDO PARA GRABAR
					Statement statement =  conexion.gConnection().createStatement();
					ResultSet resultSet = statement.executeQuery("Select* from CUENTA_HUESPED where Id_Cta='" + Integer.parseInt(VentanaCuentaHuesped.id) + "'"); 
					float saldo_todo=0;
					if (resultSet.next()) {
						saldo_todo= resultSet.getFloat("SaldoCta");
					}
					statement.close();
					// END CONSULTO SALDO PARA GRABAR
					
			         String sqll="UPDATE CUENTA_HUESPED SET Id_Cta = ?,"
			        		 + "SaldoCta =?,"
			                 + "EstadoCta =?"
			                 + "WHERE Id_Cta = '"+ Integer.parseInt(VentanaCuentaHuesped.id) + "'"; 
			         
					PreparedStatement p = conexion.gConnection().prepareStatement(sqll);
					p.setInt(1,Integer.parseInt(VentanaCuentaHuesped.id));
					p.setFloat(2, saldo_todo - Float.parseFloat(textFormatAcuenta.getText().replaceAll(",", "")));
					p.setString(3, "A"); // ACTIVO O CON DEUDA
					p.executeUpdate();
					p.close();
					
					// *************************** ACTUALIZO EL SALDO
					Statement st =  conexion.gConnection().createStatement();
					ResultSet set = st.executeQuery("Select* from CUENTA_HUESPED where Id_Cta='" + Integer.parseInt(VentanaCuentaHuesped.id) + "'"); 
					if (set.next()) {
						saldo_todo= set.getFloat("SaldoCta");
					}
					st.close();
			         String S="UPDATE CUENTA_HUESPED SET Id_Cta = ?,"
			                 + "EstadoCta =?"
			                 + "WHERE Id_Cta = '"+ Integer.parseInt(VentanaCuentaHuesped.id) + "'"; 
			         
					PreparedStatement PT = conexion.gConnection().prepareStatement(S);
					PT.setInt(1,Integer.parseInt(VentanaCuentaHuesped.id));
			     	   if (saldo_todo==0) {
								PT.setString(2, "X"); // CANCELADO
			     	   }
			     	   if (saldo_todo!=0) {
								PT.setString(2, "A"); // CANCELADO
			     	   	}
					PT.executeUpdate();
					PT.close();
					// ************************ END ACTUALIZO EL SALDO
					
					llenarNuevo();
					String sql ="INSERT INTO CUENTA_HUESPED_PAGOS (Id_DetCta,Id_Cta,ResDetCta,MPagoDetCta,FechaDetCta,HoraDetCta,AcuentaDetCta,NroHabCta,IdTurnoP) VALUES (?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setInt(2,Integer.parseInt(VentanaCuentaHuesped.id));
					ps.setString(3,(String)cbResponsable.getSelectedItem());
					ps.setString(4,(String)cbMPago.getSelectedItem());
					ps.setNString(5, dateEmision.trim());
					ps.setNString(6, Menu.HORA.trim());//12 HORAS HILO
					ps.setFloat(7, Float.parseFloat(textFormatAcuenta.getText().replaceAll(",", "")));
		     	   if (cbNH.getSelectedItem()=="%TODOS") {
		     		  ps.setString(8, "0");//NRO_HAB_CONSUMO
		     	   } 
		     	   if (cbNH.getSelectedItem()!="%TODOS") {
		     		  ps.setString(8, (String)cbNH.getSelectedItem());
		     	   } 
		     	   	ps.setInt(9,VentanaLogin.ID_APETURA_CAJA);
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					llenarTable((String)cbNH.getSelectedItem());
					llenarNuevo();
					if (TotalImpor==(Float.parseFloat(textFormatMontoFactura.getText().replaceAll(",", "")))){
						JOptionPane.showMessageDialog(null, "La cuenta Nro: " + VentanaCuentaHuesped.id +" S/." + textFormatMontoFactura.getText()  + "\nfue cancelada",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						this.frame.dispose();
					}
					textFormatAcuenta.setText("0");
					textFormatAcuenta.requestFocus(true);
					textFormatAcuenta.selectAll();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al grabar" +  Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			}
		}
		
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == cbMPago){cbMPago.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbResponsable){cbResponsable.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatAcuenta){textFormatAcuenta.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textFormatAcuenta){textFormatAcuenta.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == cbResponsable){cbResponsable.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbMPago){cbMPago.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textFormatAcuenta){textFormatAcuenta.selectAll();} 
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbMPago){cbMPago.setBackground(new Color(240,240,240));}
			if (ev.getSource() == cbResponsable){cbResponsable.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textFormatAcuenta){textFormatAcuenta.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textFormatAcuenta){textFormatAcuenta.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == cbResponsable){cbResponsable.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbMPago){cbMPago.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			  if (evet.getSource().equals(textFormatAcuenta)){
				  try {
					  if (textFormatAcuenta.getText().equals("")) {
						  textFormatAcuenta.setText("0");
						  textFormatSaldo.setText(Float.toString(Float.parseFloat(textFormatMontoFactura.getText().replaceAll(",", ""))-(Float.parseFloat(textFormatImporte.getText().replaceAll(",", "")))));
						  textFormatSaldo.setText(Menu.formateadorCurrency.format(Float.parseFloat(textFormatSaldo.getText())));
					  }
					  if (textFormatAcuenta.getText()!="0") {
						  textFormatSaldo.setText(Float.toString(Float.parseFloat(textFormatMontoFactura.getText().replaceAll(",", ""))-(Float.parseFloat(textFormatImporte.getText().replaceAll(",", "")))));
						  textFormatSaldo.setText(Float.toString(Float.parseFloat(textFormatSaldo.getText().replaceAll(",", ""))-(Float.parseFloat(textFormatAcuenta.getText().replaceAll(",", "")))));
						  textFormatSaldo.setText(Menu.formateadorCurrency.format(Float.parseFloat(textFormatSaldo.getText())));
					  }
					} catch (Exception e2) {}
			  }
				if (evet.getSource() == cbResponsable){
					if (e==KeyEvent.VK_ENTER){
						if (cbResponsable.getSelectedIndex()!=-1){
							cbMPago.requestFocus();
						}else{
							cbResponsable.requestFocus();
						}
					}	
				}
				if (evet.getSource() == cbMPago){
					if (e==KeyEvent.VK_ENTER){
						if (cbMPago.getSelectedIndex()!=-1){
							textFormatAcuenta.requestFocus();
							textFormatAcuenta.selectAll();;
						}else{
							cbMPago.requestFocus();
						}
					}	
				}
				if (evet.getSource() == textFormatAcuenta){
					int pos = textFormatAcuenta.getCaretPosition();textFormatAcuenta.setText(textFormatAcuenta.getText().toUpperCase());textFormatAcuenta.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textFormatAcuenta.getText().toLowerCase().isEmpty()|| textFormatAcuenta.getText().toLowerCase().length()>=8){
						textFormatAcuenta.requestFocus();
						textFormatAcuenta.selectAll();
						textFormatAcuenta.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){
							buttonGrabar.doClick();
						}
				} 
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char car=evet.getKeyChar();
			if (evet.getSource() == textFormatSaldo){
		       if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
			if (evet.getSource() == textFormatAcuenta){ 
				if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
			if (evet.getSource() == textFormatImporte){
				if(!Character.isDigit(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
		}
		
	@Override
	public void propertyChange(PropertyChangeEvent e) {
      	if (e.getSource()==(dateChooserFecha)){
    		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    		dateEmision = form.format(dateChooserFecha.getDate());
		}
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
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
					if (id==null || id == ""){
						tableList.requestFocus();
						return;
					}
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					descripcion="S/. "+(String) tableList.getValueAt(tableList.getSelectedRow(),5).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						
					}
				}
			} catch (Exception e) {}
	}
	}
