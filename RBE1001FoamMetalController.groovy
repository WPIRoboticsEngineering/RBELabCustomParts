def height = inchesToMilli(1)
def tractionWheelRadius = inchesToMilli(4/2)
def omniWheelRadius = inchesToMilli(4.125/2)
def largeGearRadius = inchesToMilli((3+9/16)/2)
def thumbRadius = 10

CSG outline = new Cube(inchesToMilli(16),// X dimention
			inchesToMilli(23),// Y dimention
			height//  Z dimention
			).toCSG()// this converts from the geometry to an object we can work with
			.toYMin()
			.toXMin()
			.toZMin()

CSG gearCylinder = new Cylinder(largeGearRadius, // Radius at the bottom
                      		largeGearRadius, // Radius at the top
                      		height, // Height
                      		(int)30 //resolution
                      		).toCSG()//convert to CSG to display                    			         ).toCSG()//convert to CSG to display 
                      		

CSG thumbCylinder = new Cylinder(thumbRadius, // Radius at the bottom
                      		thumbRadius, // Radius at the top
                      		height, // Height
                      		(int)30 //resolution
                      		).toCSG()//convert to CSG to display                    			         ).toCSG()//convert to CSG to display 
                      		

def addThumbHole(def part, def thumb){
	CSG newCyl = thumb.movex(part.getMaxX()).movey((part.getMinY()+part.getMaxY())/2)
	thumb = thumb.movex(part.getMinX()).movey((part.getMinY()+part.getMaxY())/2)
	return CSG.unionAll([newCyl, thumb, part])
}

def inchesToMilli(def inches){
	return inches*25.4
}


CSG cutout = addThumbHole(gearCylinder, thumbCylinder).toYMin().toXMin().movex(5).movey(5)
CSG Board = outline.difference(cutout)
return Board
