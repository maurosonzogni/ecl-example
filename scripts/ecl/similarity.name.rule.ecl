pre{
    var editDistance = 1.asDouble();
    FirstModel!ComponentInstance.all().println();
    SecondModel!ComponentInstance.all().println();
    
}

@greedy
rule CompareByComponentName
    match componentsFirstModel: FirstModel!SystemInstance
    with componentsSecondModel: SecondModel!SystemInstance {
        do {
            editDistance=0.5.asDouble().println("Edit distance changed to: ");
            //matchInfo.put("a", 10);
        }

}




