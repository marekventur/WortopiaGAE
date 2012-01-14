/**
 * Add functions to json user object
 * @param data
 * @returns
 */

function createUserFromJson(data) {
	
	this.isGuest = function() {
		return this.id < 0;
	}
	
	return data;
}

