import React, {Component} from 'react';
import styles from './LoginPage.module.css';
import logo from '../../logo.svg';
import {Alert} from 'reactstrap';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Redirect} from 'react-router-dom';

class LoginPage extends Component {

    static propTypes = {
        onLoginSuccess: PropTypes.func.isRequired,
        user: PropTypes.object
    };

    constructor(props) {
        super(props);

        this.loginRef = React.createRef();
        this.passwordRef = React.createRef();
        this.rememberMeRef = React.createRef();

        this.state = {
            loginError: null
        }
    }

    loginFormHandler = (evt) => {
        evt.preventDefault();

        this.setState({loginError: null}, () => {
            axios.post('/login', {
                username: this.loginRef.current.value,
                password: this.passwordRef.current.value,
                rememberMe: this.rememberMeRef.current.checked
            })
                .then(resp => {
                    this.props.onLoginSuccess(resp);
                    this.props.history.push('/dashboard');
                })
                .catch(err => this.setState({loginError: err.response.data.error}));
        });
    };

    inputBlurHandler = () => {
        if (this.state.loginError) {
            this.setState({loginError: null});
        }
    };

    render() {
        console.log(this.props.user);
        if (this.props.user) {
            return <Redirect to='/dashboard'/>;
        }

        const loginError = this.state.loginError ? <Alert color="danger">{this.state.loginError}</Alert> : null;
        return (
            <div className={styles.LoginPage}>
                <div className="row">
                    <div className="col">
                        <form className={styles.Form} onSubmit={this.loginFormHandler}>
                            <img className={[styles["App-logo"], 'mb-4'].join(' ')} src={logo} alt="" width="72"
                                 height="72"/>
                            <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                            {loginError}
                            <label htmlFor="inputEmail" className="sr-only">Email address</label>
                            <input type="text" id="inputLogin" className="form-control" placeholder="Login"
                                   required
                                   autoFocus
                                   onBlur={this.inputBlurHandler}
                                   ref={this.loginRef}
                            />
                            <label htmlFor="inputPassword" className="sr-only">Password</label>
                            <input type="password" id="inputPassword" className="form-control" placeholder="Password"
                                   required
                                   onBlur={this.inputBlurHandler}
                                   ref={this.passwordRef}
                            />
                            <div className="checkbox mb-3">
                                <label>
                                    <input type="checkbox" value="remember-me" ref={this.rememberMeRef}/>
                                    &nbsp;Remember me
                                </label>
                            </div>
                            <button className="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                            <p className="mt-5 mb-3 text-muted">&copy; 2017-2018</p>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

export default LoginPage;