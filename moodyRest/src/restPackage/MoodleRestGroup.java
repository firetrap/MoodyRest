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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.NodeList;

/**
 * <p>
 * Class containing the static routines to manipulate Moodle groups and users
 * within course groups.
 * </p>
 * 
 * @author Bill Antonia
 * @see MoodleGroup
 * @see MoodleGroupUser
 */
public class MoodleRestGroup implements Serializable {

	// private static final int BUFFER_MAX=4000;

	/**
	 * <p>
	 * Method to add a number of users memberships to a number of Moodle groups.
	 * </p>
	 * 
	 * @param users
	 *            MoodleGroupUser[]
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static void addMembersToGroups(MoodleGroupUser[] users)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_ADD_GROUPMEMBERS.name()
				: MoodleServices.CORE_GROUP_ADD_GROUP_MEMBERS.name();
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestGroupException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null) {
				throw new MoodleRestGroupException();
			}
			if (users[i].getGroupId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode(
								"members[" + i + "][groupid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getGroupId(),
								MoodleServices.ENCODING.toString()));
			}
			if (users[i].getUserId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("members[" + i + "][userid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getUserId(),
								MoodleServices.ENCODING.toString()));
			}
		}
		data.trimToSize();
		MoodleCallRestWebService.call(data.toString());
	}

	/**
	 * <p>
	 * Method to add a users membership to a Moodle group.
	 * </p>
	 * 
	 * @param user
	 *            MoodleGroupUser
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static void addMemberToGroup(MoodleGroupUser user)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final MoodleGroupUser[] users = new MoodleGroupUser[1];
		users[0] = user;
		addMembersToGroups(users);
	}

	/**
	 * <p>
	 * Method to create a new group in a Moodle course.
	 * </p>
	 * 
	 * @param group
	 *            MoodleGroup
	 * @return group MoodleGroup object
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleGroup createGroup(MoodleGroup group)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final MoodleGroup[] a = new MoodleGroup[1];
		a[0] = group;
		final MoodleGroup[] gps = createGroups(a);
		return gps[0];
	}

	/**
	 * <p>
	 * Method to create new groups in Moodle courses.<br />
	 * Groups to be created do not necessarily need to be within the same
	 * course.
	 * </p>
	 * 
	 * @param group
	 *            MoodleGroup[]
	 * @return group MoodleGroup[] Updated array of MoodleGroup objects. Group
	 *         ids created by Moodle stored in the id attribute of each object.
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleGroup[] createGroups(MoodleGroup[] group)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Hashtable hash = new Hashtable();
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_CREATE_GROUPS.name()
				: MoodleServices.CORE_GROUP_CREATE_GROUPS.name();
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestGroupException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < group.length; i++) {
			if (group[i] == null) {
				throw new MoodleRestGroupException();
			}
			if (group[i].getCourseId() == null) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode(
								"groups[" + i + "][courseid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + group[i].getCourseId(),
								MoodleServices.ENCODING.toString()));
			}
			if (group[i].getName() == null || group[i].getName().equals("")) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("groups[" + i + "][name]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + group[i].getName(),
								MoodleServices.ENCODING.toString()));
			}
			if (group[i].getDescription() == null) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("groups[" + i
								+ "][description]", MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode(
								"" + group[i].getDescription(),
								MoodleServices.ENCODING.toString()));
			}
			if (group[i].getEnrolmentKey() == null) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("groups[" + i
								+ "][enrolmentkey]", MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode(
								"" + group[i].getEnrolmentKey(),
								MoodleServices.ENCODING.toString()));
			}
		}
		data.trimToSize();
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		for (int j = 0; j < elements.getLength(); j += 5) {
			hash.put(elements.item(j + 2).getTextContent(), elements.item(j)
					.getTextContent());
		}
		for (int i = 0; i < group.length; i++) {
			if (hash.containsKey(group[i].getName())) {
				group[i].setId(Long.parseLong((String) hash.get(group[i]
						.getName())));
			} else {
				group[i] = null;
			}
		}
		return group;
	}

	/**
	 * <p>
	 * Method to delete a Moodle group given its id.
	 * </p>
	 * 
	 * @param groupid
	 *            long
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static void deleteGroupById(Long groupid)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Long[] a = new Long[1];
		a[0] = groupid;
		deleteGroupsById(a);
	}

	/**
	 * <p>
	 * Method to delete groups from Moodle given the ids of the groups.
	 * </p>
	 * 
	 * @param groupids
	 *            long[]
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static void deleteGroupsById(Long[] groupids)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_DELETE_GROUPS.name()
				: MoodleServices.CORE_GROUP_DELETE_GROUPS.name();
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestGroupException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < groupids.length; i++) {
			if (groupids[i] < 1) {
				throw new MoodleRestGroupException();
			}
			data.append("&")
					.append(URLEncoder.encode("groupids[" + i + "]",
							MoodleServices.ENCODING.toString())).append("=")
					.append(groupids[i]);
		}
		data.trimToSize();
		MoodleCallRestWebService.call(data.toString());
	}

	/**
	 * <p>
	 * Method to remove a users membership of a Moodle group.
	 * </p>
	 * 
	 * @param user
	 *            MoodleGroupUser
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static void deleteMemberOfGroup(MoodleGroupUser user)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final MoodleGroupUser[] users = new MoodleGroupUser[1];
		users[0] = user;
		deleteMembersOfGroups(users);
	}

	/**
	 * <p>
	 * Method to remove a number of users memberships from Moodle groups.
	 * </p>
	 * 
	 * @param users
	 *            MoodleGroupUser[]
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static void deleteMembersOfGroups(MoodleGroupUser[] users)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_DELETE_GROUPMEMBERS.name()
				: MoodleServices.CORE_GROUP_DELETE_GROUP_MEMBERS.name();
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestGroupException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null) {
				throw new MoodleRestGroupException();
			}
			if (users[i].getGroupId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode(
								"members[" + i + "][groupid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getGroupId(),
								MoodleServices.ENCODING.toString()));
			}
			if (users[i].getUserId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("members[" + i + "][userid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getUserId(),
								MoodleServices.ENCODING.toString()));
			}
		}
		data.trimToSize();
		MoodleCallRestWebService.call(data.toString());
	}

	/**
	 * <p>
	 * Method to retrieve the information about a Moodle group from its id.
	 * </p>
	 * 
	 * @param groupid
	 *            long
	 * @return group MoodleGroup
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleGroup getGroupById(Long groupid)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Long[] a = new Long[1];
		a[0] = groupid;
		final MoodleGroup[] gps = getGroupsById(a);
		return gps[0];
	}

	/**
	 * <p>
	 * Method to fetch the details of a number of Moodle groups from their ids.
	 * </p>
	 * 
	 * @param groupids
	 *            long[]
	 * @return group MoodleGroup[]
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleGroup[] getGroupsById(Long[] groupids)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Vector v = new Vector();
		MoodleGroup group = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_GET_GROUPS.name()
				: MoodleServices.CORE_GROUP_GET_GROUPS.name();
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestGroupException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < groupids.length; i++) {
			if (groupids[i] < 1) {
				throw new MoodleRestGroupException();
			}
			data.append("&")
					.append(URLEncoder.encode("groupids[" + i + "]",
							MoodleServices.ENCODING.toString())).append("=")
					.append(groupids[i]);
		}
		data.trimToSize();
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		group = null;
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (group == null) {
					group = new MoodleGroup(Long.parseLong(content));
				} else {
					v.add(group);
					group = new MoodleGroup(Long.parseLong(content));
				}
			}
			group.setMoodleGroupField(nodeName, content);
		}
		if (group != null) {
			v.add(group);
		}
		final MoodleGroup[] groups = new MoodleGroup[v.size()];
		for (int i = 0; i < v.size(); i++) {
			groups[i] = (MoodleGroup) v.get(i);
		}
		v.removeAllElements();
		return groups;
	}

	/**
	 * <p>
	 * Method to return the details of groups within a Moodle course from the
	 * course id.
	 * </p>
	 * 
	 * @param id
	 *            long
	 * @return groups MoodleGroup[]
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleGroup[] getGroupsFromCourseId(Long id)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Vector v = new Vector();
		MoodleGroup group = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_GET_COURSE_GROUPS.name()
				: MoodleServices.CORE_GROUP_GET_COURSE_GROUPS.name();
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestGroupException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		if (id < 1) {
			throw new MoodleRestGroupException();
		} else {
			data.append("&")
					.append(URLEncoder.encode("courseid",
							MoodleServices.ENCODING.toString())).append("=").append(id);
		}
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (group == null) {
					group = new MoodleGroup(Long.parseLong(content));
				} else {
					v.add(group);
					group = new MoodleGroup(Long.parseLong(content));
				}
			}
			group.setMoodleGroupField(nodeName, content);
		}
		if (group != null) {
			v.add(group);
		}
		final MoodleGroup[] groups = new MoodleGroup[v.size()];
		for (int i = 0; i < v.size(); i++) {
			groups[i] = (MoodleGroup) v.get(i);
		}
		v.removeAllElements();
		return groups;
	}

	/**
	 * <p>
	 * Method to retrieve details of the memberships of a Moodle group.
	 * </p>
	 * 
	 * @param groupid
	 *            Long
	 * @return groupUsers MoodleGroupUser[]
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleGroupUser[] getMembersFromGroupId(Long groupid)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Long[] a = new Long[1];
		a[0] = groupid;
		return getMembersFromGroupIds(a);
	}

	/**
	 * <p>
	 * Method to retrieve details of the memberships of a number of Moodle
	 * groups.
	 * </p>
	 * 
	 * @param groupids
	 *            long[]
	 * @return groupUsers MoodleGroupUser[]
	 * @throws MoodleRestGroupException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleGroupUser[] getMembersFromGroupIds(Long[] groupids)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Vector v = new Vector();
		MoodleGroupUser user = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_GET_GROUPMEMBERS.name()
				: MoodleServices.CORE_GROUP_GET_GROUP_MEMBERS.name();
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestGroupException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < groupids.length; i++) {
			if (groupids[i] < 1) {
				throw new MoodleRestGroupException();
			}
			data.append("&")
					.append(URLEncoder.encode("groupids[" + i + "]",
							MoodleServices.ENCODING.toString())).append("=")
					.append(groupids[i]);
		}
		data.trimToSize();
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		user = null;
		for (int j = 0; j < elements.getLength(); j += 2) {
			final String content1 = elements.item(j).getTextContent();
			final String content2 = elements.item(j + 1).getTextContent();
			user = new MoodleGroupUser(Long.parseLong(content1),
					Long.parseLong(content2));
			v.add(user);
		}
		if (user != null) {
			v.add(user);
		}
		final MoodleGroupUser[] users = new MoodleGroupUser[v.size()];
		for (int i = 0; i < v.size(); i++) {
			users[i] = (MoodleGroupUser) v.get(i);
		}
		v.removeAllElements();
		return users;
	}

	public void __addMembersToGroups(String url, String token,
			MoodleGroupUser[] users) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_ADD_GROUPMEMBERS.name()
				: MoodleServices.CORE_GROUP_ADD_GROUP_MEMBERS.name();
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING.toString()));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null) {
				throw new MoodleRestGroupException();
			}
			if (users[i].getGroupId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode(
								"members[" + i + "][groupid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getGroupId(),
								MoodleServices.ENCODING.toString()));
			}
			if (users[i].getUserId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("members[" + i + "][userid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getUserId(),
								MoodleServices.ENCODING.toString()));
			}
		}
		data.trimToSize();
		new MoodleCallRestWebService().__call(url, data.toString());
	}

	public void __addMemberToGroup(String url, String token,
			MoodleGroupUser user) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final MoodleGroupUser[] users = new MoodleGroupUser[1];
		users[0] = user;
		__addMembersToGroups(url, token, users);
	}

	public MoodleGroup __createGroup(String url, String token, MoodleGroup group)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final MoodleGroup[] a = new MoodleGroup[1];
		a[0] = group;
		final MoodleGroup[] gps = __createGroups(url, token, a);
		return gps[0];
	}

	public MoodleGroup[] __createGroups(String url, String token,
			MoodleGroup[] group) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final Hashtable hash = new Hashtable();
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_CREATE_GROUPS.name()
				: MoodleServices.CORE_GROUP_CREATE_GROUPS.name();
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING.toString()));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < group.length; i++) {
			if (group[i] == null) {
				throw new MoodleRestGroupException();
			}
			if (group[i].getCourseId() == null) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode(
								"groups[" + i + "][courseid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + group[i].getCourseId(),
								MoodleServices.ENCODING.toString()));
			}
			if (group[i].getName() == null || group[i].getName().equals("")) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("groups[" + i + "][name]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + group[i].getName(),
								MoodleServices.ENCODING.toString()));
			}
			if (group[i].getDescription() == null) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("groups[" + i
								+ "][description]", MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode(
								"" + group[i].getDescription(),
								MoodleServices.ENCODING.toString()));
			}
			if (group[i].getEnrolmentKey() == null) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("groups[" + i
								+ "][enrolmentkey]", MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode(
								"" + group[i].getEnrolmentKey(),
								MoodleServices.ENCODING.toString()));
			}
		}
		data.trimToSize();
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		for (int j = 0; j < elements.getLength(); j += 5) {
			hash.put(elements.item(j + 2).getTextContent(), elements.item(j)
					.getTextContent());
		}
		for (int i = 0; i < group.length; i++) {
			if (hash.containsKey(group[i].getName())) {
				group[i].setId(Long.parseLong((String) hash.get(group[i]
						.getName())));
			} else {
				group[i] = null;
			}
		}
		return group;
	}

	public void __deleteGroupById(String url, String token, Long groupid)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Long[] a = new Long[1];
		a[0] = groupid;
		__deleteGroupsById(url, token, a);
	}

	public void __deleteGroupsById(String url, String token, Long[] groupids)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_DELETE_GROUPS.name()
				: MoodleServices.CORE_GROUP_DELETE_GROUPS.name();
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING.toString()));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < groupids.length; i++) {
			if (groupids[i] < 1) {
				throw new MoodleRestGroupException();
			}
			data.append("&")
					.append(URLEncoder.encode("groupids[" + i + "]",
							MoodleServices.ENCODING.toString())).append("=")
					.append(groupids[i]);
		}
		data.trimToSize();
		new MoodleCallRestWebService().__call(url, data.toString());
	}

	public void __deleteMemberOfGroup(String url, String token,
			MoodleGroupUser user) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final MoodleGroupUser[] users = new MoodleGroupUser[1];
		users[0] = user;
		__deleteMembersOfGroups(url, token, users);
	}

	public void __deleteMembersOfGroups(String url, String token,
			MoodleGroupUser[] users) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_DELETE_GROUPMEMBERS.name()
				: MoodleServices.CORE_GROUP_DELETE_GROUP_MEMBERS.name();
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING.toString()));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null) {
				throw new MoodleRestGroupException();
			}
			if (users[i].getGroupId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode(
								"members[" + i + "][groupid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getGroupId(),
								MoodleServices.ENCODING.toString()));
			}
			if (users[i].getUserId() < 1) {
				throw new MoodleRestGroupException();
			} else {
				data.append("&")
						.append(URLEncoder.encode("members[" + i + "][userid]",
								MoodleServices.ENCODING.toString()))
						.append("=")
						.append(URLEncoder.encode("" + users[i].getUserId(),
								MoodleServices.ENCODING.toString()));
			}
		}
		data.trimToSize();
		new MoodleCallRestWebService().__call(url, data.toString());
	}

	public MoodleGroup __getGroupById(String url, String token, long groupid)
			throws MoodleRestGroupException, UnsupportedEncodingException,
			MoodleRestException {
		final Long[] a = new Long[1];
		a[0] = groupid;
		final MoodleGroup[] gps = __getGroupsById(url, token, a);
		return gps[0];
	}

	public MoodleGroup[] __getGroupsById(String url, String token,
			Long[] groupids) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final Vector v = new Vector();
		MoodleGroup group = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_GET_GROUPS.name()
				: MoodleServices.CORE_GROUP_GET_GROUPS.name();
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING.toString()));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < groupids.length; i++) {
			if (groupids[i] < 1) {
				throw new MoodleRestGroupException();
			}
			data.append("&")
					.append(URLEncoder.encode("groupids[" + i + "]",
							MoodleServices.ENCODING.toString())).append("=")
					.append(groupids[i]);
		}
		data.trimToSize();
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		group = null;
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (group == null) {
					group = new MoodleGroup(Long.parseLong(content));
				} else {
					v.add(group);
					group = new MoodleGroup(Long.parseLong(content));
				}
			}
			group.setMoodleGroupField(nodeName, content);
		}
		if (group != null) {
			v.add(group);
		}
		final MoodleGroup[] groups = new MoodleGroup[v.size()];
		for (int i = 0; i < v.size(); i++) {
			groups[i] = (MoodleGroup) v.get(i);
		}
		v.removeAllElements();
		return groups;
	}

	public MoodleGroup[] __getGroupsFromCourseId(String url, String token,
			Long id) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final Vector v = new Vector();
		MoodleGroup group = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_GET_COURSE_GROUPS.name()
				: MoodleServices.CORE_GROUP_GET_COURSE_GROUPS.name();
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING.toString()));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		if (id < 1) {
			throw new MoodleRestGroupException();
		} else {
			data.append("&")
					.append(URLEncoder.encode("courseid",
							MoodleServices.ENCODING.toString())).append("=").append(id);
		}
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (group == null) {
					group = new MoodleGroup(Long.parseLong(content));
				} else {
					v.add(group);
					group = new MoodleGroup(Long.parseLong(content));
				}
			}
			group.setMoodleGroupField(nodeName, content);
		}
		if (group != null) {
			v.add(group);
		}
		final MoodleGroup[] groups = new MoodleGroup[v.size()];
		for (int i = 0; i < v.size(); i++) {
			groups[i] = (MoodleGroup) v.get(i);
		}
		v.removeAllElements();
		return groups;
	}

	public MoodleGroupUser[] __getMembersFromGroupId(String url, String token,
			Long groupid) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final Long[] a = new Long[1];
		a[0] = groupid;
		return __getMembersFromGroupIds(url, token, a);
	}

	public MoodleGroupUser[] __getMembersFromGroupIds(String url, String token,
			Long[] groupids) throws MoodleRestGroupException,
			UnsupportedEncodingException, MoodleRestException {
		final Vector v = new Vector();
		MoodleGroupUser user = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_GROUP_GET_GROUPMEMBERS.name()
				: MoodleServices.CORE_GROUP_GET_GROUP_MEMBERS.name();
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING.toString()));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING.toString()))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING.toString()));
		for (int i = 0; i < groupids.length; i++) {
			if (groupids[i] < 1) {
				throw new MoodleRestGroupException();
			}
			data.append("&")
					.append(URLEncoder.encode("groupids[" + i + "]",
							MoodleServices.ENCODING.toString())).append("=")
					.append(groupids[i]);
		}
		data.trimToSize();
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		user = null;
		for (int j = 0; j < elements.getLength(); j += 2) {
			final String content1 = elements.item(j).getTextContent();
			final String content2 = elements.item(j + 1).getTextContent();
			user = new MoodleGroupUser(Long.parseLong(content1),
					Long.parseLong(content2));
			v.add(user);
		}
		if (user != null) {
			v.add(user);
		}
		final MoodleGroupUser[] users = new MoodleGroupUser[v.size()];
		for (int i = 0; i < v.size(); i++) {
			users[i] = (MoodleGroupUser) v.get(i);
		}
		v.removeAllElements();
		return users;
	}
}
