package leo19.term

import leo13.Stack
import leo13.push
import leo13.stack

sealed class Term
object NullTerm : Term()
data class IntTerm(val int: Int) : Term()
data class ArrayTerm(val stack: Stack<Term>) : Term()
data class ArrayGetTerm(val tuple: Term, val index: Term) : Term()
data class FunctionTerm(val function: Function) : Term()
data class InvokeTerm(val function: Term, val param: Term) : Term()
data class VariableTerm(val variable: Variable) : Term()

data class Variable(val index: Int)
data class Function(val body: Term)

val nullTerm: Term = NullTerm
fun term(int: Int): Term = IntTerm(int)
fun term(vararg terms: Term): Term = ArrayTerm(stack(*terms))
fun Term.get(index: Term): Term = ArrayGetTerm(this, index)
fun term(function: Function): Term = FunctionTerm(function)
fun Term.invoke(term: Term): Term = InvokeTerm(this, term)
fun term(variable: Variable): Term = VariableTerm(variable)

fun variable(index: Int) = Variable(index)
fun function(body: Term) = Function(body)

fun Term.plus(term: Term) = ArrayTerm((this as ArrayTerm).stack.push(term))