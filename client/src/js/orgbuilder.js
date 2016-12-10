var orgbuilder = (function(){
    var TOKEN_STORAGE_KEY = "orgapiToken";
    var BEARER_PREFIX = "Bearer";

    var jwt = {
        roles: {
            master: 'MASTER',
            admin: 'ADMIN',
            write: 'WRITE',
            read: 'READ'
        },
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
        },
        tokenExists: function(){
            return localStorage.getItem(TOKEN_STORAGE_KEY) !== undefined;
        }
    };

    var api = {
        methods: {
            get: "GET",
            post: "POST",
            put: "PUT",
            delete: "DELETE"
        },
        call: function(uri, method, json){
            return $.ajax({
                url: orgProps.serverOrigin + ensurePrecedingSlash(uri),
                type: method,
                headers: {
                    "Access-Control-Request-Headers": "X-Requested-With",
                    "Access-Control-Request-Method": method
                },
                contentType: "application/json; charset=utf-8",
                data: (function(){
                    if(json !== undefined){
                        return JSON.stringify(json);
                    }
                    return null;
                })()
            });
        }
    };

    function ensurePrecedingSlash(uri){
        if(uri.startsWith("/")){
            return uri;
        }
        return "/" + uri;
    }

    return{
        jwt: jwt,
        api: api
    }
})();

