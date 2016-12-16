var orgbuilder = (function(){
    var TOKEN_STORAGE_KEY = "orgapiToken";
    var BEARER_PREFIX = "Bearer";

    var roles = {
        master: 'MASTER',
        admin: 'ADMIN',
        write: 'WRITE',
        read: 'READ'
    };

    var methods = {
        get: "GET",
        post: "POST",
        put: "PUT",
        delete: "DELETE"
    };

    var jwt = {
        storeToken: function(token){
            token = stripBearerPrefix(token);
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
        },
        tokenExists: function(){
            return localStorage.getItem(TOKEN_STORAGE_KEY) !== undefined && localStorage.getItem(TOKEN_STORAGE_KEY) !== null;
        },
        hasRole: function(role){
            return $.inArray(role, this.getTokenPayload().rol) !== -1;
        }
    };

    //TODO need a rejection if the token has timed out. All other callbacks need to be blocked in that case, and re-routed to access denied or something similar
    var api = {
        call: function(uri, method, json){
            return $.ajax({
                url: orgProps.serverOrigin + ensurePrecedingSlash(uri),
                type: method,
                headers: (function(){
                    var result = {
                        "Access-Control-Request-Headers": [
                            "X-Requested-With",
                            "Authorization"
                        ],
                        "Access-Control-Request-Method": method
                    };

                    var token = jwt.getToken();
                    if(token !== undefined && token !== null){
                        result.Authorization = restoreBearerPrefix(token);
                    }

                    return result;
                })(),
                contentType: "application/json; charset=utf-8",
                data: (function(){
                    if(json !== undefined){
                        return JSON.stringify(json);
                    }
                    return null;
                })()
            })
                .done(function(data, status, jqXHR){
                    //TODO ensure that, if a token is not returned, reference to it is removed from localStorage. Better yet, add fail functionality for a request with this result.
                    var token = jqXHR.getResponseHeader("Authorization");
                    jwt.storeToken(token);
                });
        }
    };

    //This function accepts arguments, each representing a single role to validate
    function validateAccess(){
        if(jwt.tokenExists()){
            var valid = true;
            if(arguments){
                $.each(arguments, function(index,role){
                    if(!jwt.hasRole(role)){
                        valid = false;
                        return false;
                    }
                });
            }

            if(valid){
                return true;
            }
        }
        return false;
    }

    function ensurePrecedingSlash(uri){
        if(uri.startsWith("/")){
            return uri;
        }
        return "/" + uri;
    }

    function stripBearerPrefix (token){
        if(typeof token === "string" && token.startsWith(BEARER_PREFIX)){
            return token.substring(6).trim();
        }
        return token;
    }

    function restoreBearerPrefix (token){
        if(typeof token === "string" && !token.startsWith(BEARER_PREFIX)){
            return BEARER_PREFIX + " " + token;
        }
        return token;
    }

    return{
        roles: roles,
        methods: methods,
        jwt: jwt,
        api: api,
        validateAccess: validateAccess
    }
})();

