package uo.asw.dbManagement.model;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import uo.asw.dbManagement.tipos.PropiedadTipos;

@Document(collection = "cuadros")
public class CuadroMandos {

	@Id
	private ObjectId id = new ObjectId();

	Map<PropiedadTipos, Integer> valoresMaximos = new HashMap();
	Map<PropiedadTipos, Integer> valoresMinimos = new HashMap();

	public CuadroMandos() {
	}

	public Map<PropiedadTipos, Integer> getValoresMaximos() {
		return valoresMaximos;
	}

	public void setValoresMaximos(Map<PropiedadTipos, Integer> valoresMaximos) {
		this.valoresMaximos = valoresMaximos;
	}

	public Map<PropiedadTipos, Integer> getValoresMinimos() {
		return valoresMinimos;
	}

	public void setValoresMinimos(Map<PropiedadTipos, Integer> valoresMinimos) {
		this.valoresMinimos = valoresMinimos;
	}

	@Override
	public String toString() {
		return "CuadroMandos [id=" + id + ", valoresMaximos=" + valoresMaximos + ", valoresMinimos=" + valoresMinimos
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valoresMaximos == null) ? 0 : valoresMaximos.hashCode());
		result = prime * result + ((valoresMinimos == null) ? 0 : valoresMinimos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CuadroMandos other = (CuadroMandos) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valoresMaximos == null) {
			if (other.valoresMaximos != null)
				return false;
		} else if (!valoresMaximos.equals(other.valoresMaximos))
			return false;
		if (valoresMinimos == null) {
			if (other.valoresMinimos != null)
				return false;
		} else if (!valoresMinimos.equals(other.valoresMinimos))
			return false;
		return true;
	}
}
