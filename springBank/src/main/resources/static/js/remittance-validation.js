class InputValidation {
  constructor(inputID,regex) {
      this.inputID = inputID;
      this.regex = regex;
  }
}


$(document).ready(function(){
    var receiverID = new InputValidation('#receiverID',new RegExp('^[0-9]{1,5}$'));
    var receiverName = new InputValidation('#receiverName',new RegExp('(^(([a-zA-Z]{2,50})(\\s?))+$)'));
    var receiverSurname = new InputValidation('#receiverSurname',new RegExp('^[a-zA-Z]{2,30}$'));
    var amount = new InputValidation('#amount', new RegExp('^[\\d]+(\\.\\d{1,2})?$'));

    $.fn.invalidMessage = function(title,message){
        alert(title + "\n" +message);
    };
    $('input').focusout(function (){
        $(this).val($(this).val().trim());
    })

    $(receiverID.inputID).on('focusout', function(){
        $.check.textBox(receiverID);
    });

    $(receiverName.inputID).on('focusout', function(){
        $.check.textBox(receiverName);
    });

    $(receiverSurname.inputID).on('focusout', function(){
        $.check.textBox(receiverSurname);
    });

    $(amount.inputID).on('focusout', function(){
        $.check.textBox(amount);
    });

    $('input[type=radio]').change(function(){
        $("#senderAccountChoiceFeedback").removeClass('is-invalid');
    });

    $.check = {
        textBox: function(input){
            if(!$(input.inputID).val().match(input.regex) || $(input.inputID).val() <= 0){
                $(input.inputID).addClass('is-invalid');
            }
            else{
                $(input.inputID).removeClass('is-invalid');
            }
        },
        radio: function(feedbackId){
            if( !$('input[type=radio]').is(':checked') ) {
                $(feedbackId).addClass('is-invalid');
                return false;
            }
            return true;
        }
        ,
        inputs: function(...inputs){
            var result = true;
            for(var input of inputs){
                if(!$(input.inputID).val() || $(input.inputID).hasClass('is-invalid')){
                    $(input.inputID).addClass('is-invalid');
                    result = result && false;
                }
                else{
                    result = result && true;
                }
            }
            return result;
        }
    }

    $('#submitBtn').click(function(){
        if($.check.radio('#senderAccountChoiceFeedback') & $.check.inputs(receiverID,receiverName,receiverSurname,amount))
            $('#confirm-submit').modal('toggle');
        else{
            alert('Please fill in all the required fields');
            return;
        }
    });

});