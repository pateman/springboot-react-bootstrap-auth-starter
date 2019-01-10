import axios from 'axios';

export default class {

    static login(userName, password, shouldRemember, successCallback, failureCallback) {
        axios.post('/api/login', {
            username: userName,
            password: password,
            rememberMe: shouldRemember
        })
            .then(resp => successCallback(resp.data))
            .catch(err => failureCallback(err.response.data));
    }

    static logout(callback) {
        axios.get('/api/logout').then(callback());
    }

    static me(callback) {
        axios.get('/api/me').then(resp => callback(resp.data));
    }

}