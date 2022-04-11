package modelo.Negocio;

import java.util.ArrayList;
import java.util.List;

import modelo.Clases.ListCts;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DatasourceCts implements JRDataSource{
	private List<ListCts> lista = new ArrayList<ListCts>();
	private int indiceActual = -1;
	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		// TODO Auto-generated method stub
		Object valor = null;  

	    if("id".equals(jrField.getName())) 
	    { 
	    	valor = lista.get(indiceActual).getId();
	    } 
	    else if("descripcion".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getDescripcion(); 
	    } 
	    else if("fechahora".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getFechahora(); 
	    } 
	    else if("monto".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getMonto();
	    } 
	    else if("saldo".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getSaldo();
	    }
	 
	    return valor; 
	}

	@Override
	public boolean next() throws JRException {
		// TODO Auto-generated method stub
		return ++indiceActual < lista.size();
	}
	 public void addList(ListCts LLENO)
	    {
	        this.lista.add(LLENO);
	    }
}
