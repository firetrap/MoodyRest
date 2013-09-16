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
import java.util.ArrayList;

/**
 * 
 * @author Bill Antonia
 */
public class MoodleWebService implements Serializable{
	private Boolean downloadfiles = false;
	private String firstname = null;
	private String fullname = null;
	private ArrayList<Function> functions = null;
	private String lastname = null;
	private String sitename = null;
	private String siteurl = null;
	private Long userid = null;
	private String username = null;
	private String userpictureurl = null;

	/**
   *
   */
	public MoodleWebService() {
	}

	/**
	 * 
	 * @param function
	 */
	public void addFunction(Function function) {
		if (functions == null) {
			functions = new ArrayList<Function>();
		}
		functions.add(function);
	}

	/**
	 * 
	 * @param name
	 * @param version
	 */
	public void addFunction(String name, double version) {
		if (functions == null) {
			functions = new ArrayList<Function>();
		}
		functions.add(new Function(name, version));
	}

	/**
	 * 
	 * @return boolean
	 */
	public Boolean canDownloadFiles() {
		return downloadfiles;
	}

	/**
	 * 
	 * @return String
	 */
	public String getFirstName() {
		return firstname;
	}

	/**
	 * 
	 * @return String
	 */
	public String getFullName() {
		return fullname;
	}

	/**
	 * 
	 * @return ArrayList Function
	 */
	public ArrayList<Function> getFunctions() {
		return functions;
	}

	/**
	 * 
	 * @return String
	 */
	public String getLastName() {
		return lastname;
	}

	/**
	 * 
	 * @return
	 */
	public String getSiteName() {
		return sitename;
	}

	/**
	 * 
	 * @return String
	 */
	public String getSiteURL() {
		return siteurl;
	}

	/**
	 * 
	 * @return long
	 */
	public Long getUserId() {
		return userid;
	}

	/**
	 * 
	 * @return String
	 */
	public String getUserName() {
		return username;
	}

	/**
	 * 
	 * @return String
	 */
	public String getUserPictureURL() {
		return userpictureurl;
	}

	/**
	 * 
	 * @param downloadfiles
	 */
	public void setDownloadFiles(Boolean downloadfiles) {
		this.downloadfiles = downloadfiles;
	}

	/**
	 * 
	 * @param firstname
	 */
	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * 
	 * @param fullname
	 */
	public void setFullName(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * 
	 * @param field
	 * @param value
	 */
	public void setFunctionField(String field, String value) {
		if (field.equals("sitename") && !value.equals("")) {
			setSiteName(value);
		}
		if (field.equals("username") && !value.equals("")) {
			setUserName(value);
		}
		if (field.equals("firstname") && !value.equals("")) {
			setFirstName(value);
		}
		if (field.equals("lastname") && !value.equals("")) {
			setLastName(value);
		}
		if (field.equals("fullname") && !value.equals("")) {
			setFullName(value);
		}
		if (field.equals("userid") && !value.equals("")) {
			setUserId(Long.valueOf(value));
		}
		if (field.equals("siteurl") && !value.equals("")) {
			setSiteURL(value);
		}
		if (field.equals("userpictureurl") && !value.equals("")) {
			setUserPictureURL(value);
		}
		if (field.equals("downloadfiles") && !value.equals("")) {
			setDownloadFiles(value.equals("1") ? true : false);
		}
	}

	/**
	 * 
	 * @param lastname
	 */
	public void setLastName(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * 
	 * @param sitename
	 */
	public void setSiteName(String sitename) {
		this.sitename = sitename;
	}

	/**
	 * 
	 * @param siteurl
	 */
	public void setSiteURL(String siteurl) {
		this.siteurl = siteurl;
	}

	/**
	 * 
	 * @param userid
	 */
	public void setUserId(Long userid) {
		this.userid = userid;
	}

	/**
	 * 
	 * @param username
	 */
	public void setUserName(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @param userpictureurl
	 */
	public void setUserPictureURL(String userpictureurl) {
		this.userpictureurl = userpictureurl;
	}
}
