/**
 * 
 */
package restPackage;

import java.io.Serializable;

/**
 * @author MoodyProject Team
 * 
 */
public enum MoodleContactState implements Serializable {
	ONLINE, // the contact is online at the time of fetch
	OFFLINE, // the contact is offline at the time of fetch
	STRANGERS // the user in this contact is a stranger (not a contact list user)
				// at the time of fetch
}
