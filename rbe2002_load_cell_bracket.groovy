def bracket = new Cube(45,45,2).toCSG()
				.toZMin()
				.toXMin()
				.movex(-3)

def bolt = new Cylinder(1.6,2).toCSG()

def bolts = bolt.movey(3).union(bolt.movey(-3))

return [bracket.difference(bolts)]