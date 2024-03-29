import eu.mihosoft.vrl.v3d.svg.*;
import eu.mihosoft.vrl.v3d.Extrude;

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
						   21,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()

CSG omniWheelHole = new Cube(86,
						   26,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()
						   
CSG largeGearHole = new Cube(80,
						   12,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()
						   
CSG largeSprocketHole = new Cube(85,
						   6,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()
						   
CSG smallSprocketHole = new Cube(74,
						   6,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()	
						   
CSG plateHole = new Cube(6,
						   318,
						   height)
						   .toCSG()
						   .toYMin()
						   .toXMin()
						   .toZMin()
						   
CSG battery = CSG.unionAll([new Cube(100,28,height).toCSG().toYMin().toXMax().toZMin(), 
					   new Cube (161, 23, height).toCSG().toYMin().toXMax().toZMin()])

CSG vexNet = CSG.unionAll([new Cube(50,20,height).toCSG().toYMin().toXMax().toZMin(), 
					  new Cube (85, 12, height).toCSG().toYMin().toXMax().toZMin(),
					  new Cylinder(thumbRadius, thumbRadius, height, (int)30).toCSG().movex(-50).movey(20)])


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CSG boardLarge = new Cube(inchesToMilli(22.5),inchesToMilli(16.75),height).toCSG().toXMin().toYMin().toZMin()						   

CSG brain = new Cube(100, 138, height).toCSG().rotz(90).toYMin().toXMax().toZMin()
									 .movex(boardLarge.getMaxX() - wallSize).movey(wallSize)

CSG twoByMetal = addThumbHole(CSG.unionAll([new Cube(445, 77, height).toCSG().toYMin().toXMax().toZMin(),
						 new Cube(317, 15, height).toCSG().toYMax().toXMax().toZMin()]),
						 thumbCylinder).toYMin().toXMax().toZMin()

CSG threeByMetal = addThumbHole(new Cube(39, 445, height).toCSG().toYMin().toXMax().toZMin(), thumbCylinder).rotz(90).toYMin().toXMax().toZMin() 

CSG longL = new Cube (445,2.5,height).toCSG().toYMin().toXMax().toZMin()

def addThumbHole(def part, def thumb){
	CSG newCyl = thumb.movey(part.getMaxY()).movex((part.getMinX()+part.getMaxX())/2)
	thumb = thumb.movey(part.getMinY()).movex((part.getMinX()+part.getMaxX())/2)
	return CSG.unionAll([newCyl, thumb, part])
}

def inchesToMilli(def inches){
	return inches*25.4
}

def moveDiagonal(def angle, def distance, def part){
	return part.movex(distance*Math.sin(Math.toRadians(angle)))
			 .movey(distance*Math.cos(Math.toRadians(angle)))
}

CSG board = outline
CSG cutout = tractionWheelHole

for(int i = 0; i<8; i++){
	cutout = tractionWheelHole.movex(wallSize).movey(wallSize + i*(wallSize+tractionWheelHole.getMaxY()))
	if(i>3){
		cutout = omniWheelHole.movex(wallSize).movey(wallSize + i*(wallSize+omniWheelHole.getMaxY()))
	}
	board = board.difference(cutout)
}

cutout = vexNet.movex(-1 * vexNet.getMinX() + wallSize).movey(cutout.getMaxY() + wallSize)
board = board.difference(cutout)

for(int i = 0; i<12; i++){
	cutout = largeGearHole.movex(wallSize*3+omniWheelHole.getMaxX())
					  .movey(wallSize + i*(wallSize+largeGearHole.getMaxY()))
	if(i>7){
		cutout = largeSprocketHole.movex(wallSize*3+omniWheelHole.getMaxX())
							 .movey(2*(wallSize + largeGearHole.getMaxY()) + i*(wallSize+largeSprocketHole.getMaxY()))
	}
	if(i>9){
		cutout = smallSprocketHole.movex(wallSize*3+omniWheelHole.getMaxX())
							 .movey(2*(wallSize + largeGearHole.getMaxY()) + i*(wallSize+smallSprocketHole.getMaxY()))
	}
	board = board.difference(cutout)	    
}

cutout = plateHole.movex(wallSize*4 + omniWheelHole.getMaxX() + largeSprocketHole.getMaxX()).movey(wallSize)
board = board.difference(cutout)
cutout = cutout.movex(plateHole.getMaxX() + wallSize)
board = board.difference(cutout)
cutout = battery.movex(cutout.getMaxX()).movey(cutout.getMaxY() + wallSize + 20)
board = board.difference(cutout)
board = board.toXMax().toYMax()

File f = ScriptingEngine
	.fileFromGit(
		"https://gist.github.com/30402ce0a4ce4053c2abce63bafc1de2.git",//git repo URL
		"master",//branch
		"vexV5Controller.svg"// File from within the Git repo
	)
SVGLoad s = new SVGLoad(f.toURI())
def controllerSVG = s.extrude(height,0.01).get(0)
CSG controller = controllerSVG
controller = controller.rotz(-155).toXMin().toYMin()
		   .movex(wallSize).movey(wallSize)
boardLarge = boardLarge.difference(controller)
boardLarge = boardLarge.difference(brain)
cutout = twoByMetal.movex(boardLarge.getMaxX() - wallSize).movey(brain.getMaxY() + wallSize)
boardLarge = boardLarge.difference(cutout)
cutout = cutout.movey(10)
for(int i = 0; i<3; i++){
	cutout = threeByMetal.movex(boardLarge.getMaxX() - wallSize).movey(cutout.getMaxY() + 0)
	boardLarge = boardLarge.difference(cutout)
}
cutout = longL.movex(boardLarge.getMaxX() - wallSize).movey(boardLarge.getMaxY() - wallSize)
boardLarge = boardLarge.difference(cutout)

return boardLarge
	   

