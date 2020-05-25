package leo16.lambda

import leo.base.assertEqualTo
import leo14.emptyScript
import leo14.script
import leo15.lambda.idTerm
import leo15.lambda.valueTerm
import leo16.names.*
import leo16.nativeScriptLine
import org.junit.Test

class TypedValueTest {
	@Test
	fun empty() {
		typed().script.assertEqualTo(emptyScript)
	}

	@Test
	fun struct() {
		typed(
			_x(_zero(typed())),
			_y(_one(typed())))
			.script
			.assertEqualTo(
				script(
					_x(_zero(script())),
					_y(_one(script()))))
	}

	@Test
	fun alternative() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.script
			.assertEqualTo(script(_zero(script())))

		type(_zero.invoke(type()))
			.or(typed(_one(typed())))
			.script
			.assertEqualTo(script(_one(script())))
	}

	@Test
	fun function() {
		idTerm
			.of(type(type(_zero(type())).giving(type(_one(type()))).field))
			.script
			.assertEqualTo(script(_taking(_zero(script()), _giving(_one(script())))))
	}

	@Test
	fun native() {
		typed(123.valueTerm of "int".nativeTypeField)
			.script
			.assertEqualTo(script(123.nativeScriptLine))
	}
}