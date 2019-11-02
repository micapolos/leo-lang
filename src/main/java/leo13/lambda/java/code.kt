package leo13.lambda.java

import leo13.base.linesString
import leo13.lambda.*
import leo13.lambda.code.Gen
import leo13.lambda.code.gen
import leo13.lambda.code.inc

val Value.mainCode get() = code.mainCode

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

val Value.code get() = code(gen)
val Value.printCode get() = "object(() -> System.out.print($code))"

fun Value.code(gen: Gen): String =
	when (this) {
		is NativeValue -> native.code(gen)
		is AbstractionValue -> abstraction.code(gen)
		is ApplicationValue -> application.code(gen)
		is VariableValue -> variable.code(gen)
	}

fun Abstraction<Value>.code(gen: Gen) = "fn(${paramCode(gen)} -> ${gen.inc { body.code(it) }})"
fun Application<Value>.code(gen: Gen) = "apply(${lhs.code(gen)}, ${rhs.code(gen)})"
fun Variable<Native>.code(gen: Gen) = index(gen).varCode
fun Native.code(gen: Gen) = code.string

val Int.varCode
	get() =
		leo.base.failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

fun Variable<Native>.index(gen: Gen) =
	gen.depth - index - 1
