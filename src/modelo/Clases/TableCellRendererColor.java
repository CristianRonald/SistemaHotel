package modelo.Clases;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class TableCellRendererColor extends DefaultTableCellRenderer {
	private JLabel componente;
	private String nombre_table;
	
	public TableCellRendererColor(String nombre_table) {
		super();
		this.nombre_table = nombre_table;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		componente=(JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	
        // ==============================================================================TABLE HABITACION
        if (nombre_table.equals("HABITACION")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            //componente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(221, 211, 211)));
            componente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(221, 211, 211)));
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	//componente.setBackground(new Color(240, 240, 250));
            	componente.setBackground(new Color(255, 255, 238));
            }else{
            	componente.setBackground(new Color(255, 245, 238));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
	        if(value instanceof Float||value instanceof Double) {
	        	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
            	 //((JLabel)componente).setSize( 60, componente.getWidth() );
            	 //((JLabel)componente).setPreferredSize( new Dimension(6, componente.getWidth()) );
	        }
	        if(value instanceof String) {
	        	((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
	           	 //((JLabel)componente).setSize( 60, componente.getWidth() );
	           	 //((JLabel)componente).setPreferredSize( new Dimension(6, componente.getWidth()) );
	        }
        }
        
     // ==============================================================================TABLE PLAN
        if (nombre_table.equals("PLAN")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            //componente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(221, 211, 211)));
            componente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(221, 211, 211)));
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	//componente.setBackground(new Color(240, 240, 250));
            	componente.setBackground(new Color(255, 255, 238));
            }else{
            	componente.setBackground(new Color(255, 245, 238));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==3){
           	 	((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
            }
            if(column==0||column==1||column==2||column==4||column==5){
           	 	((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
           }
        }
        
        // ==============================================================================TABLE BLANCE DE CAJA
        if (nombre_table.equals("BALANCE")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	componente.setBackground(Color.WHITE);
            }else{
            	componente.setBackground(new Color(245, 250, 230));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==0||column==1||column==2||column==3||column==4){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
            }
            if(column==5||column==6||column==7){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
            }
            if(column==5){
           	 componente.setForeground(SystemColor.blue);
           }
            if(column==6){
           	 componente.setForeground(SystemColor.red);
           }
            if(column==7){
           	 componente.setForeground(new Color(51, 153, 51));
           }
            
        }
        
        // ==============================================================================TABLE TARIFAS
        if (nombre_table.equals("TARIFAS")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	componente.setBackground(Color.white);
            }else{
            	componente.setBackground(new Color(255, 245, 238));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==0||column==1||column==2||column==3){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
            }
            if(column==4||column==5){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
            	 componente.setForeground(SystemColor.blue);
            }
            if(column==6){
           	 	((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
            }
        }
        
        // ==============================================================================REPORTE FACTURACION
        if (nombre_table.equals("RPT_FACTURACION")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	componente.setBackground(Color.white);
            	componente.setBackground(new Color(255, 255, 238));
            }else{
            	componente.setBackground(new Color(255, 245, 238));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==0||column==1||column==2||column==3||column==4||column==5||column==6||column==12){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
            }
            if(column==7||column==8||column==9||column==10){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
            	 //componente.setForeground(SystemColor.blue);
            }
            if(column==11){
           	 	((JLabel)componente).setHorizontalAlignment(SwingConstants.CENTER );
            }
        }
        
        
        // ==============================================================================VISTA_TARIFA
        if (nombre_table.equals("VISTA_TARIFA")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	componente.setBackground(Color.white);
            	componente.setBackground(new Color(255, 255, 238));
            }else{
            	componente.setBackground(new Color(255, 245, 238));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==0){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
            }
            if(column==1||column==2){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
            	 //componente.setForeground(SystemColor.blue);
            }
        }
        
        
        // ==============================================================================TABLE BLANCE DE CAJA
        if (nombre_table.equals("VENTANA_GASTOS")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	componente.setBackground(Color.WHITE);
            }else{
            	componente.setBackground(new Color(250, 250, 220));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==0||column==1||column==2||column==3||column==4){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
            }
            if(column==5){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
              	 componente.setForeground(new Color(178, 34, 34));
            }
        }
        
        // ==============================================================================TABLE VISTA CONSUMO
        if (nombre_table.equals("VISTA_CONSUMO")) {
    		componente.setForeground(SystemColor.DARK_GRAY);
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	componente.setBackground(Color.WHITE);
            }else{
            	componente.setBackground(new Color(250, 250, 220));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==0||column==1||column==5||column==6){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
            }
            if(column==2||column==3||column==4){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
              	 //componente.setForeground(new Color(178, 34, 34));
            }
        }
        
        // ==============================================================================TABLE GENERAR DOCUMENTO
        if (nombre_table.equals("VENTANA_GENERAR_DOCUMENTO")) {
          	JCheckBox check = new JCheckBox();  
          	
    		componente.setForeground(SystemColor.DARK_GRAY);
            componente.setOpaque(true);
            
            if(row % 2 == 0){//ESTABLECEMOS LA FILAS QUE QUEREMOS CAMBIAR el color. == 0 PARA pares y != 0 PARA IMPARES
            	componente.setBackground(Color.WHITE);
            }else{
            	componente.setBackground(new Color(250, 250, 220));
            }
            if(isSelected){//CUANDO SELECIONO
            	componente.setBackground(new Color(240, 230, 140));
            	componente.setForeground(SystemColor.blue);
            	componente.setFont(new Font("SansSerif", Font.BOLD, 11));
            }
            
            if(column==0||column==2){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT);
            }
            if(column==1){
           	 	((JLabel)componente).setHorizontalAlignment(SwingConstants.CENTER);
           }
            if(column==3||column==4||column==5){
            	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
              	 //componente.setForeground(new Color(178, 34, 34));
            }
            if(column==6){
            	
            	Boolean bol = Boolean.valueOf(String.valueOf(value));
                if( value == null) {             
                    return null;
                }else{ // es un boolean   
                check = new JCheckBox();                                
                check.setHorizontalAlignment( JLabel.CENTER );                
                check.setBackground((isSelected)? new Color( 50, 153 , 254):new Color(255,255,255) );
                check.setSelected(bol); //valor de celda 
                return check;
                }
            }
        }
		 return componente;
		} 
	
}
