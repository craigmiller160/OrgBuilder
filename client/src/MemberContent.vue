<template>
    <div>
        <div class="row">
            <div class="col-xs-12 text-center">
                <h2>{{ title }}</h2>
            </div>
        </div>
        <form v-on:submit.prevent="saveChanges">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary form-group">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Personal Info</h4>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">First Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.firstName }}</p>
                                    <input v-show="edit" name="firstName" class="form-control" type="text" v-model="member.firstName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Middle Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.middleName }}</p>
                                    <input v-show="edit" name="middleName" class="form-control" type="text" v-model="member.middleName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Last Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.lastName }}</p>
                                    <input v-show="edit" name="lastName" class="form-control" type="text" v-model="member.lastName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Date of Birth:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.dateOfBirth }}</p>
                                    <input v-show="edit" name="dateOfBirth" class="form-control" type="date" v-model="member.dateOfBirth" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Sex:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.sex }}</p>
                                    <select v-show="edit" name="sex" class="form-control">
                                        <!-- TODO finish binding this -->
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <a class="btn btn-primary" type="button" title="Cancel Changes" @click="handleCancel">Cancel</a>
                    <a v-show="edit" class="btn btn-success" type="submit" title="Save Changes">Save</a>
                    <a v-show="canEdit && showDeleteBtn" class="btn btn-danger pull-right" type="button" title="Delete Member">Delete</a>
                    <a v-show="canEdit && !edit" class="btn btn-info pull-right" type="button" title="Edit Member" @click="startEdit">Edit</a>
                </div>
            </div>
        </form>
        <app-modal :context="modalContext"
                   v-on:result="modalResult($event)">
        </app-modal>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';
    import ConfirmModal from './ConfirmModal.vue';

    export default {
        name: 'member_content',
        components: {
            'app-modal': ConfirmModal
        },
        data(){
            return {
                title: 'New Member',
                edit: false,
                member: {
                    memberId: 0,
                    dateOfBirth: null,
                    firstName: null,
                    middleName: null,
                    lastName: null,
                    sex: null,
                    phones: [],
                    emails: [],
                    addresses: []
                },
                modalContext: {
                    type: '',
                    id: 0
                }
            }
        },
        beforeMount(){
            orgbuilder.access.start(this)
                .hasAnyRole(orgbuilder.jwt.roles.write, orgbuilder.jwt.roles.read)
                .validate();
        },
        mounted(){
            this.loadData();
            if(this.$route.query.memberId === undefined && this.canEdit){
                this.edit = true;
            }
        },
        computed: {
            canEdit(){
                return orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.write);
            },
            showDeleteBtn(){
                return this.$route.query.memberId !== undefined;
            },
        },
        methods: {
            startEdit(){
                this.edit = true;
            },
            loadData(){

            },
            loadInfo(){
                //TODO finish this
            },
            loadMembers(){
                //TODO finish this
            },
            modalResult(event){
                //TODO handle modal result
            },
            saveChanges(event){
                //TODO save any changes to the member
            },
            handleCancel(event){
                //TODO handle cancel event
            }
        }
    }
</script>