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
public class MoodleModAssignWarning implements Serializable{

	private String element = null;
	private Long elementid = null;
	private String message = null;
	private Long messageid = null;

	public MoodleModAssignWarning() {
	}

	public String getElement() {
		return element;
	}

	public Long getElementId() {
		return elementid;
	}

	public String getMessage() {
		return message;
	}

	public Long getMessageId() {
		return messageid;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public void setElementId(Long elementid) {
		this.elementid = elementid;
	}

	public void setField(String field, String value) {
		if (field != null && !field.isEmpty()) {
			if (field.equals("element")) {
				setElement(value);
			}
			if (field.equals("elementid")) {
				setElementId(Long.parseLong(value));
			}
			if (field.equals("messageid")) {
				setMessageId(Long.parseLong(value));
			}
			if (field.equals("message")) {
				setMessage(value);
			}
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageId(Long messageid) {
		this.messageid = messageid;
	}
}
