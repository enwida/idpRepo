/**
 * 
 */
package de.enwida.web.db.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.enwida.web.model.User;
import de.enwida.web.utils.Constants;

/**
 * This entity maintains details about files uploaded
 * 
 * @author Jitin
 * 
 */
@Entity
@Table(name = Constants.UPLOADED_FILE_TABLE_NAME, schema = Constants.UPLOADED_FILE_TABLE_SCHEMA_NAME)
@SequenceGenerator(name = "FILE_SEQ", initialValue = 1, allocationSize = 1, sequenceName = Constants.UPLOADED_FILE_SEQUENCE_NAME, schema = Constants.UPLOADED_FILE_SEQUENCE_SCHEMA_NAME)
public class UploadedFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2646705236947755657L;
	public static final String ID = "ID";
	public static final String DISPLAY_FILE_NAME = "DISPLAY_FILE_NAME";
	public static final String UPLOAD_DATE = "UPLOAD_DATE";
	public static final String MODIFICATION_DATE = "MODIFICATION_DATE";
	public static final String FORMAT = "FORMAT";
	public static final String FILE_PATH = "FILE_PATH";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_SEQ")
	@Column(name = ID)
	private int id;

	@Column(name = DISPLAY_FILE_NAME, unique = false, nullable = false, length = 255)
	private String displayFileName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = User.USER_ID, nullable = false)
	private User uploader;

	@Column(name = UPLOAD_DATE, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;

	@Column(name = MODIFICATION_DATE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationDate;

	@Column(name = FORMAT, length = 10)
	private String format;

	@Column(name = FILE_PATH, length = 256)
	private String filePath;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisplayFileName() {
		return displayFileName;
	}

	public void setDisplayFileName(String displayFileName) {
		this.displayFileName = displayFileName;
	}

	@Transient
	public User getUploader() {
		return uploader;
	}

	public void setUploader(User uploader) {
		this.uploader = uploader;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
