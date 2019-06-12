package leo5.script

typealias Tekst = String

object Pusty

data class Słowo(val tekst: Tekst)
data class SłowoPierwsze(val słowo: Słowo)

data class Linia(val pierwsze: SłowoPierwsze, val pozostały: SkryptPozostały)
data class LiniaPierwsza(val linia: Linia)

sealed class Skrypt
data class PustySkrypt(val pusty: Pusty) : Skrypt()
data class NiepustySkrypt(val niepusty: SkryptNiepusty) : Skrypt()

data class SkryptPozostały(val skrypt: Skrypt)
data class SkryptNiepusty(val pierwsza: LiniaPierwsza, val dalszy: SkryptPozostały)

val pusty = Pusty

fun skrypt(pusty: Pusty): Skrypt = PustySkrypt(pusty)
fun skrypt(niepusty: SkryptNiepusty): Skrypt = NiepustySkrypt(niepusty)
fun niepusty(pierwsza: LiniaPierwsza, pozostały: SkryptPozostały) = SkryptNiepusty(pierwsza, pozostały)
fun pozostały(skrypt: Skrypt) = SkryptPozostały(skrypt)

fun linia(pierwsze: SłowoPierwsze, pozostały: SkryptPozostały) = Linia(pierwsze, pozostały)
fun pierwsza(linia: Linia) = LiniaPierwsza(linia)

fun słowo(tekst: Tekst) = Słowo(tekst)
fun pierwsze(słowo: Słowo) = SłowoPierwsze(słowo)

fun <R> Skrypt.jeśli(pusty: Pusty.() -> R, niepusty: SkryptNiepusty.() -> R) = when (this) {
	is PustySkrypt -> pusty(this.pusty)
	is NiepustySkrypt -> niepusty(this.niepusty)
}

val x = skrypt(pusty)
val y = skrypt(
	niepusty(
		pierwsza(linia(
			pierwsze(słowo("foo")),
			pozostały(skrypt(pusty)))),
		pozostały(skrypt(pusty))))
	.jeśli(pusty = { skrypt(pusty) }, niepusty = { skrypt(pusty) })
	.rozmiar

val Skrypt.rozmiar: Int
	get() = jeśli(
		pusty = { 0 },
		niepusty = { dalszy.skrypt.rozmiar })
