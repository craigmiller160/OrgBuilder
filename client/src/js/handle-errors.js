/**
 * Created by craigmiller on 11/27/16.
 */
(function(){
    function globalHandleErrors(fn){
        try{
            fn();
        }
        catch(error){
            var errorType = parseErrorType(error);
            console.log(errorType + ": " + error.message);
            console.log(error.stack);
        }
    }

    function parseErrorType(e){
        var typeString = "Error";
        if(e instanceof EvalError){
            typeString = "EvalError";
        }
        else if(e instanceof InternalError){
            typeString = "InternalError";
        }
        else if(e instanceof RangeError){
            typeString = "RangeError";
        }
        else if(e instanceof ReferenceError){
            typeString = "ReferenceError";
        }
        else if(e instanceof SyntaxError){
            typeString = "SyntaxError";
        }
        else if(e instanceof TypeError){
            typeString = "TypeError";
        }
        else if(e instanceof URIError){
            typeString = "URIError";
        }
        return typeString;
    }

    return {
        "globalHandleErrors": globalHandleErrors
    }
})();


