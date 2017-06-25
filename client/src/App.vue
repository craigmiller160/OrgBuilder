<template>
    <div>
        <app-navbar :loggedIn="loggedIn"></app-navbar>
        <app-sidebar></app-sidebar>
        <router-view @loggedIn="loggedIn = $event"></router-view>
    </div>
</template>

<script>
    import Sidebar from './Sidebar.vue';
    import Navbar from './Navbar.vue';
    import Login from './Login.vue';
    import { orgbuilder } from './js/orgbuilder.js';
    export default {
        name: 'app',
        data () {
            return {
                loggedIn: false
            }
        },
        components: {
            'app-navbar': Navbar,
            'app-sidebar': Sidebar
        },
        beforeMount(){
            if(!orgbuilder.access().hasToken().isValid()){
                //If no token exists, simple re-direct to login page
                console.log('User needs to log in');
                window.location = '/#/login';
                return;
            }
            else{
                var app = this;
                //Otherwise, attempt to validate that the token hasn't expired
                orgbuilder.api.get('/auth/check')
                    .done(function(){
                        console.log(app.loggedIn);
                        app.loggedIn = true;
                    });
            }
        }
    }
</script>