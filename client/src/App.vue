<template>
    <div>
        <app-navbar :loggedIn="loggedIn"></app-navbar>
        <app-sidebar></app-sidebar>
        <div class="container">
            <app-alert :alert="alert"></app-alert>
            <router-view @loggedIn="loggedIn = $event" @showAlert="alert = $event"></router-view>
        </div>
    </div>
</template>

<script>
    import Sidebar from './Sidebar.vue';
    import Navbar from './Navbar.vue';
    import Alert from './Alert.vue';
    import { orgbuilder } from './js/orgbuilder.js';
    export default {
        name: 'app',
        data () {
            return {
                loggedIn: false,
                alert: {
                    show: false,
                    msg: '',
                    clazz: ''
                }
            }
        },
        components: {
            'app-navbar': Navbar,
            'app-sidebar': Sidebar,
            'app-alert': Alert
        },
        beforeMount(){
            if(!orgbuilder.access().hasToken().isValid()){
                //If no token exists, simple re-direct to login page
                window.location.href = '/#/login';
                return;
            }
            else{
                var app = this;
                //Otherwise, attempt to validate that the token hasn't expired
                orgbuilder.api.get('/auth/check')
                    .done(() => {
                        app.loggedIn = true;
                        window.location.href = '/#/main';
                    });
            }
        }
    }
</script>

<style>
    @import url('https://fonts.googleapis.com/css?family=Ubuntu');

    html, body {
        height: 100%;
        font-family: 'Ubuntu', sans-serif;
    }

    body {
        background-color: #d9edf7;
    }
</style>