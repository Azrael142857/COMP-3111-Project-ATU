package ATU;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PersonTest {
	private Person person = null;
	
	@Before public void setUp() throws Exception {
		person = new Person("20103456", "Amy Leung", "lamy@connect.ust.hk", 
							"40", "65", "1", "0", "0", "I use Macintosh.");
	}
	
	@Test public void testPerson() {
		// Test Student ID Helper
		assertEquals(person.getStudentid(), "20103456");
		person.setStudentid("test");
		assertNotEquals(person.getStudentid(), "20103456");
		assertEquals(person.getStudentid(), "test");

		// Test Student Name Helper
		assertEquals(person.getStudentname(), "Amy Leung");
		person.setStudentname("test");
		assertNotEquals(person.getStudentname(), "Amy Leung");
		assertEquals(person.getStudentname(), "test");

		// Test Student Email Helper
		assertEquals(person.getStudentemail(), "lamy@connect.ust.hk");
		person.setStudentemail("test");
		assertNotEquals(person.getStudentemail(), "lamy@connect.ust.hk");
		assertEquals(person.getStudentemail(), "test");

		// Test K1Energy Helper
		assertEquals(person.getK1energy(), "40");
		person.setK1energy("test");
		assertNotEquals(person.getK1energy(), "40");
		assertEquals(person.getK1energy(), "test");

		// Test K2Energy Helper
		assertEquals(person.getK2energy(), "65");
		person.setK2energy("99");
		assertNotEquals(person.getK2energy(), "65");
		assertEquals(person.getK2energy(), "99");

		// Test K3tick1 Helper
		assertEquals(person.getK3tick1(), "1");
		person.setK3tick1("0");
		assertNotEquals(person.getK3tick1(), "1");
		assertEquals(person.getK3tick1(), "0");

		// Test K3tick2 Helper
		assertEquals(person.getK3tick2(), "0");
		person.setK3tick2("1");
		assertNotEquals(person.getK3tick2(), "0");
		assertEquals(person.getK3tick2(), "1");

		// Test My Preference Helper
		assertEquals(person.getMypreference(), "0");
		person.setMypreference("1");
		assertNotEquals(person.getMypreference(), "0");
		assertEquals(person.getMypreference(), "1");

		// Test Concerns Helper
		assertEquals(person.getConcerns(), "I use Macintosh.");
		person.setConcerns("test");
		assertNotEquals(person.getConcerns(), "I use Macintosh.");
		assertEquals(person.getConcerns(), "test");
	}
}