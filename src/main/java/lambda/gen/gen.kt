package lambda.gen

import lambda.*
import leo.base.appendableString
import leo.base.runIf

fun Appendable.appendJs(term: Term, variableStringMap: MutableMap<Variable, Int>): Appendable =
	when (term) {
		is VariableTerm ->
			this
				.append("v")
				.append(variableStringMap[term.variable]?.toString() ?: error("Unbound ${term.variable}"))
		is ApplicationTerm ->
			this
				.appendJs(term.application.left, variableStringMap)
				.append('(')
				.appendJs(term.application.right, variableStringMap)
				.append(')')
		is FunctionTerm ->
			newVariable.let { variable ->
				val variableInt = variableStringMap.size
				variableStringMap[variable] = variableInt
				append("function(v$variableInt) { return ")
				appendJs(term.function(term(variable)), variableStringMap)
				append("; }")
				variableStringMap.remove(variable)
				this
			}
	}

fun Appendable.appendHaskell(term: Term, variableStringMap: MutableMap<Variable, Int>, parenthesis: Boolean = false): Appendable =
	this
		.runIf(parenthesis) { append('(') }
		.apply {
			when (term) {
				is VariableTerm ->
					this
						.append("v")
						.append(variableStringMap[term.variable]?.toString() ?: error("Unbound ${term.variable}"))
				is ApplicationTerm ->
					this
						.appendHaskell(term.application.left, variableStringMap, term.application.left is FunctionTerm)
						.append(' ')
						.appendHaskell(term.application.right, variableStringMap, term.application.right !is VariableTerm)
				is FunctionTerm ->
					newVariable.let { variable ->
						val variableInt = variableStringMap.size
						variableStringMap[variable] = variableInt
						append("\\v$variableInt -> ")
						val body = term.function(term(variable))
						appendHaskell(body, variableStringMap)
						variableStringMap.remove(variable)
						this
					}
			}
		}
		.runIf(parenthesis) { append(')') }

fun Appendable.appendKotlin(term: Term, variableStringMap: MutableMap<Variable, Int>): Appendable =
	when (term) {
		is VariableTerm ->
			this
				.append("v")
				.append(variableStringMap[term.variable]?.toString() ?: error("Unbound ${term.variable}"))
		is ApplicationTerm ->
			this
				.appendKotlin(term.application.left, variableStringMap)
				.append('(')
				.appendKotlin(term.application.right, variableStringMap)
				.append(')')
		is FunctionTerm ->
			newVariable.let { variable ->
				val variableInt = variableStringMap.size
				variableStringMap[variable] = variableInt
				append("term { v$variableInt -> ")
				appendKotlin(term.function(term(variable)), variableStringMap)
				append(" }")
				variableStringMap.remove(variable)
				this
			}
	}

fun Appendable.appendJava(term: Term, variableStringMap: MutableMap<Variable, Int>): Appendable =
	when (term) {
		is VariableTerm ->
			this
				.append("v")
				.append(variableStringMap[term.variable]?.toString() ?: error("Unbound ${term.variable}"))
		is ApplicationTerm ->
			this
				.appendJava(term.application.left, variableStringMap)
				.append(".apply(")
				.appendJava(term.application.right, variableStringMap)
				.append(')')
		is FunctionTerm ->
			newVariable.let { variable ->
				val variableInt = variableStringMap.size
				variableStringMap[variable] = variableInt
				append("term(v$variableInt -> ")
				appendJava(term.function(term(variable)), variableStringMap)
				append(")")
				variableStringMap.remove(variable)
				this
			}
	}

fun Appendable.appendLisp(term: Term, variableStringMap: MutableMap<Variable, Int>): Appendable =
	when (term) {
		is VariableTerm ->
			this
				.append("v")
				.append(variableStringMap[term.variable]?.toString() ?: error("Unbound ${term.variable}"))
		is ApplicationTerm ->
			this
				.append('(')
				.appendLisp(term.application.left, variableStringMap)
				.append(' ')
				.appendLisp(term.application.right, variableStringMap)
				.append(')')
		is FunctionTerm ->
			newVariable.let { variable ->
				val variableInt = variableStringMap.size
				variableStringMap[variable] = variableInt
				append("(lambda v$variableInt ")
				appendLisp(term.function(term(variable)), variableStringMap)
				append(")")
				variableStringMap.remove(variable)
				this
			}
	}

fun Appendable.appendLeo(term: Term, variableStringMap: MutableMap<Variable, Int>): Appendable =
	when (term) {
		is VariableTerm ->
			this
				.append("v")
				.append(variableStringMap[term.variable]?.toString() ?: error("Unbound ${term.variable}"))
		is ApplicationTerm ->
			this
				.appendLeo(term.application.left, variableStringMap)
				.append(" apply: ")
				.appendLeo(term.application.right, variableStringMap)
		is FunctionTerm ->
			newVariable.let { variable ->
				val variableInt = variableStringMap.size
				variableStringMap[variable] = variableInt
				append("v$variableInt is: ")
				appendLeo(term.function(term(variable)), variableStringMap)
				variableStringMap.remove(variable)
				this
			}
	}

val Term.js get() = appendableString { it.appendJs(this, mutableMapOf()) }
val Term.haskell get() = appendableString { it.appendHaskell(this, mutableMapOf()) }
val Term.kotlin get() = appendableString { it.appendKotlin(this, mutableMapOf()) }
val Term.java get() = appendableString { it.appendJava(this, mutableMapOf()) }
val Term.lisp get() = appendableString { it.appendLisp(this, mutableMapOf()) }
val Term.leo get() = appendableString { it.appendLeo(this, mutableMapOf()) }
