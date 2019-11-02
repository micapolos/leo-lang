package leo13.lambda.java

import leo13.base.linesString
import leo13.lambda.*

data class Code(val string: String)

fun code(string: String) = Code(string)

val JavaExpr.mainCode get() = code.mainCode

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

val JavaExpr.code get() = code(gen)
val JavaExpr.printCode get() = "object(() -> System.out.print($code))"

fun JavaExpr.code(gen: Gen): String =
	when (this) {
		is ValueExpr -> value.code.string
		is AbstractionExpr -> abstraction.code(gen)
		is ApplicationExpr -> application.code(gen)
		is VariableExpr -> variable.code(gen)
	}

fun JavaAbstraction.code(gen: Gen) = "fn(${paramCode(gen)} -> ${gen.inc { body.code(it) }})"
fun JavaApplication.code(gen: Gen) = "apply(${lhs.code(gen)}, ${rhs.code(gen)})"
fun JavaVariable.code(gen: Gen) = index(gen).varCode

val Int.varCode
	get() =
		leo.base.failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

val arg = variable<Java>()
