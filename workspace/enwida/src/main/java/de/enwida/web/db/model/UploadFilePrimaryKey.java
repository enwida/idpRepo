/**
 * 
 */
package de.enwida.web.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * @author Jitin
 *
 */
@Embeddable
public class UploadFilePrimaryKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7279979895700388444L;
	public static final String ID = "ID";
	public static final String REVISION = "REVISION";

	@Column(name = ID)
	private long id;

	@Column(name = REVISION)
	private int revision;

	/**
	 * 
	 */
	public UploadFilePrimaryKey() {
	}

	/**
	 * @param id
	 * @param revision
	 */
	public UploadFilePrimaryKey(long id, int revision) {
		this.id = id;
		this.revision = revision;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + revision;
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
		UploadFilePrimaryKey other = (UploadFilePrimaryKey) obj;
		if (id != other.id)
			return false;
		if (revision != other.revision)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UploadFilePrimaryKey [id=" + id + ", revision=" + revision
				+ "]";
	}

}
