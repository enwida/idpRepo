/**
 * 
 */
package de.enwida.web.service;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.chart.LineManager;
import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.LineRequest;
import de.enwida.transport.XYDataLine;
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

    @Autowired
    private LineManager lineManager;

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

    @Test
    public void getUserLine() {
        Calendar c1 = new GregorianCalendar();
        c1.setTimeInMillis(0);
        Calendar c2 = new GregorianCalendar();
        c2.set(Calendar.YEAR, 2020);
        final LineRequest request = new LineRequest(Aspect.UL_TH_LOAD_PROFILE, 
                1, 0,  c1, c2, DataResolution.DAILY , null);
        try {
            XYDataLine x = lineManager.getLine(request);
            System.out.print(x.getDataPoints().size());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
