package uo.asw.dbManagement.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import uo.asw.dbManagement.tipos.CategoriaTipos;

@Entity
@Table (name = "TCATEGORIAS")
public class Categoria {
	@Id
	@GeneratedValue /*(strategy = GenerationType.AUTO)*/
	private Long id;
	
	@Enumerated(EnumType.STRING) private CategoriaTipos categoria;
	
	@ManyToOne
//	@JoinColumn(name = "id_incidencia")
	private Incidencia incidenciaC;
	
	public Categoria() {}

	public Categoria(CategoriaTipos categoria, Incidencia idIncidenciaC) {
		super();
		this.categoria = categoria;
		this.incidenciaC = idIncidenciaC;
	}
	
	public Categoria(String categoria, Incidencia idIncidenciaC) {
		super();
		this.categoria = this.obtenerCategoria(categoria);
		this.incidenciaC = idIncidenciaC;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CategoriaTipos getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaTipos categoria) {
		this.categoria = categoria;
	}

	public Incidencia getIdIncidenciaC() {
		return incidenciaC;
	}

	public void setIdIncidenciaC(Incidencia idIncidenciaC) {
		this.incidenciaC = idIncidenciaC;
	}

	

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", categoria=" + categoria 
				+ ", incidenciaC=" + incidenciaC + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Categoria other = (Categoria) obj;
		if (categoria != other.categoria)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Devuelve un tipo de categoria según la categoria pasada como 
	 * parámetro
	 * @param String categoria
	 * @return CategoriaTipos
	 */
	private CategoriaTipos obtenerCategoria(String categoria) {  
		if (categoria.toUpperCase().equals("ACCIDENTE_CARRETERA"))
			return CategoriaTipos.ACCIDENTE_CARRETERA;
		if (categoria.toUpperCase().equals("INUNDACION"))
			return CategoriaTipos.INUNDACION;
		if (categoria.toUpperCase().equals("FUEGO"))
			return CategoriaTipos.FUEGO;
		if (categoria.toUpperCase().equals("ACCIDENTE_AEREO"))
			return CategoriaTipos.ACCIDENTE_AEREO;
		if (categoria.toUpperCase().equals("METEOROLOGICA"))
			return CategoriaTipos.METEOROLOGICA;
		return CategoriaTipos.VALOR_NO_ASIGNADO;
	}

}
