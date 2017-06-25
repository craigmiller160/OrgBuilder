export var orgbuilder = (function(){
    //Global error handling method
    window.onerror = function(message, source, lineno, colno, error){
        alert("Error on page! Cause: " + message);
    };

    //Utility code to execute on page initialization to support the alert messages
    //Close alert
    $("body").click(function(){
        $(".alert").removeClass("in");
    });
    $("[data-hide]").on("click",function(){
        $("." + $(this).attr("data-hide")).removeClass("in");
    });

    //Utility code to disable enter-key-submit action in forms
    $(".no-enter-form input.content-field").keydown(function(event){
        if(event.which && event.which === 13){
            event.preventDefault();
        }
    });

    return {
        foo: 'bar'
    }
})();