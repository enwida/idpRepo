/**
 * 
 */
package de.enwida.web.db.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

	@OneToOne(mappedBy = "lineId", targetEntity = UserLinesMetaData.class)
	private UserLinesMetaData lineMetaData;

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
	public String toString() {
		return "UserLines [id=" + id + ", "
				+ (timestamp != null ? "timestamp=" + timestamp + ", " : "")
				+ "value=" + value + ", "
				+ (lineMetaData != null ? "lineMetaData=" + lineMetaData : "")
				+ "]";
	}

}
