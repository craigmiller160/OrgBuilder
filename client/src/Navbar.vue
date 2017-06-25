<template>
    <div id="navbar">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <!-- Navbar Header -->
                <div class="navbar-header">
                    <a :href="homeLink" class="navbar-brand">OrgBuilder</a>
                </div>

                <!-- Navbar Items -->
                <div>
                    <ul class="nav navbar-nav navbar-right">
                        <li v-show="loggedIn" class="dropdown">
                            <a href="#" class="dropdown-toggle expandable" data-toggle="dropdown">
                                <i class="glyphicon glyphicon-user"></i> <span>User</span> <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="/#/users/content"> <!-- TODO try changing this to v-link in the future, plus other related links -->
                                        <i class="glyphicon glyphicon-user"></i> User Profile
                                    </a>
                                </li>
                                <li>
                                    <a href="/#/orgs/content">
                                        <i class="glyphicon glyphicon-globe"></i> Org Profile
                                    </a>
                                </li>
                                <li>
                                    <a href="/#/login">
                                        <i class="glyphicon glyphicon-off"></i> Logout
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#">
                                <i class="glyphicon glyphicon-menu-hamburger"></i> Menu
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
</template>

<script>
    var mainLink = "/#/main";
    var loginLink = "/#/login";

    export default {
        name: 'navbar',
        props: [
            'loggedIn'
        ],
        data() {
            return {

            }
        },
        computed: {
            homeLink() {
                return this.loggedIn ? mainLink : loginLink;
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

    .navbar.navbar-default,
    .navbar.navbar-default .navbar-brand,
    .nav.navbar-nav.navbar-right li > a,
    .nav.navbar-nav.navbar-right li > ul {
        color: #fff;
        background-color: #337ab7;
        border-color: #2e6da4;
    }

    .navbar.navbar-default {
        border-radius: 0;
    }

    .navbar.navbar-default .navbar-brand:hover,
    .nav.navbar-nav.navbar-right li > a:hover {
        background-color: #368cb7;
    }
</style>