<template>
    <div>
        <app-navbar :loggedIn="loggedIn"
                    :orgName="orgName"
                    @loggedIn="loggedIn = $event"
                    :expandSidebar="expandSidebar"
                    @expandSidebar="expandSidebar = $event">
        </app-navbar>
        <div id="wrapper" v-bind:class="{ displayMenu : expandSidebar}">
            <app-sidebar :expandSidebar="expandSidebar"
                         @expandSidebar="expandSidebar = $event"
                         :loggedIn="loggedIn">
            </app-sidebar>
            <div class="container" id="page-content-wrapper">
                <app-alert :alert="alert"></app-alert>
                <router-view @loggedIn="loggedIn = $event"
                             @showAlert="alert = $event"
                             :orgName="orgName">
                </router-view>
            </div>
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
                },
                expandSidebar: false
            }
        },
        components: {
            'app-navbar': Navbar,
            'app-sidebar': Sidebar,
            'app-alert': Alert
        },
        computed: {
            orgName() {
                if(this.loggedIn){
                    var onm = orgbuilder.jwt.getTokenPayload().onm;
                    if(onm !== undefined && onm !== null && onm.length > 0){
                        return onm;
                    }
                }

                return 'OrgBuilder';
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

    :root {
        --bars-background-color: #337ab7;
        --bars-foreground-color: #fff;
        --bars-highlight-color:  #368cb7;
        --bars-border-color: #2e6da4;
    }

    #page-content-wrapper {
        width: 100%;
        height: 100%;
        position: absolute;
        padding-left: 80px;
        transition: margin 0.5s, padding 0.5s;
        -moz-transition: margin 0.5s, padding 0.5s;
        -webkit-transition: margin 0.5s, padding 0.5s;
        -o-transition: margin 0.5s, padding 0.5s;
    }

    .expandable[aria-expanded = 'true'] .caret {
        -ms-transform: rotate(180deg);
        -webkit-transform: rotate(180deg);
        -moz-transform: rotate(180deg);
        -o-transform: rotate(180deg);
        transform: rotate(180deg);
    }

    /* Sidebar Show/Hide Styles */
    #wrapper.displayMenu #sidebar {
        width: 200px;
    }

    #wrapper.displayMenu #sidebar .sidebar-text {
        display: inline;
    }

    #wrapper.displayMenu #page-content-wrapper {
        padding-left: 200px;
    }

    /* Sidebar phone styles */
    @media screen and (max-width: 768px){

        #wrapper.displayMenu #page-content-wrapper {
            padding-left: 0;
            margin-left: 200px;
            overflow: hidden;
        }

        #page-content-wrapper {
            padding-left: 0;
            margin-left: 0;
        }
    }

    /* Form styles */

    .content-name {
        font-weight: bold;
    }
</style>