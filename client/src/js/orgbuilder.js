export var orgbuilder = (function(){
    //Global error handling method
    window.onerror = (message, source, lineno, colno, error) => {
        alert("Error on page! Cause: " + message);
    };

    //Utility code to execute on page initialization to support the alert messages
    //Close alert
    $("body").click(() => {
        $(".alert").removeClass("in");
    });
    $("[data-hide]").on("click", () => {
        $("." + $(this).attr("data-hide")).removeClass("in");
    });

    //Utility code to disable enter-key-submit action in forms
    $(".no-enter-form input.content-field").keydown((event) => {
        if(event.which && event.which === 13){
            event.preventDefault();
        }
    });

    function varExists(value){
        return value !== undefined && value !== null;
    }

    function varExistsString(value){
        return varExists(value) && value !== '';
    }

    return {
        varExists: varExists,
        varExistsString: varExistsString
    }
})();