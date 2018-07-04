package com.soccercrawler.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SoccermatchTempId
 */
@Embeddable
public class SoccermatchTempId implements java.io.Serializable {

	private long id;
	private String mode;

	public SoccermatchTempId() {
	}

	@Column(name = "ID", nullable = false, columnDefinition = "BIGINT UNSIGNED")
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "MODE", nullable = false, columnDefinition = "VARCHAR", length = 10)
	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SoccermatchTempId))
			return false;
		SoccermatchTempId castOther = (SoccermatchTempId) other;

		return (this.getId() == castOther.getId())
				&& ((this.getMode() == castOther.getMode()) || (this.getMode() != null && castOther.getMode() != null
						&& this.getMode().equals(castOther.getMode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getId();
		result = 37 * result + (getMode() == null ? 0 : this.getMode().hashCode());
		return result;
	}

}
