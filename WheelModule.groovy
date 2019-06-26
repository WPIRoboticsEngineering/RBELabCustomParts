//Your code here
int depth=1
double gridUnits = 25
def wheelbaseIndex = 9
def wheelbaseIndexY = 9
def wheelbase=gridUnits*wheelbaseIndex
CSGDatabase.clear()
LengthParameter printerOffset 			= new LengthParameter("printerOffset",0.5,[1.2,0])
def mm(def inches){
	return inches*25.4
}
def castor(){
	new Sphere(15.8/2)// Spheres radius
				.toCSG()
				.toZMax()
				.movez(20)
	.union([CSG.unionAll([new Cylinder(32/2,13).toCSG(),
		new Cylinder(5,13).toCSG().movex(38/2),
		new Cylinder(5,13).toCSG().movex(-38/2)
		]).hull(),
		new Cylinder(1.5,10).toCSG().toZMax().movex(38/2),
		new Cylinder(1.5,10).toCSG().toZMax().movex(-38/2)
		])
		.rotx(180)
}	

double washerThick = 1
def motorOptions = []
def shaftOptions = []
for(String vitaminsType: Vitamins.listVitaminTypes()){
	HashMap<String, Object> meta = Vitamins.getMeta(vitaminsType);
	if(meta !=null && meta.containsKey("actuator"))
		motorOptions.add(vitaminsType);
	if(meta !=null && meta.containsKey("shaft"))
		shaftOptions.add(vitaminsType);
}

StringParameter motors = new StringParameter("Motor Type","roundMotor",motorOptions)
StringParameter shafts = new StringParameter("Shaft Type","dShaft",shaftOptions)
StringParameter motorSize = new StringParameter("Motor Size","WPI-gb37y3530-50en",Vitamins.listVitaminSizes(motors.getStrValue()))
StringParameter shaftSize = new StringParameter("Shaft Size","WPI-gb37y3530-50en",Vitamins.listVitaminSizes(shafts.getStrValue()))

def motorBlank= Vitamins.get(motors.getStrValue(),motorSize.getStrValue()).rotz(180)
def shaftBlank= Vitamins.get(shafts.getStrValue(),shaftSize.getStrValue())

double motorToMountPlane = motorBlank.getMaxZ()
double shaftLength=shaftBlank.getTotalZ()

motorBlank=motorBlank.movez(-motorToMountPlane)
double tireID = mm(1.0+(5.0/8.0))
double tireOD = mm(2.0)
double width = mm(3.0/16.0)
double sweepCenter = (double)(tireOD+tireID)/4.0
def tire = CSG.unionAll(
		Extrude.revolve(new Cylinder(width/2-printerOffset.getMM()/2,1.0).toCSG().roty(90),
		sweepCenter, // rotation center radius, if 0 it is a circle, larger is a donut. Note it can be negative too
		(double)360,// degrees through wich it should sweep
		(int)30)//number of sweep increments
		)
		.roty(90)
def bearing =Vitamins.get("ballBearing","695zz").hull().makeKeepaway(printerOffset.getMM()).toZMin()
LengthParameter boltlen 			= new LengthParameter("Bolt Length",0.675,[1.2,0])
boltlen.setMM(50-1.388*2)
String size ="M5"
HashMap<String, Object>  boltData = Vitamins.getConfiguration( "capScrew",size)
def bolt =new Cylinder(boltData.outerDiameter/2+printerOffset.getMM()/2,boltlen.getMM()).toCSG()
double nursertHeight = 9.5
double nutsertRad = 6.4/2+printerOffset.getMM()/2
def nutsert=new Cylinder(nutsertRad,nursertHeight ).toCSG() 

double pitch = 3
int atheeth =16
// call a script from another library
List<Object> bevelGears = (List<Object>)ScriptingEngine
					 .gitScriptRun(
            "https://github.com/madhephaestus/GearGenerator.git", // git location of the library
            "bevelGear.groovy" , // file to load
            // Parameters passed to the funcetion
            [	  atheeth,// Number of teeth gear a
	            atheeth*3,// Number of teeth gear b
	            6,// thickness of gear A
	            pitch,// gear pitch in arch length mm
	           90
            ]
            )
println "Bevel gear radius A " + bevelGears.get(2)
println "Bevel gear radius B " + bevelGears.get(3)
def gearBThickness = bevelGears.get(6)
def wheelSectionThickness = width*1.5
def wheelCenterlineX = -wheelSectionThickness/2-bevelGears.get(2)
def wheelCore = new Cylinder(sweepCenter,wheelSectionThickness).toCSG()
			.roty(-90)
			.movez(  bevelGears.get(3))
			.movex( -wheelSectionThickness-bevelGears.get(2))
def wheelwell = new Cylinder(sweepCenter+width,wheelSectionThickness+gearBThickness).toCSG()
			.roty(-90)
			.movez(  bevelGears.get(3))
			.movex( -wheelSectionThickness-bevelGears.get(2)-washerThick)			
tire=tire .movez(  bevelGears.get(3))
		.movex( wheelCenterlineX)
bearing=bearing
		.roty(90)
		.movez(  bevelGears.get(3))
		.movex( gearBThickness-bevelGears.get(2)+washerThick)
bearing2 = bearing.movex(bearing.getTotalX() -gearBThickness-wheelSectionThickness-washerThick)
CSG outputGear = bevelGears.get(0)
CSG supportOutputGear = new Cylinder(outputGear.getTotalX()/2,Math.abs(motorToMountPlane)).toCSG()
					.toZMax()
outputGear=outputGear.union(	supportOutputGear)
CSG adrive = bevelGears.get(1)

def nutsertGrid= []
def netmover= nutsert
			.roty(90)

for(int i=-depth;i<=depth;i++)
	for(int j=-depth;j<=depth;j++){
		if(i==0&&j==0)
			continue
		nutsertGrid.add(netmover.movey(gridUnits*i)
				   .movez(gridUnits*j))
	}
def nutGrid = CSG.unionAll(nutsertGrid)
def axelBolt = bolt
			.movez(-boltlen.getMM()/2)
			.roty(-90)
			.movez(  bevelGears.get(3))
			.movex(motorBlank.getCenterX() )

def motorY = sweepCenter+width+nursertHeight +Math.abs(motorBlank.getMinY())
def motorPlate = new Cube(boltlen.getMM(),motorY,motorToMountPlane).toCSG()
				.movex(motorBlank.getCenterX() )
				.toYMin()
				.movey(motorBlank.getMinY())
				.toZMax()
def axelMount = nutsert
			.roty(90)
			.movez(  bevelGears.get(3))
			.movex(motorPlate.getMaxX() )
def lSideGrid = nutGrid
			.movez(  bevelGears.get(3))
			.movex(motorPlate.getMaxX() )
def rSideGrid = nutGrid
			.movez(  bevelGears.get(3))
			.rotz(180)
			.movex(motorPlate.getMinX() )			
def leftHeight = motorPlate.getMaxX()-bevelGears.get(1).getMaxX()-printerOffset.getMM()//-washerThick
def baseSupportRad = 17
def leftCone = new Cylinder(baseSupportRad, // Radius at the bottom
                      		boltData.outerDiameter/2+1, // Radius at the top
                      		leftHeight, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(90)
                      		.toXMax()
                      		.movex(motorPlate.getMaxX())
                      		.movez(  bevelGears.get(3))
def rightHeight = Math.abs(motorPlate.getMinX()-wheelwell.getMinX())-printerOffset.getMM()-washerThick
def rightCone = new Cylinder(baseSupportRad, // Radius at the bottom
                      		boltData.outerDiameter/2+1, // Radius at the top
                      		rightHeight, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(-90)
                      		.toXMin()
                      		.movex(motorPlate.getMinX())
                      		.movez(  bevelGears.get(3))	
def  sideWallTHickness=motorPlate.getMaxX()-bevelGears.get(0).getMaxX()-5

def sideWallPuckL = new Cylinder(baseSupportRad, // Radius at the bottom
                      		baseSupportRad, // Radius at the top
                      		sideWallTHickness, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(90)
                      		.toXMax()
                      		.movex(motorPlate.getMaxX())
                      		.movez(  bevelGears.get(3))
 def sideWallPuckR = new Cylinder(baseSupportRad, // Radius at the bottom
                      		baseSupportRad, // Radius at the top
                      		sideWallTHickness, // Height
                      		(int)30 //resolution
                      		).toCSG()
                      		.roty(-90)
                      		.toXMin()
                      		.movex(motorPlate.getMinX())
                      		.movez(  bevelGears.get(3)) 
def mountWallBar =new Cube(sideWallTHickness,nursertHeight, bevelGears.get(3)+nutsertRad*2).toCSG()
				.toZMin()

def mountWallBarL = mountWallBar
				.toXMax()
				.movex(motorPlate.getMaxX())
				.toYMax()
				.movey(motorPlate.getMaxY())
def mountWallBarR=mountWallBar
				.toXMin()
				.movex(motorPlate.getMinX())
				.toYMax()
				.movey(motorPlate.getMaxY())
def sideWallBarL = new Cube(sideWallTHickness,motorY,motorToMountPlane).toCSG()
				.toXMax()
                    .movex(motorPlate.getMaxX())
				.toYMin()
				.movey(motorBlank.getMinY())
				.toZMax()
				.union([sideWallPuckL,mountWallBarL]).hull()
 def sideWallBarR = new Cube(sideWallTHickness,motorY,motorToMountPlane).toCSG()
				.toXMin()
                     .movex(motorPlate.getMinX())
				.toYMin()
				.movey(motorBlank.getMinY())
				.toZMax()
				.union([sideWallPuckR,mountWallBarR]).hull()  
def backPlate =  mountWallBarR.union(   mountWallBarL).hull()   

def wheelMountHole = nutsert
			.roty(90)
			.rotz(-90)
			.movez(  bevelGears.get(3))
			.movey(motorPlate.getMaxY() )
			.movex( wheelCenterlineX)
def wheelMountGrid = nutGrid
			.rotz(-90)
			.movez(  bevelGears.get(3))
			.movey(motorPlate.getMaxY() )
			.movex( wheelCenterlineX)		
def gearHole =  new Cylinder(bevelGears.get(0).getMaxX()+1,motorToMountPlane).toCSG() 
				.toZMax()     

// FInal assembly section				
def bracket = CSG.unionAll([motorPlate,leftCone,rightCone,sideWallBarL,sideWallBarR,backPlate

])  .difference([axelBolt,wheelwell,motorBlank,gearHole,
axelMount,
wheelMountHole,
lSideGrid,rSideGrid,wheelMountGrid
])	

def wheelAsmb = CSG.unionAll([adrive,wheelCore
]).difference([axelBolt,tire,bearing,bearing2

])
def driveGear = outputGear.difference([shaftBlank,motorBlank])
def bracketm=bracket.mirrorx().movex(wheelbase+wheelCenterlineX*2)
// Attach production scripts

driveSection= [driveGear,bracket, bracketm,wheelAsmb,tire,motorBlank,bearing,bearing2].collect{
	it.move(-wheelCenterlineX,tire.getMaxY(),- bevelGears.get(3))
	.rotx(-90)
	}
def cast = castor()
double BottomOfPlate=driveSection[1].getMaxZ()
double castorStandoff = cast.getMinZ()
double standoffHeight = BottomOfPlate+castorStandoff
def standoffPart =CSG.unionAll([ 	new Cylinder(gridUnits/2,standoffHeight).toCSG().movex( gridUnits*0.5),
							new Cylinder(gridUnits/2,standoffHeight).toCSG().movex( -gridUnits*0.5)
				]).hull()
				.toZMax()
				.difference([	nutsert.toZMax().movex( gridUnits*0.5),
							nutsert.toZMax().movex( -gridUnits*0.5),
							
				])
				.movez(BottomOfPlate)
				.movex( gridUnits*wheelbaseIndex/2)
				.movey(gridUnits*(Math.round((wheelbaseIndexY/2.0))))
def movedCastor =cast.toZMin()
				.movex( gridUnits*wheelbaseIndex/2)
				.movey(gridUnits*(Math.round((wheelbaseIndexY/2.0))))
standoffPart=	standoffPart.difference(	movedCastor)	


def nutsertGridPlate= []
def netmoverP= new Cylinder(5.0/2,standoffHeight/2).toCSG()
			.toZMin()
			.movez(BottomOfPlate)
def netmoverV= new Cylinder(3/2,standoffHeight).toCSG()
			.toZMin()
			.movez(BottomOfPlate-10)
for(int i=0;i<wheelbaseIndexY+3;i++)
	for(int j=0;j<(wheelbaseIndex+3);j++){
		nutsertGridPlate.add(netmoverP.movey(gridUnits*i)
				   .movex(gridUnits*j-gridUnits))
}

for(int i=0;i<6;i++)
	for(int j=0;j<2;j++){
		nutsertGridPlate.add(netmoverV.movex(mm(0.5)*i+gridUnits/2)
				   .movey(mm(0.5)*j*7+gridUnits-gridUnits/2))
}
standoffPart=	standoffPart.difference(	movedCastor)	
			.difference(	nutsertGridPlate)	
wheelAsmb=driveSection[3]
bracketm=driveSection[2].difference(nutsertGridPlate)
bracket=driveSection[1].difference(nutsertGridPlate)
driveGear=driveSection[0]
tire = driveSection[4]
motorBlank = driveSection[5]
def driveGearl = driveGear.mirrorx().movex(wheelbase)
def wheelAsmbl = wheelAsmb.mirrorx().movex(wheelbase)
def tirel = tire.mirrorx().movex(wheelbase)
def motorBlankl=motorBlank.mirrorx().movex(wheelbase)

CSG plateRound =new Cylinder((gridUnits*(wheelbaseIndex+4))/2,mm(1.0/4.0)).toCSG() 
				.toZMin()
				.toXMin()
				.move(-gridUnits*2,0,BottomOfPlate)
def plateCubic = new Cube(gridUnits*(wheelbaseIndex+4),gridUnits*(wheelbaseIndexY+2),mm(1.0/4.0)).toCSG()
				.toZMin()
				.toXMin()
				.toYMin()
				.move(-gridUnits*2,-gridUnits,BottomOfPlate)
def plate =  plateRound
				.intersect(plateCubic)
				.difference(nutsertGridPlate)

wheelAsmb.setName("wheel")
	.setManufacturing({ toMfg ->
	return toMfg
			.roty(90)
			.toXMin()
			.toYMin()
			.toZMin()
})
wheelAsmbl.setName("wheel left")
	.setManufacturing({ toMfg ->
	return toMfg
			.roty(-90)
			.toXMin()
			.toYMin()
			.toZMin()
})
bracketm.setName("bracket-m")
	.setManufacturing({ toMfg ->
	return toMfg.rotx(90)
			.toXMin()
			.toYMin()
			.toZMin()
})
bracket.setName("bracket")
	.setManufacturing({ toMfg ->
	return toMfg.rotx(90)
			.toXMin()
			.toYMin()
			.toZMin()
})
driveGear.setName("driveGear")
	.setManufacturing({ toMfg ->
	return toMfg.rotx(90)
			.toXMin()
			.toYMin()
			.toZMin()
})
driveGearl.setName("driveGear left")
	.setManufacturing({ toMfg ->
	return toMfg.rotx(90)
			.toXMin()
			.toYMin()
			.toZMin()
})
standoffPart.setName("standoffPart")
	.setManufacturing({ toMfg ->
	return toMfg
			.toXMin()
			.toYMin()
			.toZMin()
})
plate.addExportFormat("svg")// make an svg of the object
plate.setName("plate")
	.setManufacturing({ toMfg ->
	return toMfg
			.toZMin()
})
movedCastor.setManufacturing({ toMfg ->
	return null
})
tire.setName("tire")
tirel.setName("tirel")
tire.setManufacturing({ toMfg ->
	return null
})
tirel.setManufacturing({ toMfg ->
	return null
})
motorBlankl.setManufacturing({ toMfg ->
	return null
})
motorBlank.setManufacturing({ toMfg ->
	return null
})
println "BottomOfPlate = "+BottomOfPlate
println "Plate dimentions x="+plate.getTotalX()+" y="+plate.getTotalY()
println "Weel center line to outer wall of bracket="+Math.abs(bracket.getMinX())
parts=  [driveGear,driveGearl,bracket, bracketm,wheelAsmb,wheelAsmbl, movedCastor,standoffPart,plate,tire,tirel,motorBlankl,motorBlank,
plateCubic
]
return parts

def toProject =[bracket,wheelAsmb,tire,plate]
def toProjectF =[bracket,bracketm,wheelAsmb,wheelAsmbl,tire,plate,tirel]
def toProjectb =[plate,movedCastor,standoffPart]
projection = toProject.collect{
	def back = it.roty(90)
				.movex(-gridUnits*3)
				.setName(it.getName()+"-projection")
	back.addExportFormat("svg")
	return back
}
projectionf = toProjectF.collect{
	def back = it.rotx(-90)
				.movey(-gridUnits*3)
				.setName(it.getName()+"-projection-f")
	back.addExportFormat("svg")
	return back
}
projectionb = toProjectb.collect{
	def back = it
	.movey(-gridUnits*(wheelbaseIndexY-1))
	.rotx(90)
	.movey(gridUnits*(wheelbaseIndexY-1))
				.movey(gridUnits*3)
				.setName(it.getName()+"-projection-b")
	back.addExportFormat("svg")
	return back
}
return [parts,projection,projectionf]
println "Making a single STL assembly..."
def singleSTL = CSG.unionAll(parts)
singleSTL.setName("fullAssembly_DO_NOT_PRINT")
singleSTL.setManufacturing({ toMfg ->
	return toMfg.movex(-wheelbase/2)
})
parts.add(singleSTL)
return parts