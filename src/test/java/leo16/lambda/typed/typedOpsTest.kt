package leo16.lambda.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo15.lambda.choiceTerm
import leo15.lambda.fn
import leo15.lambda.idTerm
import leo15.lambda.invoke
import leo15.type.term
import leo16.lambda.type.giving
import leo16.lambda.type.intType
import leo16.lambda.type.invoke
import leo16.lambda.type.stringType
import leo16.lambda.type.type
import leo16.names.*
import kotlin.test.Test

class TypedOpsTest {
	@Test
	fun getOrNull() {
		typed(
			_point(
				_x(_zero(typed())),
				_y(_one(typed()))))
			.run {
				getOrNull(_x).assertEqualTo(typed(_x(_zero(typed()))))
				getOrNull(_y).assertEqualTo(typed(_y(_one(typed()))))
				getOrNull(_z).assertEqualTo(null)
			}

		typed(
			_point(
				_x(10.typedField),
				_y(20.typedField)))
			.run {
				getOrNull(_x)!!.evaluate.assertEqualTo(typed(_x(10.typedField)))
				getOrNull(_y)!!.evaluate.assertEqualTo(typed(_y(20.typedField)))
				getOrNull(_z).assertEqualTo(null)
			}
	}

	@Test
	fun thingOrNull() {
		typed(
			_point(
				_x(10.typedField),
				_y(20.typedField)))
			.thingOrNull!!
			.assertEqualTo(
				typed(
					_x(10.typedField),
					_y(20.typedField)))
	}

	@Test
	fun matchOrNull() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.matchOrNull(
				fn(0.term) of type(_zero(type())).giving(intType),
				fn(1.term) of type(_one(type())).giving(intType))
			.assertEqualTo(
				choiceTerm(2, 1, idTerm)
					.invoke(fn(0.term))
					.invoke(fn(1.term)) of intType)
	}

	@Test
	fun matchOrNull_evaluate() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.matchOrNull(
				fn(0.term) of type(_zero(type())).giving(intType),
				fn(1.term) of type(_one(type())).giving(intType))!!
			.evaluate
			.assertEqualTo(0.term of intType)
	}

	@Test
	fun matchOrNull_invalidFirstInputType() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.matchOrNull(
				fn(0.term) of type(_one(type())).giving(intType),
				fn(1.term) of type(_one(type())).giving(intType))
			.assertNull
	}

	@Test
	fun matchOrNull_invalidSecondInputType() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.matchOrNull(
				fn(0.term) of type(_zero(type())).giving(intType),
				fn(1.term) of type(_zero(type())).giving(intType))
			.assertNull
	}

	@Test
	fun matchOrNull_differentOutputTypes() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.matchOrNull(
				fn("0".term) of type(_zero(type())).giving(stringType),
				fn(1.term) of type(_one(type())).giving(intType))
			.assertNull
	}

	@Test
	fun matchOrNull_notAlternative() {
		typed(_zero(typed()))
			.matchOrNull(
				fn("0".term) of type(_zero(type())).giving(stringType),
				fn(1.term) of type(_one(type())).giving(intType))
			.assertNull
	}

	@Test
	fun matchOrNull_firstNotFunction() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.matchOrNull(
				0.term of stringType,
				fn(1.term) of type(_one(type())).giving(intType))
			.assertNull
	}

	@Test
	fun matchOrNull_secondNotFunction() {
		typed(_zero(typed()))
			.or(type(_one(type())))
			.matchOrNull(
				fn(0.term) of type(_zero(type())).giving(intType),
				1.term of intType)
			.assertNull
	}
}