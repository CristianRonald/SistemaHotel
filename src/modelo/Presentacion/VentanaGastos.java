package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import modelo.Clases.ListGasto;
import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import modelo.Negocio.DatasourceGasto;
import modelo.Otros.JTextFieldIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class VentanaGastos implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public  JInternalFrame frame;
	private JPanel  panelBtn,panelLst;
	public  JLabel		lbl1,lbl2,lbl0;
	public JTextField 			textBus;
	private JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonPrint,buttonSalir;
    
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnTipo,rdbtnConcepto;
	
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	public Integer totalitems;
	public static String id="",detalle="";
	
	private String consultar="";
	private JFormattedTextField TextSaldo;
	private JLabel lblMonto;
	
	private JDateChooser chooserDesde,chooserHasta;
	private static String FECHA_DESDE="",FECHA_HASTA="";
	// constructor
	public VentanaGastos() {
		super();
		frameGastos();
		crearPanel();
		crearButtons();
		crearTable();
		crearComboBox();
		crearTextFields();
		crearLabels();
		crearChooser();
		panelLst.setVisible(true); // PANEL LISTA
		
		creoEmcabezado();
		llenarTableCaja();
		chooserDesde.setDate(Menu.fechaPrimerDiaMes);
		chooserHasta.setDate(Menu.fecha);

		Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
	}
	public void frameGastos() {
		frame = new JInternalFrame();
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent arg0) {
				llenarTableCaja();
			}
		});
		frame.setTitle("Gesti�n de Gastos");
		frame.setFrameIcon(new ImageIcon(VentanaGastos.class.getResource("/modelo/Images/cajasalida.png")));
		frame.setClosable(true);
		frame.setBounds(100, 100, 844, 466);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelBtn = new JPanel();
		panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelBtn.setBounds(10, 375, 808, 49);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelLst = new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 5, 808, 371); //10, 333, 659, 268
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	private void crearChooser () {
		chooserDesde= new JDateChooser();
		chooserDesde.setDateFormatString("dd-MMM-yyyy");
		chooserDesde.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		chooserDesde.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserDesde.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)chooserDesde.getDateEditor()).setEditable(false);
		
		//chooserIngreso.getCalendarButton().addActionListener(this);
		chooserDesde.addPropertyChangeListener(this);
		chooserDesde.setBounds(10, 20, 104, 22);
		panelBtn.add(chooserDesde);
		
		chooserHasta = new JDateChooser();
		chooserHasta.setDateFormatString("dd-MMM-yyyy");
		chooserHasta.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		chooserHasta.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserHasta.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)chooserHasta.getDateEditor()).setEditable(false);
		chooserHasta.setBounds(113, 20, 104, 22);
		//dateSalida.getCalendarButton().addActionListener(this);
		chooserHasta.addPropertyChangeListener(this);
		panelBtn.add(chooserHasta);
		
		rdbtnTipo = new JRadioButton("Tipo");
		buttonGroup.add(rdbtnTipo);
		rdbtnTipo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnTipo.setBounds(219, 24, 52, 15);
		rdbtnTipo.addActionListener(this);
		rdbtnTipo.addKeyListener(this);
		panelBtn.add(rdbtnTipo);
		
		rdbtnConcepto = new JRadioButton("Concepto");
		rdbtnConcepto.setSelected(true);
		buttonGroup.add(rdbtnConcepto);
		rdbtnConcepto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnConcepto.setBounds(267, 24, 72, 16);
		rdbtnConcepto.addActionListener(this);
		rdbtnConcepto.addKeyListener(this);
		panelBtn.add(rdbtnConcepto);
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	void creoEmcabezado(){
	}
	public void llenarTableCaja () {
		id="";detalle="";
		float SALDO=0;
		conexion = new ConexionDB();
        try {
        	
        	totalitems=0;
        	if (rdbtnTipo.isSelected()){
        		consultar="Select * from GASTOS where TipoGst like'" + textBus.getText() + "%'and FechaGst >='" + FECHA_DESDE.trim() + "'and FechaGst <='" + FECHA_HASTA.trim() + "' order by Id_Gst desc"; 
        	}
        	if (rdbtnConcepto.isSelected()){
        		consultar="Select * from GASTOS where ConceptoGst like'" + textBus.getText() + "%'and FechaGst >='" + FECHA_DESDE.trim() + "'and FechaGst <='" + FECHA_HASTA.trim() + "' order by Id_Gst desc"; 
        	}
        	model= new DefaultTableModel();
        	model.addColumn("Nro mov.");
	       	model.addColumn("F.Emisi�n");
	       	model.addColumn("Tipo Gasto");
	       	model.addColumn("Concepto");
	       	model.addColumn("Documento");
	       	model.addColumn("Monto S/.");
	     	  
          //************************************************** INGRESOS POR PAGOS
           Statement sp = conexion.gConnection().createStatement();
           ResultSet rp=sp.executeQuery(consultar);
	       String []datP= new String[6];
	        while(rp.next()==true) {
	        		totalitems= totalitems +1;
	        		datP[0]=" "+rp.getString("Id_gst");
		        	datP[1]=" "+Menu.formatoFechaString.format(rp.getDate("FechaGst"))+"  "+rp.getString("HoraGst");
		        	datP[2]=" "+rp.getString("TipoGst");
		        	datP[3]=" "+rp.getString("ConceptoGst");
		        	datP[4]=" "+rp.getString("DocumentoGst")+ " "+rp.getString("NumeroGst");;
		        	datP[5]=" "+Menu.formateadorCurrency.format(rp.getInt("MontoGst"));
			        SALDO=SALDO + rp.getFloat("MontoGst");
			        
		        model.addRow(datP);
	        }
	        tableList.setModel(model);
	        sp.close();
	       //************************************************** END INGRESOS POR PAGOS
	        
           // LLENO EL ESPACIO
    	   /*if (totalitems>0) {
	           String []d= new String[6];
	 		   tableList.setModel(model);
	   
	            d[4]="================";
	            d[5]="======";
	            
	            model.addRow(d);
	            tableList.setModel(model);
	            
	           // LLENO RESULTADOS
	           String []dato= new String[8];
	 		   tableList.setModel(model);
	   
	            //dato[1]=" " + Menu.date +" "+ Menu.HORA;
	            dato[4]=" TOTAL GASTOS ==>";
	            dato[5]=" " +	Menu.formateadorCurrency.format(SALDO);
	            
	            model.addRow(dato);
	            tableList.setModel(model);
	 		  }
	    	   // MODELO TABLE
	     	   int CONT=24;
	     	   if (totalitems>0) {
	     		   int l=0;
	 	            l=CONT-totalitems;
	 	            if ( CONT>totalitems) {
	 				    String []registros= new String[l];
	 				    for (int n=6; n<l;n++) {
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
	 		   }*/
	     	   // FIN MODELOTABLE
           
           tableList.getColumnModel().getColumn(0).setMinWidth(0);
           tableList.getColumnModel().getColumn(0).setMaxWidth(0);
           
     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(150);
     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(160);
     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(300);
     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(150);
     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(60);
     	   TextSaldo.setText("S/. "+Menu.formateadorCurrency.format(SALDO));
     	   
     	   tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("VENTANA_GASTOS"));//ESTABLESCO COLOR CELDAS
           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total �tems "+totalitems , TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void crearLabels() {
		lbl0= new JLabel("Desde:");
		lbl0.setForeground(new Color(0, 0, 0));
		lbl0.setBackground(new Color(128, 128, 128));
		lbl0.setBounds(39, 8, 75, 14);
		panelBtn.add(lbl0);
		lbl0.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl0.setFont(Menu.fontLabel);
		
		lbl1= new JLabel("Hasta:");
		lbl1.setForeground(new Color(0, 0, 0));
		lbl1.setBackground(new Color(128, 128, 128));
		lbl1.setBounds(139, 8, 75, 14);
		panelBtn.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Filtrar gastos:");
		lbl2.setForeground(new Color(0, 0, 0));
		lbl2.setBackground(new Color(128, 128, 128));
		lbl2.setBounds(338, 8, 159, 14);
		panelBtn.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lblMonto = new JLabel("Monto:");
		lblMonto.setForeground(new Color(0, 0, 0));
		lblMonto.setBackground(new Color(128, 128, 128));
		lblMonto.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMonto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMonto.setBounds(735, 8, 62, 14);
		panelBtn.add(lblMonto);
	}
	public void crearTextFields(){
		textBus = new  JTextFieldIcon(new JTextField(),"searchCeleste.png","Search","searchCeleste.png");
		textBus.setBackground(new Color(240, 240, 240));
		textBus.setColumns(10);
		textBus.setFont(Menu.fontText);
		textBus.setForeground(Color.WHITE);
		textBus.setHorizontalAlignment(SwingConstants.LEFT);
		textBus.addActionListener(this);
		textBus.addFocusListener(this);
		textBus.addKeyListener(this);
		textBus.addPropertyChangeListener(this);
		textBus.setBounds(340, 21, 164, 22);
		panelBtn.add(textBus);
		textBus.addFocusListener(this);
		
		TextSaldo = new JFormattedTextField();
		TextSaldo.setBackground(new Color(240, 240, 240));
		TextSaldo.setForeground(new Color(178, 34, 34));
		TextSaldo.setText("0.00");
		TextSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
		TextSaldo.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		TextSaldo.setEditable(false);
		TextSaldo.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 100, 0)));
		TextSaldo.setBounds(705, 22, 93, 21);
		panelBtn.add(TextSaldo);
	}
	public void crearComboBox() {
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Agregar �tem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(545, 20, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaGastos.class.getResource("/modelo/Images/nuevo.png")));
		panelBtn.add(buttonNuevo);

		buttonEditar= new JButton("");
		buttonEditar.setToolTipText("Modificar �tem");
		buttonEditar.addActionListener(this);
		buttonEditar.setBounds(583, 20, 36, 23);
		buttonEditar.setIcon(new ImageIcon(VentanaGastos.class.getResource("/modelo/Images/edit.png")));
		panelBtn.add(buttonEditar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setToolTipText("Eliminar �tem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(621, 20, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaGastos.class.getResource("/modelo/Images/delete.png")));
		panelBtn.add(buttonEliminar);

		buttonPrint= new JButton("");
		buttonPrint.setToolTipText("Vista Previa");
		buttonPrint.addActionListener(this);
		buttonPrint.setBounds(507, 20, 36, 23);
		buttonPrint.setIcon(new ImageIcon(VentanaGastos.class.getResource("/modelo/Images/Preview.png")));
		panelBtn.add(buttonPrint);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(659, 20, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaGastos.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
	}
	public void crearTable(){
		tableList = new JTable();
		tableList.setBackground(new Color(250, 250, 220));
		tableList.setShowHorizontalLines(false);
		tableList.setFillsViewportHeight(true);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		tableList.addMouseListener(this);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		//scrollTable.setColumnHeaderView(tableList);//ENCABEZADO
		//tableList.setFillsViewportHeight(true);//ENCABEZADO
		scrollTable.setBounds(10, 14, 788, 349);
        panelLst.add(scrollTable);
        tableList.setFillsViewportHeight(true);
    	tableList.setGridColor(new Color(220, 220, 220));
	}
	public void limpiarTexts() {
		textBus.setText(null);
		textBus.setBackground(Menu.textColorBackgroundInactivo);	
		textBus.setForeground(Menu.textColorForegroundInactivo);
		
	}
	void modificarMoviCaja() {
		if (id==null || id.equals("")){
			return;
		}
		int respuesta = JOptionPane.showConfirmDialog (null, "�Desea modificar el �tem?" + Menu.separador + detalle, Menu.SOFTLE_HOTEL,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if (respuesta == JOptionPane.YES_OPTION) {
			VentanaGastosAgregar.MOD=1;// PERMITE MODIFICAR
			VentanaGastosAgregar ven= new VentanaGastosAgregar();
			ven.frame.toFront();
			ven.frame.setVisible(true);
			ven.llenarParaModificar();
			VentanaGastosAgregar.textArea.requestFocus(true);
			ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar Datos ", TitledBorder.LEADING, TitledBorder.TOP, null,(SystemColor.RED)));
		
		}else if (respuesta == JOptionPane.NO_OPTION) {}
	}
	//	METODO ELIMINAR
	public void delete() {
		if (id==null || id.equals("")){
			JOptionPane.showMessageDialog(null, "Primero debe seleccionar un �tem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			tableList.requestFocus();
			return;
		}
		int respuesta = JOptionPane.showConfirmDialog (null, "�Desea eliminar el �tem?" + Menu.separador + detalle, Menu.SOFTLE_HOTEL,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			try {
				conexion = new ConexionDB();
				Statement statement =  conexion.gConnection().createStatement();
				String query ="Delete from GASTOS where Id_Gst ='" + id  + "'";
				statement.execute(query);
				JOptionPane.showMessageDialog(null, "El �tem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				statement.close();
				llenarTableCaja(); 
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else if (respuesta == JOptionPane.NO_OPTION) {}
	}
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonNuevo){// NUEVO
			  VentanaGastosAgregar.MOD=0;// NO PERMITE MODIFICAR
			  VentanaGastosAgregar ven= new VentanaGastosAgregar();
			    ven.frame.toFront();
			    ven.frame.setVisible(true);
			    VentanaGastosAgregar.textArea.requestFocus(true);
			    ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos ", TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));

			  }
		  if (evento.getSource() == buttonEditar){// EDITAR
			  if (id==null || id == ""){
				JOptionPane.showMessageDialog(null, "Primero debe seleccionar un �tem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			  }
			  if (id.length() > 0){
				  modificarMoviCaja();
			  	}
			  }	
		  if (evento.getSource() == buttonEliminar){// ELIMINAR
			  delete();
			  }
		  if (evento.getSource() == buttonPrint){// IMPRIMIR
			  Imprimir(); 
			  }	

		  if (evento.getSource() == buttonSalir){// SALIR
			  frame.dispose();
			  }
		  
	      if (evento.getSource().equals(rdbtnTipo)){
	      		llenarTableCaja();
	      		textBus.requestFocus();
	      	}
	      if (evento.getSource().equals(rdbtnConcepto)){
	      		llenarTableCaja();
	      		textBus.requestFocus();
	      	}
		}
		void Imprimir() {
			try {
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("LISTA DE GASTOS:      DESDE: " + Menu.formatoDiaMesAño.format(chooserDesde.getDate()) +"  |  HASTA: "+  Menu.formatoDiaMesAño.format(chooserHasta.getDate())));
				parameters.put("PrtNomEmpresa",new String (VentanaLogin.RAZON_SOCIAL_HOTEL));
				parameters.put("PrtRucEmpresa",new String (VentanaLogin.RUC_HOTEL));
				parameters.put("PrtDirEmpresa",new String (VentanaLogin.DIRECCION_HOTEL));
				if (!VentanaLogin.URLIMAGE_HOTEL.equals("")) {
					parameters.put("PrtUbiFoto",new String (VentanaLogin.URLIMAGE_HOTEL));
				//}else{
					//parameters.put("PrtUbiFoto",new String (Menu.URL_IMAGE+"login.png"));
				}
				DatasourceGasto source = new DatasourceGasto();
			    for(int i=0;i<tableList.getRowCount();i++)
			    {
			    	ListGasto c= new ListGasto(Integer.parseInt(tableList.getValueAt(i, 0).toString().trim()), tableList.getValueAt(i, 2).toString().trim(), tableList.getValueAt(i, 3).toString().trim(), tableList.getValueAt(i, 1).toString().trim(), tableList.getValueAt(i, 4).toString().trim(), tableList.getValueAt(i, 5).toString().trim());
			    	source.addList(c);
			    }
		        JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(Menu.URL+"CGastos.jasper");  
		        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, source);  
		        
		        JDialog visor = new JDialog();
		        visor.setModal(true);
		        JasperViewer jrViewer = new JasperViewer(jasperPrint, true); 
		        visor.setSize(900,600); 
		        visor.setLocationRelativeTo(null); 
				visor.setTitle("Lista de Gastos");

				visor.getContentPane().add(jrViewer.getContentPane()); 
				visor.setVisible(true);
				
			    //EXPORTAR EL INFORME A PDF
			    //JasperExportManager.exportReportToPdfFile(jasperPrint,Menu.URL + "CGastos.pdf");
			} catch (Exception e) {}
		}
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textBus){textBus.selectAll();} 
			if (ev.getSource() == textBus){textBus.setHorizontalAlignment(SwingConstants.RIGHT);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textBus){textBus.setBackground(new Color(240, 240, 240));}
			if (ev.getSource() == textBus){textBus.setForeground(Color.black);}
			
			if (ev.getSource() == textBus){textBus.setHorizontalAlignment(SwingConstants.LEFT);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
				if (evet.getSource() == textBus){
					llenarTableCaja();
					int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
							//buscar();
					}
				}
		}
		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			//char e=evet.getKeyChar();
		}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		//Object source = e.getSource();
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
      	if (e.getSource()==(chooserDesde)||e.getSource()==(chooserDesde)){
      		if (chooserDesde.getDate()!=null){
  				FECHA_DESDE = form.format(chooserDesde.getDate());
  				llenarTableCaja();
      		}
		}
      	if (e.getSource()==(chooserHasta)||e.getSource()==(chooserHasta)){
      		if (chooserHasta.getDate()!=null){
      			FECHA_HASTA = form.format(chooserHasta.getDate());
      			llenarTableCaja();
      		}
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
		id="";detalle="";
		if (Mouse_evt.getSource().equals(tableList)) {
			try {
				id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
				if (id==null || id.trim().equals("")){
					//tableList.requestFocus();
					return;
				}
				id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
				detalle=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim() +" "+ (String) tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						modificarMoviCaja();
					}
				} catch (Exception e) {}
			}
		}
}
