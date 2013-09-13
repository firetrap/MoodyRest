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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.NodeList;

/**
 * <p>
 * Class containing the static routines used to create or update Moodle courses.
 * </p>
 * 
 * @author Bill Antonia
 * @see MoodleCourse
 */
public class MoodleRestCourse {

	// private static final int BUFFER_MAX=4000;

	/**
	 * <p>
	 * From Moodle 2.3
	 * </p>
	 * 
	 * @param categories
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestCourseException
	 * @throws MoodleRestException
	 */
	public static MoodleCategory[] createCategories(MoodleCategory[] categories)
			throws UnsupportedEncodingException, MoodleRestCourseException,
			MoodleRestException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_CREATE_CATEGORIES;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		final Hashtable<String, MoodleCategory> catStore = new Hashtable();
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].getName() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " name");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][name]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getName(),
							MoodleServices.ENCODING));
			if (categories[i].getParent() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " parent");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][parent]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getParent(),
							MoodleServices.ENCODING));
			if (categories[i].getIdNumber() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][idnumber]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getIdNumber(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getDescription() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][description]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getDescription(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getTheme() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][theme]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getTheme(),
								MoodleServices.ENCODING));
			}
			catStore.put(categories[i].getName(), categories[i]);
		}
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		long id = -1;
		for (int i = 0; i < elements.getLength(); i++) {
			String parent = elements.item(i).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(i).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(i).getTextContent();
			final String nodeName = elements.item(i).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE") && nodeName.equals("id")) {
				id = Long.parseLong(content);
			} else {
				if (parent.equals("RESPONSE") && nodeName.equals("name")) {
					if (catStore.containsKey(content)) {
						catStore.get(content).setId(id);
					}
				}
			}
		}
		return categories;
	}

	/**
	 * <p>
	 * From Moodle 2.3
	 * </p>
	 * 
	 * @param category
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestCourseException
	 * @throws MoodleRestException
	 */
	public static MoodleCategory createCategory(MoodleCategory category)
			throws UnsupportedEncodingException, MoodleRestCourseException,
			MoodleRestException {
		final MoodleCategory[] categories = new MoodleCategory[1];
		categories[0] = category;
		final MoodleCategory[] createdCategory = createCategories(categories);
		return createdCategory[0];
	}

	/**
	 * <p>
	 * Method to create a MoodleCourse given the details of the course in a
	 * MoodleCourse object.<br />
	 * This call communicates with the Moodle WebServices.
	 * </p>
	 * 
	 * @param course
	 *            MoodleCourse object. Needs to have shortname,fullname and
	 *            categoryid completed before this call.
	 * @return MoodleCourse object with the id updated to the course id within
	 *         Moodle.
	 * @throws MoodleRestCourseException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleCourse createCourse(MoodleCourse course)
			throws MoodleRestCourseException, UnsupportedEncodingException,
			MoodleRestException {
		final MoodleCourse[] a = new MoodleCourse[1];
		a[0] = course;
		final MoodleCourse[] crs = createCourses(a);
		return crs[0];
	}

	/**
	 * <p>
	 * Method to create a MoodleCourse given the details of the course in a
	 * MoodleCourse object.<br />
	 * This call communicates with the Moodle WebServices.
	 * </p>
	 * 
	 * @param course
	 *            MoodleCourse[]. Array of MoodleCourse each initialised with
	 *            shortname,fullname and categoryid before the call.
	 * @return MoodleCourse[]. Updated array, each MoodleCourse object within
	 *         the array having their id values updated to that in Moodle.
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static MoodleCourse[] createCourses(MoodleCourse[] course)
			throws MoodleRestException, UnsupportedEncodingException {
		final Hashtable hash = new Hashtable();
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_COURSE_CREATE_COURSES
				: MoodleServices.CORE_COURSE_CREATE_COURSES;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < course.length; i++) {
			if (course[i] == null) {
				throw new MoodleRestCourseException();
			}
			if (course[i].getShortname() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " shortname");
			} else {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][shortname]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getShortname(),
								MoodleServices.ENCODING));
			}
			if (course[i].getFullname() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " fullname");
			} else {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][fullname]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getFullname(),
								MoodleServices.ENCODING));
			}
			if (course[i].getCategoryId() == -1) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " categoryid");
			} else {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][categoryid]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + course[i].getCategoryId(),
								MoodleServices.ENCODING));
			}
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][summaryformat]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getSummaryFormat(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][format]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(course[i].getFormat(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][showgrades]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getShowGrades(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][newsitems]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getNewsItems(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode(
							"courses[" + i + "][numsections]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getNumSections(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][maxbytes]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getMaxBytes(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode(
							"courses[" + i + "][showreports]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getShowReports(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][hiddensections]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getHiddenSections(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][groupmode]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getGroupMode(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][groupmodeforce]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getGroupModeForce(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][defaultgroupingid]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getDefaultGroupingId(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][enablecompletion]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + (course[i].getEnableCompletion() ? 1 : 0),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][completionstartonenrol]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							""
									+ (course[i].getCompletionStartOnEnrol() ? 1
											: 0), MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][completionnotify]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + (course[i].getCompletionNotify() ? 1 : 0),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][visible]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(""
							+ (course[i].getVisible() ? 1 : 0),
							MoodleServices.ENCODING));
			if (course[i].getSummary() != null) {
				data.append("&")
						.append(URLEncoder.encode(
								"courses[" + i + "][summary]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getSummary(),
								MoodleServices.ENCODING));
			}
			if (course[i].getIdNumber() != null) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][idnumber]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getIdNumber(),
								MoodleServices.ENCODING));
			}
			if (course[i].getLang() != null) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i + "][lang]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getLang(),
								MoodleServices.ENCODING));
			}
			if (course[i].getForceTheme() != null) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][forcetheme]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getForceTheme(),
								MoodleServices.ENCODING));
			}
			if (course[i].getStartDate() != -1) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][startdate]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + course[i].getStartDate(),
								MoodleServices.ENCODING));
			}
		}
		data.trimToSize();
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		for (int j = 0; j < elements.getLength(); j += 2) {
			hash.put(elements.item(j + 1).getTextContent(), elements.item(j)
					.getTextContent());
		}
		for (int i = 0; i < course.length; i++) {
			if (hash.containsKey(course[i].getShortname())) {
				course[i].setId(Long.parseLong((String) hash.get(course[i]
						.getShortname())));
			} else {
				course[i] = null;
			}
		}
		return course;
	}

	/**
	 * <p>
	 * From Moodle 2.3
	 * </p>
	 * <p>
	 * If the contents are to be moved to a new parent, set the parent attribute
	 * of the MoodleCategory objects to the id of the parent. Otherwise set it
	 * to -1.
	 * </p>
	 * 
	 * @param categories
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static void deleteCategories(MoodleCategory[] categories)
			throws MoodleRestException, UnsupportedEncodingException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_DELETE_CATEGORIES;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].getId() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " id");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][id]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getId(),
							MoodleServices.ENCODING));
			if (categories[i].getParent() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][newparent]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getParent(),
								MoodleServices.ENCODING));
			}
		}
		MoodleCallRestWebService.call(data.toString());
	}

	/**
	 * <p>
	 * From Moodle 2.3
	 * </p>
	 * <p>
	 * If the contents are to be moved to a new parent, set the parent attribute
	 * of the MoodleCategory object to the id of the parent. Otherwise set it to
	 * -1.
	 * </p>
	 * 
	 * @param category
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static void deleteCategory(MoodleCategory category)
			throws MoodleRestException, UnsupportedEncodingException {
		final MoodleCategory[] categories = new MoodleCategory[1];
		categories[0] = category;
		deleteCategories(categories);
	}

	/**
	 * <p>
	 * Method to return an array of MoodleCourse objects of all courses within
	 * the installation.<br />
	 * This call communicates with the Moodle WebServices.
	 * </p>
	 * 
	 * @return MoodleCourse[]
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static MoodleCourse[] getAllCourses() throws MoodleRestException,
			UnsupportedEncodingException {

		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_COURSE_GET_COURSES
				: MoodleServices.CORE_COURSE_GET_COURSES;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		data.append("&").append(
				URLEncoder.encode("options[ids]", MoodleServices.ENCODING));
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());

		MoodleCourse course = null;
		final Vector v = new Vector();
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (course == null) {
					course = new MoodleCourse(Long.parseLong(content));
				} else {
					v.add(course);
					course = new MoodleCourse(Long.parseLong(content));
				}
			}
			course.setMoodleCourseField(nodeName, content);
		}
		if (course != null) {
			v.add(course);
		}
		final MoodleCourse[] courses = new MoodleCourse[v.size()];
		for (int i = 0; i < v.size(); i++) {
			courses[i] = (MoodleCourse) v.get(i);
		}
		v.removeAllElements();
		return courses;
	}

	/**
	 * 
	 * @return MoodleCategory[]
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static MoodleCategory[] getCategories() throws MoodleRestException,
			UnsupportedEncodingException {
		return getCategories(0, true);
	}

	/**
	 * 
	 * @param categoryId
	 * @return MoodleCategory[]
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static MoodleCategory[] getCategories(long categoryId)
			throws MoodleRestException, UnsupportedEncodingException {
		return getCategories(categoryId, true);
	}

	/**
	 * 
	 * @param categoryId
	 * @param subcategories
	 * @return MoodleCategory[]
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static MoodleCategory[] getCategories(long categoryId,
			boolean subcategories) throws MoodleRestException,
			UnsupportedEncodingException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_GET_CATEGORIES;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		if (categoryId < 0) {
			throw new MoodleRestCourseException(
					MoodleRestException.PARAMETER_RANGE + " categoryid");
		}
		data.append("&")
				.append(URLEncoder
						.encode("categoryid", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode("" + categoryId,
						MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder.encode("addsubcategories",
						MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode("" + (subcategories ? 1 : 0),
						MoodleServices.ENCODING));
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		final ArrayList<MoodleCategory> categories = new ArrayList();
		MoodleCategory category = null;
		for (int i = 0; i < elements.getLength(); i++) {
			String parent = elements.item(i).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(i).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(i).getTextContent();
			final String nodeName = elements.item(i).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE") && nodeName.equals("id")) {
				if (category == null) {
					category = new MoodleCategory();
				} else {
					categories.add(category);
					category = new MoodleCategory();
				}
				category.setId(Long.parseLong(content));
			} else {
				category.setField(nodeName, content);
			}
		}
		if (category != null) {
			categories.add(category);
		}
		MoodleCategory[] result = null;
		if (categories.size() > 0) {
			result = new MoodleCategory[categories.size()];
		}
		for (int i = 0; i < categories.size(); i++) {
			result[i] = categories.get(i);
		}
		return result;
	}

	// core_course_get_content
	/**
	 * <p>
	 * Method to get the content of a course given the Moodle id of the course.
	 * </p>
	 * 
	 * @param courseId
	 * @param options
	 * @return MoodleCourseContent[]
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestCourseException
	 * @throws MoodleRestException
	 */
	public static MoodleCourseContent[] getCourseContent(long courseId,
			MoodleCourseContentOption[] options)
			throws UnsupportedEncodingException, MoodleRestCourseException,
			MoodleRestException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_GET_CONTENTS;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder.encode("courseid", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode("" + courseId,
						MoodleServices.ENCODING));
		if (options != null) {
			for (int i = 0; i < options.length; i++) {
				data.append("&")
						.append(URLEncoder.encode("options[" + i + "][name]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode("" + options[i].getName(),
								MoodleServices.ENCODING));
				data.append("&")
						.append(URLEncoder.encode("options[" + i + "][value]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode("" + options[i].getValue(),
								MoodleServices.ENCODING));
			}
		}
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		MoodleCourseContent result = null;
		MoodleModule module = null;
		MoodleModuleContent moduleContent = null;
		final Vector<MoodleCourseContent> v = new Vector();
		for (int i = 0; i < elements.getLength(); i++) {
			String parent = elements.item(i).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(i).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(i).getTextContent();
			final String nodeName = elements.item(i).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE") && nodeName.equals("id")) {
				if (result != null) {
					if (module != null) {
						if (moduleContent != null) {
							module.addContent(moduleContent);
						}
						result.addMoodleModule(module);
					}
					v.add(result);
					module = null;
					moduleContent = null;
				}
				result = new MoodleCourseContent(Long.parseLong(content));
			} else {
				if (parent.equals("RESPONSE")) {
					result.setMoodleCourseContentField(nodeName, content);
				} else {
					if (parent.equals("modules") && nodeName.equals("id")) {
						if (module != null) {
							if (moduleContent != null) {
								module.addContent(moduleContent);
							}
							result.addMoodleModule(module);
						}
						module = new MoodleModule(Long.parseLong(content));
						moduleContent = null;
					} else {
						if (parent.equals("modules")) {
							module.setMoodleModuleField(nodeName, content);
						} else {
							if (parent.equals("contents")
									&& nodeName.equals("type")) {
								if (moduleContent != null) {
									module.addContent(moduleContent);
								}
								moduleContent = new MoodleModuleContent();
								moduleContent.setType(content);
							} else {
								// Contents of module left other than the type
								moduleContent.setMoodleModuleContentField(
										nodeName, content);
							}
						}
					}
				}
			}
		}
		if (result != null) {
			if (module != null) {
				if (moduleContent != null) {
					module.addContent(moduleContent);
				}
				result.addMoodleModule(module);
			}
			v.add(result);
		}
		if (v.isEmpty()) {
			return null;
		}
		final MoodleCourseContent[] results = new MoodleCourseContent[v.size()];
		for (int i = 0; i < v.size(); i++) {
			results[i] = v.get(i);
		}
		return results;
	}

	/**
	 * <p>
	 * Method to return a MoodleCourse object given the id of the course within
	 * Moodle.<br />
	 * This call communicates with the Moodle WebServices.
	 * </p>
	 * 
	 * @param id
	 *            Moodle id of the course to fetch
	 * @return MoodleCourse object
	 * @throws MoodleRestCourseException
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestException
	 */
	public static MoodleCourse getCourseFromId(long id)
			throws MoodleRestCourseException, UnsupportedEncodingException,
			MoodleRestException {
		final long[] a = new long[1];
		a[0] = id;
		final MoodleCourse[] crs = getCoursesById(a);
		return crs[0];
	}

	/**
	 * <p>
	 * Method to return an array of MoodleCourse objects given an array of id's
	 * of the courses within Moodle.<br />
	 * This call communicates with the Moodle WebServices.
	 * </p>
	 * 
	 * @param courseids
	 *            Array of Moodle course ids
	 * @return MoodleCourse[] Array of MoodleCourse objects
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static MoodleCourse[] getCoursesById(long[] courseids)
			throws MoodleRestException, UnsupportedEncodingException {
		final Vector v = new Vector();
		MoodleCourse course = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_COURSE_GET_COURSES
				: MoodleServices.CORE_COURSE_GET_COURSES;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < courseids.length; i++) {
			if (courseids[i] < 1) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " courseid");
			}
			data.append("&")
					.append(URLEncoder.encode("options[ids][" + i + "]",
							MoodleServices.ENCODING)).append("=")
					.append(courseids[i]);
		}
		data.trimToSize();
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		course = null;
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (course == null) {
					course = new MoodleCourse(Long.parseLong(content));
				} else {
					v.add(course);
					course = new MoodleCourse(Long.parseLong(content));
				}
			}
			course.setMoodleCourseField(nodeName, content);
		}

		if (course != null) {
			v.add(course);
		}
		final MoodleCourse[] courses = new MoodleCourse[v.size()];
		for (int i = 0; i < v.size(); i++) {
			courses[i] = (MoodleCourse) v.get(i);
		}
		v.removeAllElements();
		return courses;
	}

	/**
	 * <p>
	 * From Moodle 2.3
	 * </p>
	 * 
	 * @param categories
	 * @throws UnsupportedEncodingException
	 * @throws MoodleRestCourseException
	 * @throws MoodleRestException
	 */
	public static void updateCategories(MoodleCategory[] categories)
			throws UnsupportedEncodingException, MoodleRestCourseException,
			MoodleRestException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_UPDATE_CATEGORIES;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestCourseException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].getId() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " id");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][id]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getId(),
							MoodleServices.ENCODING));
			if (categories[i].getName() != null) {
				data.append("&")
						.append(URLEncoder.encode(
								"categories[" + i + "][name]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode("" + categories[i].getName(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getParent() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][parent]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getParent(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getIdNumber() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][idnumber]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getIdNumber(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getDescription() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][description]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getDescription(),
								MoodleServices.ENCODING));
				// if (categories[i].getTheme()!=null)
				// data.append("&").append(URLEncoder.encode("categories["+i+"][theme]",
				// MoodleServices.ENCODING)).append("=").append(URLEncoder.encode(""+categories[i].getTheme(),
				// MoodleServices.ENCODING));;
			}
		}
		MoodleCallRestWebService.call(data.toString());
	}

	/**
	 * <p>
	 * From Moodle 2.3
	 * </p>
	 * 
	 * @param category
	 * @throws MoodleRestException
	 * @throws UnsupportedEncodingException
	 */
	public static void updateCategory(MoodleCategory category)
			throws MoodleRestException, UnsupportedEncodingException {
		final MoodleCategory[] categories = new MoodleCategory[1];
		categories[0] = category;
		updateCategories(categories);
	}

	/**
	 * <p>
	 * Constructor to create a MoodleRestCourse object.<br />
	 * Not required to be used as all member methods are static and are called
	 * via the class name.
	 * </p>
	 */
	public MoodleRestCourse() {
	}

	public MoodleCategory[] __createCategories(String url, String token,
			MoodleCategory[] categories) throws UnsupportedEncodingException,
			MoodleRestCourseException, MoodleRestException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_CREATE_CATEGORIES;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		final Hashtable<String, MoodleCategory> catStore = new Hashtable();
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].getName() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " name");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][name]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getName(),
							MoodleServices.ENCODING));
			if (categories[i].getParent() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " parent");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][parent]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getParent(),
							MoodleServices.ENCODING));
			if (categories[i].getIdNumber() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][idnumber]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getIdNumber(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getDescription() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][description]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getDescription(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getTheme() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][theme]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getTheme(),
								MoodleServices.ENCODING));
			}
			catStore.put(categories[i].getName(), categories[i]);
		}
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		long id = -1;
		for (int i = 0; i < elements.getLength(); i++) {
			String parent = elements.item(i).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(i).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(i).getTextContent();
			final String nodeName = elements.item(i).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE") && nodeName.equals("id")) {
				id = Long.parseLong(content);
			} else {
				if (parent.equals("RESPONSE") && nodeName.equals("name")) {
					if (catStore.containsKey(content)) {
						catStore.get(content).setId(id);
					}
				}
			}
		}
		return categories;
	}

	public MoodleCategory __createCategory(String url, String token,
			MoodleCategory category) throws UnsupportedEncodingException,
			MoodleRestCourseException, MoodleRestException {
		final MoodleCategory[] categories = new MoodleCategory[1];
		categories[0] = category;
		final MoodleCategory[] createdCategory = __createCategories(url, token,
				categories);
		return createdCategory[0];
	}

	public MoodleCourse __createCourse(String url, String token,
			MoodleCourse course) throws MoodleRestCourseException,
			UnsupportedEncodingException, MoodleRestException {
		final MoodleCourse[] a = new MoodleCourse[1];
		a[0] = course;
		final MoodleCourse[] crs = __createCourses(url, token, a);
		return crs[0];
	}

	public MoodleCourse[] __createCourses(String url, String token,
			MoodleCourse[] course) throws MoodleRestException,
			UnsupportedEncodingException {
		final Hashtable hash = new Hashtable();
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_COURSE_CREATE_COURSES
				: MoodleServices.CORE_COURSE_CREATE_COURSES;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < course.length; i++) {
			if (course[i] == null) {
				throw new MoodleRestCourseException();
			}
			if (course[i].getShortname() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " shortname");
			} else {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][shortname]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getShortname(),
								MoodleServices.ENCODING));
			}
			if (course[i].getFullname() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " fullname");
			} else {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][fullname]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getFullname(),
								MoodleServices.ENCODING));
			}
			if (course[i].getCategoryId() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " categoryid");
			} else {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][categoryid]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + course[i].getCategoryId(),
								MoodleServices.ENCODING));
			}
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][summaryformat]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getSummaryFormat(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][format]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(course[i].getFormat(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][showgrades]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getShowGrades(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][newsitems]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getNewsItems(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode(
							"courses[" + i + "][numsections]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getNumSections(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][maxbytes]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getMaxBytes(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode(
							"courses[" + i + "][showreports]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getShowReports(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][hiddensections]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getHiddenSections(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][groupmode]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + course[i].getGroupMode(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][groupmodeforce]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getGroupModeForce(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][defaultgroupingid]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + course[i].getDefaultGroupingId(),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][enablecompletion]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + (course[i].getEnableCompletion() ? 1 : 0),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][completionstartonenrol]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							""
									+ (course[i].getCompletionStartOnEnrol() ? 1
											: 0), MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i
							+ "][completionnotify]", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(
							"" + (course[i].getCompletionNotify() ? 1 : 0),
							MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("courses[" + i + "][visible]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(""
							+ (course[i].getVisible() ? 1 : 0),
							MoodleServices.ENCODING));
			if (course[i].getSummary() != null) {
				data.append("&")
						.append(URLEncoder.encode(
								"courses[" + i + "][summary]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getSummary(),
								MoodleServices.ENCODING));
			}
			if (course[i].getIdNumber() != null) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][idnumber]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getIdNumber(),
								MoodleServices.ENCODING));
			}
			if (course[i].getLang() != null) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i + "][lang]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getLang(),
								MoodleServices.ENCODING));
			}
			if (course[i].getForceTheme() != null) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][forcetheme]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(course[i].getForceTheme(),
								MoodleServices.ENCODING));
			}
			if (course[i].getStartDate() != null) {
				data.append("&")
						.append(URLEncoder.encode("courses[" + i
								+ "][startdate]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + course[i].getStartDate(),
								MoodleServices.ENCODING));
			}
		}
		data.trimToSize();
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		for (int j = 0; j < elements.getLength(); j += 2) {
			hash.put(elements.item(j + 1).getTextContent(), elements.item(j)
					.getTextContent());
		}
		for (int i = 0; i < course.length; i++) {
			if (hash.containsKey(course[i].getShortname())) {
				course[i].setId(Long.parseLong((String) hash.get(course[i]
						.getShortname())));
			} else {
				course[i] = null;
			}
		}
		return course;
	}

	public void __deleteCategories(String url, String token,
			MoodleCategory[] categories) throws MoodleRestException,
			UnsupportedEncodingException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_DELETE_CATEGORIES;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].getId() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " id");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][id]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getId(),
							MoodleServices.ENCODING));
			if (categories[i].getParent() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][newparent]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getParent(),
								MoodleServices.ENCODING));
			}
		}
		new MoodleCallRestWebService().__call(url, data.toString());
	}

	public void __deleteCategory(String url, String token,
			MoodleCategory category) throws MoodleRestException,
			UnsupportedEncodingException {
		final MoodleCategory[] categories = new MoodleCategory[1];
		categories[0] = category;
		__deleteCategories(url, token, categories);
	}

	public MoodleCourse[] __getAllCourses(String url, String token)
			throws MoodleRestException, UnsupportedEncodingException {
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_COURSE_GET_COURSES
				: MoodleServices.CORE_COURSE_GET_COURSES;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		data.append("&").append(
				URLEncoder.encode("options[ids]", MoodleServices.ENCODING));
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		MoodleCourse course = null;
		final Vector v = new Vector();
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (course == null) {
					course = new MoodleCourse(Long.parseLong(content));
				} else {
					v.add(course);
					course = new MoodleCourse(Long.parseLong(content));
				}
			}
			course.setMoodleCourseField(nodeName, content);
		}
		if (course != null) {
			v.add(course);
		}
		final MoodleCourse[] courses = new MoodleCourse[v.size()];
		for (int i = 0; i < v.size(); i++) {
			courses[i] = (MoodleCourse) v.get(i);
		}
		v.removeAllElements();
		return courses;
	}

	public MoodleCategory[] __getCategories(String url, String token)
			throws MoodleRestException, UnsupportedEncodingException {
		return __getCategories(url, token, 0L, true);
	}

	public MoodleCategory[] __getCategories(String url, String token,
			Long categoryId) throws MoodleRestException,
			UnsupportedEncodingException {
		return __getCategories(url, token, categoryId, true);
	}

	public MoodleCategory[] __getCategories(String url, String token,
			Long categoryId, Boolean subcategories) throws MoodleRestException,
			UnsupportedEncodingException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_GET_CATEGORIES;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		if (categoryId < 0) {
			throw new MoodleRestCourseException(
					MoodleRestException.PARAMETER_RANGE + " categoryid");
		}
		data.append("&")
				.append(URLEncoder
						.encode("categoryid", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode("" + categoryId,
						MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder.encode("addsubcategories",
						MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode("" + (subcategories ? 1 : 0),
						MoodleServices.ENCODING));
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		final ArrayList<MoodleCategory> categories = new ArrayList();
		MoodleCategory category = null;
		for (int i = 0; i < elements.getLength(); i++) {
			String parent = elements.item(i).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(i).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(i).getTextContent();
			final String nodeName = elements.item(i).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE") && nodeName.equals("id")) {
				if (category == null) {
					category = new MoodleCategory();
				} else {
					categories.add(category);
					category = new MoodleCategory();
				}
				category.setId(Long.parseLong(content));
			} else {
				category.setField(nodeName, content);
			}
		}
		if (category != null) {
			categories.add(category);
		}
		MoodleCategory[] result = null;
		if (categories.size() > 0) {
			result = new MoodleCategory[categories.size()];
		}
		for (int i = 0; i < categories.size(); i++) {
			result[i] = categories.get(i);
		}
		return result;
	}

	public MoodleCourseContent[] __getCourseContent(String url, String token,
			long courseId, MoodleCourseContentOption[] options)
			throws UnsupportedEncodingException, MoodleRestCourseException,
			MoodleRestException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_GET_CONTENTS;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder.encode("courseid", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode("" + courseId,
						MoodleServices.ENCODING));
		if (options != null) {
			for (int i = 0; i < options.length; i++) {
				data.append("&")
						.append(URLEncoder.encode("options[" + i + "][name]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode("" + options[i].getName(),
								MoodleServices.ENCODING));
				data.append("&")
						.append(URLEncoder.encode("options[" + i + "][value]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode("" + options[i].getValue(),
								MoodleServices.ENCODING));
			}
		}
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		MoodleCourseContent result = null;
		MoodleModule module = null;
		MoodleModuleContent moduleContent = null;
		final Vector<MoodleCourseContent> v = new Vector();
		for (int i = 0; i < elements.getLength(); i++) {
			String parent = elements.item(i).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(i).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(i).getTextContent();
			final String nodeName = elements.item(i).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE") && nodeName.equals("id")) {
				if (result != null) {
					if (module != null) {
						if (moduleContent != null) {
							module.addContent(moduleContent);
						}
						result.addMoodleModule(module);
					}
					v.add(result);
					module = null;
					moduleContent = null;
				}
				result = new MoodleCourseContent(Long.parseLong(content));
			} else {
				if (parent.equals("RESPONSE")) {
					result.setMoodleCourseContentField(nodeName, content);
				} else {
					if (parent.equals("modules") && nodeName.equals("id")) {
						if (module != null) {
							if (moduleContent != null) {
								module.addContent(moduleContent);
							}
							result.addMoodleModule(module);
						}
						module = new MoodleModule(Long.parseLong(content));
						moduleContent = null;
					} else {
						if (parent.equals("modules")) {
							module.setMoodleModuleField(nodeName, content);
						} else {
							if (parent.equals("contents")
									&& nodeName.equals("type")) {
								if (moduleContent != null) {
									module.addContent(moduleContent);
								}
								moduleContent = new MoodleModuleContent();
								moduleContent.setType(content);
							} else {
								// Contents of module left other than the type
								moduleContent.setMoodleModuleContentField(
										nodeName, content);
							}
						}
					}
				}
			}
		}
		if (result != null) {
			if (module != null) {
				if (moduleContent != null) {
					module.addContent(moduleContent);
				}
				result.addMoodleModule(module);
			}
			v.add(result);
		}
		if (v.isEmpty()) {
			return null;
		}
		final MoodleCourseContent[] results = new MoodleCourseContent[v.size()];
		for (int i = 0; i < v.size(); i++) {
			results[i] = v.get(i);
		}
		return results;
	}

	public MoodleCourse __getCourseFromId(String url, String token, long id)
			throws MoodleRestCourseException, UnsupportedEncodingException,
			MoodleRestException {
		final long[] a = new long[1];
		a[0] = id;
		final MoodleCourse[] crs = __getCoursesById(url, token, a);
		return crs[0];
	}

	public MoodleCourse[] __getCoursesById(String url, String token,
			long[] courseids) throws MoodleRestException,
			UnsupportedEncodingException {
		final Vector v = new Vector();
		MoodleCourse course = null;
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_COURSE_GET_COURSES
				: MoodleServices.CORE_COURSE_GET_COURSES;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < courseids.length; i++) {
			if (courseids[i] < 1) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " courseid");
			}
			data.append("&")
					.append(URLEncoder.encode("options[ids][" + i + "]",
							MoodleServices.ENCODING)).append("=")
					.append(courseids[i]);
		}
		data.trimToSize();
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		course = null;
		for (int j = 0; j < elements.getLength(); j++) {
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (nodeName.equals("id")) {
				if (course == null) {
					course = new MoodleCourse(Long.parseLong(content));
				} else {
					v.add(course);
					course = new MoodleCourse(Long.parseLong(content));
				}
			}
			course.setMoodleCourseField(nodeName, content);
		}

		if (course != null) {
			v.add(course);
		}
		final MoodleCourse[] courses = new MoodleCourse[v.size()];
		for (int i = 0; i < v.size(); i++) {
			courses[i] = (MoodleCourse) v.get(i);
		}
		v.removeAllElements();
		return courses;
	}

	public void __updateCategories(String url, String token,
			MoodleCategory[] categories) throws UnsupportedEncodingException,
			MoodleRestCourseException, MoodleRestException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.CORE_COURSE_UPDATE_CATEGORIES;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].getId() == null) {
				throw new MoodleRestCourseException(
						MoodleRestException.REQUIRED_PARAMETER + " id");
			}
			data.append("&")
					.append(URLEncoder.encode("categories[" + i + "][id]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + categories[i].getId(),
							MoodleServices.ENCODING));
			if (categories[i].getName() != null) {
				data.append("&")
						.append(URLEncoder.encode(
								"categories[" + i + "][name]",
								MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode("" + categories[i].getName(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getParent() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][parent]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getParent(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getIdNumber() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][idnumber]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getIdNumber(),
								MoodleServices.ENCODING));
			}
			if (categories[i].getDescription() != null) {
				data.append("&")
						.append(URLEncoder.encode("categories[" + i
								+ "][description]", MoodleServices.ENCODING))
						.append("=")
						.append(URLEncoder.encode(
								"" + categories[i].getDescription(),
								MoodleServices.ENCODING));
				// if (categories[i].getTheme()!=null)
				// data.append("&").append(URLEncoder.encode("categories["+i+"][theme]",
				// MoodleServices.ENCODING)).append("=").append(URLEncoder.encode(""+categories[i].getTheme(),
				// MoodleServices.ENCODING));;
			}
		}
		new MoodleCallRestWebService().__call(url, data.toString());
	}

	public void __updateCategory(String url, String token,
			MoodleCategory category) throws MoodleRestException,
			UnsupportedEncodingException {
		final MoodleCategory[] categories = new MoodleCategory[1];
		categories[0] = category;
		__updateCategories(url, token, categories);
	}
}
