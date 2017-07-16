import { orgbuilder } from './orgbuilder.js';

orgbuilder.access = (() => {

    function doesTokenExist(app){
        if(!orgbuilder.jwt.tokenExists()){
            //If no token exists, simple re-direct to login page
            if(app !== undefined){
                app.$emit('loggedIn', false);
            }
            window.location.href = orgbuilder.createUri('#/login');
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
                    window.location.href = orgbuilder.createUri('#/?denied=true');
                }
            })
            .fail(() => {
                if(app !== undefined){
                    app.$emit('loggedIn', false);
                }
                window.location.href = orgbuilder.createUri('#/login');
            });
    }

    class Validator {

        constructor(app, valid, denied){
            this.app = app;
            this.valid = valid !== undefined ? valid : doesTokenExist(app);
            this.denied = denied !== undefined ? denied : false;
            this.priorSuccess = valid !== undefined ? valid : false;
        }

        hasAnyRole(){
            if(this.valid){
                var hasAnyRole = false;
                if (arguments) {
                    $.each(arguments, function (index, role) {
                        if (orgbuilder.jwt.hasRole(role)) {
                            hasAnyRole = true;
                            return false;
                        }
                    });

                    if(!hasAnyRole){
                        this.valid = false;
                        this.denied = true;
                    }
                }
            }

            return this;
        }

        hasAllRoles(){
            if(this.valid){
                var hasAllRoles = true;
                if (arguments) {
                    $.each(arguments, function (index, role) {
                        if (!orgbuilder.jwt.hasRole(role)) {
                            hasAllRoles = false;
                            return false;
                        }
                    });
                }

                if(!hasAllRoles){
                    this.valid = false;
                    this.denied = true;
                }
            }

            return this;
        }

        isOrg(orgid){
            if (this.valid) {
                this.valid = orgbuilder.jwt.getTokenPayload().oid == orgid;
                this.denied = !this.valid;
            }

            return this;
        }

        isUser(userid){
            if (this.valid) {
                this.valid = orgbuilder.jwt.getTokenPayload().uid == userid;
                this.denied = !this.valid;
            }

            return this;
        }

        next(){
            if(this.valid){
                return new Validator(this.app, true);
            }
            else{
                return new Validator(this.app, true, this.denied);
            }
        }

        validate(){
            //No matter what, check the token expiration too
            if(this.priorSuccess){
                checkTokenExp(this.app, false);
            }
            else{
                checkTokenExp(this.app, this.denied);
            }
        }
    }

    function start(app){
        return new Validator(app);
    }

    return {
        start: start
    }

})();