import com.neuronrobotics.bowlerstudio.vitamins.Vitamins

import eu.mihosoft.vrl.v3d.CSG

CSG vitamin_vexSpacer_threeEighth = Vitamins.get("vexSpacer", "threeEighth")

def dimX = vitamin_vexSpacer_threeEighth.getTotalX() + 5 // add 5mm cushion


CSG printbed = vitamin_vexSpacer_threeEighth

for(double i = 0; i<180; i+= dimX){
	for(double j = 0; j<180; j+= dimX){
		printbed = printbed.union(vitamin_vexSpacer_threeEighth.movex(i).movey(j))
	}
}
	
String filename = "Printbed-three-eighths-inch-Spacer.stl";

def abs_path = Paths.get(filename)

FileUtil.write(abs_path,
		printbed.toStlString());
println "STL EXPORT to "+filename
println abs_path
return printbed;