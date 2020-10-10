package leo19.term

sealed class Term
data class IntTerm(val int: Int) : Term()
data class TupleTerm(val list: List<Term>) : Term()
data class TupleGetTerm(val tuple: Term, val index: Term) : Term()
data class FunctionTerm(val function: Function) : Term()
data class InvokeTerm(val function: Term, val param: Term) : Term()
data class VariableTerm(val variable: Variable) : Term()

data class Variable(val index: Int)
data class Function(val body: Term)

fun term(int: Int): Term = IntTerm(int)
fun term(vararg terms: Term): Term = TupleTerm(terms.toList())
fun Term.get(index: Term): Term = TupleGetTerm(this, index)
fun term(function: Function): Term = FunctionTerm(function)
fun Term.invoke(term: Term): Term = InvokeTerm(this, term)
fun term(variable: Variable): Term = VariableTerm(variable)
fun variable(index: Int) = Variable(index)
fun function(body: Term) = Function(body)