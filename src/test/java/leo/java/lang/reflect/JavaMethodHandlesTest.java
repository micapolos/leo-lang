package leo.java.lang.reflect;

import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class JavaMethodHandlesTest {
	@Test
	public void testt() throws Throwable {
		MethodHandle m = MethodHandles
			.lookup()
			.findStatic(
				Integer.class,
				"sum",
				MethodType.methodType(int.class, int.class, int.class));
		int i = (int) m.invokeExact(1, 2);
		System.out.println(i);
	}
}
