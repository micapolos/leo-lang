package leo15.lambda

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo15.repeatName
import leo15.terms.term
import leo15.valueName
import org.junit.Test

class ScriptTest {
//	@Test
//	fun bind() {
//		fn(fn("plus".valueTerm))
//			.invoke(10.valueTerm)
//			.invoke(20.valueTerm)
//			.script
//			.assertEqualTo(
//				leo(
//					"bind" lineTo 10.valueTerm.script,
//					"bind" lineTo 20.valueTerm.script,
//					"apply" lineTo "plus".valueTerm.script))
//	}

	@Test
	fun repeat() {
		10.term.repeat.script.assertEqualTo(leo(repeatName(valueName("10"()))))
	}
}