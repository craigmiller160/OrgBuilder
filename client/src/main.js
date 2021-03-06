//Import URI.js
import * as URI from 'uri-js';

//Import these orgbuilder js files so the orgbuilder object can be built properly and provided as a single import
import './js/orgbuilder.js';
import './js/org-jwt.js';
import './js/org-api.js';
import './js/org-access';
import './js/org-vue.js';

import Vue from 'vue';
import VueRouter from 'vue-router';
import App from './App.vue';
import Login from './Login.vue';
import Main from './Main.vue';
import OrgManage from './OrgManage.vue';
import OrgContent from './OrgContent.vue';
import UserManage from './UserManage.vue';
import UserContent from './UserContent.vue';
import MemberManage from './MemberManage.vue';
import MemberContent from './MemberContent.vue';

Vue.use(VueRouter);

const routes = [
    {
        path: '/login',
        component: Login
    },
    {
        path: '/',
        component: Main
    },
    {
        path: '/orgs/manage',
        component: OrgManage
    },
    {
        path: '/orgs/content',
        component: OrgContent
    },
    {
        path: '/users/manage',
        component: UserManage
    },
    {
        path: '/users/content',
        component: UserContent
    },
    {
        path: '/members/manage',
        component: MemberManage
    },
    {
        path: '/members/content',
        component: MemberContent
    }
];

const router = new VueRouter({
    routes: routes
});

new Vue({
    el: '#app',
    router: router,
    render: (h) => h(App)
});
