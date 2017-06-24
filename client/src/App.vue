<template>
    <div>
        <app-navbar></app-navbar>
        <app-sidebar></app-sidebar>
    </div>
</template>

<script>
    import Sidebar from './Sidebar.vue'
    import Navbar from './Navbar.vue'
    export default {
        name: 'app',
        data () {
            return {
                msg: 'Welcome to Your Vue.js App'
            }
        },
        components: {
            'app-navbar': Navbar
            'app-sidebar': Sidebar
        }
    }
</script>