function currencyChange(accountID){
    const currency = document.getElementById(accountID.value).innerText;
   // document.getElementById("currency").innerHTML = currency;
    document.getElementById("currency").value = currency;
    //confirm on modal
    document.getElementById("confirmSenderID").innerHTML = accountID.value;
}

$('#submitBtn').click(function() {
    /* when the button in the form, display the entered values in the modal */
    $('#confirmReceiverID').text($('#receiverID').val());
    $('#confirmReceiverName').text($('#receiverName').val() + " " + $('#receiverSurname').val());
    $('#confirmAmount').text($('#amount').val() + "  " + $('#currency').text());
    $('#confirmDescription').text($('#description').val() == "" ? "No description" : $('#description').val());
});