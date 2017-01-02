var orgbuilder = (function(){
    var TOKEN_STORAGE_KEY = "orgapiToken";
    var BEARER_PREFIX = "Bearer";

    window.onerror = function(message, source, lineno, colno, error){
        alert("Error on page! Cause: " + message);
    };

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
                console.log("Error! Unable to parse JSON payload in token.");
                throw e;
            }
        },
        clearToken: function(){
            delete localStorage[TOKEN_STORAGE_KEY];
        },
        tokenExists: function(){
            return localStorage.getItem(TOKEN_STORAGE_KEY) !== undefined && localStorage.getItem(TOKEN_STORAGE_KEY) !== null;
        },
        hasRole: function(role){
            try{
                return $.inArray(role, this.getTokenPayload().rol) !== -1;
            }
            catch(e){
                console.log("Error! Unable to check the role in the token payload");
                throw e;
            }
        }
    };

    //TODO need a rejection if the token has timed out. All other callbacks need to be blocked in that case, and re-routed to access denied or something similar
    var api = {
        send: function(uri, method, json){
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
                    var token = jqXHR.getResponseHeader("Authorization");
                    jwt.storeToken(token);
                })
                .fail(function(jqXHR){
                    var status = jqXHR.status;
                    console.log("API Request Failed. Status: " + status);
                    if(status === 403){
                        window.location = orgProps.clientOrigin + "/access-denied.html";
                    }
                    else if(status === 0){
                        window.location = orgProps.clientOrigin + "/server-error.html";
                    }
                    else if(status >= 500){
                        var error = jqXHR.responseJSON;
                        console.log("Critical error server-side, please check server logs for details");
                        console.log("Error Message: " + error.errorMessage);
                        window.location = orgProps.clientOrigin + "/server-error.html";
                    }
                    else{
                        alert("Error communicating with server. Status: " + status);
                        //TODO more detailed response is ultimately preferred here
                    }
                });
        }
    };
    //The functions in this object accept arguments, each representing a single role to validate
    var validateAccess = {
        allRoles: function(){
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
        },
        anyRole: function(){
            if(jwt.tokenExists()){
                var valid = false;
                if(arguments){
                    $.each(arguments, function(index,role){
                        if(jwt.hasRole(role)){
                            valid = true;
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
    };

    var validateData = {
        user: function(userData){
            return userData !== null &&
                userData.userEmail !== undefined &&
                userData.roles !== undefined &&
                userData.password !== undefined &&
                userData.orgId !== undefined &&
                userData.orgName !== undefined &&
                userData.firstName !== undefined &&
                userData.lastName !== undefined &&
                userData.userId !== undefined;
        },
        org: function(orgData){
            return orgData !== null &&
                orgData.createdDate !== undefined &&
                orgData.orgId !== undefined &&
                orgData.orgName !== undefined &&
                orgData.orgDescription !== undefined &&
                orgData.schemaName !== undefined;
        },
        orgList: function(orgListData){
            var orgFn = this.org;
            return orgListData !== null &&
                    orgListData.orgList !== undefined && orgListData.orgList !== null &&
                    (function(){
                        var valid = true;
                        $.each(orgListData.orgList, function (index,org){
                            if(!orgFn(org)){
                                valid = false;
                                return false;
                            }
                        });
                        return valid;
                    })()
        },
        userList: function(userListData){
            var userFn = this.user;
            return userListData !== null &&
                    userListData.userList !== undefined && userListData !== null &&
                    (function(){
                        var valid = true;
                        $.each(userListData.userList, function(index,user){
                            if(!userFn(user)){
                                valid = false;
                                return false;
                            }
                        });
                        return valid;
                    })()
        }
    };

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

    function cancelChangesCheck(){
        return confirm("Are you sure you want to cancel? All changes will be lost.");
    }

    var checkRequiredFields = function(formId){
        var parsedFormId = formId.startsWith("#") ? formId : "#" + formId;

        var allComplete = true;
        //IMPORTANT: At the moment, this only works for text and password input fields
        $(parsedFormId + " input[required]").filter(":visible").each(function(index,input){
            if(($(input).attr("type") === "text" || $(input).attr("type") === "password") && ($(input).val() === null || $(input).val() === "")){
                var label = $(input).parent().siblings("td[field = 'label']")[0];
                alert("Missing required field: " + $(label).text());
                allComplete = false;
                return false;
            }
        });

        return allComplete;
    };

    return{
        roles: roles,
        methods: methods,
        jwt: jwt,
        api: api,
        validateAccess: validateAccess,
        checkRequiredFields: checkRequiredFields,
        cancelChangesCheck: cancelChangesCheck,
        validateData: validateData
    }
})();

