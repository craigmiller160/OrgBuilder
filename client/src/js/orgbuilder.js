export var orgbuilder = (function(){

    const pathRoot = 'orgclient';

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

    function calculateAge(dateOfBirth){
        let date = createDate(dateOfBirth);
        let ageDiff = Date.now() - date.getTime();
        return Math.abs(new Date(ageDiff).getUTCFullYear() - 1970);
    }

    //Creates date object out of format MM-dd-yyyy
    function createDate(dateString){
        let split = dateString.split('-');
        if(split.length !== 3){
            throw 'Date string does not have three parts: ' + dateString;
        }

        return new Date(split[0], split[1] - 1, split[2]);
    }

    function createUri(uri){
        return '/' + pathRoot + '/' + uri;
    }

    return {
        varExists: varExists,
        varExistsString: varExistsString,
        calculateAge: calculateAge,
        createUri: createUri
    }
})();