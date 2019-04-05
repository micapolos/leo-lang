package leo32.dsl

import leo32.runtime.TermField
import leo32.runtime.term
import leo32.runtime.to

val one get() = "one" to term()
val zero get() = "zero" to term()

fun bit(vararg fields: TermField) = "bit" to term(*fields)
fun byte(vararg fields: TermField) = "byte" to term(*fields)
fun center(vararg fields: TermField) = "center" to term(*fields)
fun circle(vararg fields: TermField) = "circle" to term(*fields)
fun either(vararg fields: TermField) = "either" to term(*fields)
fun not(vararg fields: TermField) = "not" to term(*fields)
fun _false(vararg fields: TermField) = "false" to term(*fields)
fun plus(vararg fields: TermField) = "plus" to term(*fields)
fun radius(vararg fields: TermField) = "radius" to term(*fields)
fun _true(vararg fields: TermField) = "true" to term(*fields)
fun x(vararg fields: TermField) = "x" to term(*fields)
fun y(vararg fields: TermField) = "y" to term(*fields)

fun i32(int: Int) = int.toString() to term() // TODO: Make it a primitive value
