package TTT;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import TTT.commons.Hello;

public class TestTest {
	@Test
	public void test(){
		Hello h = new Hello();
		h.play();
		assertEquals(1,1);
	}
}
