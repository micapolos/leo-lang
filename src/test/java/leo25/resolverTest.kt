package leo25

import kotlinx.collections.immutable.persistentMapOf
import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ResolverTest {
	@Test
	fun plusAny() {
		resolver()
			.plus(
				definition(
					pattern(script(anyName)),
					binding(value("ok"))
				)
			)
			.assertEqualTo(
				Resolver(persistentMapOf(token(anyEnd) to resolution(binding(value("ok")))))
			)
	}

	@Test
	fun applyString() {
		resolver()
			.plus(
				definition(
					pattern(script("ping")),
					binding(value("pong"))
				)
			)
			.applyOrNull(value("ping"))
			.assertEqualTo(value("pong"))
	}

	@Test
	fun applyStruct() {
		resolver()
			.plus(
				definition(
					pattern(script("name" lineTo script(anyName))),
					binding(value("ok"))
				)
			)
			.run {
				applyOrNull(value("name" fieldTo value())).assertEqualTo(value("ok"))
				applyOrNull(value("name" fieldTo value("michal"))).assertEqualTo(value("ok"))
				applyOrNull(value("name" fieldTo value(field(literal("Michał"))))).assertEqualTo(value("ok"))
			}
	}

	@Test
	fun applyAny() {
		resolver()
			.plus(
				definition(
					pattern(script(anyName)),
					binding(value("pong"))
				)
			)
			.run {
				applyOrNull(value("ping")).assertEqualTo(value("pong"))
				applyOrNull(value("ping")).assertEqualTo(value("pong"))
			}
	}

	@Test
	fun anyValueApply() {
		resolver()
			.plus(
				definition(
					pattern(script(anyName lineTo script(), "plus" lineTo script(anyName))),
					binding(value("ok"))
				)
			)
			.run {
				applyOrNull(value("a" fieldTo value(), "plus" fieldTo value("b" fieldTo value())))
					.assertEqualTo(value("ok"))
			}
	}

	@Test
	fun literalApply() {
		resolver()
			.plus(
				definition(
					pattern(script(textName lineTo script(anyName))),
					binding(value("ok"))
				)
			)
			.applyOrNull(value(field(literal("foo"))))
			.assertEqualTo(value("ok"))

		resolver()
			.plus(
				definition(
					pattern(script(literal("foo"))),
					binding(value("ok"))
				)
			)
			.applyOrNull(value(field(literal("foo"))))
			.assertEqualTo(value("ok"))

		resolver()
			.plus(
				definition(
					pattern(script(literal("foo"))),
					binding(value("ok"))
				)
			)
			.applyOrNull(value(field(literal("bar"))))
			.assertEqualTo(null)

		resolver()
			.plus(
				definition(
					pattern(script(literal(123))),
					binding(value("ok"))
				)
			)
			.applyOrNull(value(field(literal(123))))
			.assertEqualTo(value("ok"))

		resolver()
			.plus(
				definition(
					pattern(script(literal(123))),
					binding(value("ok"))
				)
			)
			.applyOrNull(value(field(literal(124))))
			.assertEqualTo(null)
	}

	@Test
	fun plusDifferentTokens() {
		resolver(
			token(begin("x")) to resolution(
				resolver(
					token(emptyEnd) to resolution(
						resolver(
							token(emptyEnd) to resolution(binding(value("x")))
						)
					)
				)
			)
		)
			.plus(
				resolver(
					token(begin("y")) to resolution(
						resolver(
							token(emptyEnd) to resolution(
								resolver(
									token(emptyEnd) to resolution(binding(value("y")))
								)
							)
						)
					)
				)
			)
			.assertEqualTo(
				resolver(
					token(begin("x")) to resolution(
						resolver(
							token(emptyEnd) to resolution(
								resolver(
									token(emptyEnd) to resolution(binding(value("x")))
								)
							)
						)
					),
					token(begin("y")) to resolution(
						resolver(
							token(emptyEnd) to resolution(
								resolver(
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
		resolver(
			token(begin("point")) to resolution(
				resolver(
					token(begin("x")) to resolution(
						resolver(
							token(emptyEnd) to resolution(
								resolver(
									token(emptyEnd) to resolution(
										resolver(
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
				resolver(
					token(begin("point")) to resolution(
						resolver(
							token(begin("y")) to resolution(
								resolver(
									token(emptyEnd) to resolution(
										resolver(
											token(emptyEnd) to resolution(
												resolver(
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
				resolver(
					token(begin("point")) to resolution(
						resolver(
							token(begin("x")) to resolution(
								resolver(
									token(emptyEnd) to resolution(
										resolver(
											token(emptyEnd) to resolution(
												resolver(
													token(emptyEnd) to resolution(binding(value("x")))
												)
											)
										)
									)
								)
							),
							token(begin("y")) to resolution(
								resolver(
									token(emptyEnd) to resolution(
										resolver(
											token(emptyEnd) to resolution(
												resolver(
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
		resolver(
			token(begin("x")) to resolution(
				resolver(
					token(emptyEnd) to resolution(
						resolver(
							token(emptyEnd) to resolution(binding(value("x")))
						)
					)
				)
			),
			token(emptyEnd) to resolution(binding(value("end")))
		)
			.plus(
				resolver(
					token(anyEnd) to resolution(
						resolver(
							token(emptyEnd) to resolution(binding(value("y")))
						)
					)
				)
			)
			.assertEqualTo(
				resolver(
					token(anyEnd) to resolution(
						resolver(
							token(emptyEnd) to resolution(binding(value("y")))
						)
					)
				)
			)
	}

	@Test
	fun switchOrNull() {
		resolver()
			.switchOrNull(
				value("shape" fieldTo value("circle" fieldTo value("radius" fieldTo value("zero")))),
				script(
					"circle" lineTo script(getName lineTo script("radius")),
					"rectangle" lineTo script(getName lineTo script("side"))
				)
			)
			.assertEqualTo(value("radius" fieldTo value("zero")))
	}
}