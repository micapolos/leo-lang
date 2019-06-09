package leo5.core

import leo.base.assertEqualTo
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class ValueTest {
	val bitZero get() = value(bit0)
	val bitOne get() = value(bit1)
	val negate get() = value(function(branch(function(argument), pair(function(value(bit1)), function(value(bit0))))))
	val and = value(
		function(
			branch(
				function(at(function(argument), bit0)),
				pair(
					function(
						branch(
							function(at(function(argument), bit1)),
							pair(
								function(value(bit0)),
								function(value(bit0))))),
					function(
						branch(
							function(at(function(argument), bit1)),
							pair(
								function(value(bit0)),
								function(value(bit1)))))))))

	@Test
	fun negate() {
		negate.invoke(bitZero).assertEqualTo(bitOne)
	}

	@Test
	fun and() {
		and.invoke(value(pair(bitZero, bitZero))).assertEqualTo(bitZero)
		and.invoke(value(pair(bitZero, bitOne))).assertEqualTo(bitZero)
		and.invoke(value(pair(bitOne, bitZero))).assertEqualTo(bitZero)
		and.invoke(value(pair(bitOne, bitOne))).assertEqualTo(bitOne)
	}

	@Test
	fun call() {
		value(function(call(function(negate), function(argument)))).invoke(bitZero).assertEqualTo(bitOne)
	}
}