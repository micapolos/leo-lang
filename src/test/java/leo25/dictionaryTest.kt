package leo25

import kotlinx.collections.immutable.persistentMapOf
import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class DictionaryTest {
	@Test
	fun plusAny() {
		dictionary()
			.plus(script(anyName), binding(value("ok")))
			.assertEqualTo(
				Dictionary(persistentMapOf(token(anyEnd) to resolution(binding(value("ok")))))
			)
	}

	@Test
	fun applyString() {
		dictionary()
			.plus(script("ping"), binding(value("pong")))
			.applyOrNull(value("ping"))
			.assertEqualTo(value("pong"))
	}

	@Test
	fun applyStruct() {
		dictionary()
			.plus(script("name" lineTo script(anyName)), binding(value("ok")))
			.run {
				applyOrNull(value("name" lineTo value())).assertEqualTo(value("ok"))
				applyOrNull(value("name" lineTo value("michal"))).assertEqualTo(value("ok"))
				applyOrNull(value("name" lineTo value(line(literal("Michał"))))).assertEqualTo(value("ok"))
			}
	}

	@Test
	fun applyAny() {
		dictionary()
			.plus(script(anyName), binding(value("pong")))
			.run {
				applyOrNull(value("ping")).assertEqualTo(value("pong"))
				applyOrNull(value("ping")).assertEqualTo(value("pong"))
			}
	}

	@Test
	fun anyValueApply() {
		dictionary()
			.plus(
				script(anyName lineTo script(), "plus" lineTo script(anyName)),
				binding(value("ok"))
			)
			.run {
				applyOrNull(value("a" lineTo value(), "plus" lineTo value("b" lineTo value())))
					.assertEqualTo(value("ok"))
			}
	}

	@Test
	fun literalApply() {
		dictionary()
			.plus(script(literal("foo")), binding(value("ok")))
			.applyOrNull(value(line(literal("foo"))))
			.assertEqualTo(value("ok"))

		dictionary()
			.plus(script(literal("foo")), binding(value("ok")))
			.applyOrNull(value(line(literal("bar"))))
			.assertEqualTo(null)

		dictionary()
			.plus(script(literal(123)), binding(value("ok")))
			.applyOrNull(value(line(literal(123))))
			.assertEqualTo(value("ok"))

		dictionary()
			.plus(script(literal(123)), binding(value("ok")))
			.applyOrNull(value(line(literal(124))))
			.assertEqualTo(null)
	}

	@Test
	fun plusDifferentTokens() {
		dictionary(
			token(begin("x")) to resolution(
				dictionary(
					token(emptyEnd) to resolution(
						dictionary(
							token(emptyEnd) to resolution(binding(value("x")))
						)
					)
				)
			)
		)
			.plus(
				dictionary(
					token(begin("y")) to resolution(
						dictionary(
							token(emptyEnd) to resolution(
								dictionary(
									token(emptyEnd) to resolution(binding(value("y")))
								)
							)
						)
					)
				)
			)
			.assertEqualTo(
				dictionary(
					token(begin("x")) to resolution(
						dictionary(
							token(emptyEnd) to resolution(
								dictionary(
									token(emptyEnd) to resolution(binding(value("x")))
								)
							)
						)
					),
					token(begin("y")) to resolution(
						dictionary(
							token(emptyEnd) to resolution(
								dictionary(
									token(emptyEnd) to resolution(binding(value("y")))
								)
							)
						)
					)
				)
			)
	}

	@Test
	fun plusSharedTokens() {
		dictionary(
			token(begin("point")) to resolution(
				dictionary(
					token(begin("x")) to resolution(
						dictionary(
							token(emptyEnd) to resolution(
								dictionary(
									token(emptyEnd) to resolution(
										dictionary(
											token(emptyEnd) to resolution(binding(value("x")))
										)
									)
								)
							)
						)
					)
				)
			)
		)
			.plus(
				dictionary(
					token(begin("point")) to resolution(
						dictionary(
							token(begin("y")) to resolution(
								dictionary(
									token(emptyEnd) to resolution(
										dictionary(
											token(emptyEnd) to resolution(
												dictionary(
													token(emptyEnd) to resolution(binding(value("y")))
												)
											)
										)
									)
								)
							)
						)
					)
				)
			)
			.assertEqualTo(
				dictionary(
					token(begin("point")) to resolution(
						dictionary(
							token(begin("x")) to resolution(
								dictionary(
									token(emptyEnd) to resolution(
										dictionary(
											token(emptyEnd) to resolution(
												dictionary(
													token(emptyEnd) to resolution(binding(value("x")))
												)
											)
										)
									)
								)
							),
							token(begin("y")) to resolution(
								dictionary(
									token(emptyEnd) to resolution(
										dictionary(
											token(emptyEnd) to resolution(
												dictionary(
													token(emptyEnd) to resolution(binding(value("y")))
												)
											)
										)
									)
								)
							)
						)
					)
				)
			)
	}

	@Test
	fun plusAnyOverride() {
		dictionary(
			token(begin("x")) to resolution(
				dictionary(
					token(emptyEnd) to resolution(
						dictionary(
							token(emptyEnd) to resolution(binding(value("x")))
						)
					)
				)
			),
			token(emptyEnd) to resolution(binding(value("end")))
		)
			.plus(
				dictionary(
					token(anyEnd) to resolution(
						dictionary(
							token(emptyEnd) to resolution(binding(value("y")))
						)
					)
				)
			)
			.assertEqualTo(
				dictionary(
					token(anyEnd) to resolution(
						dictionary(
							token(emptyEnd) to resolution(binding(value("y")))
						)
					)
				)
			)
	}

	@Test
	fun switchOrNull() {
		dictionary()
			.switchOrNull(
				value("shape" lineTo value("circle" lineTo value("radius" lineTo value("zero")))),
				script(
					"circle" lineTo script(getName lineTo script("radius")),
					"rectangle" lineTo script(getName lineTo script("side"))
				)
			)
			.assertEqualTo(value("radius" lineTo value("zero")))
	}
}