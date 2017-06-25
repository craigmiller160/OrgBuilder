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