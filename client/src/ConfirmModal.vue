<template>
    <div>
        <div id="confirmModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h2 class="modal-title">Confirm {{ context.type }}</h2>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to {{ context.type }}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="modal-yes btn btn-success" data-dismiss="modal" @click="yes">Yes</button>
                        <button type="button" class="modal-no btn btn-danger" data-dismiss="modal" @click="no">No</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'confirm_modal',
        props: [
            'context'
        ],
        data(){
            return {

            }
        },
        methods: {
            yes(){
                this.$emit('result', {
                    context: this.context,
                    status: true
                });
            },
            no(){
                this.$emit('result', {
                    context: this.context,
                    status: false
                });
            }
        }
    }
</script>