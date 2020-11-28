package leo23.term

import leo14.Number
import leo14.number
import leo23.term.type.ArrowType
import leo23.term.type.BooleanType
import leo23.term.type.ChoiceType
import leo23.term.type.NilType
import leo23.term.type.NumberType
import leo23.term.type.TextType
import leo23.term.type.TupleType
import leo23.term.type.Type
import leo23.term.type.booleanType
import leo23.term.type.checkEqual
import leo23.term.type.indexIn
import leo23.term.type.nilType
import leo23.term.type.numberType
import leo23.term.type.textType

val nilExpr: Expr get() = Expr(NilTerm, NilType)
fun expr(boolean: Boolean): Expr = Expr(BooleanTerm(boolean), BooleanType)
fun expr(string: String): Expr = Expr(StringTerm(string), TextType)
fun expr(int: Int): Expr = Expr(NumberTerm(int.number), NumberType)
fun expr(double: Double): Expr = Expr(NumberTerm(double.number), NumberType)
fun expr(number: Number): Expr = Expr(NumberTerm(number), NumberType)
val Expr.isNil: Expr get() = Expr(IsNilTerm(this), BooleanType)
fun Expr.numberPlus(rhs: Expr): Expr = Expr(NumberPlusTerm(check(numberType), rhs.check(numberType)), numberType)
fun Expr.numberMinus(rhs: Expr): Expr = Expr(NumberMinusTerm(check(numberType), rhs.check(numberType)), numberType)
fun Expr.numberTimes(rhs: Expr): Expr = Expr(NumberTimesTerm(check(numberType), rhs.check(numberType)), numberType)
fun Expr.numberEquals(rhs: Expr): Expr = Expr(NumberEqualsTerm(check(numberType), rhs.check(numberType)), booleanType)
val Expr.numberText: Expr get() = Expr(NumberStringTerm(check(numberType)), textType)
fun Expr.textAppend(rhs: Expr): Expr = Expr(StringAppendTerm(check(textType), rhs.check(textType)), textType)
fun Expr.textEquals(rhs: Expr): Expr = Expr(StringEqualsTerm(check(textType), rhs.check(textType)), booleanType)
val Expr.textNumberOrNil: Expr get() = Expr(StringNumberOrNilTerm(check(textType)), ChoiceType(listOf(numberType, nilType)))
fun tuple(vararg terms: Expr): Expr = Expr(TupleTerm(listOf(*terms)), TupleType(terms.map { it.type }))
fun Expr.tupleAt(index: Int): Expr = Expr(TupleAtTerm(this, index), (type as TupleType).itemTypes[index])
fun Expr.ifThenElse(trueTerm: Expr, falseTerm: Expr): Expr = Expr(ConditionalTerm(check(booleanType), trueTerm, falseTerm), trueTerm.type.checkEqual(falseTerm.type))
fun params(type: Type, vararg types: Type): List<Type> = listOf(type, *types)
fun List<Type>.does(body: Expr): Expr = Expr(FunctionTerm(this, body), ArrowType(this, body.type, false))
fun List<Type>.doesRecursively(body: Expr): Expr = Expr(RecursiveFunctionTerm(this, body), ArrowType(this, body.type, true))
fun Expr.apply(vararg params: Expr): Expr = Expr(ApplyTerm(this, listOf(*params).check((type as ArrowType).paramTypes)), type.returnType)
fun argExpr(index: Int, type: Type): Expr = Expr(VariableTerm(index), type)
infix fun Expr.cast(choiceType: Type): Expr = Expr(IndexedTerm(type.indexIn(choiceType), this), choiceType)
fun Expr.switch(vararg terms: Expr): Expr = Expr(SwitchTerm(this, listOf(*terms)), terms.map { it.type }.checkEqual)

fun argExpr0(type: Type): Expr = argExpr(0, type)
fun argExpr1(type: Type): Expr = argExpr(1, type)
fun argExpr2(type: Type): Expr = argExpr(2, type)
