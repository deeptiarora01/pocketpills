package service;

import java.io.Serializable;
import java.util.Date;

public class ResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileId;
	private String versionId;
	private String url;
	private String fileName;
	private Date expiryDate;
	private Date createdBy;
	private Date createdDate;
	private Date modifiedDate;
	private Date modifiedBy;
	private String message;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Date createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Date modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
