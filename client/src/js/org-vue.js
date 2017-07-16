import { orgbuilder } from './orgbuilder.js';

orgbuilder.vue = (() => {

    const alert = {
        showSuccess(app,msg){
            app.$emit('showAlert', {
                show: true,
                msg: msg,
                clazz: 'alert-success'
            });
        },
        showError(app,msg){
            app.$emit('showAlert', {
                show: true,
                msg: msg,
                clazz: 'alert-danger'
            });
        }
    };

    return {
        alert: alert
    }

})();