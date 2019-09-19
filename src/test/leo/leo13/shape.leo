define: circle has: radius: float

define: rectangle has
	width: float
	height: float

define: shape has
	either: circle
	either: rectangle

define
	area: circle
	gives
		pi float
		times: given area circle radius float
		times: given area circle radius float

define
	area: rectangle
	gives
		given area rectangle width float
		times: given area rectangle height float
