package leo7

sealed class Letter
data class ALetter(val a: A) : Letter()
data class BLetter(val b: B) : Letter()
data class CLetter(val c: C) : Letter()
data class DLetter(val d: D) : Letter()
data class ELetter(val e: E) : Letter()
data class FLetter(val f: F) : Letter()
data class GLetter(val g: G) : Letter()
data class HLetter(val h: H) : Letter()
data class ILetter(val i: I) : Letter()
data class JLetter(val j: J) : Letter()
data class KLetter(val k: K) : Letter()
data class LLetter(val l: L) : Letter()
data class MLetter(val m: M) : Letter()
data class NLetter(val n: N) : Letter()
data class OLetter(val o: O) : Letter()
data class PLetter(val p: P) : Letter()
data class QLetter(val q: Q) : Letter()
data class RLetter(val r: R) : Letter()
data class SLetter(val s: S) : Letter()
data class TLetter(val t: T) : Letter()
data class ULetter(val u: U) : Letter()
data class VLetter(val v: V) : Letter()
data class WLetter(val w: W) : Letter()
data class XLetter(val x: X) : Letter()
data class YLetter(val y: Y) : Letter()
data class ZLetter(val z: Z) : Letter()

val A.letter: Letter get() = ALetter(a)
val B.letter: Letter get() = BLetter(b)
val C.letter: Letter get() = CLetter(c)
val D.letter: Letter get() = DLetter(d)
val E.letter: Letter get() = ELetter(e)
val F.letter: Letter get() = FLetter(f)
val G.letter: Letter get() = GLetter(g)
val H.letter: Letter get() = HLetter(h)
val I.letter: Letter get() = ILetter(i)
val J.letter: Letter get() = JLetter(j)
val K.letter: Letter get() = KLetter(k)
val L.letter: Letter get() = LLetter(l)
val M.letter: Letter get() = MLetter(m)
val N.letter: Letter get() = NLetter(n)
val O.letter: Letter get() = OLetter(o)
val P.letter: Letter get() = PLetter(p)
val Q.letter: Letter get() = QLetter(q)
val R.letter: Letter get() = RLetter(r)
val S.letter: Letter get() = SLetter(s)
val T.letter: Letter get() = TLetter(t)
val U.letter: Letter get() = ULetter(u)
val V.letter: Letter get() = VLetter(v)
val W.letter: Letter get() = WLetter(w)
val X.letter: Letter get() = XLetter(x)
val Y.letter: Letter get() = YLetter(y)
val Z.letter: Letter get() = ZLetter(z)

val Letter.char
	get() = when (this) {
		is ALetter -> 'a'
		is BLetter -> 'b'
		is CLetter -> 'c'
		is DLetter -> 'd'
		is ELetter -> 'e'
		is FLetter -> 'f'
		is GLetter -> 'g'
		is HLetter -> 'h'
		is ILetter -> 'i'
		is JLetter -> 'j'
		is KLetter -> 'k'
		is LLetter -> 'l'
		is MLetter -> 'm'
		is NLetter -> 'n'
		is OLetter -> 'o'
		is PLetter -> 'p'
		is QLetter -> 'q'
		is RLetter -> 'r'
		is SLetter -> 's'
		is TLetter -> 't'
		is ULetter -> 'u'
		is VLetter -> 'v'
		is WLetter -> 'w'
		is XLetter -> 'x'
		is YLetter -> 'y'
		is ZLetter -> 'z'
	}

val Char.letterOrNull
	get() = when (this) {
		'a' -> a.letter
		'b' -> b.letter
		'c' -> c.letter
		'd' -> d.letter
		'e' -> e.letter
		'f' -> f.letter
		'g' -> g.letter
		'h' -> h.letter
		'i' -> i.letter
		'j' -> j.letter
		'k' -> k.letter
		'l' -> l.letter
		'm' -> m.letter
		'n' -> n.letter
		'o' -> o.letter
		'p' -> p.letter
		'q' -> q.letter
		'r' -> r.letter
		's' -> s.letter
		't' -> t.letter
		'u' -> u.letter
		'v' -> v.letter
		'w' -> w.letter
		'x' -> x.letter
		'y' -> y.letter
		'z' -> z.letter
		else -> null
	}
