import React, {Component} from 'react';
import {BrowserRouter as Router, Route} from "react-router-dom";
import LoginPage from './components/LoginPage/LoginPage';
import DashboardPage from './components/DashboardPage/DashboardPage';

class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            user: null
        }
    }

    successfulAuthenticationHandler = (response) => {
        const data = response.data.principal;
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

    render() {
        return (
            <Router>
                <div>
                    <Route path='/'
                           exact
                           render={(props) => <LoginPage {...props}
                                                         onLoginSuccess={this.successfulAuthenticationHandler}
                                                         user={this.state.user}/>}/>
                    <Route path='/dashboard/'
                           render={(props) => <DashboardPage {...props} user={this.state.user}/>}/>
                </div>
            </Router>
        );
    }
}

export default App;
