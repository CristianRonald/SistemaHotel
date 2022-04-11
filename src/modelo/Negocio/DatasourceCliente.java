package modelo.Negocio;

import java.util.ArrayList;
import java.util.List;

import modelo.Clases.ListCliente;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DatasourceCliente implements JRDataSource {
	private List<ListCliente> lista = new ArrayList<ListCliente>();
    private int indiceActual = -1;
	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		// TODO Auto-generated method stub
		Object valor = null;  

	    if("codigo".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getCodigo(); 
	    } 
	    
	    else if("nombres".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getNombres(); 
	    } 
	    else if("documento".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getDocumento(); 
	    } 
	    else if("ruc".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getRuc(); 
	    } 
	    else if("nacionalidad".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getNacionalidad();
	    }
	    else if("telefono".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getTelefono();
	    }
	    return valor; 
	}

	@Override
	public boolean next() throws JRException {
		// TODO Auto-generated method stub
		return ++indiceActual < lista.size();
	}
	 public void addList(ListCliente LLENO)
	    {
	        this.lista.add(LLENO);
	    }
}
