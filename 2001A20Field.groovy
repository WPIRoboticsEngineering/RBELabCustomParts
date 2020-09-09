//Your code here

double plateThickness = 0.25*25.4

double roofAngleOne =25
double RoofAngleTwo = 45
double blockSize = 20
double houseWidth = 8*25.4
double mountBoltWidth = 2.5*25.4
double mopuntBoltDown = 5.0/8.0*25.4
double hypotOfRoof = 3.1*25.4
double totalDepthOfBuilding = 5.0*25.4
double biteDepthForRoof = 0.5 *25.4
double biteWidth= 2.75*25.4
double buildingHeight = 9.75*25.4


def wedgeOne = new Wedge(blockSize/Math.tan(Math.toRadians(roofAngleOne)),
					blockSize,
					blockSize 
					).toCSG()
def wedgeTwo = new Wedge(blockSize/Math.tan(Math.toRadians(RoofAngleTwo)),
					blockSize,
					blockSize 
					).toCSG()
					.rotz(180)
					.toYMin()
def block = wedgeOne.union(wedgeTwo)

def boltLocations =[
 	new Transform()
	.movex(blockSize/2)
	.roty(-roofAngleOne)
	.movez(blockSize)
	.movey(blockSize/3),
	new Transform()
	.movex(-blockSize/2)
	.roty(RoofAngleTwo)
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
	.movey(blockSize)
	]
				


def parts=[Vitamins.get("capScrew", "M5x25").movez(plateThickness), 
Vitamins.get("heatedThreadedInsert", "M5")
								.toZMax()
								]


def bolts =[]
for(def tr:boltLocations){
	for(def p:parts){
		bolts.add(p.transformed(tr))
		bolts.add(p.transformed(tr).movey(totalDepthOfBuilding-blockSize))
	}
}

def backPlateHolder = new Cube(plateThickness*2,blockSize+10,blockSize/2).toCSG()
					.toZMin()
					.toYMin()

block = block.union(backPlateHolder)
def blockm = block.mirrory().movey(totalDepthOfBuilding)

def centerPlateOver =blockSize/3
def centerPlate = new Cube(plateThickness,totalDepthOfBuilding-(blockSize*2),buildingHeight-blockSize+centerPlateOver).toCSG()
				.union(new Cube(plateThickness,totalDepthOfBuilding,buildingHeight-blockSize).toCSG().movez(-centerPlateOver/2))
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

return [	block1	,block2,centerPlate]			