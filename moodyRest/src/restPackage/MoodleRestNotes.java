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

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.NodeList;

/**
 * <p>
 * Class containing static Methods to call Moodle REST notes web services.
 * </p>
 * 
 * @author Bill Antonia
 */
public class MoodleRestNotes implements Serializable{

	// private static final int BUFFER_MAX=4000;

	/**
	 * <p>
	 * Method to attach a single note to a user.
	 * </p>
	 * 
	 * @param note
	 * @return MoodleNote
	 * @throws MoodleRestNotesException
	 * @throws MoodleRestException
	 */
	public static MoodleNote createNote(MoodleNote note)
			throws MoodleRestNotesException, MoodleRestException {
		final MoodleNote[] a = new MoodleNote[1];
		a[0] = note;
		final MoodleNote[] n = createNotes(a);
		return n[0];
	}

	/**
	 * <p>
	 * Method to attach notes to users.
	 * </p>
	 * 
	 * @param MoodleNote
	 *            [] notes
	 * @return MoodleNote[]
	 * @throws MoodleRestNotesException
	 * @throws MoodleRestException
	 */
	public static MoodleNote[] createNotes(MoodleNote[] notes)
			throws MoodleRestNotesException, MoodleRestException {
		int processedCount = 0;
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_NOTES_CREATE_NOTES
				: MoodleServices.CORE_NOTES_CREATE_NOTES;
		try {
			final StringBuilder data = new StringBuilder();
			if (MoodleCallRestWebService.getAuth() == null) {
				throw new MoodleRestNotesException();
			} else {
				data.append(MoodleCallRestWebService.getAuth());
			}
			data.append("&")
					.append(URLEncoder.encode("wsfunction",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(functionCall,
							MoodleServices.ENCODING));
			for (int i = 0; i < notes.length; i++) {
				if (notes[i] == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.NOTES_NULL);
				}
				if (notes[i].getUserId() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.USERID_NOT_SET);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][userid]", MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									"" + notes[i].getUserId(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getPublishState() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.PUBLISHSTATE_NULL);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][publishstate]",
									MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									notes[i].getPublishState(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getCourseId() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.COURSEID_NOT_SET);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][courseid]", MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									"" + notes[i].getCourseId(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getText() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.TEXT_NULL);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i + "][text]",
									MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(notes[i].getText(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getFormat() == null) {
					notes[i].setFormat("text");
				}
				if (notes[i].getFormat().equals("text")
						|| notes[i].getFormat().equals("html")) {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][format]", MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(notes[i].getFormat(),
									MoodleServices.ENCODING));
				} else {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.FORMAT_INCORRECT);
				}
				if (notes[i].getClientNoteId() != null) {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][clientnoteid]",
									MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									notes[i].getClientNoteId(),
									MoodleServices.ENCODING));
				}
			}
			data.trimToSize();
			final NodeList elements = MoodleCallRestWebService.call(data
					.toString());
			for (int j = 0; j < elements.getLength(); j += 3, processedCount++) {
				for (int k = 0; k < 3; k++) {
					final String content = elements.item(j + k)
							.getTextContent();
					final String nodeName = elements.item(j + k)
							.getParentNode().getAttributes()
							.getNamedItem("name").getNodeValue();
					notes[processedCount].setMoodleNoteField(nodeName, content);
				}
			}
		} catch (final IOException ex) {
			Logger.getLogger(MoodleRestNotes.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return notes;
	}

	// core_notes_create_notes

	public MoodleNote __createNote(String url, String token, MoodleNote note)
			throws MoodleRestNotesException, MoodleRestException {
		final MoodleNote[] a = new MoodleNote[1];
		a[0] = note;
		final MoodleNote[] n = __createNotes(url, token, a);
		return n[0];
	}

	public MoodleNote[] __createNotes(String url, String token,
			MoodleNote[] notes) throws MoodleRestNotesException,
			MoodleRestException {
		int processedCount = 0;
		final String functionCall = MoodleCallRestWebService.isLegacy() ? MoodleServices.MOODLE_NOTES_CREATE_NOTES
				: MoodleServices.CORE_NOTES_CREATE_NOTES;
		try {
			final StringBuilder data = new StringBuilder();
			data.append(URLEncoder.encode("wstoken", MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(token, MoodleServices.ENCODING));
			data.append("&")
					.append(URLEncoder.encode("wsfunction",
							MoodleServices.ENCODING))
					.append("=")
					.append(URLEncoder.encode(functionCall,
							MoodleServices.ENCODING));
			for (int i = 0; i < notes.length; i++) {
				if (notes[i] == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.NOTES_NULL);
				}
				if (notes[i].getUserId() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.USERID_NOT_SET);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][userid]", MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									"" + notes[i].getUserId(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getPublishState() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.PUBLISHSTATE_NULL);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][publishstate]",
									MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									notes[i].getPublishState(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getCourseId() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.COURSEID_NOT_SET);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][courseid]", MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									"" + notes[i].getCourseId(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getText() == null) {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.TEXT_NULL);
				} else {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i + "][text]",
									MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(notes[i].getText(),
									MoodleServices.ENCODING));
				}
				if (notes[i].getFormat() == null) {
					notes[i].setFormat("text");
				}
				if (notes[i].getFormat().equals("text")
						|| notes[i].getFormat().equals("html")) {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][format]", MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(notes[i].getFormat(),
									MoodleServices.ENCODING));
				} else {
					throw new MoodleRestNotesException(
							MoodleRestNotesException.FORMAT_INCORRECT);
				}
				if (notes[i].getClientNoteId() != null) {
					data.append("&")
							.append(URLEncoder.encode("notes[" + i
									+ "][clientnoteid]",
									MoodleServices.ENCODING))
							.append("=")
							.append(URLEncoder.encode(
									notes[i].getClientNoteId(),
									MoodleServices.ENCODING));
				}
			}
			data.trimToSize();
			final NodeList elements = new MoodleCallRestWebService().__call(
					url, data.toString());
			for (int j = 0; j < elements.getLength(); j += 3, processedCount++) {
				for (int k = 0; k < 3; k++) {
					final String content = elements.item(j + k)
							.getTextContent();
					final String nodeName = elements.item(j + k)
							.getParentNode().getAttributes()
							.getNamedItem("name").getNodeValue();
					notes[processedCount].setMoodleNoteField(nodeName, content);
				}
			}
		} catch (final IOException ex) {
			Logger.getLogger(MoodleRestNotes.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return notes;
	}
}
