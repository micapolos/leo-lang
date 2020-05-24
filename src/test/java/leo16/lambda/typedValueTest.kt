package leo16.lambda

import leo.base.assertEqualTo
import leo15.lambda.idTerm
import leo15.lambda.valueTerm
import leo16.emptyValue
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.value
import org.junit.Test

class TypedValueTest {
	@Test
	fun empty() {
		typed().value.assertEqualTo(emptyValue)
	}

	@Test
	fun struct() {
		typed(
			_x(_zero(typed())),
			_y(_one(typed())))
			.value
			.assertEqualTo(
				value(
					_x(_zero(value())),
					_y(_one(value()))))
	}

	@Test
	fun alternative() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.value
			.assertEqualTo(value(_zero(value())))

		type(_zero.invoke(type()))
			.or(typed(_one(typed())))
			.value
			.assertEqualTo(value(_one(value())))
	}

	@Test
	fun function() {
		idTerm
			.of(type(type(_zero(type())).giving(type(_one(type()))).field))
			.value
			.assertEqualTo(value(_taking(_zero(), _giving(_one()))))
	}

	@Test
	fun native() {
		typed(123.valueTerm of "int".nativeTypeField)
			.value
			.assertEqualTo(value(123.nativeField))
	}
}