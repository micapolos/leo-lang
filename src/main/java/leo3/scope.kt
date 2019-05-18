package leo3

import leo.base.Empty
import leo.binary.Bit
import leo32.base.Branch
import leo32.base.at
import leo32.base.branch

data class Scope(
	val matchOrNullBranch: Branch<Match?>)

val Empty.scope
	get() = Scope(branch<Match?>(null, null))

fun Scope.matchAt(bit: Bit): Match? =
	matchOrNullBranch.at(bit)

