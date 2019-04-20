package leo32.runtime.v2

import leo.base.*
import leo32.base.seq
import leo32.runtime.append

fun Appendable.append(script: Script): Appendable =
	append(script.block, 0.indent)

fun Appendable.append(block: Block, indent: Indent): Appendable =
	when (block) {
		is SimpleBlock -> append(block.primitive, indent)
		is ComplexBlock -> append(block.complex, indent)
	}

fun Appendable.appendWhitespaces(block: Block, indent: Indent): Appendable =
	when (block) {
		is SimpleBlock -> append(' ')
		is ComplexBlock -> append('\n').append(indent)
	}

fun Appendable.append(field: Field, indent: Indent): Appendable = this
	.append(field.symbol)
	.appendWhitespaces(field.script.block, field.script.block.childIndent(indent))
	.append(field.script.block, field.script.block.childIndent(indent))

fun Appendable.append(primitive: Primitive, indent: Indent): Appendable =
	when (primitive) {
		is IntPrimitive -> append("int ${primitive.int}")
		is StringPrimitive -> append("\"${primitive.string}\"") // TODO: Escape
		is SymbolPrimitive -> append(primitive.symbol)
		is FieldPrimitive -> append(primitive.field, indent)
	}

fun Appendable.append(complex: Complex, indent: Indent): Appendable =
	this
		.append(complex.primitive, indent)
		.append('\n')
		.append(indent)
		.append(complex.firstField, indent)
		.fold(complex.remainingFieldList.seq) {
			append('\n').append(indent).append(it, indent)
		}

fun Block.childIndent(indent: Indent): Indent =
	when (this) {
		is SimpleBlock -> indent
		is ComplexBlock -> indent.inc
	}

