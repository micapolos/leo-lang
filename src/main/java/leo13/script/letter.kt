package leo13.script

import leo13.Scripting
import leo13.letterName

enum class Letter : Scripting {
	A {
		override fun toString() = scriptingLine.toString()
	},
	B {
		override fun toString() = scriptingLine.toString()
	},
	C {
		override fun toString() = scriptingLine.toString()
	},
	D {
		override fun toString() = scriptingLine.toString()
	},
	E {
		override fun toString() = scriptingLine.toString()
	},
	F {
		override fun toString() = scriptingLine.toString()
	},
	G {
		override fun toString() = scriptingLine.toString()
	},
	H {
		override fun toString() = scriptingLine.toString()
	},
	I {
		override fun toString() = scriptingLine.toString()
	},
	J {
		override fun toString() = scriptingLine.toString()
	},
	K {
		override fun toString() = scriptingLine.toString()
	},
	L {
		override fun toString() = scriptingLine.toString()
	},
	M {
		override fun toString() = scriptingLine.toString()
	},
	N {
		override fun toString() = scriptingLine.toString()
	},
	O {
		override fun toString() = scriptingLine.toString()
	},
	P {
		override fun toString() = scriptingLine.toString()
	},
	Q {
		override fun toString() = scriptingLine.toString()
	},
	R {
		override fun toString() = scriptingLine.toString()
	},
	S {
		override fun toString() = scriptingLine.toString()
	},
	T {
		override fun toString() = scriptingLine.toString()
	},
	U {
		override fun toString() = scriptingLine.toString()
	},
	V {
		override fun toString() = scriptingLine.toString()
	},
	W {
		override fun toString() = scriptingLine.toString()
	},
	X {
		override fun toString() = scriptingLine.toString()
	},
	Y {
		override fun toString() = scriptingLine.toString()
	},
	Z {
		override fun toString() = scriptingLine.toString()
	};

	override val scriptingLine
		get() =
			letterName lineTo script(nameString)

	val char get() = name[0].toLowerCase()
	val nameString get() = name.toLowerCase()
}

fun letter(char: Char): Letter = Letter.valueOf(char.toString().toUpperCase())
