<template>
    <div class="row">
        <div class="text-center col-xs-12 col-sm-6 col-sm-offset-3">
            <h2>Welcome to {{ orgName }}</h2>
        </div>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';

    export default {
        name: 'main',
        props: [
            'orgName'
        ],
        beforeMount(){
            orgbuilder.access.hasValidToken(this);
        },
        mounted(){
            var denied = this.$route.query.denied;
            var errorMessage = this.$route.query.errorMessage;

            if(denied !== undefined && denied){
                var msgTxt = (() => {
                    var text = 'Access denied!';
                    if(errorMessage !== undefined){
                        return text + ' Message: ' + errorMessage;
                    }
                    return text;
                })();

                this.$emit('showAlert', {
                    show: true,
                    msg: msgTxt,
                    clazz: 'alert-danger'
                });
            }
        }
    }
</script>