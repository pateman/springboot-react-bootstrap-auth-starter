import React, {Component} from 'react';
import {BrowserRouter as Router, Route} from "react-router-dom";
import AuthService from './services/AuthService/AuthService';
import LoginPage from './components/LoginPage/LoginPage';
import DashboardPage from './components/DashboardPage/DashboardPage';

class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            user: null
        }
    }

    componentDidMount() {
        AuthService.me(data => {
            if (data.authenticated) {
                this.successfulAuthenticationHandler(data);
            }
        });
    }

    successfulAuthenticationHandler = (response) => {
        const data = response.principal;
        const roles = data.authorities.reduce((last, cur) => {
            last.push(cur.role);
            return last;
        }, []);
        this.setState({
            user: {
                name: data.username,
                role: roles
            }
        });
    };

    successfulLogoutHandler = () => this.setState({user: null});

    render() {
        const loginRender = (props) => <LoginPage {...props}
                                                  onLoginSuccess={this.successfulAuthenticationHandler}
                                                  user={this.state.user}/>;
        const dashboardRender = (props) => <DashboardPage {...props}
                                                          onLogoutSuccess={this.successfulLogoutHandler}
                                                          user={this.state.user}/>;
        return (
            <Router>
                <div>
                    <Route path='/'
                           exact
                           render={(p) => this.state.user ? dashboardRender(p) : loginRender(p)}/>
                    <Route path='/login'
                           render={loginRender}/>
                    <Route path='/dashboard/'
                           render={dashboardRender}/>
                </div>
            </Router>
        );
    }
}

export default App;
