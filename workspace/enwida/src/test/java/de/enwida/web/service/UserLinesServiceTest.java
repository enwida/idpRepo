/**
 * 
 */
package de.enwida.web.service;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.web.service.interfaces.IUserLinesService;

/**
 * @author Jitin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class UserLinesServiceTest {

	@Autowired
	private IUserLinesService userLineService;

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("Testing");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link de.enwida.web.service.implementation.UserLinesServiceImpl#createUserLine(de.enwida.web.db.model.UserLines)}.
	 */
	@Test
	public void testCreateUserLine() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.enwida.web.service.implementation.UserLinesServiceImpl#createUserLineMetaData(de.enwida.web.db.model.UserLinesMetaData)}.
	 */
	@Test
	@Ignore
	public void testCreateUserLineMetaDataUserLinesMetaData() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.enwida.web.service.implementation.UserLinesServiceImpl#updateUserLineMetaData(de.enwida.web.db.model.UserLinesMetaData)}.
	 */
	@Test
	@Ignore
	public void testUpdateUserLineMetaData() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.enwida.web.service.implementation.UserLinesServiceImpl#createUserLines(java.util.List, de.enwida.web.db.model.UserLinesMetaData)}.
	 */
	@Test
	@Ignore
	public void testCreateUserLines() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.enwida.web.service.implementation.UserLinesServiceImpl#createUserLineMetaData(de.enwida.web.db.model.UserLinesMetaData, de.enwida.web.db.model.UploadedFile)}.
	 */
	@Test
	@Ignore
	public void testCreateUserLineMetaDataUserLinesMetaDataUploadedFile() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.enwida.web.service.implementation.UserLinesServiceImpl#eraseUserLines(long)}.
	 */
	@Test
	@Ignore
	public void testEraseUserLines() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.enwida.web.service.implementation.UserLinesServiceImpl#eraseUserLineMetaData(long)}.
	 */
	@Test
	@Ignore
	public void testEraseUserLineMetaData() {
		fail("Not yet implemented");
	}

}
