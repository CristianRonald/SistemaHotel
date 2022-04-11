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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

import java.awt.Font;
import javax.swing.border.LineBorder;

public class VistaTarifa implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public JDialog 	frame;
	private JPanel  panelLst;


	private JLabel				lbl1;
	private JButton  			buttonSalir;
	protected static JTextField textNom;
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	private String consultar="";
	private JLabel lblTipoTarifaPrecio;
	public VistaTarifa() {
		frameTarifa();
		crearPanel();
		crearButtons();
		crearTable();
		crearTextFields();
		crearLabels();
		
		LimpiarTable();
		frame.setModal(true);
	}
	public void frameTarifa() {
		frame = new JDialog();
		frame.setBackground(new Color(143, 188, 143));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaTarifa.class.getResource("/modelo/Images/searchNormal.png")));
		frame.setResizable(false);
		frame.setTitle("Vista previa de tarifas       ");
		frame.setBounds(100, 100, 364, 411);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
	}
	public void crearPanel() {
		panelLst = new JPanel();
		panelLst.setBackground(new Color(143, 188, 143));
		panelLst.setBorder(new LineBorder(new Color(255, 255, 0)));
		panelLst.setBounds(0, 0, 358, 382);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	public void crearLabels(){
		lbl1= new JLabel("DETALLE DEL MENSAJE");
		lbl1.setBackground(new Color(143, 188, 143));
		lbl1.setForeground(new Color(0, 51, 102));
		lbl1.setOpaque(true);
		lbl1.setBounds(4, 11, 350, 20);
		panelLst.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.LEFT);
		lbl1.setFont(new Font("Tahoma", Font.BOLD, 12));
		{
			lblTipoTarifaPrecio = new JLabel(" TIPO DE TARIFA                                         | PRECIO 1  | PRECIO 2");
			lblTipoTarifaPrecio.setOpaque(true);
			lblTipoTarifaPrecio.setHorizontalAlignment(SwingConstants.LEFT);
			lblTipoTarifaPrecio.setForeground(new Color(0, 255, 255));
			lblTipoTarifaPrecio.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblTipoTarifaPrecio.setBackground(new Color(188, 143, 143));
			lblTipoTarifaPrecio.setBounds(2, 34, 354, 20);
			panelLst.add(lblTipoTarifaPrecio);
		}
	}
	public void crearTextFields() {
		textNom =  new  JTextFieldIcon(new JTextField(),"searchNormal.png","Nro #","searchNormal.png");
		textNom.setBackground(new Color(143, 188, 143));
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundInactivo);
		textNom.setHorizontalAlignment(SwingConstants.CENTER);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(292, 12, 62, 20);
		panelLst.add(textNom);
	}
	public void crearButtons() {
		buttonSalir= new JButton("Salir");
		buttonSalir.setBounds(4, 10, 55, 23);
		panelLst.add(buttonSalir);
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setVisible(false);
	}
	public void crearTable() {
		tableList = new JTable();
		tableList.setFillsViewportHeight(true);
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(2, 55, 354, 324);
        panelLst.add(scrollTable);
        tableList.setGridColor(new Color(238, 232, 170));
        tableList.setTableHeader(null);
	}
	public void limpiarTexts() {
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);
		
        //llenarTable("");
	}
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(new Color(46, 139, 87));}
		if (ev.getSource() == textNom){textNom.setForeground(new Color(255, 255, 0));}
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(new Color(143, 188, 143));}
		if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundInactivo);} 
	}
	protected void llenarTable(String Nro, String Detalle) {
		conexion = new ConexionDB();
        try {
        	
     	   int totalitems=0;
     	   model= new DefaultTableModel();
		   model.addColumn("TIPO TARIFA");
		   model.addColumn("PRECIO 1");
		   model.addColumn("PRECIO 2");
		   
 		   String []datos= new String[3];
 		   tableList.setModel(model);
 		  
		   consultar="SELECT * FROM HABITACION as H,TARIFAS as T where T.NumeroHab=H.NumeroHab and T.NumeroHab ='" + Nro + "' order by TipoTar asc";
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
 		  
 		   lbl1.setText("");
    	   while(rs.next()) {
    		   	datos[0]=" "+rs.getString("TipoTar");
		    	datos[1]=Menu.formateadorCurrency.format(rs.getFloat("PrecioTar"))+" ";
		    	datos[2]=Menu.formateadorCurrency.format(rs.getFloat("Precio2Tar"))+" ";
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableList.setModel(model);
	            
	            lbl1.setText(rs.getString("Tipo_Hab") +" #"+ rs.getString("NumeroHab"));
	        }
           rs.close();
           st.close();
           
        // MODELO TABLE
    	   int CONT=20;
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
			    String []registros= new String[CONT];
			    for (int n=0; n<CONT;n++) {
				    model.addRow(registros);
				    tableList.setModel(model);
			    }
		   }
    	   // FIN MODELOTABLE
    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(200);
    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(50);
    	   tableList.getColumnModel().getColumn(2).setPreferredWidth(50);
    	   
    	   tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("VISTA_TARIFA"));//ESTABLESCO COLOR CELDAS
    	   
           //panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "total tarifas: "+totalitems));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
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
			llenarTable(textNom.getText(),"");
			if (textNom.getText().toLowerCase().isEmpty()|| textNom.getText().toLowerCase().length()>=38){
				textNom.requestFocus();
				textNom.selectAll();
				textNom.setText(null);
				} 
				 if (e==KeyEvent.VK_ENTER){
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
		if (action.getSource().equals(buttonSalir)) {
			frame.dispose();
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			if (e.getSource().equals(tableList)) {
				//textNom.setText((String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim());
			}
		} catch (Exception x) {}
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
			if (Mouse_evt.getClickCount() == 2) {
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent evet) {
		// TODO Auto-generated method stub
	}

}

