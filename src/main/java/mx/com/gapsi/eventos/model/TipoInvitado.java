/**
 *
 * Copyright (c) 2016 Liverpool SA de CV. Todos los derechos reservados.
 *
 * Este software contiene información confidencial propiedad de Liverpool 
 * S.A. de C.V. Por lo cual no puede ser reproducido, distribuido o 
 * alterado sin el consentimiento previo de Liverpool S.A. de C.V. 
 */
package mx.com.gapsi.eventos.model;
//
//ID_TIPO_INVITADO  INTEGER NOT NULL PRIMARY KEY,
//NOMBRE 	VARCHAR(100)

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TIPO_INVITADO")
public class TipoInvitado implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7867533536755325088L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_TIPO_INVITADO")
	private Integer id;
	private String nombre;
	
	//bi-directional many-to-one association to Invitado
	@OneToMany(mappedBy="tipoInvitado")
	private List<Invitado> invitados;
	
	
	public TipoInvitado() {
		// TODO Auto-generated constructor stub
	}
	
	
	public TipoInvitado(Integer id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	/* (non-Javadoc)
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
		if (!(obj instanceof TipoInvitado)) {
			return false;
		}
		TipoInvitado other = (TipoInvitado) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TipoInvitado [id=" + id + ", nombre=" + nombre + "]";
	}
	
	

	public List<Invitado> getInvitados() {
		return this.invitados;
	}

	public void setInvitados(List<Invitado> invitados) {
		this.invitados = invitados;
	}

	public Invitado addInvitado(Invitado invitado) {
		getInvitados().add(invitado);
		invitado.setTipoInvitado(this);

		return invitado;
	}

	public Invitado removeInvitado(Invitado invitado) {
		getInvitados().remove(invitado);
		invitado.setTipoInvitado(null);

		return invitado;
	}

}
