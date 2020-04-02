package leo14.lambda.java

import leo13.base.linesString
import leo14.lambda.*
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.code.inc

val Term.mainCode get() = code.mainCode

val String.mainCode
	get() =
		linesString(
			"import java.util.function.Function;",
			"@SuppressWarnings(\"ALL\")",
			"public final class Main {",
			"static Object fn(Function<Object, Object> fn) { return fn; }",
			"static Object apply(Object fn, Object arg) { return ((Function<Object, Object>) fn).apply(arg); }",
			"static void run(Object arg) { return; }",
			"static Object object(Runnable runnable) { runnable.run(); return null; }",
			"public static void main(String[] args) {",
			"run($this);",
			"}",
			"}"
		)

val Term.code get() = code(gen)
val Term.printCode get() = "object(() -> System.out.print($code))"

fun Term.code(gen: Gen): String =
	when (this) {
		is NativeTerm -> native.code(gen)
		is AbstractionTerm -> abstraction.code(gen)
		is ApplicationTerm -> application.code(gen)
		is VariableTerm -> variable.code(gen)
	}

fun Abstraction<Term>.code(gen: Gen) = "fn(${paramCode(gen)} -> ${gen.inc { body.code(it) }})"
fun Application<Term>.code(gen: Gen) = "apply(${lhs.code(gen)}, ${rhs.code(gen)})"
fun Variable<Native>.code(gen: Gen) = index(gen).varCode
fun Native.code(gen: Gen) = code.string

val Int.varCode
	get() =
		leo.base.failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

fun Variable<Native>.index(gen: Gen) =
	gen.depth - index - 1
