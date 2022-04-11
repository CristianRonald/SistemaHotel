package modelo.Clases;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class TableColor extends DefaultTableCellRenderer{
	private JLabel componente;
    private int columna_patron ;

    public TableColor(int Colpatron)
    {
        this.columna_patron = Colpatron;
    }

    @Override
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {        
    	componente=(JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	
        setBackground(Color.white);//color de fondo
        table.setForeground(Color.black);//color de texto
        //Si la celda corresponde a una fila con estado FALSE, se cambia el color de fondo a rojo
        if( table.getValueAt(row,columna_patron).toString().trim().equals("CONSUMO") )
        {
            setBackground(Color.cyan);
        }
        
        if(column==0||column==1||column==2||column==3||column==4||column==8){
       	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.LEFT );
        }
        if(column==5||column==6){
       	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.RIGHT );
        }
        if(column==7){
          	 ((JLabel)componente).setHorizontalAlignment(SwingConstants.CENTER );
           }
        if(isSelected){//CUANDO SELECIONO
        	componente.setBackground(new Color(240, 230, 140));
        	componente.setBackground(Color.DARK_GRAY);
        }
        return this;
 }

}
