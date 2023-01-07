$(document).ready(function(){
    var addData = {};

    $(".eml").submit(function() {
        addData["Addres"] = $('#EmailAddr').val();
        addData["Message"] = $('#EmailText').val();
        addData["Subject"] = $('#Subject').val();
        console.log(addData);
        doAjaxAddToCart();
    });

    function doAjaxAddToCart() {
        $.ajax({
            type: 'POST',
            contentType: 'application/String',
            url: '/SendEmail',
            data: JSON.stringify(addData),
            dataType: 'html',
            success: function(result) {
                window.location.replace("http://localhost:8080/");

            },
            error: function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})
