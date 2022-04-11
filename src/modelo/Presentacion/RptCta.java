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
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import modelo.Clases.ListCts;
import modelo.Clases.TableColor;
import modelo.Datos.ConexionDB;
import modelo.Negocio.DatasourceCts;
import modelo.Otros.JTextFieldIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import javax.swing.border.LineBorder;

public class RptCta implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public JInternalFrame 	frame;
	private JDialog frameDetalle;
	private JPanel  panelDto,panelLst;


	private JLabel				lbl1,lbl2,lbl3;
	private JButton  			buttonNuevo,buttonPint,buttonSalir;
	protected static JTextField textNom;
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	private String consultar="";
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnPendiente,rdbtnCancelada;
	private String PAGO="";
	
	private JDateChooser chooserDesde,chooserHasta;
	private JTable tableListDestalle_1;
	
	private static String FECHA_DESDE="",FECHA_HASTA="";
	public RptCta() {
		frameRptCta();
		crearPanel();
		crearButtons();
		crearTable();
		crearTextFields();
		crearLabels();
		crearChooser();
		LimpiarTable();
		chooserDesde.setDate(Menu.fecha);
		chooserHasta.setDate(Menu.fecha);
		PAGO="A";
		llenarTable(PAGO, textNom.getText());
		//frame.setModal(true);
		
		Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
		frameDetalle(0);
	}
	public void frameRptCta() {
		frame = new JInternalFrame();
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				llenarTable(PAGO, textNom.getText());
			}
			//@Override
		});
		frame.setTitle("Historial de cuentas");
		frame.setFrameIcon(new ImageIcon(RptCta.class.getResource("/modelo/Images/Histori.png")));
		frame.setClosable(true);
		frame.setBounds(100, 100, 774, 322);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDto.setBounds(10, 5, 743, 56);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		panelLst = new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 60, 743, 224);
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
		chooserDesde.setBounds(10, 25, 104, 21);
		panelDto.add(chooserDesde);
		
		chooserHasta = new JDateChooser();
		chooserHasta.setDateFormatString("dd-MMM-yyyy");
		chooserHasta.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		chooserHasta.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserHasta.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)chooserHasta.getDateEditor()).setEditable(false);
		chooserHasta.setBounds(113, 25, 104, 21);
		//dateSalida.getCalendarButton().addActionListener(this);
		chooserHasta.addPropertyChangeListener(this);
		panelDto.add(chooserHasta);
		
		rdbtnPendiente = new JRadioButton("PENDIENTE (A)");
		rdbtnPendiente.setSelected(true);
		buttonGroup.add(rdbtnPendiente);
		rdbtnPendiente.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnPendiente.setBounds(223, 23, 104, 23);
		rdbtnPendiente.addActionListener(this);
		panelDto.add(rdbtnPendiente);
		
		rdbtnCancelada = new JRadioButton("CANCLADO (X)");
		buttonGroup.add(rdbtnCancelada);
		rdbtnCancelada.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnCancelada.setBounds(329, 23, 100, 23);
		rdbtnCancelada.addActionListener(this);
		panelDto.add(rdbtnCancelada);
	}
	public void crearLabels(){
		lbl1= new JLabel("Desde:");
		lbl1.setBounds(46, 11, 68, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Hasta:");
		lbl2.setBounds(149, 11, 68, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Cliente:");
		lbl3.setBounds(491, 11, 129, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
	}
	public void crearTextFields() {
		textNom =  new  JTextFieldIcon(new JTextField(),"searchNormal.png","Search","searchNormal.png");
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundInactivo);
		textNom.setHorizontalAlignment(SwingConstants.LEFT);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(435, 24, 184, 22);
		panelDto.add(textNom);
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setBounds(624, 23, 36, 23);
		panelDto.add(buttonNuevo);
		buttonNuevo.setToolTipText("");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setIcon(new ImageIcon(RptCta.class.getResource("/modelo/Images/ok.png")));
		
		buttonPint= new JButton("");
		buttonPint.setBounds(661, 23, 36, 23);
		panelDto.add(buttonPint);
		buttonPint.setToolTipText("Vista Previa");
		buttonPint.addActionListener(this);
		buttonPint.setIcon(new ImageIcon(RptCta.class.getResource("/modelo/Images/print.png")));
		
		buttonSalir= new JButton("");
		buttonSalir.setBounds(698, 23, 36, 23);
		panelDto.add(buttonSalir);
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setIcon(new ImageIcon(RptCta.class.getResource("/modelo/Images/Exit.png")));
	}

	public void crearTable() {
		tableList = new JTable(); 
		tableList.setShowHorizontalLines(false);
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 15, 725, 200);
        panelLst.add(scrollTable);
        
	}
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundActivo);}
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundInactivo);}
		if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundInactivo);} 
	}
	
	protected void llenarTable(String EST, String DES) {
		conexion = new ConexionDB();
		LimpiarTable();
		float MONTO=0,SALDO=0;
        try {
        	
     	   int totalitems=0;
     	   model= new DefaultTableModel();
		   model.addColumn("Item");
		   model.addColumn("# Cta");
		   model.addColumn("Cliente");
		   model.addColumn("Descripción");
		   model.addColumn("Fecha Emisión");
		   model.addColumn("Monto S/.");
		   model.addColumn("Saldo S/.");
		   model.addColumn("E");
		   model.addColumn("Tur / Ope");
		   
 		   Object []datos= new Object[9];
 		   tableList.setModel(model);
 		   
 		   consultar="SELECT *, DATE_FORMAT(FechaCta, '%d/%m/%y') AS FechaCta FROM CUENTA_HUESPED as C, CLIENTES as H, ALQUILER as A where C.Id_A=A.Id_A and A.Id_Cli=H.Id_Cli and FechaCta BETWEEN'" + FECHA_DESDE.trim() + "'and '" + FECHA_HASTA.trim() + "'and C.EstadoCta  like'" + EST + "%'and H.NombreCli  like'" + DES + "%' order by C.Id_Cta desc";
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
    	   while(rs.next()) {
		    	datos[0]=""+totalitems;
		    	datos[1]=" "+Menu.formatid_9.format(rs.getInt("Id_Cta"));
		    	datos[2]=" "+rs.getString("NombreCli");
		    	datos[3]=" "+rs.getString("DescripcionCta");
		    	datos[4]=" "+Menu.formatoFechaString.format(rs.getDate("FechaCta")) +"  " +rs.getString("HoraCta");
		    	datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("MontoCta"));
		    	datos[6]=" "+Menu.formateadorCurrency.format(rs.getFloat("SaldoCta"));
		    	datos[7]=" "+rs.getString("EstadoCta");
		    	
		    	
	            MONTO= MONTO + rs.getFloat("MontoCta");
	            SALDO= SALDO+ rs.getFloat("SaldoCta");
	            
		    	//CONSULTO TURNO Y USER
		    	String strin="";
				Statement ss = conexion.gConnection().createStatement();
				//ResultSet rr = ss.executeQuery("Select * from EMPLEADO  as E, CAJA_APE_CIE as AP where AP.Id_ApeCie='" + rs.getString("IdTurno")+"'");
				ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + rs.getInt("IdTurnoCta")+"'");
				if (rr.next()== true) {
					// NOMBRE CORTO DE USUARIO Y TURNO
					String TUR="",USU="";
					String TEMP_TUR="",TEMP_NOM="";
					String delimiter = " ";
					String[] temp;
					
					temp = rr.getString("turno").trim().split(delimiter);
					for(int i =0; i < temp.length ; i++){
						if (temp[i].trim().length()>=(1)) {
							TEMP_TUR=temp[i].trim().substring(0, 1);
							}
							TUR=TUR.trim() + TEMP_TUR.trim();
					}
					
					temp =  rr.getString("NombresEmp").trim().split(delimiter);
					for(int i =0; i < temp.length ; i++){
						if (temp[i].trim().length()>=(1)) {
							TEMP_NOM=temp[i].trim().substring(0, 1);
							}
							USU=USU.trim() + TEMP_NOM.trim();
					}
					strin=TUR +" / "+ USU;
					// NOMBRE CORTO DE USUARIO Y TURNO
				}
				rr.close();
				ss.close();
				//END CONSULTO TURNO Y USER
				datos[8]=" "+strin.trim();
				
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableList.setModel(model);

	        }
	    	// LLENO EL ESPACIO
    	   if (totalitems>0) {
	           String []d= new String[9];
	 		   tableList.setModel(model);
	 		   	d[1]="";
	 		   	d[2]="";
	 		   	d[3]="";
	 		   	d[7]="";
	 		   	
	 	        d[4]="=================";
	            d[5]="======";
	            d[6]="======";
	            model.addRow(d);
	            tableList.setModel(model);
	            
	           // LLENO RESULTADOS
	           String []dato= new String[9];
	 		   tableList.setModel(model);
	 		   
	            dato[1]="";
	            dato[2]="";
	            dato[3]="";
	            
	            dato[4]=" RESULTADO TOTAL ===> ";
	            dato[5]=" " +	Menu.formateadorCurrency.format(MONTO);
	            dato[6]=" " +	Menu.formateadorCurrency.format(SALDO);
	            dato[7]="";
	            model.addRow(dato);
	            tableList.setModel(model);
 		   }
    	   // MODELO TABLE
    	  /* int CONT=11;
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
			   //CONT=CONT+2;
			    String []registros= new String[CONT];
			    for (int n=0; n<CONT;n++) {
				    model.addRow(registros);
				    tableList.setModel(model);
			    }
		   }*/
    	   // FIN MODELOTABLE
           
           //tableList.getColumnModel().getColumn(1).setMinWidth(0);
           //tableList.getColumnModel().getColumn(1).setMaxWidth(0);
           
     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(0);
     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(200);
     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(90);
     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(140);
     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(6).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(7).setPreferredWidth(10);
     	   tableList.getColumnModel().getColumn(8).setPreferredWidth(50);
           rs.close();
           	
           
           
           
       	/*DefaultTableModel TableModel = new DefaultTableModel();
        //se colocan algunos datos en el TableModel
        TableModel.setDataVector(new Object[][] {
        { false, "12345789", "Elsa", "Pallo" },
        { false, "94675631", "Marcia Ana", "Tierra" },
        { true,  "65663522", "Elsa", "Porrico" },
        { true,  "24343556", "Aquiles", "Castro" },
        { true, "84848844", "Jorge", "Nitales" },
        { false, "84848488", "Debora", "Melbollo" },
        { true, "21212111", "Alex", "Cremenento" },
        { false, "67674455", "Carlitos", "Tado" },
        { false, "99873132", "Elton", "Tito" },
        { true, "90053535", "Irma", "Tando" },
        { false, "64665112", "Matias", "Queroso" },
        { true, "73363844", "Rosa", "Meltrazo" },
        { false, "32111993", "Dolores", "Delano" } }, new Object[] {
        "", "Codigo", "Nombre", "Ap. Paterno" });
        tableList.setModel(TableModel);*/

        //se crea instancia a clase FormatoTable y se indica columna patron
        TableColor ft = new TableColor(3);
        tableList.setDefaultRenderer (Object.class, ft );
        tableList.getColumnModel().getColumn(0).setMaxWidth(0);
        tableList.getColumnModel().getColumn(0).setMinWidth(0);
        tableList.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        tableList.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
        
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
      	if (e.getSource()==(chooserDesde)||e.getSource()==(chooserDesde)){
      		if (chooserDesde.getDate()!=null){
  				FECHA_DESDE = form.format(chooserDesde.getDate());
  				llenarTable(PAGO, textNom.getText());
      		}
		}
      	if (e.getSource()==(chooserHasta)||e.getSource()==(chooserHasta)){
      		if (chooserHasta.getDate()!=null){
      			FECHA_HASTA = form.format(chooserHasta.getDate());
      			llenarTable(PAGO, textNom.getText());
      		}
		}
	}
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		//char e=evet.getKeyChar();
	}
	@Override
	public void keyReleased(KeyEvent evet) {
		// TODO Auto-generated method stub
		char e=evet.getKeyChar();
		if (evet.getSource().equals(textNom)) {
			int pos = textNom.getCaretPosition();textNom.setText(textNom.getText().toUpperCase());textNom.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
			llenarTable(PAGO,textNom.getText());
			if (textNom.getText().toLowerCase().isEmpty()|| textNom.getText().toLowerCase().length()>=38){
				textNom.requestFocus();
				textNom.selectAll();
				textNom.setText(null);
				} 
				 if (e==KeyEvent.VK_ENTER){
					 buttonPint.doClick();
				}
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void actionPerformed(ActionEvent action) {
		// TODO Auto-generated method stub
		if (action.getSource().equals(buttonNuevo)) {
			  textNom.requestFocus();
		}
		if (action.getSource().equals(buttonPint)) {
			Imprimir();
		}
      	if (action.getSource().equals(rdbtnPendiente)){
      		PAGO="A";
      		llenarTable(PAGO, textNom.getText());
      	}
      	if (action.getSource().equals(rdbtnCancelada)){
      		PAGO="X";
      		llenarTable(PAGO, textNom.getText());
      	}
		if (action.getSource().equals(buttonSalir)) {
			frame.dispose();
		}
	}
	void Imprimir() {
		try {
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("PrtNomEmpresa",new String (VentanaLogin.RAZON_SOCIAL_HOTEL));
			parameters.put("PrtRucEmpresa",new String (VentanaLogin.RUC_HOTEL));
			parameters.put("PrtDirEmpresa",new String (VentanaLogin.DIRECCION_HOTEL));
			if (!VentanaLogin.URLIMAGE_HOTEL.equals("")) {
				parameters.put("PrtUbiFoto",new String (VentanaLogin.URLIMAGE_HOTEL));
			//}else{
				//parameters.put("PrtUbiFoto",new String (Menu.URL_IMAGE+"login.png"));
			}
			DatasourceCts source = new DatasourceCts();
		    for(int i=0;i<tableList.getRowCount();i++)
		    {
		    	ListCts c= new ListCts(tableList.getValueAt(i, 1).toString().trim(),tableList.getValueAt(i, 2).toString().trim()+"  "+tableList.getValueAt(i, 3).toString().trim(), tableList.getValueAt(i, 4).toString().trim(), tableList.getValueAt(i, 5).toString().trim(), tableList.getValueAt(i, 6).toString().trim());
		    	source.addList(c);
		    }
       
			if(rdbtnPendiente.isSelected()) {
				parameters.put("PrtSubTitle",new String ("LISTADO DE CTAS:  ")+rdbtnPendiente.getText());
			}
			if(rdbtnCancelada.isSelected()) {
				parameters.put("PrtSubTitle",new String ("LISTADO DE CTAS:  ")+rdbtnCancelada.getText());
			}
	        JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(Menu.URL+"RCta.jasper");  
	        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, source);  
	        
	        //===============================  REPORTE NORMAL
	        //JasperViewer visor = new JasperViewer(jasperPrint,false) ;
			//visor.setTitle("Cuentas ");
			//visor.setVisible(true) ; 
	        //=============================== END REPORTE NORMAL
	        
	        //=============================== END REPORTE INCRUSTADO CON FRAME
	        
			
	   		JRViewer jrViewer = new  JRViewer (jasperPrint);
	   		JInternalFrame fram = new JInternalFrame("Cuentas");
			//fram.setPreferredSize(new java.awt.Dimension(1000, 700));
			//fram.setLayout(new java.awt.BorderLayout());
			fram.setMaximizable(true);
			fram.setClosable(true);
			fram.setIconifiable(true);
			fram.repaint();
			fram.revalidate();
			fram.setSize(900,700);
			fram.getContentPane().add(jrViewer);
			fram.setVisible(true);
			Menu.Desktop.add(fram);
			fram.setMaximum(true);
			
			//Menu.Desktop.removeAll();//CIERRA TODAS LAS VENTASNA
			//=============================== END REPORTE INCRUSTADO CON FRAME
			
			//=============================== END REPORTE INCRUSTADO
			/*JRViewer jrViewer = new  JRViewer (jasperPrint);  
			Menu.Desktop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
			Menu.Desktop.setPreferredSize(new java.awt.Dimension(1000, 700));
			Menu.Desktop.setLayout(new java.awt.BorderLayout());
			Menu.Desktop.removeAll();
			Menu.Desktop.add(jrViewer);
			//Menu.Desktop.repaint();
			Menu.Desktop.revalidate();*/
			//=============================== END REPORTE INCRUSTADO
			
		    //EXPORTAR EL INFORME A PDF
		    //JasperExportManager.exportReportToPdfFile(jasperPrint,Menu.URL + "RCta..pdf");
		} catch (Exception e) {}
	}
	@Override
	public void mouseClicked(MouseEvent e) {

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mousePressed(MouseEvent Mouse_evt) {
		if (Mouse_evt.getSource().equals(tableList)) {
			try {
			frameDetalle(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim()));
			if (Mouse_evt.getClickCount() == 2) {
				frameDetalle.setVisible(true);
				frameDetalle(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim()));
			}
			} catch (Exception e) {}
		}
	}
	@Override
	public void mouseReleased(MouseEvent evet) {
		// TODO Auto-generated method stub
	}
	public void frameDetalle(int nroCta) {
		JScrollPane scrollTableDetalle = new JScrollPane();
		JTable tableListDestalle = new JTable();
		
		frameDetalle = new JDialog();
		frameDetalle.getContentPane().setBackground(new Color(245, 245, 220));
		frameDetalle.setBackground(new Color(245, 245, 220));
		frameDetalle.getContentPane().setForeground(Color.DARK_GRAY);
		frameDetalle.setForeground(Color.DARK_GRAY);
		frameDetalle.setIconImage(Toolkit.getDefaultToolkit().getImage(RptCta.class.getResource("/modelo/Images/ok.png")));
		frameDetalle.setResizable(false);
		frameDetalle.setTitle("Cronograma de pagos por cuenta");
		frameDetalle.setBounds(100, 100, 729, 230);
		frameDetalle.getContentPane().setLayout(null);
		frameDetalle.setLocationRelativeTo(null);
		frameDetalle.setModal(true);
		// CREO LISTA
		tableListDestalle_1 = new JTable(); 
		tableListDestalle_1.setFillsViewportHeight(true);
		tableListDestalle_1.setBackground(new Color(245, 245, 220));
		tableListDestalle_1.setForeground(Color.DARK_GRAY);
		tableListDestalle_1.setShowHorizontalLines(false);
		tableListDestalle_1.addMouseListener(this);
		tableListDestalle_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
		scrollTableDetalle = new JScrollPane();
		scrollTableDetalle.setViewportView(tableListDestalle_1);
		scrollTableDetalle.setBounds(10, 28, 703, 163);
        frameDetalle.getContentPane().add(scrollTableDetalle);
		
        // LLENO TABLE
        conexion = new ConexionDB();
        String sq="";
        try {
        	
     	   int totalitems=0;
     	  tableListDestalle_1.setTableHeader(null);
     	  
     	  JLabel Lbl_1 = new JLabel("# Pago");
     	  Lbl_1.setBackground(new Color(245, 245, 220));
     	  Lbl_1.setForeground(Color.DARK_GRAY);
     	  JLabel Lbl_2 = new JLabel("Hab.");
     	  Lbl_2.setBackground(new Color(245, 245, 220));
     	  Lbl_2.setForeground(Color.DARK_GRAY);
     	  JLabel Lbl_3 = new JLabel("Responsable de pago");
     	  Lbl_3.setBackground(new Color(245, 245, 220));
     	  Lbl_3.setForeground(Color.DARK_GRAY);
     	  JLabel Lbl_4 = new JLabel("Modo de pago");
     	  Lbl_4.setBackground(new Color(245, 245, 220));
     	  Lbl_4.setForeground(Color.DARK_GRAY);
     	  JLabel Lbl_5 = new JLabel("Fecha de emisión");
     	  Lbl_5.setBackground(new Color(245, 245, 220));
     	  Lbl_5.setForeground(Color.DARK_GRAY);
     	  JLabel Lbl_6 = new JLabel("Importes S/.");
     	  Lbl_6.setBackground(new Color(245, 245, 220));
     	  Lbl_6.setForeground(Color.DARK_GRAY);
     	 
     	  JLabel Lbl_7 = new JLabel("Tur / Ope");
     	  Lbl_7.setBackground(new Color(245, 245, 220));
     	  Lbl_7.setForeground(Color.DARK_GRAY);
     	  
     	  Lbl_1.setBounds(10, 11, 45, 14);
     	  Lbl_1.setHorizontalAlignment(SwingConstants.LEFT);
     	  Lbl_1.setFont(Menu.fontLabel);
  	
     	  Lbl_2.setBounds(59, 11, 45, 14);
     	  Lbl_2.setHorizontalAlignment(SwingConstants.LEFT);
     	  Lbl_2.setFont(Menu.fontLabel);
  	
     	  Lbl_3.setBounds(135, 11, 115, 14);
     	  Lbl_3.setHorizontalAlignment(SwingConstants.LEFT);
     	  Lbl_3.setFont(Menu.fontLabel);
  	
     	  Lbl_4.setBounds(281, 11, 106, 14);
     	  Lbl_4.setHorizontalAlignment(SwingConstants.RIGHT);
     	  Lbl_4.setFont(Menu.fontLabel);
  	
     	  Lbl_5.setBounds(438, 11, 96, 14);
     	  Lbl_5.setHorizontalAlignment(SwingConstants.LEFT);
     	  Lbl_5.setFont(Menu.fontLabel);
  	
     	  Lbl_6.setBounds(558, 11, 76, 14);
     	  Lbl_6.setHorizontalAlignment(SwingConstants.RIGHT);
     	  Lbl_6.setFont(Menu.fontLabel);
  	
     	  Lbl_7.setBounds(647, 11, 57, 14);
     	  Lbl_7.setHorizontalAlignment(SwingConstants.RIGHT);
     	  Lbl_7.setFont(Menu.fontLabel);
     	  
     	  frameDetalle.getContentPane().add(Lbl_1);
     	  frameDetalle.getContentPane().add(Lbl_2);
     	  frameDetalle.getContentPane().add(Lbl_3);
     	  frameDetalle.getContentPane().add(Lbl_4);
     	  frameDetalle.getContentPane().add(Lbl_5);
     	  frameDetalle.getContentPane().add(Lbl_6);
     	  frameDetalle.getContentPane().add(Lbl_7);
     	  
     	   model= new DefaultTableModel();
		   model.addColumn("# Pago");
		   model.addColumn("Hab.");
		   model.addColumn("Responsable de pago");
		   model.addColumn("Modo de pago");
		   model.addColumn("Fecha de emisión");
		   model.addColumn("Importes S/.");
		   model.addColumn("Tur / Ope");
		   
 		   String []datos= new String[7];
 		   tableListDestalle_1.setModel(model);
 		  
		   sq="SELECT * FROM CUENTA_HUESPED_PAGOS where Id_Cta  ='" + nroCta + "' order by Id_DetCta asc";
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(sq);

    	   while(rs.next()) {
		    	datos[0]=" "+rs.getString("Id_DetCta");
		    	datos[1]=" "+rs.getString("NrohabCta");
		    	datos[2]=" "+rs.getString("ResDetCta");
		    	datos[3]=" "+rs.getString("MPagoDetCta");
		    	datos[4]=" "+Menu.formatoFechaString.format(rs.getDate("FechaDetCta")) +"  " +rs.getString("HoraDetCta");
		    	datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("AcuentaDetCta"));
		    	
		    	//CONSULTO TURNO Y USER
		    	String strin="";
				Statement ss = conexion.gConnection().createStatement();
				//ResultSet rr = ss.executeQuery("Select * from EMPLEADO  as E, CAJA_APE_CIE as AP where AP.Id_ApeCie='" + rs.getString("IdTurno")+"'");
				ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + rs.getInt("IdTurnoP")+"'");
				if (rr.next()== true) {
					// NOMBRE CORTO DE USUARIO Y TURNO
					String TUR="",USU="";
					String TEMP_TUR="",TEMP_NOM="";
					String delimiter = " ";
					String[] temp;
					
					temp = rr.getString("turno").trim().split(delimiter);
					for(int i =0; i < temp.length ; i++){
						if (temp[i].trim().length()>=(1)) {
							TEMP_TUR=temp[i].trim().substring(0, 1);
							}
							TUR=TUR.trim() + TEMP_TUR.trim();
					}
					
					temp =  rr.getString("NombresEmp").trim().split(delimiter);
					for(int i =0; i < temp.length ; i++){
						if (temp[i].trim().length()>=(1)) {
							TEMP_NOM=temp[i].trim().substring(0, 1);
							}
							USU=USU.trim() + TEMP_NOM.trim();
					}
					strin=TUR +" / "+ USU;
					// NOMBRE CORTO DE USUARIO Y TURNO
				}
				rr.close();
				ss.close();
				//END CONSULTO TURNO Y USER
				datos[6]="    "+strin.trim();
		    	
		    	
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableListDestalle.setModel(model);

	        }
    	   // MODELO TABLE
    	   int CONT=10;
    	   if (totalitems>0) {
    		   int c=0;
	            c=CONT-totalitems;
	            if ( CONT>totalitems) {
				    String []registros= new String[c];
				    for (int n=0; n<c;n++) {
					    model.addRow(registros);
					    tableListDestalle.setModel(model);
				    }
	            }   
		   }else{
			   //CONT=CONT+2;
			    String []registros= new String[CONT];
			    for (int n=0; n<CONT;n++) {
				    model.addRow(registros);
				    tableListDestalle.setModel(model);
			    }
		   }
    	   // FIN MODELOTABLE
    	   
           DefaultTableCellRenderer modeloRight = new DefaultTableCellRenderer();
           modeloRight.setHorizontalAlignment(SwingConstants.RIGHT);

           DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
           modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
           
           tableListDestalle_1.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
           
           //tableList.getColumnModel().getColumn(1).setMinWidth(0);
           //tableList.getColumnModel().getColumn(1).setMaxWidth(0);
           
           	tableListDestalle_1.getColumnModel().getColumn(0).setPreferredWidth(20);
           	tableListDestalle_1.getColumnModel().getColumn(1).setPreferredWidth(15);
           	tableListDestalle_1.getColumnModel().getColumn(2).setPreferredWidth(180);
     	  	tableListDestalle_1.getColumnModel().getColumn(3).setPreferredWidth(100);
     	  	tableListDestalle_1.getColumnModel().getColumn(4).setPreferredWidth(125);
     	  	tableListDestalle_1.getColumnModel().getColumn(5).setPreferredWidth(50);
     	  	tableListDestalle_1.getColumnModel().getColumn(6).setPreferredWidth(50);
           rs.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

