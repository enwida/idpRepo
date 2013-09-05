/**
 * 
 */
package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.rl.dtos.DOUserLines;
import de.enwida.web.db.model.UserLinesMetaData;

/**
 * @author Jitin
 *
 */
public interface IUserLinesDao extends IDao<UserLinesMetaData> {

	boolean createUserLines(List<DOUserLines> userlines);

	List<DOUserLines> getUserLines(long userLineId);

	boolean deleteUserLines(long userLineId);

}
