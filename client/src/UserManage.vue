<template>
    <div>
        <div class="row">
            <div class="col-xs-12 text-center">
                <h2>Users</h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <div class="panel panel-primary">
                    <table class="table table-primary table-striped">
                        <thead>
                            <tr>
                                <th>Email</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Org</th>
                                <th>Roles</th>
                                <th>Options</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="user in userList" :userId="user.userId">
                                <td>{{ user.userEmail }}</td>
                                <td>{{ user.firstName }}</td>
                                <td>{{ user.lastName }}</td>
                                <td>{{ user.orgName }}</td>
                                <td>{{ user.roles }}</td>
                                <td>Options Will Go Here</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';

    export default {
        name: 'user_manage',
        data(){
            return {
                userList: []
            }
        }
    }
</script>