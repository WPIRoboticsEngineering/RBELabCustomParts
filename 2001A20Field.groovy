//Your code here

double plateThickness = 0.25*25.4

double roofAngleOne =25
double roofAngleTwo = 45
double blockSize = 20
double houseWidth = 8*25.4
double mountBoltWidth = 2.5*25.4
double mopuntBoltDown = 5.0/8.0*25.4
double hypotOfRoof = 3.1*25.4
double totalDepthOfBuilding = 5.0*25.4
double biteDepthForRoof = 0.5 *25.4
double biteWidth= 2.75*25.4
double buildingHeight = 9.75*25.4

def pickup = new Cube(3*25.4,3*25.4,2.5*25.4).toCSG()
			.toZMin()
			.movex(100)


def wedgeOne = new Wedge(blockSize/Math.tan(Math.toRadians(roofAngleOne)),
					blockSize,
					blockSize 
					).toCSG()
def wedgeTwo = new Wedge(blockSize/Math.tan(Math.toRadians(roofAngleTwo)),
					blockSize,
					blockSize 
					).toCSG()
					.rotz(180)
					.toYMin()
def block = wedgeOne.union(wedgeTwo)

def boltLocations =[
 	new Transform()
	.movex(blockSize/3)
	.roty(-roofAngleOne)
	.movez(blockSize)
	.movey(blockSize/3),
	new Transform()
	.movex(-blockSize/3)
	.roty(roofAngleTwo)
	.movez(blockSize)
	.movey(blockSize/3*2),
	new Transform()
	.rotx(-90)
	.movex(blockSize/2+5)
	.movez(blockSize/3),
	new Transform()
	.rotx(90)
	.movex(blockSize/2+5)
	.movez(blockSize/3)
	.movey(blockSize),
	new Transform()
	.rotx(90)
	.movex(-blockSize/2+2)
	.movez(blockSize/3-2)
	.movey(blockSize),
	new Transform()
	.rotx(-90)
	.movex(-blockSize/2+2)
	.movez(blockSize/3-2),
	]
				


def parts=[Vitamins.get("capScrew", "M5x25").movez(plateThickness), 
Vitamins.get("heatedThreadedInsert", "M5")
								.toZMax()
								]


def bolts =[]
for(def tr:boltLocations){
	for(def p:parts){
		bolts.add(p.transformed(tr))
		bolts.add(p.transformed(tr).movey(houseWidth-blockSize))
	}
}

def backPlateHolder = new Cube(plateThickness*2,blockSize+10,blockSize/2).toCSG()
					.toZMin()
					.toYMin()

block = block.union(backPlateHolder)
def blockm = block.mirrory().movey(houseWidth)

def centerPlateOver =blockSize/3
def centerPlate = new Cube(plateThickness,houseWidth-(blockSize*2),buildingHeight-blockSize+centerPlateOver).toCSG()
				.union(new Cube(plateThickness,houseWidth,buildingHeight-blockSize).toCSG()
				.movez(-centerPlateOver/2))
				.toYMin()
				.toZMax()
				.movez(centerPlateOver)
centerPlate.setName("CenterPlate")
centerPlate.addExportFormat("svg")
centerPlate.setManufacturing ({ mfg ->
	return mfg.roty(90).toZMin()
})

def block1 = block.difference(bolts).difference(centerPlate)
def block2 = blockm.difference(bolts).difference(centerPlate)

def roofBolts = [
	Vitamins.get("capScrew", "M5x25").movey(mountBoltWidth/2),
	Vitamins.get("capScrew", "M5x25").movey(-mountBoltWidth/2)
].collect{it.movex(mopuntBoltDown).movez(plateThickness).movey(houseWidth/2)}

def roofPlateBlank = new Cube(hypotOfRoof,houseWidth,plateThickness).toCSG()
				.toXMin()
				.toYMin()
				.toZMin()
				.difference(new Cube(biteDepthForRoof,biteWidth,plateThickness).toCSG()
					.toXMax()
					.toZMin()
					.movey(houseWidth/2)
					.movex(hypotOfRoof)
					.union(roofBolts)
				)

def roofPlateOne = roofPlateBlank.roty(-roofAngleOne)
				.movez(blockSize)
				.difference(bolts)
def roofPlateTwo = roofPlateBlank
				.rotz(180)
				.toYMin()
				.roty(roofAngleTwo)
				.movez(blockSize)
				.difference(bolts)
def sidePlate = new Cube(totalDepthOfBuilding,plateThickness,buildingHeight).toCSG()
				.toZMax()
				.toYMax()
				.movez(blockSize)				
				.difference(bolts)
def sp2 = sidePlate.movey(	houseWidth+plateThickness)			
centerPlate.addExportFormat("svg")
centerPlate.setManufacturing ({ mfg ->
	return mfg.roty(90).toZMin()
})

sidePlate.addExportFormat("svg")
sidePlate.setManufacturing ({ mfg ->
	return mfg.rotx(90).toZMin()
})

sp2.addExportFormat("svg")
sp2.setManufacturing ({ mfg ->
	return mfg.rotx(90).toZMin()
})

roofPlateOne.addExportFormat("svg")
roofPlateOne.setManufacturing ({ mfg ->
	return mfg.roty(roofAngleOne).toZMin()
})

roofPlateTwo.addExportFormat("svg")
roofPlateTwo.setManufacturing ({ mfg ->
	return mfg.roty(-roofAngleTwo).toZMin()
})

block1.setName("Block1")
block2.setName("Block2")
centerPlate.setName("CenterPlate")
roofPlateOne.setName("roof1")
roofPlateTwo.setName("roof2")
sidePlate.setName("SidePlate1")
sp2.setName("SidePlate2")		
return [	block1	,block2,centerPlate,roofPlateOne,sidePlate,roofPlateTwo,sp2,pickup]			