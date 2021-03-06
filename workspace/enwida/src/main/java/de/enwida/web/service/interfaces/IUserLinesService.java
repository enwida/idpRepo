package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.rl.dtos.DOUserLines;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLinesMetaData;

public interface IUserLinesService {

	boolean createUserLines(List<DOUserLines> lines, long userLineId)
			throws Exception;

	boolean createUserLine(DOUserLines line);

	public boolean createUserLines(List<DOUserLines> userlines);

	public List<DOUserLines> getUserLines(long userLineId);

	void createUserLineMetaData(UserLinesMetaData metaData) throws Exception;

	void createUserLineMetaData(UserLinesMetaData metaData, UploadedFile file) throws Exception;

	/*void updateUserLineMetaData(UserLinesMetaData metaData) throws Exception;*/

	boolean eraseUserLines(long userLineId);

}
