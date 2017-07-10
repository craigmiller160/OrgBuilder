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
                                    <select v-show="edit" name="sex" class="form-control" v-model="member.sex">
                                        <option v-for="sex in info.sexes" :value="sex">{{ sex }}</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Addresses</h4>
                        </div>
                        <div class="panel-body">
                            <div v-for="(address, index) in member.addresses" class="row" :addressId="address.addressId">
                                <div class="col-sm-1">
                                    <p>{{ address.preferred ? '*' : '' }}</p>
                                </div>
                                <div class="col-sm-2">
                                    <p>{{ address.addressType }}</p>
                                </div>
                                <div class="col-sm-5">
                                    <p>{{ parseAddress(address) }}</p>
                                </div>
                                <div class="col-sm-4">
                                    <button v-show="edit" type="button" class="btn btn-info" title="Edit Address" @click="(event) => editContactInfo('address', index)">Edit</button>
                                    <button v-show="edit" type="button" class="btn btn-danger" title="Delete Address">Delete</button>
                                </div>
                            </div>
                            <div v-show="edit" class="row">
                                <div class="col-sm-6">
                                    <button type="button" class="btn btn-primary" title="Add Address" @click="(event) => editContactInfo('address')">Add</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Phones</h4>
                        </div>
                        <div class="panel-body">
                            <div v-for="phone in member.phones" class="row" :phoneId="phone.phoneId">
                                <div class="col-sm-1">
                                    <p>{{ phone.preferred ? '*' : '' }}</p>
                                </div>
                                <div class="col-sm-2">
                                    <p>{{ phone.phoneType }}</p>
                                </div>
                                <div class="col-sm-5">
                                    <p>{{ parsePhone(phone) }}</p>
                                </div>
                                <div class="col-sm-4">
                                    <button v-show="edit" type="button" class="btn btn-info" title="Edit Phone">Edit</button>
                                    <button v-show="edit" type="button" class="btn btn-danger" title="Delete Phone">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Emails</h4>
                        </div>
                        <div class="panel-body">
                            <div v-for="email in member.emails" class="row" :emailId="email.emailId">
                                <div class="col-sm-1">
                                    <p>{{ email.preferred ? '*' : '' }}</p>
                                </div>
                                <div class="col-sm-2">
                                    <p>{{ email.emailType }}</p>
                                </div>
                                <div class="col-sm-5">
                                    <p>{{ email.emailAddress }}</p>
                                </div>
                                <div class="col-sm-4">
                                    <button v-show="edit" type="button" class="btn btn-info" title="Edit Email">Edit</button>
                                    <button v-show="edit" type="button" class="btn btn-danger" title="Delete Email">Delete</button>
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
        <div id="addressModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Address</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Address Type:</p>
                            </div>
                            <div class="col-sm-6">
                                <select v-if="member.addresses[selectedAddressIndex] !== undefined" name="addressType" class="form-control" v-model="member.addresses[selectedAddressIndex].addressType">
                                    <option v-for="type in info.contactTypes.addressTypes" :value="type">{{ type }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Address Line 1:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="address1" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].address1" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Address Line 2:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="address2" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].address2" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">City:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="city" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].city" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">State:</p>
                            </div>
                            <div class="col-sm-6">
                                <select v-if="member.addresses[selectedAddressIndex] !== undefined" name="state" class="form-control" v-model="member.addresses[selectedAddressIndex].state">
                                    <option v-for="state in info.states" :value="state">{{ state }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Zip Code:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="zipCode" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].zipCode" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" id="preferredAddress" name="preferred" type="checkbox" v-model="member.addresses[selectedAddressIndex].preferred" @click="preferredChange('address')" />
                                <label for="preferredAddress">Preferred</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';
    import ConfirmModal from './ConfirmModal.vue';

    const emptyAddr = {
        addressId: null,
        secondaryId: null,
        addressType: null,
        address1: null,
        address2: null,
        city: null,
        state: null,
        zipCode: null,
        preferred: false
    };

    export default {
        name: 'member_content',
        components: {
            'app-modal': ConfirmModal
        },
        data(){
            return {
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
                selectedAddressIndex: 0,
                selectedPhoneIndex: 0,
                selectedEmailIndex: 0,
                info: {
                    appInfo: {},
                    sexes: [],
                    states: [],
                    roles: [],
                    contactTypes: {
                        addressTypes: [],
                        phoneTypes: [],
                        emailTypes: []
                    }
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
            title(){
                let title = '';
                if(orgbuilder.varExistsString(this.member.firstName)){
                    title = this.member.firstName;
                }

                if(orgbuilder.varExistsString(this.member.lastName)){
                    title = (title.length > 0 ? title + ' ' : '') + this.member.lastName;
                }

                if(orgbuilder.varExistsString(title)){
                    return title;
                }
                else{
                    return 'New Member';
                }
            }
        },
        methods: {
            startEdit(){
                if(this.canEdit){
                    this.edit = true;
                }
            },
            loadData(){
                if(this.info.sexes.length === 0){
                    this.loadInfoAndMember();
                }
                else{
                    this.loadMember();
                }
            },
            loadInfoAndMember(){
                const app = this;
                orgbuilder.api.get('info')
                    .done((data) => {
                        app.info = data;
                        app.loadMember();
                    });
            },
            loadMember(){
                const app = this;
                orgbuilder.api.get('members/' + app.$route.query.memberId)
                    .done((data, status, jqXHR) => {
                        if(jqXHR.status === 204){
                            console.log('Member not found on server');
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'Member not found on server',
                                clazz: 'alert-danger'
                            });
                            return;
                        }

                        app.member = data;
                    })
                    .fail(() => console.log('Load member FAILED'));
            },
            modalResult(event){
                //TODO handle modal result
            },
            saveChanges(event){
                //TODO save any changes to the member
            },
            handleCancel(event){
                //TODO handle cancel event
            },
            parseAddress(address){
                let text = '';
                text = orgbuilder.varExistsString(address.address1) ? address.address1 : '';
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.address2) ? address.address2 : '');
                text = text !== '' ? text + ',' : '';
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.city) ? address.city + ',' : '');
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.state) ? address.state : '');
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.zipCode) ? address.zipCode : '');

                return text;
            },
            parsePhone(phone){
                let text = '';
                text = phone.areaCode !== null ? '(' + phone.areaCode + ')' : '';
                text = (text !== '' ? text + ' ' : '') + (phone.prefix !== null ? phone.prefix : '');
                text = text + '-';
                text = text + (phone.lineNumber !== null ? phone.lineNumber : '');
                text = text + (phone.extension !== null ? ' x' + phone.extension : '');

                return text;
            },
            editContactInfo(type, index){
                if('address' === type){
                    if(index === undefined){
                        this.selectedAddressIndex = this.member.addresses.length;
                        this.member.addresses.push($.extend({}, emptyAddr));
                    }
                    else{
                        this.selectedAddressIndex = index;
                    }

                    $('#addressModal').modal({
                        background: 'static'
                    });
                }
                else if('phone' === type){
                    //TODO finish this
                }
                else if('email' === type){
                    //TODO finish this
                }
            },
            preferredChange(type){
                if('address' === type){
                    let oldVal = this.member.addresses[this.selectedAddressIndex].preferred;
                    if(oldVal === false){
                        //Because we get the old value here, we're looking to see if the value is false because it's about to become true
                        let app = this;
                        $.each(this.member.addresses, (index, addr) => {
                            if(app.selectedAddressIndex === index){
                                return true;
                            }

                            addr.preferred = false;
                        });
                    }
                }
                else if('phone' === type){
                    //TODO finish this
                }
                else if('email' === type){
                    //TODO finish this
                }
            }
        }
    }
</script>