var orgbuilder = (function(){
    var TOKEN_STORAGE_KEY = "orgapiToken";
    var BEARER_PREFIX = "Bearer";

    var jwt = {
        stripBearerPrefix: function(token){
            if(typeof token === "string" && token.startsWith(BEARER_PREFIX)){
                return token.substring(6).trim();
            }
            return token;
        },
        storeToken: function(token){
            token = this.stripBearerPrefix(token);
            localStorage.setItem(TOKEN_STORAGE_KEY, token);
        },
        getToken: function(){
            return localStorage.getItem(TOKEN_STORAGE_KEY);
        },
        getTokenPayload: function(){
            var token = this.getToken();
            if(token === null || token === undefined || token === ""){
                console.log("No token to parse payload from");
                return;
            }

            try{
                return JSON.parse(atob(token.split(".")[1]));
            }
            catch(e){
                console.log("Error! Unable to parse JSON payload in token. Cause: " + e.message);
            }
        },
        clearToken: function(){
            delete localStorage[TOKEN_STORAGE_KEY];
        }
    };

    return{
        jwt: jwt
    }
})();

