<template>
    <div>
        <div class="row">
            <div class="col-xs-12 text-center">
                <h2>Members</h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <div class="panel panel-primary">
                    <table class="table table-primary table-striped">
                        <thead>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Age</th>
                            <th>City</th>
                            <th>State</th>
                            <th>Options</th>
                        </thead>
                        <tbody>
                            <tr v-for="member in memberList" :memberId="member.memberId">
                                <td>{{ member.firstName }}</td>
                                <td>{{ member.lastName }}</td>
                                <td>{{ parseField('age', member.dateOfBirth) }}</td>
                                <td>{{ parseField('city', member.addresses) }}</td>
                                <td>{{ parseField('state', member.addresses) }}</td>
                                <td>
                                    <a :href="'/#/members/content?memberId=' + member.memberId" class="btn btn-info" title="Edit Member">Edit</a>
                                    <a v-show="edit" class="btn btn-danger" title="Delete Member" @click="showModal">Delete</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <a class="btn btn-primary" href="/#/members/content" title="Add Member">Add</a>
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
        name: 'member_manage',
        data(){
            return {
                memberList: [],
                modalContext: {
                    type: '',
                    id: 0
                },
                edit: false
            }
        },
        components: {
            'app-modal': ConfirmModal
        },
        beforeMount(){
            orgbuilder.access.start(this)
                .hasAnyRole(orgbuilder.jwt.roles.write, orgbuilder.jwt.roles.read)
                .validate();
        },
        mounted(){
            this.edit = orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.write);
            this.loadMembers();
        },
        methods: {
            parseField(field, value){
                if('age' === field){
                    let result = orgbuilder.calculateAge(value);
                    return result;
                }
                else if('city' === field && value.length > 0){
                    //Member list JSON payload only includes primary contact info
                    return value[0].city;
                }
                else if('state' === field && value.length > 0){
                    //Member list JSON payload only includes primary contact info
                    return value[0].state;
                }
            },
            loadMembers(){
                orgbuilder.api.get('members')
                    .done((data, status, jqXHR) => {
                        if(jqXHR.status === 204){
                            console.log("No members found on server");
                            return;
                        }

                        this.memberList = data.memberList;
                    });
            },
            showModal(event){
                this.modalContext.id = $(event.target).parents('tr').attr('memberId');
                this.modalContext.type = 'Delete';
                $('.modal').modal({
                    backdrop: 'static'
                });
            },
            modalResult(arg){
                if(arg.status){
                    var app = this;

                    orgbuilder.api.del('members/' + arg.context.id)
                        .done(() => {
                            console.log('Member successfully deleted');
                            app.loadMembers();
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'Member successfully deleted',
                                clazz: 'alert-success'
                            });
                        })
                        .fail(() => console.log('Member delete FAILED'));
                }
            }
        }
    }
</script>