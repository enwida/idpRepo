/**
 * 
 */
package de.enwida.web.db.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.enwida.web.utils.Constants;

/**
 * @author Jitin
 *
 */
@Entity
@Table(name = Constants.USER_LINES_METADATA_TABLE_NAME, schema = Constants.USER_LINES_METADATA_TABLE_SCHEMA_NAME)
public class UserLinesMetaData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4956441651030174844L;
	public static final String LINE_METADATA_ID = "LINE_METADATA_ID";
	public static final String LINE_ID = "LINE_ID";
	public static final String TYPE = "TYPE";
	public static final String NAME = "NAME";
	public static final String UNITS = "UNITS";
	public static final String COMMENTS = "COMMENTS";
	public static final String COUNTRY = "COUNTRY";
	public static final String INTERPOLATION_STYLE = "INTERPOLATION_STYLE";
	public static final String FILE_ID = "FILE_ID";
	public static final String RESOLUTION = "RESOLUTION";
	
	@Id
	@Column(name = LINE_METADATA_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int metaDataId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lineMetaData", fetch = FetchType.EAGER)
	@ElementCollection(targetClass = UserLines.class)
	private Set<UserLines> userLines;

	@OneToOne
	@JoinColumn(name = FILE_ID, referencedColumnName = UploadedFile.ID)
	private UploadedFile file;

	@Column(name = TYPE)
	private String type;

	@Column(name = NAME)
	private String name;

	@Column(name = UNITS)
	private String units;

	@Column(name = COMMENTS)
	private String comments;

	@Column(name = COUNTRY)
	private String country;

	@Column(name = INTERPOLATION_STYLE)
	private String interpolation;

	@Column(name = RESOLUTION)
	private String resolution;

	public int getMetaDataId() {
		return metaDataId;
	}

	public void setMetaDataId(int metaDataId) {
		this.metaDataId = metaDataId;
	}

	public Set<UserLines> getUserLines() {
		return userLines;
	}

	public void setUserLines(Set<UserLines> userLines) {
		this.userLines = userLines;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(String interpolation) {
		this.interpolation = interpolation;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	@Override
	public String toString() {
		return "UserLinesMetaData [metaDataId="
				+ metaDataId
				+ ", "
				+ (userLines != null ? "userLines=" + userLines + ", " : "")
				+ (file != null ? "file=" + file + ", " : "")
				+ (type != null ? "type=" + type + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (units != null ? "units=" + units + ", " : "")
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (country != null ? "country=" + country + ", " : "")
				+ (interpolation != null ? "interpolation=" + interpolation
						+ ", " : "")
				+ (resolution != null ? "resolution=" + resolution : "") + "]";
	}

}
