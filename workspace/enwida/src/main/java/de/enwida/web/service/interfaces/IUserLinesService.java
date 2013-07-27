package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.model.User;

public interface IUserLinesService {

	void createUserLines(List<UserLines> lines, UserLinesMetaData metaData,
			User user, UploadedFile file);

	void createUserLine(UserLines line);

	void createUserLineMetaData(UserLinesMetaData metaData);

	void createUserLineMetaData(UserLinesMetaData metaData, User user,
			UploadedFile file);

}
