/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "INVITADO")
public class Invitado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVITADO_SEQ")
	@SequenceGenerator(name = "INVITADO_SEQ", sequenceName = "INVITADO_SEQ", allocationSize = 1)
	@Column(name = "ID_INVITADO")
	private Integer id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_TIPO_INVITADO")
	private TipoInvitado tipoInvitado;
	private String nombre;
	@Column(name = "AP_PATERNO")
	private String apPaterno;
	@Column(name = "AP_MATERNO")
	private String apMaterno;
	private String email;
	private String invitador;
	private String empresa;
	
	private transient String idEventoInvitado;

	//bi-directional many-to-one association to EventoInvitado
	//@OneToMany(mappedBy="invitado", fetch=FetchType.EAGER)
	@OneToMany(mappedBy="invitado", fetch=FetchType.LAZY)
	private List<EventoInvitado> eventoInvitados;

	@ManyToMany(mappedBy = "invitados", fetch = FetchType.EAGER)
	private List<Evento> eventos;

	public Invitado() {
	}

	public Invitado(Integer id, TipoInvitado tipoInvitado, String nombre, String apPaterno, String apMaterno,
			String email, String invitador, String empresa) {
		super();
		this.id = id;
		this.tipoInvitado = tipoInvitado;
		this.nombre = nombre;
		this.apPaterno = apPaterno;
		this.apMaterno = apMaterno;
		this.email = email;
		this.invitador = invitador;
		this.empresa = empresa;
	}

	public Invitado(TipoInvitado tipoInvitado, String nombre, String apPaterno, String apMaterno, String email,
			String invitador, String empresa) {
		this.tipoInvitado = tipoInvitado;
		this.nombre = nombre;
		this.apPaterno = apPaterno;
		this.apMaterno = apMaterno;
		this.email = email;
		this.invitador = invitador;
		this.empresa = empresa;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the tipoInvitado
	 */
	public TipoInvitado getTipoInvitado() {
		return tipoInvitado;
	}

	/**
	 * @param tipoInvitado
	 *            the tipoInvitado to set
	 */
	public void setTipoInvitado(TipoInvitado tipoInvitado) {
		this.tipoInvitado = tipoInvitado;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the apPaterno
	 */
	public String getApPaterno() {
		return apPaterno;
	}

	/**
	 * @param apPaterno
	 *            the apPaterno to set
	 */
	public void setApPaterno(String apPaterno) {
		this.apPaterno = apPaterno;
	}

	/**
	 * @return the apMaterno
	 */
	public String getApMaterno() {
		return apMaterno;
	}

	/**
	 * @param apMaterno
	 *            the apMaterno to set
	 */
	public void setApMaterno(String apMaterno) {
		this.apMaterno = apMaterno;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the invitador
	 */
	public String getInvitador() {
		return invitador;
	}

	/**
	 * @param invitador
	 *            the invitador to set
	 */
	public void setInvitador(String invitador) {
		this.invitador = invitador;
	}

	/**
	 * @return the empresa
	 */
	public String getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            the empresa to set
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Invitado)) {
			return false;
		}
		Invitado other = (Invitado) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the eventos
	 */
	public List<Evento> getEventos() {
		return eventos;
	}

	/**
	 * @param eventos
	 *            the eventos to set
	 */
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Invitado [id=" + id + ", tipoInvitado=" + tipoInvitado + ", nombre=" + nombre + ", apPaterno="
				+ apPaterno + ", apMaterno=" + apMaterno + ", email=" + email + ", invitador=" + invitador
				+ ", eventos=" + eventos + "]";
	}
	public List<EventoInvitado> getEventoInvitados() {
		return this.eventoInvitados;
	}
	
	public EventoInvitado getEventoInvitado() {
		for (EventoInvitado eventoInvitado : eventoInvitados) {
			if (eventoInvitado!=null && eventoInvitado.getEstatus()!=null){
				return eventoInvitado;
			}
		}
		return new EventoInvitado();
	}

	public void setEventoInvitados(List<EventoInvitado> eventoInvitados) {
		this.eventoInvitados = eventoInvitados;
	}

	public EventoInvitado addEventoInvitado(EventoInvitado eventoInvitado) {
		getEventoInvitados().add(eventoInvitado);
		eventoInvitado.setInvitado(this);

		return eventoInvitado;
	}

	public EventoInvitado removeEventoInvitado(EventoInvitado eventoInvitado) {
		getEventoInvitados().remove(eventoInvitado);
		eventoInvitado.setInvitado(null);

		return eventoInvitado;
	}

	/**
	 * @return the nombreFull
	 */
//	@Transient
	public String getNombreFull() {
		return nombre+" "+apPaterno+" "+apMaterno;
	}

	/**
	 * @param nombreFull the nombreFull to set
	 */
//	@Transient
	public void setNombreFull(String nombreFull) {
		
	}

	public String getIdEventoInvitado() {
		return idEventoInvitado;
	}

	public void setIdEventoInvitado(String idEventoInvitado) {
		this.idEventoInvitado = idEventoInvitado;
	}
}
