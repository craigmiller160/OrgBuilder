<template>
    <div class="row">
        <div class="text-center col-xs-12 col-sm-6 col-sm-offset-3">
            <h2>Welcome to {{ orgName }}</h2>
        </div>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';

    export default {
        name: 'main',
        props: [
            'orgName'
        ],
        beforeMount(){
//            if(!orgbuilder.access().hasToken().isValid()){
//                //If no token exists, simple re-direct to login page
//                window.location.href = '/#/login';
//                return;
//            }
//            else{
//                var app = this;
//                //Otherwise, attempt to validate that the token hasn't expired
//                orgbuilder.api.get('/auth/check')
//                    .done(() => {
//                        app.loggedIn = true;
//                        window.location.href = '/#/';
//                    });
//            }
            orgbuilder.access.hasValidToken(this);
        }
    }
</script>