<template>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <!-- Navbar Header -->
            <div class="navbar-header">
                <a :href="homeLink" class="navbar-brand">{{ orgName }}</a>
            </div>

            <!-- Navbar Items -->
            <div>
                <ul class="nav navbar-nav navbar-right">
                    <li v-show="loggedIn" class="dropdown">
                        <a href="#" class="dropdown-toggle expandable" data-toggle="dropdown">
                            <i class="glyphicon glyphicon-user"></i> {{ currentUserName }} <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a :href="currentUserProfileLink">
                                    <i class="glyphicon glyphicon-user"></i> User Profile
                                </a>
                            </li>
                            <li v-show="showOrgProfile">
                                <a :href="currentOrgProfileLink">
                                    <i class="glyphicon glyphicon-globe"></i> Org Profile
                                </a>
                            </li>
                            <li>
                                <a :href="createUri('#/login')" @click="logout">
                                    <i class="glyphicon glyphicon-off"></i> Logout
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="sidebar-menu-btn">
                        <a :href="noChangeUrl" @click="toggleSidebar">
                            <i class="glyphicon glyphicon-menu-hamburger"></i> Menu
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';

    var mainLink = orgbuilder.createUri('#/');
    var loginLink = orgbuilder.createUri('#/login');

    export default {
        name: 'navbar',
        props: [
            'orgName',
            'loggedIn',
            'expandSidebar'
        ],
        data() {
            return {

            }
        },
        computed: {
            homeLink() {
                return this.loggedIn ? mainLink : loginLink;
            },
            currentUserName() {
                if(this.loggedIn){
                    var unm = orgbuilder.jwt.getTokenPayload().unm;
                    if(unm !== undefined && unm !== null && unm.length > 0){
                        return unm;
                    }
                }

                return 'User';
            },
            noChangeUrl(){
                return orgbuilder.createUri('#' + this.$route.fullPath);
            },
            showOrgProfile(){
                return this.loggedIn && !orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master);
            },
            currentUserProfileLink(){
                var url = orgbuilder.createUri('#/users/content');
                if(this.loggedIn){
                    url = url + '?userId=' + orgbuilder.jwt.getTokenPayload().uid;
                }

                return url;
            },
            currentOrgProfileLink(){
                var url = orgbuilder.createUri('#/orgs/content');
                if(this.loggedIn){
                    url = url + '?orgId=' + orgbuilder.jwt.getTokenPayload().oid;
                }

                return url;
            }
        },
        methods: {
            createUri(uri){
                return orgbuilder.createUri(uri);
            },
            logout(){
                orgbuilder.jwt.clearToken();
                this.$emit('loggedIn', false);
            },
            toggleSidebar(){
                this.$emit('expandSidebar', !this.expandSidebar);
            }
        }
    }
</script>

<style>
    .dropdown-menu {
        transform-origin: top;
        -webkit-transform-origin: top;
        -moz-transform-origin: top;
        -o-transform-origin: top;
        -ms-transform-origin: top;

        animation-fill-mode: forwards;
        -webkit-animation-fill-mode: forwards;
        -moz-animation-fill-mode: forwards;
        -o-animation-fill-mode: forwards;

        transition: all 0.2s linear;
        -webkit-transition: all 0.2s linear;
        -moz-transition: all 0.2s linear;
        -o-transition: all 0.2s linear;

        transform: scale(1,0);
        -webkit-transform: scale(1,0);
        -moz-transform: scale(1,0);
        -o-transform: scale(1,0);
        -ms-transform: scale(1,0);

        display: block;
    }

    .open > .dropdown-menu {
        transform: scale(1,1);
        -webkit-transform: scale(1,1);
        -moz-transform: scale(1,1);
        -o-transform: scale(1,1);
        -ms-transform: scale(1,1);
    }

    nav.navbar {
        padding-bottom: 0;
        margin-bottom: 0;
    }

    nav.navbar,
    nav.navbar .navbar-brand,
    nav.navbar ul.nav.navbar-nav li > ul,
    nav.navbar ul.nav.navbar-nav li > a {
        color: var(--bars-foreground-color);
        background-color: var(--bars-background-color);
        border-color: var(--bars-border-color);
    }

    nav.navbar {
        border-radius: 0;
    }

    nav.navbar .navbar-brand:hover,
    nav.navbar .nav.navbar-nav.navbar-right li > a:hover{
        background-color: var(--bars-highlight-color);
    }

    nav.navbar > div > div > ul.navbar-right > li.sidebar-menu-btn {
        display: none;
    }

    /* Phone style */

    @media screen and (max-width: 768px){

        nav.navbar > div > div > ul.navbar-right > li.sidebar-menu-btn {
            display: block;
        }

    }
</style>