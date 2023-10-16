pre { 
    var editDistance = 1.asDouble();
}

rule CompareByComponentName
    match componentsFirstModel: FirstModel!ComponentInstance
    with componentsSecondModel: SecondModel!ComponentInstance {
        // check if category are equals, that's avoid to compare SW components with HW components
        guard : true//componentsFirstModel.category.toString().equalsIgnoreCase(componentsSecondModel.category.toString())  
        // 0.0 if are equals
        compare: true//levenshtein.distance(clearName(componentsFirstModel.name),clearName(componentsSecondModel.name)).asDouble() < threshold.asDouble()
        do {
            editDistance=0.5.asDouble();

        }

}




