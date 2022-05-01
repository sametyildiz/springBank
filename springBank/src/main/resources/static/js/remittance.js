function currencyChange(accountID){
    const currency = document.getElementById(accountID.value).innerText;
    document.getElementById("currency").innerHTML = currency;
}