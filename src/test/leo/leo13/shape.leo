define: circle has: radius: float

define: rectangle has
	width: float
	height: float

define: shape has
	either: circle
	either: rectangle

define: circle area gives
	pi float
	times: given area circle radius float
	times: given area circle radius float

define: rectangle area gives
	given area rectangle width float
	times: given area rectangle height float
