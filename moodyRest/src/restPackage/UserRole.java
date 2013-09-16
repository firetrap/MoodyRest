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
public class UserRole implements Serializable{

	private String name = null;
	private Long roleid = null;
	private String shortname = null;
	private Integer sortorder = null;

	/**
   *
   */
	public UserRole() {
	}

	/**
	 * 
	 * @param roleid
	 * @param name
	 * @param shortname
	 * @param sortorder
	 */
	public UserRole(long roleid, String name, String shortname, int sortorder) {
		this.roleid = roleid;
		this.name = name;
		this.shortname = shortname;
		this.sortorder = sortorder;
	}

	/**
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return Long
	 */
	public Long getRoleId() {
		return roleid;
	}

	/**
	 * 
	 * @return String
	 */
	public String getShortName() {
		return shortname;
	}

	/**
	 * 
	 * @return Integer
	 */
	public Integer getSortOrder() {
		return sortorder;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param roleid
	 */
	public void setRoleId(Long roleid) {
		this.roleid = roleid;
	}

	/**
	 * 
	 * @param shortname
	 */
	public void setShortName(String shortname) {
		this.shortname = shortname;
	}

	/**
	 * 
	 * @param sortorder
	 */
	public void setSortOrder(Integer sortorder) {
		this.sortorder = sortorder;
	}

	/**
	 * 
	 * @param nodeName
	 * @param content
	 */
	public void setUserRoleField(String nodeName, String content) {
		if (nodeName.equals("roleid")) {
			setRoleId(Long.valueOf(content));
		}
		if (nodeName.equals("name")) {
			setName(content);
		}
		if (nodeName.equals("shortname")) {
			setShortName(content);
		}
		if (nodeName.equals("sortorder")) {
			setSortOrder(Integer.valueOf(content));
		}
	}

}
