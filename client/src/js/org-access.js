import { orgbuilder } from './orgbuilder.js';

orgbuilder.access = (() => {

    // function Validator(){
    //     this.valid = true;
    // }
    //
    // Validator.prototype = {
    //     constructor: Validator,
    //     hasToken () {
    //         //Only do this test if valid is still true
    //         if (this.valid) {
    //             //If there is no token, it's invalid
    //             if (!orgbuilder.jwt.tokenExists()) {
    //                 this.valid = false;
    //             }
    //         }
    //
    //         //Return this with the current state of valid
    //         return this;
    //     },
    //     hasAllRoles () {
    //         //Only do this test if valid is still true
    //         if (this.valid) {
    //             var hasAllRoles = true;
    //             //If arguments are provided, test each argument as a role to see if the token has that role
    //             if (arguments) {
    //                 $.each(arguments, function (index, role) {
    //                     if (!orgbuilder.jwt.hasRole(role)) {
    //                         hasAllRoles = false;
    //                         return false;
    //                     }
    //                 });
    //             }
    //
    //             //If it doesn't have all roles, set valid to false
    //             if (!hasAllRoles) {
    //                 this.valid = false;
    //             }
    //         }
    //
    //         //Return this with the current state of valid
    //         return this;
    //     },
    //     hasAnyRole() {
    //         //Only do this test if valid is still true
    //         if (this.valid) {
    //             var hasAnyRole = false;
    //             //If arguments are provided, test each argument as a role to see if the token has that role
    //             if (arguments) {
    //                 $.each(arguments, function (index, role) {
    //                     if (orgbuilder.jwt.hasRole(role)) {
    //                         hasAnyRole = true;
    //                         return false;
    //                     }
    //                 });
    //             }
    //
    //             if (!hasAnyRole) {
    //                 this.valid = false;
    //             }
    //         }
    //
    //         //Return this with the current state of valid
    //         return this;
    //     },
    //     isUser(userid) {
    //         //Only do this test if valid is still true
    //         if (this.valid) {
    //             this.valid = orgbuilder.jwt.getTokenPayload().uid == userid;
    //         }
    //
    //         //Return this with the current state of valid
    //         return this;
    //     },
    //     isOrg(orgid) {
    //         //Only do this test if valid is still true
    //         if (this.valid) {
    //             this.valid = orgbuilder.jwt.getTokenPayload().oid == orgid;
    //         }
    //
    //         //Return this with the current state of valid
    //         return this;
    //     },
    //     isValid() {
    //         return this.valid;
    //     }
    // };
    //
    // return function(){
    //     return new Validator();
    // }

    function doesTokenExist(app){
        if(!orgbuilder.jwt.tokenExists()){
            //If no token exists, simple re-direct to login page
            if(app !== undefined){
                app.$emit('loggedIn', false);
            }
            window.location.href = '/#/login';
            return false;
        }

        return true;
    }

    function checkTokenExp(app, denied){
        orgbuilder.api.get('auth/check')
            .done(() => {
                if(app !== undefined){
                    app.$emit('loggedIn', true);
                }

                if(denied){
                    window.location.href = '/#/?denied=true';
                }
            })
            .fail(() => {
                if(app !== undefined){
                    app.$emit('loggedIn', false);
                }
                window.location.href = '/#/login';
            });
    }

    function hasValidToken(app){
        if(doesTokenExist(app)){
            checkTokenExp(app, false);
        }
    }

    function hasMasterAccess(app){
        if(doesTokenExist(app)){
            if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                checkTokenExp(app, false);
            }
            else{
               checkTokenExp(app, true);
            }
        }
    }

    function isSameOrg(orgId){
        return orgbuilder.jwt.getTokenPayload().oid == orgId;
    }

    function hasMasterAccessOrSameOrg(app, orgId){
        if(doesTokenExist(app)){
            if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                checkTokenExp(app, false);
            }
            else if(isSameOrg(orgId)){
                checkTokenExp(app, false);
            }
            else{
                checkTokenExp(app, true);
            }
        }
    }

    function hasAdminOrHigherAccess(app){
        if(doesTokenExist(app)){
            if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master) ||
                orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.admin)){
                checkTokenExp(app, false);
            }
            else{
                checkTokenExp(app, true);
            }
        }
    }

    return {
        hasValidToken: hasValidToken,
        hasMasterAccess: hasMasterAccess,
        hasMasterAccessOrSameOrg: hasMasterAccessOrSameOrg,
        hasAdminOrHigherAccess: hasAdminOrHigherAccess
    }

})();