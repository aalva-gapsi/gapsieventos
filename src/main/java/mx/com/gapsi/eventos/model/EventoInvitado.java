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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

//ID_EVENTO 	  INT  NOT NULL,
//ID_INVITADO  	  INT  NOT NULL,
//NO_BOLETOS      INT,
//ASIENTOS_ASIGNADOS        VARCHAR(50),
//INVITACION_ENVIADA INT

@Entity
@Table(name="EVENTO_INVITADO")
public class EventoInvitado implements Serializable {
	private static final long serialVersionUID = 7691874129334755422L;
	
	public static final String ESTATUS_LIBRE = "LIBRE";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENTO_INVITADO_SEQ")
	@SequenceGenerator(name = "EVENTO_INVITADO_SEQ", sequenceName = "EVENTO_INVITADO_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Integer id;

	@ManyToOne(optional = false, cascade=CascadeType.ALL)
	@JoinColumn(name = "ID_EVENTO")
	private Evento evento;

	@ManyToOne(optional = false, cascade=CascadeType.ALL)
	@JoinColumn(name = "ID_INVITADO")
	private Invitado invitado;

	@Column(name = "NO_BOLETOS")
	private Integer noBoletos;

	@Column(name = "ASIENTOS_ASIGNADOS")
	private String asientosAsignados;

	@Column(name = "INVITACION_ENVIADA")
	private Boolean invitacionEnviada;

	@Column(name = "ESTATUS")
	private String estatus;

	@Column(name = "ASISTIRA")
	private Boolean asistira;

	@Column(name = "ID_MD5")
	private String idMD5;
	
	public EventoInvitado() {
		this.estatus = ESTATUS_LIBRE;
	}

	public EventoInvitado(Integer id, Evento evento, Invitado invitado, Integer noBoletos, String asientosAsignados,
			Boolean invitacionEnviada) {
		super();
		this.id = id;
		this.evento = evento;
		this.invitado = invitado;
		this.noBoletos = noBoletos;
		this.asientosAsignados = asientosAsignados;
		this.invitacionEnviada = invitacionEnviada;
	}

	public EventoInvitado(Evento evento, Invitado invitado) {
		this.evento = evento;
		this.invitado = invitado;
		this.noBoletos = 1;
		this.invitacionEnviada = false;
	}

	public EventoInvitado(Integer id) {
		this.id = id;
	}

	/**
	 * @return the noBoletos
	 */
	public Integer getNoBoletos() {
		return noBoletos;
	}


	/**
	 * @param noBoletos the noBoletos to set
	 */
	public void setNoBoletos(Integer noBoletos) {
		this.noBoletos = noBoletos;
	}


	/**
	 * @return the asientosAsignados
	 */
	public String getAsientosAsignados() {
		return asientosAsignados;
	}


	/**
	 * @param asientosAsignados the asientosAsignados to set
	 */
	public void setAsientosAsignados(String asientosAsignados) {
		this.asientosAsignados = asientosAsignados;
	}


	/**
	 * @return the invitacionEnviada
	 */
	public Boolean getInvitacionEnviada() {
		return invitacionEnviada;
	}


	/**
	 * @param invitacionEnviada the invitacionEnviada to set
	 */
	public void setInvitacionEnviada(Boolean invitacionEnviada) {
		this.invitacionEnviada = invitacionEnviada;
	
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}

	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	/**
	 * @return the invitado
	 */
	public Invitado getInvitado() {
		return invitado;
	}

	/**
	 * @param invitado the invitado to set
	 */
	public void setInvitado(Invitado invitado) {
		this.invitado = invitado;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventoInvitado [id=" + id + ", evento=" + evento + ", invitado=" + invitado + ", noBoletos=" + noBoletos
				+ ", asientosAsignados=" + asientosAsignados + ", invitacionEnviada=" + invitacionEnviada + "]";
	}

	/**
	 * @return the estatus
	 */
	public String getEstatus() {
		return estatus;
	}

	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Boolean getAsistira() {
		return asistira;
	}

	public void setAsistira(Boolean asistira) {
		this.asistira = asistira;
	}

	public String getIdMD5() {
		return idMD5;
	}

	public void setIdMD5(String idMD5) {
		this.idMD5 = idMD5;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * (result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}
	private int hashCode(int i) {
		final int prime = 31;
		int result = 1;
		result =  ( i-result);
		result =  ( result)/prime;
		return result;
	}
	
	public int revertHashCode(){
		return hashCode(id);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventoInvitado other = (EventoInvitado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
