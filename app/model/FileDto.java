package model;

import java.io.Serializable;

/**
 * Model class for filedto.
 * 
 * @author deeptiarora
 *
 */
public class FileDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private byte[] content;
	private String contentType;
	private String path;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
