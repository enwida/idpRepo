package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.rl.dtos.DOUserLines;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLinesMetaData;

public interface IUserLinesService {

	boolean createUserLines(List<DOUserLines> lines, UserLinesMetaData metaData);

	boolean createUserLine(DOUserLines line);

	public boolean createUserLines(List<DOUserLines> userlines);

	public List<DOUserLines> getUserLines(int metaDataId);

	void createUserLineMetaData(UserLinesMetaData metaData);

	void createUserLineMetaData(UserLinesMetaData metaData, UploadedFile file);

	void updateUserLineMetaData(UserLinesMetaData metaData);

	boolean eraseUserLines(int metaDataId);

	boolean eraseUserLineMetaData(long fileId);

}
