/**
 * 
 */
package de.enwida.web.dao.interfaces;

import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;

/**
 * @author Jitin
 *
 */
public interface IUserLinesDao extends IDao<UserLines> {

	void create(UserLinesMetaData metaData);

}
