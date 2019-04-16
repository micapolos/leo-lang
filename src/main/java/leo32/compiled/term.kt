package leo32.compiled

import leo.binary.Bit
import leo32.base.Branch
import leo32.base.at
import leo32.base.branch

sealed class Term

data class BitTerm(val bit: Bit) : Term()
data class CombineTerm(val branch: Branch<Term>) : Term()
data class InvokeTerm(val function: Term, val param: Term) : Term()
data class SelectTerm(val cases: Term, val selector: Term) : Term()
data class AtTerm(val term: Term, val bit: Bit) : Term()
object ArgumentTerm : Term()

fun Term.invoke(argument: Term): Term =
	when (this) {
		is ArgumentTerm -> argument
		is BitTerm -> this
		is CombineTerm -> CombineTerm(branch(branch.at0.invoke(argument), branch.at1.invoke(argument)))
		is InvokeTerm -> function.invoke(param.invoke(argument))
		is AtTerm -> (argument as CombineTerm).branch.at(bit)
		is SelectTerm -> (cases as CombineTerm).branch.at((selector.invoke(argument) as BitTerm).bit)
	}
