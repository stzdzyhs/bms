
package com.db.bms.entity;


public class ResourceAlbumMap {

	private Long id;

	private Integer type;

	private Long resourceId;

	private Long albumId;
	
	private Long createdBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/* use EntityType instead !
	public static enum MapType {
		TOPIC(0), COLUMN(1);

		private final int index;

		private MapType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static MapType getType(int index) {
			switch (index) {
			case 0:
				return TOPIC;
			case 1:
				return COLUMN;
			}
			return TOPIC;
		}
	}
	*/

}
