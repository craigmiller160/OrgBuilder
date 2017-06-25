<template>
    <div id="sidebar">
        <ul class="sidebar-nav nav flex-column">
            <li>
                <a href="#" @click="toggleSidebar">
                    <i class="glyphicon glyphicon-menu-hamburger"></i><span class="sidebar-text"> Menu</span>
                </a>
            </li>
            <li>
                <a href="/">
                    <i class="glyphicon glyphicon-home"></i><span class="sidebar-text"> Home</span>
                </a>
            </li>
            <li> <!-- TODO might need to add hidden here -->
                <a href="#" class="expandable" data-toggle="collapse" data-target="#orgs-collapse">
                    <i class="glyphicon glyphicon-globe"></i><span class="sidebar-text"> Orgs <span class="caret"></span></span>
                </a>
                <ul id="orgs-collapse" class="collapse menu-collapse">
                    <li>
                        <a href="/#/orgs/manage">Manage</a>
                    </li>
                </ul>
            </li>
            <li> <!-- TODO might need to add hidden here -->
                <a href="#" class="expandable" data-toggle="collapse" data-target="#users-collapse">
                    <i class="glyphicon glyphicon-user"></i><span class="sidebar-text"> Users <span class="caret"></span></span>
                </a>
                <ul id="users-collapse" class="collapse menu-collapse">
                    <li>
                        <a href="/#/users/manage">Manage</a>
                    </li>
                </ul>
            </li>
            <li> <!-- TODO might need to add hidden here -->
                <a href="#" class="expandable" data-toggle="collapse" data-target="#members-collapse">
                    <i class="glyphicon glyphicon-th-list"></i><span class="sidebar-text"> Members <span class="caret"></span></span>
                </a>
                <ul id="members-collapse" class="collapse menu-collapse">
                    <li>
                        <a href="/#/members/manage">Manage</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: 'sidebar',
        props: [
            'showSidebar'
        ],
        methods: {
            toggleSidebar(){
                this.$emit('showSidebar', !this.showSidebar);
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
    #sidebar > .sidebar-nav.nav li > a:active {
        background-color: var(--bars-highlight-color);
    }
</style>