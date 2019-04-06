package leo32.dsl

import leo32.runtime.TermField
import leo32.runtime.invoke
import leo32.runtime.term
import leo32.runtime.to

fun bit(vararg fields: TermField) = "bit" to invoke(*fields)
fun byte(vararg fields: TermField) = "byte" to invoke(*fields)
fun center(vararg fields: TermField) = "center" to term(*fields)
fun circle(vararg fields: TermField) = "circle" to term(*fields)
fun either(vararg fields: TermField) = "either" to term(*fields)
fun not(vararg fields: TermField) = "not" to term(*fields)
fun _false(vararg fields: TermField) = "false" to term(*fields)
fun one(vararg fields: TermField) = "one" to term(*fields)
fun plus(vararg fields: TermField) = "plus" to term(*fields)
fun radius(vararg fields: TermField) = "radius" to term(*fields)
fun _true(vararg fields: TermField) = "true" to term(*fields)
fun vec(vararg fields: TermField) = "vec" to term(*fields)
fun w(vararg fields: TermField) = "w" to term(*fields)
fun x(vararg fields: TermField) = "x" to term(*fields)
fun y(vararg fields: TermField) = "y" to term(*fields)
fun z(vararg fields: TermField) = "z" to term(*fields)
fun zero(vararg fields: TermField) = "zero" to term(*fields)

fun i32(int: Int) = int.toString() to term() // TODO: Make it a primitive value
