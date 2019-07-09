def height = inchesToMilli(1)
def tractionWheelRadius = inchesToMilli(4/2)
def omniWheelRadius = inchesToMilli(4.125/2)
def largeGearRadius = inchesToMilli((3+9/16)/2)
def thumbRadius = 10
def wallSize = 20

CSG outline = new Cube(inchesToMilli(12),// X dimention
			inchesToMilli(18),// Y dimention
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

CSG tractionWheelHole = new Cube(85,
						   20,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()

CSG omniWheelHole = new Cube(85,
						   26,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()
						   
CSG largeGearHole = new Cube(80,
						   10,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()
						   
CSG largeSprocketHole = new Cube(85,
						   5,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()
						   
CSG smallSprocketHole = new Cube(74,
						   5,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()						   				   

def addThumbHole(def part, def thumb){
	CSG newCyl = thumb.movex(part.getMaxX()).movey((part.getMinY()+part.getMaxY())/2)
	thumb = thumb.movex(part.getMinX()).movey((part.getMinY()+part.getMaxY())/2)
	return CSG.unionAll([newCyl, thumb, part])
}

def inchesToMilli(def inches){
	return inches*25.4
}

def moveDiagonal(def angle, def distance, def part){
	return part.movex(distance*Math.sin(Math.toRadians(angle)))
			 .movey(distance*Math.cos(Math.toRadians(angle)))
}

CSG Board = outline
CSG cutout = tractionWheelHole

for(int i = 0; i<8; i++){
	cutout = tractionWheelHole.movex(wallSize).movey(wallSize + i*(wallSize+tractionWheelHole.getMaxY()))
	if(i>3){
		cutout = omniWheelHole.movex(wallSize).movey(wallSize + i*(wallSize+omniWheelHole.getMaxY()))
	}
	Board = Board.difference(cutout)
}

for(int i = 0; i<12; i++){
	cutout = largeGearHole.movex(wallSize*3+omniWheelHole.getMaxX()).movey(wallSize + i*(wallSize+largeGearHole.getMaxY()))
	if(i>7){
		cutout = largeSprocketHole.movex(wallSize*3+omniWheelHole.getMaxX()).movey(2*(wallSize + largeGearHole.getMaxY()) + i*(wallSize+largeSprocketHole.getMaxY()))
	}
	if(i>9){
		cutout = smallSprocketHole.movex(wallSize*3+omniWheelHole.getMaxX()).movey(2*(wallSize + largeGearHole.getMaxY()) + i*(wallSize+smallSprocketHole.getMaxY()))
	}
	Board = Board.difference(cutout)	    
}

return Board