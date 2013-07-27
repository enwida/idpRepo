/**
 * 
 */
package de.enwida.web.db.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.enwida.web.utils.Constants;

/**
 * @author Jitin
 *
 */
@Entity
@Table(name = Constants.USER_LINES_TABLE_NAME, schema = Constants.USER_LINES_TABLE_SCHEMA_NAME)
public class UserLines implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4302904063975334378L;
	public static final String ID = "ID";
	public static final String TIMESTAMP = "TIMESTAMP";
	public static final String VALUE = "VALUE";

	@Id
	@Column(name = ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = TIMESTAMP)
	private Calendar timestamp;

	@Column(name = VALUE)
	private double value;

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = UserLinesMetaData.class)
	@JoinTable(name = Constants.USER_LINES_METADATA_MAPPING_TABLE_NAME, schema = Constants.USER_LINES_METADATA_MAPPING_SCHEMA_NAME, joinColumns = { @JoinColumn(name = UserLinesMetaData.LINE_ID, referencedColumnName = UserLines.ID) }, inverseJoinColumns = { @JoinColumn(name = UserLinesMetaData.LINE_METADATA_ID, referencedColumnName = UserLinesMetaData.LINE_METADATA_ID) })
	private UserLinesMetaData lineMetaData;

	/**
	 * 
	 */
	public UserLines() {
	}

	/**
	 * @param timestamp
	 * @param value
	 * @param lineMetaData
	 */
	public UserLines(Calendar timestamp, double value,
			UserLinesMetaData lineMetaData) {
		this.timestamp = timestamp;
		this.value = value;
		this.lineMetaData = lineMetaData;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Transient
	public UserLinesMetaData getLineMetaData() {
		return lineMetaData;
	}

	public void setLineMetaData(UserLinesMetaData lineMetaData) {
		this.lineMetaData = lineMetaData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		UserLines other = (UserLines) obj;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserLines [id=" + id + ", "
				+ (timestamp != null ? "timestamp=" + timestamp + ", " : "")
				+ "value=" + value + ", "
				+ (lineMetaData != null ? "lineMetaData=" + lineMetaData : "")
				+ "]";
	}

}
