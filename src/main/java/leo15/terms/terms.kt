package leo15.terms

import leo15.lambda.*

typealias Fn = (Term) -> Term

data class Switch(val term: Term)

fun Term.switch(f: Fn): Switch = Switch(invoke(lambda(f)))
fun Switch.otherwise(f: Fn) = term.invoke(lambda(f))

// primitives
val nil = value(null)
val Int.term get() = valueTerm
val String.term get() = valueTerm

fun Term.ifIntZero(f: Fn): Switch = apply { if (value as Int == 0) nil.firstOr else nil.secondOr }.switch(f)
fun Term.intPlus(rhs: Term): Term = valueApply(rhs) { (this as Int).plus(it as Int) }
fun Term.intMinus(rhs: Term): Term = valueApply(rhs) { (this as Int).minus(it as Int) }
fun Term.intTimes(rhs: Term): Term = valueApply(rhs) { (this as Int).times(it as Int) }
val Term.intString: Term get() = valueApply { (this as Int).toString() }

fun Term.stringPlus(rhs: Term): Term = valueApply(rhs) { (this as String).plus(it as String) }
val Term.stringLength: Term get() = valueApply { (this as String).length }

// pair
infix fun Term.and(rhs: Term): Term = pairTerm.invoke(this).invoke(rhs)
val Term.first: Term get() = invoke(firstTerm)
val Term.second: Term get() = invoke(secondTerm)

// either
val Term.firstOr: Term get() = choiceTerm(size = 2, index = 1, term = this)
val Term.secondOr: Term get() = choiceTerm(size = 2, index = 0, term = this)
fun Term.ifFirst(f: Fn): Switch = switch(f)
fun Switch.orSecond(f: Fn) = otherwise(f)

// optional
val absent: Term get() = nil.firstOr
val Term.present: Term get() = secondOr
fun Term.ifAbsent(fn: Fn) = ifFirst(fn)
fun Switch.orPresent(fn: Fn) = orSecond(fn)

// link
infix fun Term.linkTo(head: Term): Term = this.and(head)
val Term.tail: Term get() = first
val Term.head: Term get() = second

// list
val empty = absent
infix fun Term.append(head: Term): Term = this.linkTo(head).present
fun Term.ifEmpty(fn: Fn) = ifAbsent(fn)
fun Switch.orLink(fn: Fn) = orPresent(fn)

// recursion
fun Term.fix(fn: Term.(Term) -> Term): Term =
	TODO()