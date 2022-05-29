const processTypeList = document.getElementsByClassName("processType")
const processAmountList = document.getElementsByClassName("processAmount")

if(processTypeList.length == processAmountList.length){
    for(var i = 0 ; i < processTypeList.length ; i++){
        if(processTypeList[i].innerHTML == "Inflow"){
            processAmountList[i].innerHTML = "+" + processAmountList[i].innerHTML
            processAmountList[i].style.color = "green"
        }
        else if(processTypeList[i].innerHTML == "Outflow"){
            processAmountList[i].innerHTML = "-" + processAmountList[i].innerHTML
            processAmountList[i].style.color = "red"
        }
    }
}