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
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

//ID_EVENTO 	INT  NOT NULL PRIMARY  KEY,
//NOMBRE 	    VARCHAR(100) NOT NULL,
//DESCRIPCION	VARCHAR(100),
//LLAVE_EVENTO	VARCHAR(50),
//LLAVE_SECRETO	VARCHAR(50),
//TOTAL_ASIENTOS INT


@Entity
@Table(name="EVENTO")
public class Evento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3988898104477742767L;

	@Id
	@SequenceGenerator(name="EVENTO_IDEVENTO_GENERATOR", sequenceName="EVENTO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EVENTO_IDEVENTO_GENERATOR")
	@Column(name="ID_EVENTO")
	private Integer id;

	private String nombre;
	private String descripcion;
	private String lugar;
	private Date fecha;

	@Column(name = "SUBJECT_EMAIL")
	private String subjectEmail;
	
	@Column(name = "LLAVE_EVENTO")
	private String llaveEvento;

	@Column(name = "LLAVE_SECRETA")
	private String llaveSecreta;

	@Column(name= "LLAVE_PUBLICA")
	private String llavePublica;

	@Column(name = "TOTAL_ASIENTOS")
	private int totalAsientos;


	@Lob
    @Basic(fetch = FetchType.LAZY)
	@Column(name = "IMAGEN_INVITACION")
	private byte[] imagenInvitacion;

	@Column(name="IMAGEN_QR_X")
	private int imagenQRX;

	@Column(name="IMAGEN_QR_y")
	private int imagenQRY;
	
	//bi-directional many-to-one association to EventoInvitado
	@OneToMany(mappedBy="evento")
	private List<EventoInvitado> eventoInvitados;	

	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="EVENTO_INVITADO",
            joinColumns=
            @JoinColumn(name="ID_EVENTO", referencedColumnName="ID_EVENTO"),
      inverseJoinColumns=
            @JoinColumn(name="ID_INVITADO", referencedColumnName="ID_INVITADO")
    )
    private List<Invitado> invitados;       
	

	public Evento() {
		// TODO Auto-generated constructor stub
	}

	public Evento(Integer id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public Evento(Integer id, String nombre, String descripcion, String llaveEvento, String llaveSecreta,
			int totalAsientos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.llaveEvento = llaveEvento;
		this.llaveSecreta = llaveSecreta;
		this.totalAsientos = totalAsientos;
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

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the llaveEvento
	 */
	public String getLlaveEvento() {
		return llaveEvento;
	}

	/**
	 * @param llaveEvento the llaveEvento to set
	 */
	public void setLlaveEvento(String llaveEvento) {
		this.llaveEvento = llaveEvento;
	}

	/**
	 * @return the llaveSecreta
	 */
	public String getLlaveSecreta() {
		return llaveSecreta;
	}

	/**
	 * @param llaveSecreta the llaveSecreta to set
	 */
	public void setLlaveSecreta(String llaveSecreta) {
		this.llaveSecreta = llaveSecreta;
	}

	/**
	 * @return the totalAsientos
	 */
	public int getTotalAsientos() {
		return totalAsientos;
	}

	/**
	 * @param totalAsientos the totalAsientos to set
	 */
	public void setTotalAsientos(int totalAsientos) {
		this.totalAsientos = totalAsientos;
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
		if (!(obj instanceof Evento)) {
			return false;
		}
		Evento other = (Evento) obj;
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
	 * @return the invitados
	 */
	public List<Invitado> getInvitados() {
		return invitados;
	}

	/**
	 * @param invitados the invitados to set
	 */
	public void setInvitados(List<Invitado> invitados) {
		this.invitados = invitados;
	}

	public EventoInvitado addEventoInvitado(EventoInvitado eventoInvitado) {
		getEventoInvitados().add(eventoInvitado);
		eventoInvitado.setEvento(this);

		return eventoInvitado;
	}

	public EventoInvitado removeEventoInvitado(EventoInvitado eventoInvitado) {
		getEventoInvitados().remove(eventoInvitado);
		eventoInvitado.setEvento(null);

		return eventoInvitado;
	}
	public List<EventoInvitado> getEventoInvitados() {
		return this.eventoInvitados;
	}

	public void setEventoInvitados(List<EventoInvitado> eventoInvitados) {
		this.eventoInvitados = eventoInvitados;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getSubjectEmail() {
		return subjectEmail;
	}

	public void setSubjectEmail(String subjectEmail) {
		this.subjectEmail = subjectEmail;
	}

	public String getLlavePublica() {
		return llavePublica;
	}

	public void setLlavePublica(String llavePublica) {
		this.llavePublica = llavePublica;
	}

	public byte[] getImagenInvitacion() {
		return imagenInvitacion;
	}

	public void setImagenInvitacion(byte[] imagenInvitacion) {
		this.imagenInvitacion = imagenInvitacion;
	}

	public int getImagenQRX() {
		return imagenQRX;
	}

	public void setImagenQRX(int imagenQRX) {
		this.imagenQRX = imagenQRX;
	}

	public int getImagenQRY() {
		return imagenQRY;
	}

	public void setImagenQRY(int imagenQRY) {
		this.imagenQRY = imagenQRY;
	}

	public boolean isHasSeatsMaps() {
		return  this.getLlaveEvento() != null && !this.getLlaveEvento().isEmpty() &&
				this.getLlavePublica() != null && !this.getLlavePublica().isEmpty() &&
				this.getLlaveSecreta() != null && !this.getLlaveSecreta().isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Evento [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion
				+ ", fecha=" + fecha + ", lugar=" + lugar + ", subject_email=" + subjectEmail
				+ ", llaveEvento=" + llaveEvento + ", llaveSecreta=" + llaveSecreta + ", llavePublica=" + llavePublica
				+ ", totalAsientos=" + totalAsientos + ", invitados="
				+ invitados + "]";
	}

}
