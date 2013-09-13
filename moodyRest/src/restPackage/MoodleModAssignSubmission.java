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

import java.util.ArrayList;

/**
 * 
 * @author Bill Antonia
 */
public class MoodleModAssignSubmission {

	private ArrayList<MoodleModAssignFile> files = null;
	private Long id = null;
	private ArrayList<MoodleModAssignOnlineText> onlinetexts = null;
	private String status = null;
	private Long timecreated = null;
	private Long timemodified = null;
	private Long userid = null;

	public MoodleModAssignSubmission() {
	}

	public MoodleModAssignSubmission(Long id) {
		this.id = id;
	}

	public MoodleModAssignSubmission(Long id, Long userid) {
		this.id = id;
		this.userid = userid;
	}

	public void addFile(MoodleModAssignFile file) {
		if (files == null) {
			files = new ArrayList();
		}
		files.add(file);
	}

	public void addOnlineText(MoodleModAssignOnlineText onlinetext) {
		if (onlinetexts == null) {
			onlinetexts = new ArrayList();
		}
		onlinetexts.add(onlinetext);
	}

	public MoodleModAssignFile[] getFiles() {
		return (MoodleModAssignFile[]) (files == null ? null : files.toArray());
	}

	public Long getId() {
		return id;
	}

	public MoodleModAssignOnlineText[] getOnlineTexts() {
		return (MoodleModAssignOnlineText[]) (onlinetexts == null ? null
				: onlinetexts.toArray());
	}

	public String getStatus() {
		return status;
	}

	public Long getTimeCreated() {
		return timecreated;
	}

	public Long getTimeModified() {
		return timemodified;
	}

	public Long getUserId() {
		return userid;
	}

	public void setField(String field, String value) {
		if (field != null && !field.isEmpty()) {
			if (field.equals("id")) {
				setId(Long.parseLong(value));
			}
			if (field.equals("userid")) {
				setUserId(Long.parseLong(value));
			}
			if (field.equals("timecreated")) {
				setTimeCreated(Long.parseLong(value));
			}
			if (field.equals("timemodified")) {
				setTimeModified(Long.parseLong(value));
			}
			if (field.equals("status")) {
				setStatus(value);
			}
		}
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTimeCreated(Long timecreated) {
		this.timecreated = timecreated;
	}

	public void setTimeModified(Long timemodified) {
		this.timemodified = timemodified;
	}

	public void setUserId(Long userid) {
		this.userid = userid;
	}
}
