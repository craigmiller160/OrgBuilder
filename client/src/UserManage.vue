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
                                <td>
                                    <a :href="'/#/users/content?userId=' + user.userId" class="btn btn-info" title="Edit User">Edit</a>
                                    <a class="btn btn-danger" title="Delete User" @click="showModal">Delete</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <a class="btn btn-primary" href="/#/users/content" title="Add new User">Add</a>
            </div>
        </div>
        <app-modal :context="modalContext"
                   v-on:result="modalResult($event)">
        </app-modal>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';
    import ConfirmModal from './ConfirmModal.vue';

    export default {
        name: 'user_manage',
        data(){
            return {
                userList: [],
                modalContext: {
                    type: '',
                    id: 0
                }
            }
        },
        components: {
            'app-modal': ConfirmModal
        },
        beforeMount(){
            orgbuilder.access.start(this)
                .hasAnyRole(orgbuilder.jwt.roles.master, orgbuilder.jwt.roles.admin)
                .validate();
        },
        mounted(){
            this.loadUsers();
        },
        methods: {
            loadUsers(){
                orgbuilder.api.get('users')
                    .done((data,status,jqXHR) => {
                        if(jqXHR.status === 204){
                            console.log('No users found on server');
                            return;
                        }

                        this.userList = data.userList;
                    });
            },
            showModal(event){
                this.modalContext.id = $(event.target).parents('tr').attr('userId');
                this.modalContext.type = 'Delete';
                $(".modal").modal({
                    backdrop: 'static'
                });
            },
            modalResult(arg){
                if(arg.status){
                    var app = this;

                    orgbuilder.api.del('users/' + arg.context.id)
                        .done(() => {
                            console.log('User successfully deleted');
                            app.loadUsers();
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'User successfully deleted',
                                clazz: 'alert-success'
                            });
                        })
                        .fail(() => console.log('Org delete FAILED'));
                }
            }
        }
    }
</script>