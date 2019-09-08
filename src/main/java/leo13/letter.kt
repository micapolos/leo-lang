package leo13

import leo.base.appendableString

enum class Letter {
	A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;

	override fun toString() = appendableString { it.append(this) }
}

fun letter(char: Char) = enumValueOf<Letter>("$char".toUpperCase())
val Letter.char: Char get() = name.toLowerCase()[0]
fun Appendable.append(letter: Letter): Appendable = append(letter.char)
