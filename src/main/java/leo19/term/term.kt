package leo19.term

import leo13.Stack
import leo13.push
import leo13.stack

sealed class Term
object NullTerm : Term()
data class IntTerm(val int: Int) : Term()
data class PairTerm(val lhs: Term, val rhs: Term) : Term()
data class LhsTerm(val pair: Term) : Term()
data class RhsTerm(val pair: Term) : Term()
data class ArrayTerm(val stack: Stack<Term>) : Term()
data class ArrayGetTerm(val tuple: Term, val index: Term) : Term()
data class FunctionTerm(val function: Function) : Term()
data class InvokeTerm(val function: Term, val param: Term) : Term()
data class VariableTerm(val variable: Variable) : Term()
data class EqualsTerm(val lhs: Term, val rhs: Term) : Term()

data class Variable(val index: Int)
data class Function(val body: Term, val isRecursive: Boolean)

val nullTerm: Term = NullTerm
fun term(int: Int): Term = IntTerm(int)
fun term(vararg terms: Term): Term = ArrayTerm(stack(*terms))
fun Term.to(rhs: Term): Term = PairTerm(this, rhs)
val Term.lhs: Term get() = LhsTerm(this)
val Term.rhs: Term get() = RhsTerm(this)
fun Term.get(index: Term): Term = ArrayGetTerm(this, index)
fun term(function: Function): Term = FunctionTerm(function)
fun Term.invoke(term: Term): Term = InvokeTerm(this, term)
fun term(variable: Variable): Term = VariableTerm(variable)
fun Term.termEquals(rhs: Term): Term = EqualsTerm(this, rhs)

fun variable(index: Int) = Variable(index)
fun function(body: Term, isRecursive: Boolean) = Function(body, isRecursive)
fun function(body: Term) = function(body, isRecursive = false)
fun recursiveFunction(body: Term) = function(body, isRecursive = true)

fun Term.plus(term: Term) = ArrayTerm((this as ArrayTerm).stack.push(term))