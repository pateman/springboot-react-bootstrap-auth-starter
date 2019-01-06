import React, {Component} from 'react';
import styles from './LoginPage.module.css';
import logo from '../../logo.svg';
import axios from 'axios';

class LoginPage extends Component {

    constructor(props) {
        super(props);

        this.loginRef = React.createRef();
        this.passwordRef = React.createRef();
    }

    loginFormHandler = (evt) => {
        evt.preventDefault();

        axios.post('/login', {
            username: this.loginRef.current.value,
            password: this.passwordRef.current.value
        });
    };

    render() {
        return <div className={styles.LoginPage}>
            <form className={styles.Form} onSubmit={this.loginFormHandler}>
                <img className={[styles["App-logo"], 'mb-4'].join(' ')} src={logo} alt="" width="72" height="72"/>
                <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                <label htmlFor="inputEmail" className="sr-only">Email address</label>
                <input type="text" id="inputEmail" className="form-control" placeholder="Email address"
                       required
                       autoFocus
                       ref={this.loginRef}
                />
                <label htmlFor="inputPassword" className="sr-only">Password</label>
                <input type="password" id="inputPassword" className="form-control" placeholder="Password"
                       required
                       ref={this.passwordRef}
                />
                <div className="checkbox mb-3">
                    <label>
                        <input type="checkbox" value="remember-me"/>
                        &nbsp;Remember me
                    </label>
                </div>
                <button className="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                <p className="mt-5 mb-3 text-muted">&copy; 2017-2018</p>
            </form>
        </div>
    }
}

export default LoginPage;