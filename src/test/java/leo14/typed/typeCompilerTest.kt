package leo14.typed

import leo.base.assertEqualTo
import leo14.compile
import leo14.lineTo
import leo14.resultCompiler
import leo14.script
import kotlin.test.Test

class TypeCompilerTest {
	@Test
	fun empty() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script())
			.assertEqualTo(emptyType)
	}

	@Test
	fun native() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script("native" lineTo script()))
			.assertEqualTo(nativeType)
	}

	@Test
	fun simple() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script("foo" lineTo script()))
			.assertEqualTo(type("foo" fieldTo type()))
	}

	@Test
	fun field() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script("foo" lineTo script("bar" lineTo script())))
			.assertEqualTo(type("foo" fieldTo type("bar" fieldTo type())))
	}

	@Test
	fun fields() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script(
				"x" lineTo script("zero" lineTo script()),
				"y" lineTo script("one" lineTo script())))
			.assertEqualTo(type(
				"x" fieldTo type("zero" fieldTo type()),
				"y" fieldTo type("one" fieldTo type())))
	}

	@Test
	fun noChoice() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script("choice" lineTo script()))
			.assertEqualTo(type(line(choice())))
	}

	@Test
	fun singleChoice() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script("choice" lineTo script("foo" lineTo script())))
			.assertEqualTo(type(line(choice("foo" fieldTo type()))))
	}

	@Test
	fun choices() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script("choice" lineTo script(
				"foo" lineTo script(),
				"bar" lineTo script())))
			.assertEqualTo(type(line(choice(
				"foo" fieldTo type(),
				"bar" fieldTo type()))))
	}

	@Test
	fun function() {
		emptyType
			.plusCompiler { resultCompiler(it) }
			.compile<Type>(script("function" lineTo script(
				"from" lineTo script("foo" lineTo script()),
				"to" lineTo script("bar" lineTo script()))))
			.assertEqualTo(type(line(
				type("foo" fieldTo type()) arrowTo type("bar" fieldTo type()))))
	}
}