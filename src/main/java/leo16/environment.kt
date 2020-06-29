package leo16

import leo.base.notNullIf
import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.push
import leo.stak.top
import leo13.map
import leo13.mapFirst

data class Environment(val valueStak: Stak<Value>)

val emptyEnvironment: Environment = Environment(emptyStak())
operator fun Environment.plus(value: Value): Environment = Environment(valueStak.push(value))
operator fun Environment.get(index: Int): Value = valueStak.top(index)!!

fun Environment.eval(expression: Expression): Value =
	when (expression) {
		is ValueExpression -> expression.value
		is MakeExpression -> eval(expression.make)
		is ApplyExpression -> eval(expression.apply)
		is VariableExpression -> eval(expression.variable)
	}

fun Environment.eval(make: Make): Value =
	make.lineStack.map { eval(this) }.value

fun Environment.eval(apply: Apply): Value =
	eval(eval(apply.lhs), apply.op)

fun Environment.eval(variable: Variable): Value =
	get(variable.index)

fun Environment.eval(line: Line): Sentence =
	line.word sentenceTo eval(line.rhs)

fun Environment.eval(lhs: Value, op: Op): Value =
	when (op) {
		is GetOp -> eval(lhs, op.get)
		is SwitchOp -> eval(lhs, op.switch)
		is InvokeOp -> eval(lhs, op.invoke)
	}

fun eval(lhs: Value, get: Get): Value =
	lhs[get.word]

fun Environment.eval(lhs: Value, switch: Switch): Value =
	lhs.thingOrNull!!.onlySentenceOrNull!!.let { sentence ->
		switch.lineStack.mapFirst {
			notNullIf(sentence.word == word) {
				plus(value(sentence)).eval(rhs)
			}
		}!!
	}

fun Environment.eval(lhs: Value, invoke: Invoke): Value =
	eval(invoke.expression).let { rhs ->
		when (lhs) {
			EmptyValue -> null
			is LinkValue -> null
			is NativeValue -> nativeInvoke(lhs.native, rhs)
			is FunctionValue -> null
			is FuncValue -> invoke(lhs.func, rhs)
			is LazyValue -> null
		}!!
	}

fun Environment.eval(func: Func): Value =
	eval(func.expression)

fun Environment.invoke(func: Func, value: Value): Value =
	plus(value).eval(func)

@Suppress("UNCHECKED_CAST")
fun Environment.nativeInvoke(native: Any?, value: Value): Value =
	(native as (Environment.() -> Value)).invoke(plus(value))