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

    //Store the JSON payload returned by the server to access later
    var dataObj;
    var data = {
        storeData: function(data){
            dataObj = data;
        },
        hasData: function(){
            return dataObj !== undefined;
        },
        getData: function(){
            return dataObj;
        }
    };

    //Utility methods for common functionality for the various content.html pages
    var content = (function(){
        function updateJsonMsg(jsonMsg, field){
            if($(field).attr("name") === undefined){
                return;
            }

            if($(field).is("input") || $(field).is("textarea")){
                jsonMsg[$(field).attr("name")] = $(field).val();
            }
            else if($(field).is("div.content-checkbox-group")){
                var vals = [];
                $.each($(field).find(".content-checkbox:checked"), function(index,checkbox){
                    vals.push($(checkbox).attr("name"));
                });
                jsonMsg[$(field).attr("name")] = vals;
            }
        }

        function toggleEdit(event,postProcessFn){
            var section = $(event.target).parents(".panel.form-group");
            if($(section).attr("status") === "view"){
                $(section).attr("status", "edit");
                $(section).find(".content-field, .content-checkbox").removeAttr("disabled");
                if(postProcessFn){
                    postProcessFn();
                }
            }
        }

        function orgSubmitAction(event, id){
            submitAction(event, "Org", id, "orgId", "orgs", orgbuilder.validateData.org);
        }

        function userSubmitAction(event, id, preValidationFn){
            submitAction(event, "User", id, "userId", "users", orgbuilder.validateData.user, preValidationFn);
        }

        function memberSubmitAction(event, id){
            submitAction(event, "Member", id, "memberId", "members", orgbuilder.validateData.member);
        }

        function submitAction(event, contentName, contentId, contentIdName, uri, returnDataValidationFn, preValidationFn){
            event.preventDefault();

            var fieldArray;
            var doneFn;
            if($(document.activeElement).hasClass("section-save")){
                console.log("Section save button");

                if($(document.activeElement).parents(".panel.form-group").length === 0){
                    console.log("Error! Cannot find edited section to save content from.");
                    orgbuilder.showAlert("alert-danger", "Cannot find edited section to save content from.");
                    return;
                }

                var section = $(document.activeElement).parents(".panel.form-group")[0];
                fieldArray = $(section).find(".content-field, .content-checkbox-group");

                doneFn = function(data){
                    if(returnDataValidationFn && !returnDataValidationFn(data)){
                        console.log("Error! Data returned from server for " + contentName + " does not contain the " + contentName + ".");
                        orgbuilder.showAlert("alert-danger", "Invalid data for " + contentName + " returned from server.");
                        return;
                    }

                    orgbuilder.showAlert("alert-success", contentName + " changes saved successfully");

                    orgbuilder.data.storeData(data);

                    $(section).attr("status", "view");
                    $(section).find(".content-field, .content-checkbox").attr("disabled", true);

                    var contents = $(section).find(".content");
                    $.each(contents, function(index,content){
                        var name = $(content).children(".content-field").attr("name");
                        $(content).children(".content-label:not(.password)").text(data[name]);
                        $(content).children(".content-field[type='password']").val("");
                    });
                }
            }
            else{
                console.log("Main save button");

                fieldArray = $(event.target).find(".panel.form-group[status = 'edit'] .content-field, .panel.form-group[status='edit'] .content-checkbox-group");

                doneFn = function(data){
                    window.location = (function(){
                        var url = window.location.pathname;
                        if(contentId){
                            url = url + "?" + contentIdName + "=" + contentId + "&";
                        }
                        else if(data !== undefined){
                            url = url + "?" + contentIdName + "=" + data[contentIdName] + "&";
                        }
                        else{
                            url = url + "?";
                        }

                        return url + "saved=true";
                    })();
                }
            }

            if(preValidationFn){
                preValidationFn();
            }

            var jsonMsg = orgbuilder.data.hasData() ? orgbuilder.data.getData() : {};

            $.each(fieldArray, function(index, field){
                updateJsonMsg(jsonMsg, field);
            });

            $.each($(".default-field"), function(index, field){
                updateJsonMsg(jsonMsg, field);
            });

            var failFn = function(){
                console.log("Save FAILED");
            };

            if(contentId){
                console.log("Updating existing " + contentName);
                orgbuilder.api.send(uri + "/" + contentId, orgbuilder.methods.put, jsonMsg)
                    .done(doneFn)
                    .fail(failFn);
            }
            else{
                console.log("Creating new " + contentName);
                orgbuilder.api.send(uri, orgbuilder.methods.post, jsonMsg)
                    .done(doneFn)
                    .fail(failFn);
            }
        }

        return {
            toggleEdit: toggleEdit,
            orgSubmitAction: orgSubmitAction,
            userSubmitAction: userSubmitAction,
            memberSubmitAction: memberSubmitAction
        }
    })();

    //Utility methods for communicating with the OrgBuilder API
    var api = {
        send: function(uri, method, json){
            console.log("Calling API: " + method + " " + uri);
            return $.ajax({
                url: orgProps.serverOrigin + ensurePrecedingSlash(uri),
                type: method,
                headers: (function(){
                    var result = {
                        /*"Access-Control-Request-Headers": [
                            "X-Requested-With",
                            "Authorization"
                        ],
                        "Access-Control-Request-Method": method*/
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
                        window.location = orgProps.clientOrigin + "/index.html?denied=true&message=" + jqXHR.responseJSON.errorMessage;
                    }
                    else if(status === 0){
                        console.log("SERVER ERROR");
                        showAlert("alert-danger", "Unable to contact server!");
                    }
                    else if(status === 401){
                        //This comes up during a bad login or if the token has expired
                        var loginUrl = orgProps.clientOrigin + "/login.html";
                        if(window.location.href !== loginUrl){
                            window.location = loginUrl;
                        }
                    }
                    else if(status >= 500){
                        var error = jqXHR.responseJSON;
                        console.log("Critical error server-side, please check server logs for details");
                        console.log("Error Message: " + error.exceptionName + ": " + error.errorMessage);
                        showAlert("alert-danger", "Server Error: " + error.exceptionName + ": " + error.errorMessage);
                    }
                    else{
                        console.log("Error communicating with server. Status: " + status + " " + jqXHR.statusText);
                        showAlert("alert-danger", "Unknown critical server error!");
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

    function cancelChangesCheck(event){
        if($(".panel.form-group[status = 'edit']").length > 0){
            modal.showCancel(function(){
                window.location = event.target.href;
            });
            return false;
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

        function loadNavbar(data, type){
            //Add the html from the template file to the container divs on the current page
            var navbar = $(data).filter("#navbar-template");
            $("#navbar-container").html(navbar);

            if(type !== types.login){
                //Assign UI actions to the template elements
                $("#logoutBtn > a").click(jwt.clearToken);
                $("#logoutBtn > a").attr("href", orgProps.clientOrigin + "/index.html");

                //Handle dynamic values
                $("#userName").text(jwt.getTokenPayload().unm);
                $("#user-profile-btn > a").attr("href", $("#user-profile-btn > a").attr("href") + "?userId=" + jwt.getTokenPayload().uid);
                if(!jwt.hasRole(roles.master)){
                    if(jwt.getTokenPayload().onm !== ""){
                        $(".org-brand").text(orgbuilder.jwt.getTokenPayload().onm);
                    }
                    $("#org-profile-btn > a").attr("href", $("#org-profile-btn > a").attr("href") + "?orgId=" + jwt.getTokenPayload().oid);
                }
                else{
                    $("#org-profile-btn").addClass("hidden");
                }

                //Fix path to link to link to home page, either index.html or login.html
                $(".home-link").attr("href", orgProps.clientOrigin + "/index.html");
            }
            else{
                $("#navbar-user-menu").addClass("hidden");

                //Fix path to link to link to home page, either index.html or login.html
                $(".home-link").attr("href", orgProps.clientOrigin + "/login.html");
            }
        }

        function loadSidebar(data, activeElement){
            //Add the html from the template file to the container divs on the current page
            var sidebar = $(data).filter("#sidebar-template");
            $("#sidebar-container").html(sidebar);

            //Assign UI actions to the template elements
            $(".sidebar-menu-btn > a").click(toggleMenu);
            $(".sidebar-parent-item").click(parentItemAction);

            displayAccessibleMenuItems();

            if(activeElement){
                $("#" + activeElement + " > a").addClass("active");
            }
        }

        function loadModals(data){
            //Add the modal templates, if a wrapper exists there
            if($("#modal-wrapper").length > 0){
                var modal = $(data).filter("#modal-template");
                $("#modal-wrapper").html(modal);
            }

            $(".org-modal .modal-yes").click(function(event){
                $(event.target).parents(".modal-footer").attr("status", "yes");
            });

            $(".org-modal .modal-no").click(function(event){
                $(event.target).parents(".modal-footer").attr("status", "no");
            });
        }

        function loadTemplates(type, activeElement){
            $.get(orgProps.clientOrigin + "/template/menus-template.html")
                .done(function(data){
                    loadNavbar(data, type);
                    loadModals(data);

                    if(type !== types.login){
                        loadSidebar(data, activeElement);

                        //Fix links to be absolute paths using the clientOrigin
                        $.each($(".template-link"), function(index,link){
                            $(link).attr("href", orgProps.clientOrigin + $(link).attr("href"));
                        });
                    }

                    //Add cancellation behavior
                    $(".template-link, .home-link").click(cancelChangesCheck);
                })
                .fail(function(jqXHR){
                    console.log("Failed to log template. Status: " + jqXHR.status);
                    showAlert("alert-danger", "Unable to load all content for page.");
                });
        }

        function loadLoginTemplate(){
            loadTemplates(types.login);
        }

        function loadMainTemplate(activeElement){
            loadTemplates(types.main, activeElement);
        }

        return {
            loadMainTemplate: loadMainTemplate,
            loadLoginTemplate: loadLoginTemplate
        }

    })();

    var modal = (function(){

        function getModal(type, item){
            if(type === "cancel"){
                return $("#cancelWarningModal");
            }
            else{
                $("#deleteWarningModal .type").text(item);
                return $("#deleteWarningModal");
            }
        }

        function showModal(type, callback, item){
            var modal = getModal(type, item);
            $(modal).on("hidden.bs.modal", function(event){
                var status = $(event.target).find(".modal-footer").attr("status");
                $(event.target).find(".modal-footer").removeAttr("status");
                if("yes" === status && callback !== undefined && callback !== null){
                    callback();
                }
            });

            $(modal).modal({
                backdrop: "static"
            });
        }

        function showCancel(callback){
            return showModal("cancel", callback);
        }

        function showDelete(item, callback){
            return showModal("delete", callback, item);
        }

        return {
            showCancel: showCancel,
            showDelete: showDelete
        }
    })();

    //Utility function to show an alert on the page
    function showAlert(clazz, message){
        if($("#actionAlert").length > 0){
            $("#actionAlert > p").text(message);
            $("#actionAlert").addClass(clazz);
            $("#actionAlert").addClass("in");
        }
    }

    return{
        roles: roles,
        methods: methods,
        jwt: jwt,
        api: api,
        validateAccess: validateAccess,
        checkRequiredFields: checkRequiredFields,
        cancelChangesCheck: cancelChangesCheck,
        validateData: validateData,
        menus: menus,
        data: data,
        showAlert: showAlert,
        content: content,
        modal: modal
    }
})();

