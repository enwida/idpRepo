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
// @Entity
// @Table(name = Constants.USER_LINES_METADATA_TABLE_NAME, schema =
// Constants.USER_LINES_METADATA_TABLE_SCHEMA_NAME)
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
	public static final String HEADER1 = "HEADER1";
	public static final String HEADER2 = "HEADER2";
	public static final String ASPECT = "ASPECT";
	
	// @Id
	// @Column(name = LINE_METADATA_ID)
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private int metaDataId;

	// @OneToMany(cascade = CascadeType.ALL, mappedBy = "lineMetaData", fetch =
	// FetchType.LAZY)
	// @ElementCollection(targetClass = UserLines.class)
	// private Set<UserLines> userLines;

	// @OneToOne
	// @JoinColumn(name = FILE_ID, referencedColumnName = UploadedFile.ID)
	// private UploadedFile file;

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

	@Column(name = HEADER1)
	private String header1;

	@Column(name = HEADER2)
	private String header2;

	@Column(name = ASPECT)
	private String aspect;

	// public int getMetaDataId() {
	// return metaDataId;
	// }
	//
	// public void setMetaDataId(int metaDataId) {
	// this.metaDataId = metaDataId;
	// }

	// public Set<UserLines> getUserLines() {
	// return userLines;
	// }
	//
	// public void setUserLines(Set<UserLines> userLines) {
	// this.userLines = userLines;
	// }

	// public UploadedFile getFile() {
	// return file;
	// }
	//
	// public void setFile(UploadedFile file) {
	// this.file = file;
	// }

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

	public String getHeader1() {
		return header1;
	}

	public void setHeader1(String header1) {
		this.header1 = header1;
	}

	public String getHeader2() {
		return header2;
	}

	public void setHeader2(String header2) {
		this.header2 = header2;
	}

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public String getAspectKey() {
		return "de.enwida.chart.aspect." + aspect.toLowerCase() + ".title";
	}

	@Override
	public String toString() {
		return "UserLinesMetaData [metaDataId="
				// + metaDataId
				+ ", "
				// + (userLines != null ? "userLines=" + userLines + ", " : "")
				// + (file != null ? "file=" + file + ", " : "")
				+ (aspect != null ? "aspect=" + aspect : "")
				+ (type != null ? "type=" + type + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (units != null ? "units=" + units + ", " : "")
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (country != null ? "country=" + country + ", " : "")
				+ (interpolation != null ? "interpolation=" + interpolation
						+ ", " : "")
				+ (resolution != null ? "resolution=" + resolution + ", " : "")
				+ (header1 != null ? "header1=" + header1 + ", " : "")
				+ (header2 != null ? "header2=" + header2 + ", " : "") + "]";
	}

}
