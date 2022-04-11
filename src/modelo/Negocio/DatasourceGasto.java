package modelo.Negocio;

import java.util.ArrayList;
import java.util.List;

import modelo.Clases.ListGasto;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DatasourceGasto implements JRDataSource{
	private List<ListGasto> lista = new ArrayList<ListGasto>();
	private int indiceActual = -1;
	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		// TODO Auto-generated method stub
		Object valor = null;  

	    if("id".equals(jrField.getName())) 
	    { 
	    	valor = lista.get(indiceActual).getId();
	    } 
	    else if("tipo".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getTipo(); 
	    } 
	    else if("descripcion".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getDescripcion(); 
	    } 
	    else if("documento".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getDocumento(); 
	    } 
	    else if("fechahora".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getFechahora();
	    } 
	    else if("monto".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getMonto();
	    }
	 
	    return valor; 
	}

	@Override
	public boolean next() throws JRException {
		// TODO Auto-generated method stub
		return ++indiceActual < lista.size();
	}
	 public void addList(ListGasto LLENO)
	    {
	        this.lista.add(LLENO);
	    }
}
