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
 * @author root
 */
public class MoodleModAssignSubmissionParam {

	private ArrayList<MoodleModAssignAssignment> assignmentids = null;
	private Long before = 0L;
	private Long since = 0L;
	private String status = "";

	public MoodleModAssignSubmissionParam() {
	}

	public MoodleModAssignSubmissionParam(MoodleModAssignAssignment assignment) {
		assignmentids = new ArrayList();
		assignmentids.add(assignment);
	}

	public void addAssignmentIds(MoodleModAssignAssignment assignment) {
		if (assignmentids == null) {
			assignmentids = new ArrayList();
		}
		assignmentids.add(assignment);
	}

	public MoodleModAssignAssignment[] getAssignmentIds() {
		return (MoodleModAssignAssignment[]) assignmentids.toArray();
	}

	public Long getBefore() {
		return before;
	}

	public Long getSince() {
		return since;
	}

	public String getStatus() {
		return status;
	}

	public void setBefore(Long before) {
		this.before = before;
	}

	public void setField(String field, String value) {
		if (field != null && !field.isEmpty()) {
			if (field.equals("status")) {
				setStatus(value);
			}
			if (field.equals("since")) {
				setSince(Long.parseLong(value));
			}
			if (field.equals("before")) {
				setBefore(Long.parseLong(value));
			}
		}
	}

	public void setSince(Long since) {
		this.since = since;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
