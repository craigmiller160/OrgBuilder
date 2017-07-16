<template>
    <div v-show="loggedIn" id="sidebar">
        <ul class="sidebar-nav nav flex-column">
            <li>
                <a :href="noChangeUrl" @click="toggleSidebar" class="sidebar-menu-btn">
                    <i class="glyphicon glyphicon-menu-hamburger"></i><span class="sidebar-text"> Menu</span>
                </a>
            </li>
            <li>
                <a :href="createUri('#/')" :class="{active : homeActive}">
                    <i class="glyphicon glyphicon-home"></i><span class="sidebar-text"> Home</span>
                </a>
            </li>
            <li v-show="showOrgsBtn">
                <a :href="noChangeUrl" :class="{active : orgsActive}" class="expandable" data-toggle="collapse" data-target="#orgs-collapse" @click="doShowSidebar">
                    <i class="glyphicon glyphicon-globe"></i><span class="sidebar-text"> Orgs <span class="caret"></span></span>
                </a>
                <ul id="orgs-collapse" class="collapse menu-collapse">
                    <li>
                        <a :href="createUri('#/orgs/manage')">Manage</a>
                    </li>
                </ul>
            </li>
            <li v-show="showUsersBtn">
                <a :href="noChangeUrl" :class="{active : usersActive}" class="expandable" data-toggle="collapse" data-target="#users-collapse" @click="doShowSidebar">
                    <i class="glyphicon glyphicon-user"></i><span class="sidebar-text"> Users <span class="caret"></span></span>
                </a>
                <ul id="users-collapse" class="collapse menu-collapse">
                    <li>
                        <a :href="createUri('#/users/manage')">Manage</a>
                    </li>
                </ul>
            </li>
            <li v-show="showMembersBtn">
                <a :href="noChangeUrl" :class="{active : membersActive}" class="expandable" data-toggle="collapse" data-target="#members-collapse" @click="doShowSidebar">
                    <i class="glyphicon glyphicon-th-list"></i><span class="sidebar-text"> Members <span class="caret"></span></span>
                </a>
                <ul id="members-collapse" class="collapse menu-collapse">
                    <li>
                        <a :href="createUri('#/members/manage')">Manage</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';

    export default {
        name: 'sidebar',
        props: [
            'expandSidebar',
            'loggedIn'
        ],
        methods: {
            createUri(uri){
                return orgbuilder.createUri(uri);
            },
            toggleSidebar(){
                if(this.expandSidebar){ //Test for true here because it hasn't been changed to false yet
                    $(".menu-collapse").collapse("hide");
                }
                this.$emit('expandSidebar', !this.expandSidebar);
            },
            doShowSidebar(){
                this.$emit('expandSidebar', true);
            }
        },
        computed: {
            showOrgsBtn(){
                if(!orgbuilder.jwt.tokenExists()){
                    return false;
                }
                return orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master);
            },
            showUsersBtn(){
                if(!orgbuilder.jwt.tokenExists()){
                    return false;
                }
                return orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master) ||
                        orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.admin);
            },
            showMembersBtn(){
                if(!orgbuilder.jwt.tokenExists()){
                    return false;
                }
                return !orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master);
            },
            noChangeUrl(){
                console.log('NoChange: ' + this.$route.fullPath);
                return orgbuilder.createUri('#' + this.$route.fullPath);
            },
            homeActive(){
                return this.$route.path === '/';
            },
            orgsActive(){
                return this.$route.path.includes('orgs');
            },
            usersActive() {
                return this.$route.path.includes('users');
            },
            membersActive(){
                return this.$route.path.includes('members');
            }
        }
    }
</script>

<style>
    #sidebar {
        z-index: 1;
        position: absolute;
        width: 80px;
        height: 100%;
        overflow-y: hidden;
        border-right: 1px solid #2e6da4;
        transition: width 0.5s;
        -moz-transition: width 0.5s;
        -webkit-transition: width 0.5s;
        -o-transition: width 0.5s;
    }

    .sidebar-nav {
        padding: 0;
        list-style: none;
    }

    .sidebar-nav li {
        text-indent: 10px;
        line-height: 40px;
    }

    .sidebar-nav li a {
        display: block;
        text-decoration: none;
        font-size: 14px;
        white-space: nowrap;
    }

    .menu-collapse {
        list-style: none;
    }

    .sidebar-nav li a .sidebar-text {
        display: none;
    }

    #sidebar .glyphicon {
        text-indent: 0;
        margin-right: 5px;
    }

    #sidebar,
    #sidebar > .sidebar-nav.nav li > a {
        color: var(--bars-foreground-color);
        background-color: var(--bars-background-color);
        border-color: var(--bars-border-color);
    }

    #sidebar > .sidebar-nav.nav li > a:hover,
    #sidebar > .sidebar-nav.nav li > a.active {
        background-color: var(--bars-highlight-color);
    }

    /* Sidebar phone style */

    @media screen and (max-width: 768px){
        #sidebar {
            width: 0;
        }

        #sidebar > .sidebar-nav > .sidebar-menu-btn {
            display: none;
        }
    }
</style>