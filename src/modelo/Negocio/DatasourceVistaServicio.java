package modelo.Negocio;

import java.util.ArrayList;
import java.util.List;

import modelo.Clases.ListVistaServicio;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DatasourceVistaServicio implements JRDataSource {
	private List<ListVistaServicio> lista = new ArrayList<ListVistaServicio>();
    private int indiceActual = -1;
	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		// TODO Auto-generated method stub
		Object valor = null;  

	    if("codigo".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getCodigo(); 
	    } 
	    
	    else if("fecha".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getFecha(); 
	    } 
	    else if("nro".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getNro(); 
	    } 
	    else if("alojamiento".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getAlojamiento(); 
	    } 
	    else if("servicios".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getServicios();
	    }
	    else if("vitrina".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getVitrina();
	    }
	    else if("detalle".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getDetalle();
	    }
	    else if("turno".equals(jrField.getName())) 
	    { 
	        valor = lista.get(indiceActual).getTurno();
	    }
	    return valor; 
	}

	@Override
	public boolean next() throws JRException {
		// TODO Auto-generated method stub
		return ++indiceActual < lista.size();
	}
	 public void addList(ListVistaServicio LLENO)
	    {
	        this.lista.add(LLENO);
	    }
}
