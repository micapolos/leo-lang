package leo23.typed.term

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo13.combine
import leo13.map2OrNull
import leo23.term.Expr
import leo23.term.numberEquals
import leo23.term.numberMinus
import leo23.term.numberPlus
import leo23.term.numberText
import leo23.term.numberTimes
import leo23.term.textAppend
import leo23.term.textEquals
import leo23.type.booleanType
import leo23.type.numberEqualsTypeStack
import leo23.type.numberMinusTypeStack
import leo23.type.numberPlusTypeStack
import leo23.type.numberTextTypeStack
import leo23.type.numberTimesTypeStack
import leo23.type.numberType
import leo23.type.onlyNameOrNull
import leo23.type.textEqualsTypeStack
import leo23.type.textPlusTypeStack
import leo23.type.textType
import leo23.typed.of

val StackCompiled.resolve: StackCompiled
	get() = resolveOrNull?.stack ?: this

val StackCompiled.resolveOrNull: Compiled?
	get() =
		null
			?: resolveNumberPlusOrNull
			?: resolveNumberMinusOrNull
			?: resolveNumberTimesOrNull
			?: resolveNumberEqualsOrNull
			?: resolveNumberTextOrNull
			?: resolveTextPlusOrNull
			?: resolveTextEqualsOrNull
			?: resolveGetOrNull
			?: resolveMakeOrNull

val StackCompiled.resolveGetOrNull: Compiled?
	get() =
		linkOrNull?.combine { rhsCompiled ->
			rhsCompiled.t.onlyNameOrNull?.let { name ->
				onlyOrNull?.getOrNull(name)
			}
		}

val StackCompiled.resolveMakeOrNull: Compiled?
	get() =
		linkOrNull?.combine { rhsCompiled ->
			rhsCompiled.t.onlyNameOrNull?.let { name ->
				make(name)
			}
		}

val StackCompiled.resolveNumberPlusOrNull: Compiled?
	get() =
		ifOrNull(t == numberPlusTypeStack) {
			v.map2OrNull(Expr::numberPlus)!!.of(numberType)
		}

val StackCompiled.resolveNumberMinusOrNull: Compiled?
	get() =
		ifOrNull(t == numberMinusTypeStack) {
			v.map2OrNull(Expr::numberMinus)!!.of(numberType)
		}

val StackCompiled.resolveNumberTimesOrNull: Compiled?
	get() =
		ifOrNull(t == numberTimesTypeStack) {
			v.map2OrNull(Expr::numberTimes)!!.of(numberType)
		}

val StackCompiled.resolveNumberEqualsOrNull: Compiled?
	get() =
		ifOrNull(t == numberEqualsTypeStack) {
			v.map2OrNull(Expr::numberEquals)!!.of(booleanType)
		}

val StackCompiled.resolveNumberTextOrNull: Compiled?
	get() =
		ifOrNull(t == numberTextTypeStack) {
			v.map2OrNull { lhs, rhs -> lhs.numberText }!!.of(textType)
		}

val StackCompiled.resolveTextPlusOrNull: Compiled?
	get() =
		ifOrNull(t == textPlusTypeStack) {
			v.map2OrNull(Expr::textAppend)!!.of(textType)
		}

val StackCompiled.resolveTextEqualsOrNull: Compiled?
	get() =
		ifOrNull(t == textEqualsTypeStack) {
			v.map2OrNull(Expr::textEquals)!!.of(booleanType)
		}
