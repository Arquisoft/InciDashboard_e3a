package uo.asw.dbManagement.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import uo.asw.dbManagement.tipos.EstadoTipos;
import uo.asw.dbManagement.tipos.PerfilTipos;

@Entity
@Table(name = "TINCIDENCIAS")
public class Incidencia {

	@Id
	@GeneratedValue /* (strategy = GenerationType.AUTO) */
	private Long id;

	@NotNull
	@Column(name = "nombre_incidencia")
	private String nombreIncidencia;
	private String descripcion;
	private String latitud;
	private String longitud;
	@Enumerated(EnumType.STRING)
	private EstadoTipos estado;
	@Column(name = "fecha_entrada")
	@Temporal(TemporalType.DATE)
	private Date fechaEntrada;
	@Column(name = "fecha_caducidad")
	@Temporal(TemporalType.DATE)
	private Date fechaCaducidad;
	private Double minimoValor;
	private Double maximoValor;
	// @Column(name = "id_agente")
	@ManyToOne
	private Agente agente;

	@ManyToOne
	private Usuario operario;

	@OneToMany(mappedBy = "incidencia")
	private Set<Propiedad> propiedades = new HashSet<Propiedad>();

	@OneToMany(mappedBy = "incidenciaC")
	private Set<Categoria> categorias = new HashSet<Categoria>();

	public Incidencia() {
	}

	/**
	 * Constructor que crea una incidencia desde parámetros String
	 * 
	 * @param nombreIncidencia
	 * @param descripcion
	 * @param latitud
	 * @param longitud
	 * @param estado
	 * @param fechaEntrada
	 * @param fechaCaducidad
	 * @param agente
	 * @param propiedades
	 * @param categorias
	 */
	public Incidencia(@NotNull String nombreIncidencia, String descripcion, String latitud, String longitud,
			EstadoTipos estado, Date fechaEntrada, Date fechaCaducidad, Double minimoValor, Double maximoValor,
			Agente agente, String propiedades, String categorias) {

		this.nombreIncidencia = nombreIncidencia;
		this.descripcion = descripcion;
		this.latitud = latitud;
		this.longitud = longitud;
		this.estado = EstadoTipos.ABIERTA;
		this.fechaEntrada = fechaEntrada;
		this.fechaCaducidad = fechaCaducidad;
		this.minimoValor = minimoValor;
		this.maximoValor = maximoValor;
		this.agente = agente;
		this.addListaPropiedades(propiedades);
		this.addListaCategorias(categorias);
	}

	public Incidencia(String nombreIncidencia, String descripcion, String latitud, String longitud, Date fechaEntrada,
			Date fechaCaducidad, Agente agente, Set<Propiedad> propiedades, Set<Categoria> categorias) {
		super();
		this.nombreIncidencia = nombreIncidencia;
		this.descripcion = descripcion;
		this.latitud = latitud;
		this.longitud = longitud;
		this.estado = EstadoTipos.ABIERTA;
		this.fechaEntrada = fechaEntrada;
		this.fechaCaducidad = fechaCaducidad;
		this.agente = agente;
		this.propiedades = propiedades;
		this.categorias = categorias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreIncidencia() {
		return nombreIncidencia;
	}

	public void setNombreIncidencia(String nombreIncidencia) {
		this.nombreIncidencia = nombreIncidencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public EstadoTipos getEstado() {
		return estado;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	/*
	 * public Long getIdAgente() { return idAgente; }
	 * 
	 * public void setIdAgente(Long idAgente) { this.idAgente = idAgente; }
	 */

	public Set<Propiedad> getPropiedades() {
		return propiedades;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public void setPropiedades(Set<Propiedad> propiedades) {
		this.propiedades = propiedades;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Usuario getOperario() {
		return operario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaEntrada == null) ? 0 : fechaEntrada.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((agente == null) ? 0 : agente.hashCode());
		result = prime * result + ((nombreIncidencia == null) ? 0 : nombreIncidencia.hashCode());
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
		Incidencia other = (Incidencia) obj;
		if (fechaEntrada == null) {
			if (other.fechaEntrada != null)
				return false;
		} else if (!fechaEntrada.equals(other.fechaEntrada))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (agente == null) {
			if (other.agente != null)
				return false;
		} else if (!agente.equals(other.agente))
			return false;
		if (nombreIncidencia == null) {
			if (other.nombreIncidencia != null)
				return false;
		} else if (!nombreIncidencia.equals(other.nombreIncidencia))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Incidencia [id=" + id + ", nombreIncidencia=" + nombreIncidencia + ", descripcion=" + descripcion
				+ ", latitud=" + latitud + ", longitud=" + longitud + ", estado=" + estado + ", fechaEntrada="
				+ fechaEntrada + ", fechaCaducidad=" + fechaCaducidad + ", agente=" + agente + ", propiedades="
				+ propiedades + ", categorias=" + categorias + "]";
	}

	/**
	 * Añade una categoria al conjunto de categorias de la Incidencia
	 * 
	 * @param categoria
	 */
	public void addCategoria(Categoria categoria) {
		this.categorias.add(categoria);
	}

	/**
	 * Añade una propiedad al conjunto de propiedades de la Incidencia
	 * 
	 * @param propiedad
	 */
	public void addPropiedad(Propiedad propiedad) {
		this.propiedades.add(propiedad);

	}

	/**
	 * Recibe un string de categorias separadas por comas y las añade al
	 * conjunto de categorias de la incidencia
	 * 
	 * @param String
	 *            lista
	 */
	public void addListaCategorias(String lista) {
		String[] categorias = lista.split(",");
		for (int i = 0; i < categorias.length; i++) {
			this.addCategoria(new Categoria(categorias[i], this));
		}
	}

	/**
	 * REcibe un string de propiedades separadas por comas y las añade al
	 * conjunto de propiedades de la incidencia
	 * 
	 * @param String
	 *            lista
	 */
	public void addListaPropiedades(String lista) {
		String[] propiedades = lista.split(",");
		for (int i = 0; i < propiedades.length; i++) {
			String[] propiedad = propiedades[i].split("/");
			// this.addPropiedad(new Propiedad(propiedad[0],
			// this.getId(), Double.parseDouble(propiedad[1])));
			this.addPropiedad(new Propiedad(propiedad[0], this, Double.parseDouble(propiedad[1])));
		}
	}

	/**
	 * Recibe un usuario de tipo operario y lo añade
	 * 
	 * @param operario
	 *            de tipo Usuario
	 * @return true si se ha asignado false en caso contrario
	 */
	public boolean asignarOperario(Usuario operario) {
		if (estado.equals(EstadoTipos.ABIERTA) && operario.getPerfil().equals(PerfilTipos.OPERARIO)) {
			this.operario = operario;
			this.estado = EstadoTipos.EN_PROCESO;
			return true;
		}
		return false;
	}

	/**
	 * Cierra la incidencia si esta se encuentra en proceso y si tiene asignada
	 * un operario
	 * 
	 * @return true si se pasa a estado cerrada false en caso contrario
	 */
	public boolean cerrarIncidencia() {
		if (estado.equals(EstadoTipos.EN_PROCESO) && operario != null) {
			this.estado = EstadoTipos.CERRADA;
			return true;
		}
		return false;
	}

	/**
	 * Anula la incidencia siempre que esta no este en estado cerrada
	 * 
	 * @return true si se ha anulado y false en caso contrario
	 */
	public boolean anularIncidencia() {
		if (!estado.equals(EstadoTipos.CERRADA)) {
			this.estado = EstadoTipos.ANULADA;
			return true;
		}
		return false;

	}

	public Double getMinimoValor() {
		return minimoValor;
	}

	public void setMinimoValor(Double minimoValor) {
		this.minimoValor = minimoValor;
	}

	public Double getMaximoValor() {
		return maximoValor;
	}

	public void setMaximoValor(Double maximoValor) {
		this.maximoValor = maximoValor;
	}
}
