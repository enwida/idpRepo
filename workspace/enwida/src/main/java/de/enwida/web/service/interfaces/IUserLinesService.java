package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;

public interface IUserLinesService {

	boolean createUserLines(List<UserLines> lines, UserLinesMetaData metaData);

	boolean createUserLine(UserLines line);

	void createUserLineMetaData(UserLinesMetaData metaData);

	void createUserLineMetaData(UserLinesMetaData metaData,
			UploadedFile file);

	void updateUserLineMetaData(UserLinesMetaData metaData);

	boolean eraseUserLines(long fileId);

	boolean eraseUserLineMetaData(long fileId);

}
