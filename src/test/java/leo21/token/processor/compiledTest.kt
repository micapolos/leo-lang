package leo21.token.processor

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.compiled.compiled
import leo21.compiled.lineTo
import leo21.compiled.of
import leo21.compiler.Compiler
import leo21.compiler.emptyBindings
import leo21.prim.Prim
import leo21.prim.prim
import leo21.token.compiler.TokenCompiler
import leo21.type.doubleType
import leo21.type.lineTo
import leo21.type.type
import kotlin.test.Test

class CompiledTest {
	@Test
	fun literal() {
		emptyCompilerTokenProcessor
			.plus(script(literal(10.0)))
			.assertEqualTo(
				CompilerTokenProcessor(
					TokenCompiler(
						null,
						Compiler(
							emptyBindings,
							compiled(10.0))))
			)
	}

	@Test
	fun name() {
		emptyCompilerTokenProcessor
			.plus(script("zero"))
			.assertEqualTo(
				CompilerTokenProcessor(
					TokenCompiler(
						null,
						Compiler(
							emptyBindings,
							compiled("zero" lineTo compiled()))))
			)
	}

	@Test
	fun struct() {
		emptyCompilerTokenProcessor
			.plus(
				script(
					"x" lineTo script(literal(10.0)),
					"y" lineTo script(literal(20.0))))
			.assertEqualTo(
				CompilerTokenProcessor(
					TokenCompiler(
						null,
						Compiler(
							emptyBindings,
							compiled(
								"x" lineTo compiled(10.0),
								"y" lineTo compiled(20.0)))))
			)
	}

	@Test
	fun do_() {
		script(
			"x" lineTo script(literal(10.0)),
			"do" lineTo script("given"))
			.compiled
			.assertEqualTo(
				fn(arg<Prim>(0)).invoke(nativeTerm(prim(10)))
					.of(type("given" lineTo type("x" lineTo doubleType))))
	}
}