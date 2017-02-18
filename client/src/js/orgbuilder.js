var orgbuilder = (function(){
    var TOKEN_STORAGE_KEY = "orgapiToken";
    var BEARER_PREFIX = "Bearer";

    //Global error handling method
    window.onerror = function(message, source, lineno, colno, error){
        alert("Error on page! Cause: " + message);
    };

    //Constants for the access roles for the application
    var roles = {
        master: 'MASTER',
        admin: 'ADMIN',
        write: 'WRITE',
        read: 'READ'
    };

    //Contants for the HTTP methods
    var methods = {
        get: "GET",
        post: "POST",
        put: "PUT",
        delete: "DELETE"
    };

    //Constants for the widow sizes used by bootstrap
    var grid = {
        xsmall: 768, // <
        small: 768, // >=
        medium: 992, // >=
        large: 1200 // >=
    };

    //Utility methods for working with the JWT
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

    //Utility methods for communicating with the OrgBuilder API
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
                        //TODO FC-3
                        window.location = orgProps.clientOrigin + "/login.html?denied=true";
                    }
                    else if(status === 0){
                        // window.location = orgProps.clientOrigin + "/server-error.html";
                        console.log("SERVER ERROR"); //TODO FC-3
                    }
                    else if(status === 401){
                        //TODO FC-3
                        //This comes up during a bad login or if the token has expired
                        window.location = orgProps.clientOrigin + "/login.html";
                    }
                    else if(status >= 500){
                        //TODO FC-3
                        var error = jqXHR.responseJSON;
                        console.log("Critical error server-side, please check server logs for details");
                        console.log("Error Message: " + error.errorMessage);
                        // window.location = orgProps.clientOrigin + "/server-error.html";
                    }
                    else{
                        alert("Error communicating with server. Status: " + status);
                        //TODO FC-3
                    }
                });
        }
    };

    //A utility using a builder pattern to validate whether or not a user has access to a given part of the application
    var validateAccess = (function(){
        function Validator(){
            this.valid = true;
        }
        Validator.prototype = {
            constructor: Validator,
            hasToken: function(){
                //Only do this test if valid is still true
                if(this.valid){
                    //If there is no token, it's invalid
                    if(!jwt.tokenExists()){
                        this.valid = false;
                    }
                }

                //Return this with the current state of valid
                return this;
            },
            hasAllRoles: function(){
                //Only do this test if valid is still true
                if(this.valid){
                    var hasAllRoles = true;
                    //If arguments are provided, test each argument as a role to see if the token has that role
                    if(arguments){
                        $.each(arguments, function(index,role){
                            if(!jwt.hasRole(role)){
                                hasAllRoles = false;
                                return false;
                            }
                        });
                    }

                    //If it doesn't have all roles, set valid to false
                    if(!hasAllRoles){
                        this.valid = false;
                    }
                }

                //Return this with the current state of valid
                return this;
            },
            hasAnyRole: function(){
                //Only do this test if valid is still true
                if(this.valid){
                    var hasAnyRole = false;
                    //If arguments are provided, test each argument as a role to see if the token has that role
                    if(arguments){
                        $.each(arguments, function(index,role){
                            if(jwt.hasRole(role)){
                                hasAnyRole = true;
                                return false;
                            }
                        });
                    }

                    if(!hasAnyRole){
                        this.valid = false;
                    }
                }

                //Return this with the current state of valid
                return this;
            },
            isUser: function(userid){
                //Only do this test if valid is still true
                if(this.valid){
                    this.valid = jwt.getTokenPayload().uid == userid;
                }

                //Return this with the current state of valid
                return this;
            },
            isOrg: function(orgid){
                //Only do this test if valid is still true
                if(this.valid){
                    this.valid = jwt.getTokenPayload().oid == orgid;
                }

                //Return this with the current state of valid
                return this;
            },
            isValid: function(){
                return this.valid;
            }
        };

        return function(){
            return new Validator();
        }
    })();

    //Utility methods for validating data returned by the API.
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
                    userListData.userList !== undefined && userListData.userList !== null &&
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
        },
        member: function(memberData){
            return memberData != null &&
                    memberData.dateOfBirth !== undefined &&
                    memberData.memberId !== undefined &&
                    memberData.firstName !== undefined &&
                    memberData.middleName !== undefined &&
                    memberData.lastName !== undefined &&
                    memberData.sex !== undefined;
        },
        memberList: function(memberListData){
            var memberFn = this.member;
            return memberListData !== null &&
                    memberListData.memberList !== undefined && memberListData.memberList !== null &&
                    (function(){
                        var valid = true;
                        $.each(memberListData.memberList, function(index, member){
                            if(!memberFn(member)){
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
        if($(".panel.form-group[status = 'edit']").length > 0){
            return confirm("Are you sure you want to cancel? All changes will be lost.");
        }
        return true;
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

    var menus = (function(){
        function toggleMenu(event){
            event.preventDefault();
            $("#sidebar-wrapper .menu-collapse").collapse("hide");
            $("#wrapper").toggleClass("display-menu");
        }

        function parentItemAction(event){
            event.preventDefault();
            $("#sidebar-wrapper .menu-collapse").collapse("hide");
            if(!$("#wrapper").hasClass("display-menu")){
                $("#wrapper").addClass("display-menu");
            }
        }

        function displayAccessibleMenuItems(){
            if(jwt.hasRole(roles.master)){
                $("#sidebar-orgs-btn").removeClass("hidden");
                $("#sidebar-users-btn").removeClass("hidden");
            }
            else{
                if(jwt.hasRole(roles.admin)){
                    $("#sidebar-users-btn").removeClass("hidden");
                }

                if(jwt.hasRole(roles.read) || jwt.hasRole(roles.write)){
                    $("#sidebar-members-btn").removeClass("hidden");
                }
            }
        }

        var types = {
            main: "main",
            login: "login"
        };

        function loadTemplates(type){
            $.get(orgProps.clientOrigin + "/template/menus-template.html")
                .done(function(data){
                    //Add the html from the template file to the container divs on the current page
                    var navbar = $(data).filter("#navbar-template");
                    $("#navbar-container").html(navbar);
                    if(type !== types.login){
                        var sidebar = $(data).filter("#sidebar-template");
                        $("#sidebar-container").html(sidebar);
                    }

                    //Assign UI actions to the template elements
                    if(type !== types.login){
                        $(".sidebar-menu-btn > a").click(toggleMenu);
                        $(".sidebar-parent-item").click(parentItemAction);
                        $("#logoutBtn").click(jwt.clearToken);
                    }

                    //Display dynamic values in navbar
                    if(type !== types.login){
                        $("#userName").text(jwt.getTokenPayload().unm);
                        $("#user-profile-btn > a").attr("href", orgProps.clientOrigin + "/users/content.html?userid=" + jwt.getTokenPayload().uid);
                        if(!jwt.hasRole(roles.master)){
                            if(jwt.getTokenPayload().onm !== ""){
                                $(".org-brand").text(orgbuilder.jwt.getTokenPayload().onm);
                            }
                            $("#org-profile-btn > 1").attr("href", orgProps.clientOrigin + "/orgs/content.html?orgid=" + jwt.getTokenPayload().oid);
                        }
                        else{
                            $("#org-profile-btn").addClass("hidden");
                        }
                    }
                    else{
                        $("#navbar-user-menu").addClass("hidden");
                    }

                    //Fix path to link to link to home page, either index.html or login.html
                    if(type !== types.login){
                        $(".home-link").attr("href", orgProps.clientOrigin + "/index.html");
                    }
                    else{
                        $(".home-link").attr("href", orgProps.clientOrigin + "/login.html");
                    }
                    $(".origin-link").attr("href", orgProps.clientOrigin + $(".origin-link").attr("href"));

                    //Fix the logout button link, to use an absolute path to the login page
                    $("#logoutBtn").attr("href", orgProps.clientOrigin + "/login.html");

                    //Display the menu items that the user has access to
                    if(type !== types.login){
                        displayAccessibleMenuItems();
                    }

                    //Add cancellation behavior
                    $(".template-link").click(cancelChangesCheck);
                })
                .fail(function(jqXHR){
                    //TODO TC-3
                    console.log("Failed to log template. Status: " + jqXHR.status);
                });
        }

        function loadLoginTemplate(){
            loadTemplates(types.login);
        }

        function loadMainTemplate(){
            loadTemplates(types.main);
        }

        return {
            loadMainTemplate: loadMainTemplate,
            loadLoginTemplate: loadLoginTemplate
        }

    })();

    return{
        roles: roles,
        methods: methods,
        jwt: jwt,
        api: api,
        validateAccess: validateAccess,
        checkRequiredFields: checkRequiredFields,
        cancelChangesCheck: cancelChangesCheck,
        validateData: validateData,
        menus: menus
    }
})();

