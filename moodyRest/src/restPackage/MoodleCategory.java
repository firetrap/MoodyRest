/*
 *  Copyright (C) 2012 Bill Antonia
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package restPackage;

import java.io.Serializable;

/**
 * 
 * @author Bill Antonia
 */
public class MoodleCategory implements Serializable{

	private Integer coursecount = null;
	private Integer depth = null;
	private String description = null;
	private Long id = null;
	private String idnumber = null;
	private String name = null;

	private Long parent = null;
	private String path = null;
	private Integer sortorder = null;
	private String theme = null;
	private Long timemodified = null;
	private Boolean visible = null;
	private Boolean visibleold = null;

	public MoodleCategory() {
	}

	public MoodleCategory(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public MoodleCategory(String name, Long parent) {
		this.name = name;
		this.parent = parent;
	}

	public MoodleCategory(String name, Long parent, String idnumber,
			String description, String theme) {
		this.name = name;
		this.parent = parent;
		this.idnumber = idnumber;
		this.description = description;
		this.theme = theme;
	}

	public Integer getCourseCount() {
		return coursecount;
	}

	public Integer getDepth() {
		return depth;
	}

	public String getDescription() {
		return description;
	}

	public String getFieldAsString(String field) {
		if (field != null && !field.isEmpty()) {
			if (field.equals("id")) {
				return Long.toString(getId());
			}
			if (field.equals("name")) {
				return getName();
			}
			if (field.equals("parent")) {
				return Long.toString(getParent());
			}
			if (field.equals("idnumber")) {
				return getIdNumber();
			}
			if (field.equals("description")) {
				return getDescription();
			}
			if (field.equals("theme")) {
				return getTheme();
			}
			if (field.equals("sortorder")) {
				return Integer.toString(getSortOrder());
			}
			if (field.equals("coursecount")) {
				return Integer.toString(getCourseCount());
			}
			if (field.equals("visible")) {
				return getVisible() ? "1" : "0";
			}
			if (field.equals("visibleold")) {
				return getVisibleOld() ? "1" : "0";
			}
			if (field.equals("timemodified")) {
				return Long.toString(getTimeModified());
			}
			if (field.equals("depth")) {
				return Integer.toString(getDepth());
			}
			if (field.equals("path")) {
				return getPath();
			}
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public String getIdNumber() {
		return idnumber;
	}

	public String getName() {
		return name;
	}

	public Long getParent() {
		return parent;
	}

	public String getPath() {
		return path;
	}

	public Integer getSortOrder() {
		return sortorder;
	}

	public String getTheme() {
		return theme;
	}

	public Long getTimeModified() {
		return timemodified;
	}

	public Boolean getVisible() {
		return visible;
	}

	public Boolean getVisibleOld() {
		return visibleold;
	}

	public void setCourseCount(Integer coursecount) {
		this.coursecount = coursecount;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setField(String field, String value) {
		if (field != null && !field.isEmpty()) {
			if (field.equals("id")) {
				setId(Long.parseLong(value));
			}
			if (field.equals("name")) {
				setName(value);
			}
			if (field.equals("parent")) {
				setParent(Long.parseLong(value));
			}
			if (field.equals("idnumber")) {
				setIdNumber(value);
			}
			if (field.equals("description")) {
				setDescription(value);
			}
			if (field.equals("theme")) {
				setTheme(value);
			}
			if (field.equals("sortorder")) {
				setSortOrder(Integer.parseInt(value));
			}
			if (field.equals("coursecount")) {
				setCourseCount(Integer.parseInt(value));
			}
			if (field.equals("visible")) {
				setVisible(value.equals("0") ? false : true);
			}
			if (field.equals("visibleold")) {
				setVisibleOld(value.equals("0") ? false : true);
			}
			if (field.equals("timemodified")) {
				setTimeModified(Long.parseLong(value));
			}
			if (field.equals("depth")) {
				setDepth(Integer.parseInt(value));
			}
			if (field.equals("path")) {
				setPath(value);
			}

		}
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdNumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSortOrder(Integer sortorder) {
		this.sortorder = sortorder;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setTimeModified(Long timemodified) {
		this.timemodified = timemodified;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public void setVisibleOld(Boolean visibleold) {
		this.visibleold = visibleold;
	}
}
