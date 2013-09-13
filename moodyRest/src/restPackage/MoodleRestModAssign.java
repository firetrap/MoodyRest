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

import org.w3c.dom.NodeList;

/**
 * 
 * @author root
 */
public class MoodleRestModAssign {

	public static MoodleModAssignSubmissionReturn getSubmissions(
			MoodleModAssignSubmissionParam params) throws MoodleRestException,
			UnsupportedEncodingException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.MOD_ASSIGN_GET_SUBMISSIONS;
		if (MoodleCallRestWebService.getAuth() == null) {
			throw new MoodleRestModAssignException();
		} else {
			data.append(MoodleCallRestWebService.getAuth());
		}
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		if (params.getAssignmentIds() == null
				|| params.getAssignmentIds().length == 0) {
			throw new MoodleRestModAssignException(
					MoodleRestException.REQUIRED_PARAMETER);
		}
		final MoodleModAssignAssignment[] assignmentIds = params
				.getAssignmentIds();
		for (int i = 0; i < assignmentIds.length; i++) {
			data.append("&")
					.append(URLEncoder.encode("assignmentids[" + i + "]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + assignmentIds[i],
							MoodleServices.ENCODING));
		}
		if (!params.getStatus().isEmpty()) {
			data.append("&")
					.append(URLEncoder
							.encode("status", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(params.getStatus(),
							MoodleServices.ENCODING));
		}
		if (!(params.getSince() == 0)) {
			data.append("&")
					.append(URLEncoder.encode("since", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + params.getSince(),
							MoodleServices.ENCODING));
		}
		if (!(params.getBefore() == 0)) {
			data.append("&")
					.append(URLEncoder
							.encode("before", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + params.getBefore(),
							MoodleServices.ENCODING));
		}
		data.trimToSize();
		final NodeList elements = MoodleCallRestWebService
				.call(data.toString());
		MoodleModAssignSubmissionReturn results = null;
		MoodleModAssignWarning warning = null;
		MoodleModAssignSubmissions submissions = null;
		MoodleModAssignSubmission submission = null;
		MoodleModAssignFile file = null;
		MoodleModAssignOnlineText onlineText = null;
		for (int j = 0; j < elements.getLength(); j++) {
			String parent = elements.item(j).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(j).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE")
					&& (nodeName.equals("assignments") || nodeName
							.equals("warnings"))) {
				if (results == null) {
					results = new MoodleModAssignSubmissionReturn();
				}
			}
			if (parent.equals("assignments")) {
				if (nodeName.equals("assignmentid")) {
					if (submissions != null) {
						if (submission != null) {
							if (file != null) {
								submission.addFile(file);
								file = null;
							}
							if (onlineText != null) {
								submission.addOnlineText(onlineText);
								onlineText = null;
							}
							submissions.addSubmission(submission);
							submission = null;
						}
						results.addAssignment(submissions);
					}
					submissions = new MoodleModAssignSubmissions();
					submissions.setAssignmentId(Long.parseLong(content));
				}
			}
			if (parent.equals("warnings")) {
				if (nodeName.equals("element")) {
					if (warning != null) {
						results.addWarning(warning);
					}
					warning = new MoodleModAssignWarning();
				}
				warning.setField(nodeName, content);
			}
			if (parent.equals("submissions")) {
				if (nodeName.equals("id")) {
					if (submission != null) {
						if (file != null) {
							submission.addFile(file);
							file = null;
						}
						if (onlineText != null) {
							submission.addOnlineText(onlineText);
							onlineText = null;
						}
						submissions.addSubmission(submission);
					}
					submission = new MoodleModAssignSubmission();
				}
				submission.setField(nodeName, content);
			}
			if (parent.equals("files")) {
				if (nodeName.equals("id")) {
					if (file != null) {
						submission.addFile(file);
					}
					file = new MoodleModAssignFile();
				}
				file.setField(nodeName, content);
			}
			if (parent.equals("onlinetext")) {
				if (nodeName.equals("id")) {
					if (onlineText != null) {
						submission.addOnlineText(onlineText);
					}
					onlineText = new MoodleModAssignOnlineText();
				}
				onlineText.setField(nodeName, content);
			}
		}
		if (results != null) {
			if (submissions != null) {
				if (submission != null) {
					if (file != null) {
						submission.addFile(file);
					}
					if (onlineText != null) {
						submission.addOnlineText(onlineText);
					}
					submissions.addSubmission(submission);
				}
				results.addAssignment(submissions);
			}
			if (warning != null) {
				results.addWarning(warning);
			}
		}
		return results;
	}

	public MoodleModAssignSubmissionReturn __getSubmissions(String url,
			String token, MoodleModAssignSubmissionParam params)
			throws MoodleRestException, UnsupportedEncodingException {
		if (MoodleCallRestWebService.isLegacy()) {
			throw new MoodleRestException(MoodleRestException.NO_LEGACY);
		}
		final StringBuilder data = new StringBuilder();
		final String functionCall = MoodleServices.MOD_ASSIGN_GET_SUBMISSIONS;
		data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder.encode(token, MoodleServices.ENCODING));
		data.append("&")
				.append(URLEncoder
						.encode("wsfunction", MoodleServices.ENCODING))
				.append("=")
				.append(URLEncoder
						.encode(functionCall, MoodleServices.ENCODING));
		if (params.getAssignmentIds() == null
				|| params.getAssignmentIds().length == 0) {
			throw new MoodleRestModAssignException(
					MoodleRestException.REQUIRED_PARAMETER);
		}
		final MoodleModAssignAssignment[] assignmentIds = params
				.getAssignmentIds();
		for (int i = 0; i < assignmentIds.length; i++) {
			data.append("&")
					.append(URLEncoder.encode("assignmentids[" + i + "]",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + assignmentIds[i],
							MoodleServices.ENCODING));
		}
		if (!params.getStatus().isEmpty()) {
			data.append("&")
					.append(URLEncoder
							.encode("status", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(params.getStatus(),
							MoodleServices.ENCODING));
		}
		if (!(params.getSince() == 0)) {
			data.append("&")
					.append(URLEncoder.encode("since", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + params.getSince(),
							MoodleServices.ENCODING));
		}
		if (!(params.getBefore() == 0)) {
			data.append("&")
					.append(URLEncoder
							.encode("before", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode("" + params.getBefore(),
							MoodleServices.ENCODING));
		}
		data.trimToSize();
		final NodeList elements = new MoodleCallRestWebService().__call(url,
				data.toString());
		MoodleModAssignSubmissionReturn results = null;
		MoodleModAssignWarning warning = null;
		MoodleModAssignSubmissions submissions = null;
		MoodleModAssignSubmission submission = null;
		MoodleModAssignFile file = null;
		MoodleModAssignOnlineText onlineText = null;
		for (int j = 0; j < elements.getLength(); j++) {
			String parent = elements.item(j).getParentNode().getParentNode()
					.getParentNode().getParentNode().getNodeName();
			if (parent.equals("KEY")) {
				parent = elements.item(j).getParentNode().getParentNode()
						.getParentNode().getParentNode().getAttributes()
						.getNamedItem("name").getNodeValue();
			}
			final String content = elements.item(j).getTextContent();
			final String nodeName = elements.item(j).getParentNode()
					.getAttributes().getNamedItem("name").getNodeValue();
			if (parent.equals("RESPONSE")
					&& (nodeName.equals("assignments") || nodeName
							.equals("warnings"))) {
				if (results == null) {
					results = new MoodleModAssignSubmissionReturn();
				}
			}
			if (parent.equals("assignments")) {
				if (nodeName.equals("assignmentid")) {
					if (submissions != null) {
						if (submission != null) {
							if (file != null) {
								submission.addFile(file);
								file = null;
							}
							if (onlineText != null) {
								submission.addOnlineText(onlineText);
								onlineText = null;
							}
							submissions.addSubmission(submission);
							submission = null;
						}
						results.addAssignment(submissions);
					}
					submissions = new MoodleModAssignSubmissions();
					submissions.setAssignmentId(Long.parseLong(content));
				}
			}
			if (parent.equals("warnings")) {
				if (nodeName.equals("element")) {
					if (warning != null) {
						results.addWarning(warning);
					}
					warning = new MoodleModAssignWarning();
				}
				warning.setField(nodeName, content);
			}
			if (parent.equals("submissions")) {
				if (nodeName.equals("id")) {
					if (submission != null) {
						if (file != null) {
							submission.addFile(file);
							file = null;
						}
						if (onlineText != null) {
							submission.addOnlineText(onlineText);
							onlineText = null;
						}
						submissions.addSubmission(submission);
					}
					submission = new MoodleModAssignSubmission();
				}
				submission.setField(nodeName, content);
			}
			if (parent.equals("files")) {
				if (nodeName.equals("id")) {
					if (file != null) {
						submission.addFile(file);
					}
					file = new MoodleModAssignFile();
				}
				file.setField(nodeName, content);
			}
			if (parent.equals("onlinetext")) {
				if (nodeName.equals("id")) {
					if (onlineText != null) {
						submission.addOnlineText(onlineText);
					}
					onlineText = new MoodleModAssignOnlineText();
				}
				onlineText.setField(nodeName, content);
			}
		}
		if (results != null) {
			if (submissions != null) {
				if (submission != null) {
					if (file != null) {
						submission.addFile(file);
					}
					if (onlineText != null) {
						submission.addOnlineText(onlineText);
					}
					submissions.addSubmission(submission);
				}
				results.addAssignment(submissions);
			}
			if (warning != null) {
				results.addWarning(warning);
			}
		}
		return results;
	}
}
